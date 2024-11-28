package com.pm.cc.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pm.cc.core.onFailure
import com.pm.cc.core.runCatching
import com.pm.cc.di.CcComponent
import com.pm.cc.di.Qualifiers
import com.pm.cc.di.get
import com.pm.cc.domain.Currency
import kotlinx.coroutines.launch

@Database(
    entities = [
        RateEntity::class,
        CurrencyEntity::class,
        OperationEntity::class,
        TransactionEntity::class
    ],
    version = 1
)
@TypeConverters(OperationTypeConverter::class, DateConverter::class)
abstract class CcDatabase : RoomDatabase(), CcComponent {
    abstract fun rateDao(): RateDao
    abstract fun currencyCodeDao(): CurrencyCodeDao
    abstract fun operationDao(): OperationDao
    abstract fun complexDao(): ComplexDao
}


internal class CcDatabaseCallback : RoomDatabase.Callback(), CcComponent {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        populateDatabase(db = db)
    }

    private fun populateDatabase(db: SupportSQLiteDatabase) {

        val appScope = get(Qualifiers.Coroutines.Scopes.APP)
        val context = get(Qualifiers.Coroutines.Contexts.DB)

        appScope.launch(context = context) {

            kotlin.runCatching {
                val payload = "Init transaction. Add 1000.0 EUR to account."

                val euroId = Currency.BASE_CURRENCY.id
                val euroCode = Currency.BASE_CURRENCY.code
                db.execSQL("insert into currency_codes (id, code) values($euroId, '$euroCode')")
                val timestamp = System.currentTimeMillis()
                db.execSQL("insert into operations (id, date, type, payload) values(1, $timestamp, ${OperationType.INIT.ordinal}, '$payload')")
                db.execSQL("insert into transactions (id, currencyId, operationId, amount) values(1, $euroId, ${OperationType.INIT.ordinal}, 1000.0)")
            }.onFailure { it.printStackTrace() }

        }

    }

}
