package com.nekodev.hackathonapp.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class StateDTO(
    val id: Int,
    val serialNumber: String,
    val orderId: Int?,
    val state: String,
    val latitude: Double,
    val longitude: Double
)
