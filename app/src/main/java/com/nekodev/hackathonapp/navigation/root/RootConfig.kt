package com.nekodev.hackathonapp.navigation.root

import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface RootConfig : Parcelable {
    @Parcelize
    data object MainScreen : RootConfig
    @Parcelize
    data class DetailsScreen(
        val orderId: Int
    ) : RootConfig
    @Parcelize
    data object SplashScreen : RootConfig
}