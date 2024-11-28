package com.pm.cc.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OperationDao {

    @Query("SELECT * FROM operations")
    fun getAllAsFlow(): Flow<List<OperationEntity>>

    @Query("SELECT * FROM operations")
    suspend fun getAll(): List<OperationEntity>



}