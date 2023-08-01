package com.krishna.assessmenttest.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDaoClass {

    @Insert
    fun insertData(model: Model)

    @Query("SELECT * FROM Model")
    fun getAllData(): Flow<List<Model>>
}