package com.nekodev.hackathonapp.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey


@Entity(
    tableName = "states",
    foreignKeys = [
        ForeignKey(
            entity = OrderDB::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class StateDB (
    @ColumnInfo("id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("serial_number")
    val serialNumber: Int,
    @ColumnInfo("order_id", index = true)
    val orderId: Int,
    @ColumnInfo("state", index = true)
    val state: String,
    @ColumnInfo("latitude")
    val latitude: Double,
    @ColumnInfo("longitude")
    val longitude: Double
)