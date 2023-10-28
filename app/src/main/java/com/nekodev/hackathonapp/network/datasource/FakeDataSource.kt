package com.nekodev.hackathonapp.network.datasource

import arrow.core.Option
import arrow.core.some
import com.nekodev.hackathonapp.model.OrderState

class FakeDataSource: OrdersDataSource {
    override suspend fun getStateByOrderId(orderId: Int): Option<OrderState> {
        val onlyOrder = OrderState.OnlyOrder(
            orderId = orderId,
            dimensions = listOf(5,5,5),
            weight = 100,
            endLatitude = 51.662514,
            endLongitude = 39.196181
        )
        val orderAndState = OrderState.OrderAndState(
            orderId = orderId,
            dimensions = listOf(5,5,5),
            weight = 100,
            endLatitude = 51.662514,
            endLongitude = 39.196181,
            stateId = orderId + 1,
            serialNumber = "S2",
            state = "in base",
            currentLatitude = 51.666666,
            currentLongitude = 39.444444
        )
        val random = (0..1).random()
        return if (random == 0) onlyOrder.some() else orderAndState.some()
    }
}