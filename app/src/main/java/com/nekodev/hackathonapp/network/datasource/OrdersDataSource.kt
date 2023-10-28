package com.nekodev.hackathonapp.network.datasource

import arrow.core.Option
import com.nekodev.hackathonapp.model.OrderState

interface OrdersDataSource {
    suspend fun getStateByOrderId(orderId: Int): Option<OrderState>
}