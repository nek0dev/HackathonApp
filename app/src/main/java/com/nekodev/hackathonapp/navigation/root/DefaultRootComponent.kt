package com.nekodev.hackathonapp.navigation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.nekodev.hackathonapp.navigation.root.RootComponent.Child.DetailsScreenChild
import com.nekodev.hackathonapp.navigation.root.RootComponent.Child.MainScreenChild
import com.nekodev.hackathonapp.navigation.root.RootComponent.Child.SplashScreenChild
import com.nekodev.hackathonapp.screens.details.RealDetailsScreenComponent
import com.nekodev.hackathonapp.screens.main.RealMainScreenComponent
import com.nekodev.hackathonapp.screens.splash.RealSplashScreenComponent
import com.nekodev.hackathonapp.util.BaseComponent
import com.nekodev.hackathonapp.util.toStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

    private fun child(config: RootConfig, componentContext: ComponentContext): RootComponent.Child {
        return when(config){
            is RootConfig.MainScreen -> MainScreenChild(
                RealMainScreenComponent(
                    componentContext = componentContext,
                    navigateToDetails = {
                        mainScope.launch {
                            navigation.replaceAll(RootConfig.MainScreen, RootConfig.DetailsScreen(it))
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
                    navigateToMain = {
                        mainScope.launch {
                            navigation.replaceAll(RootConfig.MainScreen)
                        }
                    }
                )
            )
        }
    }

    init {
        lifecycle.doOnCreate {
            mainScope.launch {
                delay(1_000L)
                navigation.replaceAll(RootConfig.MainScreen)
            }
        }
    }
}