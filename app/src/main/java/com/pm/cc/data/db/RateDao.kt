package com.pm.cc.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RateDao {

    @Query("select rates.id as id, rates.date as date, rates.src as srcId, cc1.code as srcCode, rates.dst as dstId, cc2.code as dstCode, rates.rate as rate from rates \n" +
            "left join currency_codes as cc1 on rates.src = cc1.id " +
            "left join currency_codes as cc2 on rates.dst = cc2.id")
    fun getAllAsFlow(): Flow<List<ExtRateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(rates: List<RateEntity>)

}