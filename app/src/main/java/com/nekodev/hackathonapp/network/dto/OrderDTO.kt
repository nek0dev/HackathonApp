package com.nekodev.hackathonapp.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderDTO(
    val id: Int,
    val dimensions: List<Int>,
    val weight: Int,
    val latitude: Double,
    val longitude: Double
)