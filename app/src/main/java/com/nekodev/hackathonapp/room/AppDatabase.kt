package com.nekodev.hackathonapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nekodev.hackathonapp.room.dao.OrderAndStateDao
import com.nekodev.hackathonapp.room.entities.OrderDB
import com.nekodev.hackathonapp.room.entities.StateDB

@Database(entities = [OrderDB::class, StateDB::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract val orderAndStateDao: OrderAndStateDao
}