package com.nekodev.hackathonapp.screens.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnPause
import com.arkivanov.essenty.lifecycle.doOnResume
import com.nekodev.hackathonapp.data.OrderRepository
import com.nekodev.hackathonapp.model.OrderState
import com.nekodev.hackathonapp.room.DatabaseDataSource
import com.nekodev.hackathonapp.util.BaseComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.inject


class RealDetailsScreenComponent(
    componentContext: ComponentContext,
    private val orderId: Int,
    private val navigateToMain: () -> Unit,
) : BaseComponent(componentContext), DetailsScreenComponent {
    private val repo: OrderRepository by inject()
    private val _state: MutableStateFlow<OrderState?> = MutableStateFlow(null)
    private val _hasError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val databaseSource: DatabaseDataSource by inject()
    private val context: Context by inject()
    private var bool = false

    private val _pollFlow = channelFlow<OrderState?> {
        while (isActive && !bool){
            bool = true
            delay(3_000L)
            val data = withContext(Dispatchers.IO) { repo.getStateByOrderId(orderId) }
            if (data.isNone()) {
                _hasError.value = true
                _state.value = null
                return@channelFlow
            }
            val dataNonNull = data.getOrNull()!!
            databaseSource.createOrderState(dataNonNull)
            _state.value = dataNonNull
            send(dataNonNull)
            bool = false
        }
    }.flowOn(Dispatchers.IO).stateIn(mainScope, SharingStarted.Eagerly, null)

    init {
        lifecycle.doOnCreate {
            mainScope.launch {
                val stateFromRepo = repo.getStateByOrderId(orderId)
                if (stateFromRepo.isNone()) {
                    _hasError.value = true
                    _state.value = null
                    return@launch
                }
                val fromRepoValue = stateFromRepo.getOrNull()!!
                databaseSource.createOrderState(fromRepoValue)
                _state.value = fromRepoValue
                _pollFlow.collect {
                    _state.value = it
                }
            }
        }
        lifecycle.doOnResume {
            bool = false
        }
        lifecycle.doOnPause {
            bool = true
        }
    }

    override val state: StateFlow<OrderState?>
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
        try {
            startActivity(context, intent, null)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Не установлено прлиожение Яндекс Карты!", Toast.LENGTH_SHORT).show()
        }
        
    }
}