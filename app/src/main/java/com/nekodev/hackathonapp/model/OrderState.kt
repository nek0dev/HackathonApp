package com.nekodev.hackathonapp.model

sealed class OrderState {
    data class OnlyOrder(
        val orderId: Int,
        val dimensions: List<Int>,
        val weight: Int,
        val endLatitude: Double,
        val endLongitude: Double
    ): OrderState()
    data class OrderAndState(
        val orderId: Int,
        val dimensions: List<Int>,
        val weight: Int,
        val endLatitude: Double,
        val endLongitude: Double,
        val stateId: Int,
        val serialNumber: String,
        val state: String,
        val currentLatitude: Double,
        val currentLongitude: Double
    ): OrderState()
}