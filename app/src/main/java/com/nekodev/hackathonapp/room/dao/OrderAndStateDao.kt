package com.nekodev.hackathonapp.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.nekodev.hackathonapp.room.entities.OrderAndState
import com.nekodev.hackathonapp.room.entities.OrderDB
import com.nekodev.hackathonapp.room.entities.StateDB

@Dao
interface OrderAndStateDao {
    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderAndState(orderId: Int): OrderAndState

    @Transaction
    @Query("SELECT * FROM orders")
    suspend fun getAllStates(): List<OrderAndState>

    @Insert(onConflict = REPLACE)
    suspend fun insertState(state: StateDB)

    @Insert(onConflict = REPLACE)
    suspend fun insertOrder(order: OrderDB)

    @Query("UPDATE states SET state=:state WHERE id = :id")
    suspend fun updateOrder(id: Int, state: String)

    @Delete
    suspend fun deleteState(state: StateDB)
}