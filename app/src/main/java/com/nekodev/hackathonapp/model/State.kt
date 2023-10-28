package com.nekodev.hackathonapp.model

data class State(
    val id: Int,
    val serialNumber: String,
    val order: Order,
    val state: String,
    val latitude: Double,
    val longitude: Double
)