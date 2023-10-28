package com.nekodev.hackathonapp.screens.details

import com.nekodev.hackathonapp.model.OrderState
import kotlinx.coroutines.flow.StateFlow

interface DetailsScreenComponent {
    val state: StateFlow<OrderState?>
    val hasError: StateFlow<Boolean>
    fun gotoMain()
    fun openMaps(latitude: Double, longitude: Double)
}