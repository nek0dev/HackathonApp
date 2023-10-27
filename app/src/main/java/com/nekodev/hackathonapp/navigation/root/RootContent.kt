package com.nekodev.hackathonapp.navigation.root

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.nekodev.hackathonapp.screens.details.DetailsScreen
import com.nekodev.hackathonapp.screens.main.MainScreen
import com.nekodev.hackathonapp.screens.splash.SplashScreen
import com.nekodev.hackathonapp.util.screenModifier

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    val stack by component.stack.collectAsStateWithLifecycle()
    Children(
        stack = stack,
        modifier = modifier,
    ) { createdChild ->
        when (val child = createdChild.instance) {
            is RootComponent.Child.MainScreenChild -> MainScreen(
                component = child.component,
                modifier = Modifier.screenModifier().background(Color.White)
            )

            is RootComponent.Child.SplashScreenChild -> SplashScreen(
                component = child.component,
                modifier = Modifier.screenModifier().background(Color.White)
            )

            is RootComponent.Child.DetailsScreenChild -> DetailsScreen(
                component = child.component,
                modifier = Modifier.screenModifier().background(Color.White)
            )
        }
    }
}