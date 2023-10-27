package com.nekodev.hackathonapp.util

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.screenModifier() =
    this.fillMaxSize()