package com.nekodev.hackathonapp.model

data class Order (
    val id: Int,
    val dimensions: List<Int>,
    val weight: Int,
    val latitude: Double,
    val longitude: Double
)