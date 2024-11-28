package com.pm.cc.data.network

import com.pm.cc.core.CcFailure
import com.pm.cc.core.CcResult
import com.pm.cc.core.UnhandledCcFailure
import com.pm.cc.core.asFailure
import com.pm.cc.core.toFailure
import com.pm.cc.core.toSuccess
import com.pm.cc.data.CurrencyExchangeRates
import com.pm.cc.data.network.NetworkRatesDataSource.RatesFailure
import com.pm.cc.di.CcComponent
import com.pm.cc.di.Qualifiers
import com.pm.cc.di.inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.withContext

interface NetworkRatesDataSource {

    suspend fun rates(): CcResult<CurrencyExchangeRates, RatesFailure>

    sealed class RatesFailure : CcFailure {
        data class Unhandled(override val cause: CcFailure) : RatesFailure(), UnhandledCcFailure
    }

}

internal class NetworkRatesDataSourceImpl : NetworkRatesDataSource, CcComponent {

    private val ioContext by inject(qualifier = Qualifiers.Coroutines.Contexts.IO)

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun rates(): CcResult<CurrencyExchangeRates, RatesFailure> =
        withContext(context = ioContext) {
            runCatching {
                client
                    .get("https://developers.paysera.com/tasks/api/currency-exchange-rates")
                    .body<CurrencyExchangeRates>()
            }
                .fold(
                    onSuccess = { data -> data.toSuccess() },
                    onFailure = { throwable ->
                        RatesFailure.Unhandled(cause = throwable.asFailure()).toFailure()
                    }
                )
        }

}