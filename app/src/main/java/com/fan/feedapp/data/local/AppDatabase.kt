package com.fan.feedapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fan.feedapp.data.local.dao.TextDao
import com.fan.feedapp.data.local.model.TextData

@Database(entities = [TextData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val textDao: TextDao
}