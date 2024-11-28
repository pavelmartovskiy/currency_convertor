package com.pm.cc.presentation.screen.home.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pm.cc.R
import com.pm.cc.presentation.screen.BodyMedium
import com.pm.cc.presentation.screen.HeaderMediumGrey
import com.pm.cc.presentation.screen.home.model.BalanceItem

@Composable
fun MyBalances(items: List<BalanceItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = MaterialTheme.colorScheme.onPrimary)
    ) {
        HeaderMediumGrey(
            text = stringResource(id = R.string.my_balances).uppercase(),
            modifier = Modifier.padding(all = 8.dp)
        )
        LazyRow {
            this.items(items = items) { item ->
                Row (modifier = Modifier.padding(horizontal = 8.dp)) {
                    BodyMedium(
                        text = item.code,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    BodyMedium(text = item.amount,)
                }
            }
        }
    }
}

@Preview
@Composable
fun MyBalancesPreview() {
    val data = listOf(
        BalanceItem(code = "EUR", "1000.0"),
        BalanceItem(code = "UDS", "2000.0"),
        BalanceItem(code = "AUS", "3000.0"),
        BalanceItem(code = "AUS2", "3000.0"),
        BalanceItem(code = "AUS3", "3000.0"),
        BalanceItem(code = "AUS4", "3000.0"),
    )
    MyBalances(items = data)
}