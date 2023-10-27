package com.nekodev.hackathonapp.navigation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.nekodev.hackathonapp.screens.details.DetailsScreenComponent
import com.nekodev.hackathonapp.screens.main.MainScreenComponent
import com.nekodev.hackathonapp.screens.splash.SplashScreenComponent
import kotlinx.coroutines.flow.StateFlow

interface RootComponent {
    val stack: StateFlow<ChildStack<*, Child>>
    sealed class Child {
        class MainScreenChild(val component: MainScreenComponent): Child()
        class DetailsScreenChild(val component: DetailsScreenComponent): Child()
        class SplashScreenChild(val component: SplashScreenComponent) : Child()
    }
}