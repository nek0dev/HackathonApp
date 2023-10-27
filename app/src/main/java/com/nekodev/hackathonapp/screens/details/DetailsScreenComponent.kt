package com.nekodev.hackathonapp.screens.details

import com.nekodev.hackathonapp.model.State
import kotlinx.coroutines.flow.StateFlow

interface DetailsScreenComponent {
    val state: StateFlow<State?>
    val hasError: StateFlow<Boolean>
    fun gotoMain()
    fun openMaps(latitude: Double, longitude: Double)
}