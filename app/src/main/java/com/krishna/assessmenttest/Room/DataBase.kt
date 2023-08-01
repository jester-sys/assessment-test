package com.krishna.assessmenttest.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Model::class], version = 2)
@TypeConverters(UriTypeConverter::class)
abstract class DataBase : RoomDatabase() {
    abstract fun dataDao(): DataDaoClass

    companion object {
        private var INSTANCE: DataBase? = null

        fun getInstance(context: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "UserData_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
