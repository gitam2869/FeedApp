package com.fan.feedapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fan.feedapp.data.local.model.TextData

@Dao
interface TextDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(textData: TextData): Long

    @Query("SELECT *FROM text LIMIT 1")
    fun getTexts(): List<TextData>
}