package com.pm.cc.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
abstract class ComplexDao  {

    @Query("select currency_codes.id as currencyId, currency_codes.code as currencyCode, SUM(transactions.amount) as amount from currency_codes left join transactions on currency_codes.id = transactions.currencyId group by currency_codes.code")
    abstract fun getBalanceAsFlow(): Flow<List<BalanceEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun createOperation(operation: OperationEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun createTransaction(transaction: TransactionEntity): Long

    @Query("select count(operations.id) as amount from operations where operations.type = 1 group by operations.type")
    abstract suspend fun getConvertOperationNumber() : Long

    @Query("select sum(transactions.amount) as amount from transactions where transactions.currencyId = :currencyId group by transactions.currencyId")
    abstract suspend fun getBalanceBy(currencyId: Long): Double

    @Transaction
    open suspend fun convert(
        srcId: Long,
        srcAmount: Double,
        dstId: Long,
        dstAmount: Double,
        commission: Double?,
        payload: String
    ) {
        val operationId = createOperation(
            OperationEntity(date = Date(), type = OperationType.CONVERT, payload = payload)
        )

        createTransaction(
            TransactionEntity(currencyId = srcId, amount = -srcAmount, operationId = operationId)
        )

        createTransaction(
            TransactionEntity(currencyId = dstId, amount = dstAmount, operationId = operationId)
        )

        if (commission != null) {
            createTransaction(
                TransactionEntity(currencyId = srcId, amount = -commission, operationId = operationId)
            )
        }

    }
}