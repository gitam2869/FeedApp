package com.fan.feedapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.fan.feedapp.domain.model.Text
import com.fan.feedapp.presentation.feed.data.Feed

@Entity(tableName = "text")
data class TextData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,

    @ColumnInfo(name = "text") val text: String
) {
    @Ignore
    constructor(
        text: String
    ) : this(
        0, text
    )
}

fun TextData.toDomainText(): Text {
    return Text(
        Feed.TYPE_TEXT,
        this.id,
        this.text
    )
}