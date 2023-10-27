package com.nekodev.hackathonapp.room.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.nekodev.hackathonapp.model.Order
import com.nekodev.hackathonapp.model.State

data class OrderAndState(
    @Embedded
    val order: OrderDB,
    @Relation(
        parentColumn = "id",
        entityColumn = "order_id"
    )
    val state: StateDB
) {
    fun asDomainModel(): State {
        return with(this) {
            State(
                id = state.id,
                serialNumber = state.serialNumber,
                order = Order(
                    id = order.id,
                    dimensions = order.dimensions,
                    weight = order.weight,
                    latitude = order.latitude,
                    longitude = order.longitude
                ),
                state = state.state,
                latitude = state.latitude,
                longitude = state.longitude,
            )
        }
    }
}
