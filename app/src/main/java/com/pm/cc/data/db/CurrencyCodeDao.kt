package com.pm.cc.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyCodeDao {

    @Query("SELECT * FROM currency_codes")
    suspend fun getAll(): List<CurrencyEntity>

    @Query("SELECT * FROM currency_codes")
    fun getAllAsFlow(): Flow<List<CurrencyEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create(currencies: List<CurrencyEntity>)

}