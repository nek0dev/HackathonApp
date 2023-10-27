package com.nekodev.hackathonapp.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.nekodev.hackathonapp.util.IntListConverter

@TypeConverters(IntListConverter::class)
@Entity(
    tableName = "orders"
)
data class OrderDB(
    @ColumnInfo("id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("dimensions", index = true)
    val dimensions: List<Int>,
    @ColumnInfo("weight", index = true)
    val weight: Int,
    @ColumnInfo("latitude", index = true)
    val latitude: Double,
    @ColumnInfo("longitude", index = true)
    val longitude: Double
)
