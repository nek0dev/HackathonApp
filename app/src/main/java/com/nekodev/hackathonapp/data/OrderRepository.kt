package com.nekodev.hackathonapp.data

import arrow.core.Option
import arrow.core.none
import com.nekodev.hackathonapp.model.OrderState
import com.nekodev.hackathonapp.network.datasource.OrdersDataSource
import com.nekodev.hackathonapp.room.DatabaseDataSource

class OrderRepository(
    private val networkSource: OrdersDataSource,
    private val databaseSource: DatabaseDataSource,
) {
    suspend fun getStateByOrderId(
        orderId: Int
    ): Option<OrderState> {
        val netRes = networkSource.getStateByOrderId(orderId)
        if (netRes.isSome()) {
            return netRes
        }
        val dbRes = databaseSource.getOrderById(orderId)
        if (dbRes.isNone()) {
            return none()
        }
        return dbRes
    }

    suspend fun getAllStates(): List<OrderState> {
        return databaseSource.getAllStates()
    }

}