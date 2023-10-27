package com.nekodev.hackathonapp.network.datasource

import arrow.core.Option
import arrow.core.some
import com.nekodev.hackathonapp.model.Order
import com.nekodev.hackathonapp.model.State

class FakeDataSource: OrdersDataSource {
    override suspend fun getStateByOrderId(orderId: Int): Option<State> {
        return State(
            id = orderId,
            serialNumber = 2,
            order = Order(
                id = orderId,
                dimensions = listOf(5,5,5),
                weight = 100,
                latitude = 51.662514,
                longitude = 39.196181
            ),
            state = "in base",
            latitude = 0.0,
            longitude = 0.0
        ).some()
    }
}