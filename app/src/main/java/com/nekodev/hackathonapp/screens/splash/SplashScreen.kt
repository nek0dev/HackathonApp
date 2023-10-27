package com.nekodev.hackathonapp.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nekodev.hackathonapp.R

@Composable
fun SplashScreen(
    component: SplashScreenComponent,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.dronepost),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
    }
}