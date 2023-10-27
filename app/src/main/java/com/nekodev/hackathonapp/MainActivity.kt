package com.nekodev.hackathonapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import com.nekodev.hackathonapp.navigation.root.DefaultRootComponent
import com.nekodev.hackathonapp.navigation.root.RootContent
import com.nekodev.hackathonapp.ui.theme.HackathonAppTheme
import com.nekodev.hackathonapp.util.screenModifier
import com.yandex.mapkit.MapKitFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)

        installSplashScreen().apply {
            setKeepOnScreenCondition { false }
        }

        val root = DefaultRootComponent(defaultComponentContext())

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            HackathonAppTheme {
                RootContent(
                    component = root,
                    modifier = Modifier.screenModifier().background(Color.White)
                )
            }
        }
    }
}