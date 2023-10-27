package com.nekodev.hackathonapp.navigation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnResume
import com.nekodev.hackathonapp.data.OrderRepository
import com.nekodev.hackathonapp.model.State
import com.nekodev.hackathonapp.navigation.root.RootComponent.Child.*
import com.nekodev.hackathonapp.screens.details.RealDetailsScreenComponent
import com.nekodev.hackathonapp.screens.main.RealMainScreenComponent
import com.nekodev.hackathonapp.screens.splash.RealSplashScreenComponent
import com.nekodev.hackathonapp.util.BaseComponent
import com.nekodev.hackathonapp.util.toStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.inject

class DefaultRootComponent(
    componentContext: ComponentContext
): RootComponent, BaseComponent(componentContext) {
    private val navigation = StackNavigation<RootConfig>()

    private val _stack: StateFlow<ChildStack<RootConfig, RootComponent.Child>>
        get() = childStack(
            source = navigation,
            initialConfiguration = RootConfig.SplashScreen,
            handleBackButton = true,
            childFactory = ::child,
            key = "RootChildStack"
        ).toStateFlow(lifecycle)

    override val stack: StateFlow<ChildStack<*, RootComponent.Child>> = _stack

    private val _states: MutableStateFlow<List<State>> = MutableStateFlow(emptyList())

    private val repo: OrderRepository by inject()

    private fun child(config: RootConfig, componentContext: ComponentContext): RootComponent.Child {
        return when(config){
            is RootConfig.MainScreen -> MainScreenChild(
                RealMainScreenComponent(
                    componentContext = componentContext,
                    _states = _states,
                    navigateToDetails = {
                        mainScope.launch {
                            navigation.push(RootConfig.DetailsScreen(it))
                        }
                    }
                )
            )
            is RootConfig.SplashScreen -> SplashScreenChild(
                RealSplashScreenComponent(
                    componentContext = componentContext
                )
            )

            is RootConfig.DetailsScreen -> DetailsScreenChild(
                RealDetailsScreenComponent(
                    componentContext = componentContext,
                    orderId = config.orderId,
                    updateOrders = { state ->
                        _states.update {
                            if (it.indexOfFirst { state -> state.order.id == config.orderId } == -1){
                                val newList = it.toMutableList()
                                newList += state
                                return@update newList
                            } else {
                                val newList = it.toMutableList()
                                val lol = newList.indexOfFirst {
                                    it.order.id == config.orderId
                                }
                                if (lol == -1) return@update newList
                                newList.removeAt(lol)
                                newList += state
                                return@update newList
                            }
                        }
                    },
                    navigateToMain = {
                        mainScope.launch {
                            val result = withContext(Dispatchers.IO){ repo.getAllStates() }
                            _states.value = result
                            navigation.replaceCurrent(RootConfig.MainScreen)
                        }
                    }
                )
            )
        }
    }

    init {
        lifecycle.doOnCreate {
            mainScope.launch {
                val result = withContext(Dispatchers.IO){ repo.getAllStates() }
                _states.value = result
                delay(1_000L)
                navigation.replaceCurrent(RootConfig.MainScreen)
            }
        }
        lifecycle.doOnResume(isOneTime = false) {
            mainScope.launch {
                if (
                    stack.value.active.configuration is RootConfig.MainScreen ||
                    stack.value.active.configuration is RootConfig.DetailsScreen
                    ) {
                    val result = withContext(Dispatchers.IO){ repo.getAllStates() }
                    _states.value = result
                }
            }
        }
    }
}