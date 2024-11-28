package com.pm.cc.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.pm.cc.core.CurrencyCode
import java.util.Date

@Entity(tableName = "currency_codes", indices = [Index(value = ["code"], unique = true)])
data class CurrencyEntity(
    @PrimaryKey
    val id: Long? = null,
    val code: String,
)

@Entity(
    tableName = "rates",
    foreignKeys = [
        ForeignKey(
            entity = CurrencyEntity::class,
            parentColumns = ["id"],
            childColumns = ["src"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = CurrencyEntity::class,
            parentColumns = ["id"],
            childColumns = ["dst"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [Index(value = ["date", "src", "dst"], unique = true)]
)
data class RateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val date: Date,
    val src: Long,
    val dst: Long,
    val rate: Double
)

@Entity
data class ExtRateEntity(
    val id: Long,
    val date: Long,
    val srcId: Long,
    val srcCode: CurrencyCode,
    val dstId: Long,
    val dstCode: CurrencyCode,
    val rate: Double
)

enum class OperationType {
    INIT, CONVERT
}


class OperationTypeConverter {

    @TypeConverter
    fun fromInt(value: Int): OperationType = OperationType.entries[value]

    @TypeConverter
    fun dateToTimestamp(operationType: OperationType): Int = operationType.ordinal

}

class DateConverter {
    @TypeConverter
    fun fromTimeStamp(timestamp: Long): Date = Date(timestamp)

    @TypeConverter
    fun toTimeStamp(date: Date): Long = date.time
}

@Entity(tableName = "operations")
data class OperationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val date: Date,
    val type: OperationType,
    val payload: String
)


@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CurrencyEntity::class,
            parentColumns = ["id"],
            childColumns = ["currencyId"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = OperationEntity::class,
            parentColumns = ["id"],
            childColumns = ["operationId"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val currencyId: Long,
    val operationId: Long,
    val amount: Double,
)

data class BalanceEntity(
    val currencyId: Long,
    val currencyCode: String,
    val amount: Double
)




