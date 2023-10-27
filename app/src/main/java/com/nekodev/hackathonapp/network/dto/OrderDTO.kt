package com.nekodev.hackathonapp.network.dto

import com.nekodev.hackathonapp.model.Order
import kotlinx.serialization.Serializable

@Serializable
data class OrderDTO(
    val id: Int,
    val dimensions: List<Int>,
    val weight: Int,
    val latitude: Double,
    val longitude: Double
) {
    fun asDomainModel(): Order {
        return with(this) {
            Order(id, dimensions, weight, latitude, longitude)
        }
    }
}
