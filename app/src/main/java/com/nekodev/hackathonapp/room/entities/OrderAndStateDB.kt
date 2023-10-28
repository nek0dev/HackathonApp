package com.nekodev.hackathonapp.room.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.nekodev.hackathonapp.model.OrderState

data class OrderAndStateDB(
    @Embedded
    val order: OrderDB,
    @Relation(
        parentColumn = "id",
        entityColumn = "order_id"
    )
    val state: StateDB?
) {
    fun asDomainModel(): OrderState {
        if (state == null) {
            return OrderState.OnlyOrder(
                orderId = order.id,
                dimensions = order.dimensions,
                weight = order.weight,
                endLatitude = order.latitude,
                endLongitude = order.longitude
            )
        }
        return OrderState.OrderAndState(
            orderId = order.id,
            dimensions = order.dimensions,
            weight = order.weight,
            endLatitude = order.latitude,
            endLongitude = order.longitude,
            stateId = state.id,
            serialNumber = state.serialNumber,
            state = state.state,
            currentLatitude = state.latitude,
            currentLongitude = state.longitude
        )
    }
}
