package com.nekodev.hackathonapp.screens.main

import android.content.Context
import android.widget.Toast
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnResume
import com.nekodev.hackathonapp.data.OrderRepository
import com.nekodev.hackathonapp.model.OrderState
import com.nekodev.hackathonapp.util.BaseComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.inject

class RealMainScreenComponent(
    componentContext: ComponentContext,
    private val navigateToDetails: (id: Int) -> Unit
): BaseComponent(componentContext), MainScreenComponent {
    private val repo: OrderRepository by inject()
    private val context: Context by inject()
    private val _states: MutableStateFlow<List<OrderState>> = MutableStateFlow(emptyList())

    override fun makeSearch(value: String) {
        ioScope.launch {
            val id: Int
            try {
                id = value.toInt()
            } catch (e: Exception) {
                mainScope.launch {
                    Toast.makeText(context, "Не удалось преобразовать в число!", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
            val fetchResult = repo.getStateByOrderId(id)
            if (fetchResult.isNone()) {
                mainScope.launch {
                    Toast.makeText(context, "Не удалось найти заказ!", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
            navigateToDetails(id)
        }
    }

    override fun selectState(orderId: Int) {
        navigateToDetails(orderId)
    }

    init {
        lifecycle.doOnResume {
            mainScope.launch {
                val result = withContext(Dispatchers.IO){ repo.getAllStates() }
                _states.value = result
            }
        }
    }

    override val states: StateFlow<List<OrderState>>
        get() = _states
}