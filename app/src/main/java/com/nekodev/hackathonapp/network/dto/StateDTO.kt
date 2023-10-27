package com.nekodev.hackathonapp.network.dto

import com.nekodev.hackathonapp.model.State
import kotlinx.serialization.Serializable

@Serializable
data class StateDTO(
    val id: Int,
    val serialNumber: Int,
    val order: OrderDTO,
    val state: String,
    val latitude: Double,
    val longitude: Double
) {
    fun asDomainModel(): State {
        return with(this) {
            State(
                id = id,
                serialNumber = serialNumber,
                order = order.asDomainModel(),
                state = state,
                latitude = latitude,
                longitude = longitude
            )
        }
    }
}
