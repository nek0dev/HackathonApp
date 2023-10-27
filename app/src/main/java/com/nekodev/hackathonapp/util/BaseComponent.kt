package com.nekodev.hackathonapp.util

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import org.koin.core.component.KoinComponent
import kotlin.coroutines.CoroutineContext

abstract class BaseComponent(
    componentContext: ComponentContext,
): KoinComponent, ComponentContext by componentContext {
    // The scope is automatically cancelled when the component is destroyed
    val ioScope = componentContext.coroutineScope(Dispatchers.IO)
    val mainScope = componentContext.coroutineScope(Dispatchers.Main)
}

fun LifecycleOwner.coroutineScope(context: CoroutineContext): CoroutineScope {
    val scope = CoroutineScope(context)
    lifecycle.doOnDestroy(scope::cancel)

    return scope
}