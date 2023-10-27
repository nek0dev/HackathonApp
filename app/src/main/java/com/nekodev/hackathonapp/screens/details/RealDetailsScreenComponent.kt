package com.nekodev.hackathonapp.screens.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.nekodev.hackathonapp.data.OrderRepository
import com.nekodev.hackathonapp.model.State
import com.nekodev.hackathonapp.room.DatabaseDataSource
import com.nekodev.hackathonapp.util.BaseComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject


class RealDetailsScreenComponent(
    componentContext: ComponentContext,
    private val orderId: Int,
    private val navigateToMain: () -> Unit,
    private val updateOrders: (State) -> Unit
) : BaseComponent(componentContext), DetailsScreenComponent {
    private val repo: OrderRepository by inject()
    private val _state: MutableStateFlow<State?> = MutableStateFlow(null)
    private val _hasError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val databaseSource: DatabaseDataSource by inject()
    private val context: Context by inject()

    init {
        lifecycle.doOnCreate {
            mainScope.launch {
                val stateFromRepo = repo.getStateByOrderId(orderId)
                if (stateFromRepo.isNone()) {
                    _hasError.value = true
                    _state.value = null
                    return@launch
                }
                databaseSource.createState(stateFromRepo.getOrNull()!!)
                _state.value = stateFromRepo.getOrNull()!!
                updateOrders(stateFromRepo.getOrNull()!!)
            }
        }
    }

    override val state: StateFlow<State?>
        get() = _state
    override val hasError: StateFlow<Boolean>
        get() = _hasError

    override fun gotoMain() {
        navigateToMain()
    }

    override fun openMaps(latitude: Double, longitude: Double) {
        val uri =
            Uri.parse("yandexmaps://maps.yandex.ru/?pt=$longitude,$latitude")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
    }
}