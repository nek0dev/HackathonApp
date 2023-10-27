package com.nekodev.hackathonapp.screens.main

import com.nekodev.hackathonapp.model.State
import kotlinx.coroutines.flow.StateFlow

interface MainScreenComponent {
    fun makeSearch(value: String)
    fun selectState(orderId: Int)

    val states: StateFlow<List<State>>
}