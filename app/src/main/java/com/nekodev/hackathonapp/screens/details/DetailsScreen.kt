package com.nekodev.hackathonapp.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nekodev.hackathonapp.screens.main.SECONDARY
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    component: DetailsScreenComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.collectAsStateWithLifecycle()
    val hasError by component.hasError.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .fillMaxHeight()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp)
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "ЗАКАЗ",
            fontSize = 20.sp,
            color = Color.Black
        )
        if (state != null && !hasError) {
            val name = remember {
                "Заказ №${state!!.order.id}"
            }
            val weight = remember {
                "Вес: ${state!!.order.weight} г"
            }
            val dimensions = remember {
                val dimensions = state!!.order.dimensions
                "Размеры груза: ${dimensions[0]} см X ${dimensions[0]} см X ${dimensions[0]} см"
            }
            val currentLongitude = remember {
                state!!.longitude
            }
            val currentLatitude = remember {
                state!!.latitude
            }

            val endLongitude = remember {
                state!!.order.longitude
            }
            val endLatitude = remember {
                state!!.order.latitude
            }
            AndroidView(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White)
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f),
                factory = {
                    MapKitFactory.getInstance().onStart()
                    MapView(it).apply {
                        onStart()
                        map.isScrollGesturesEnabled = false
                        map.isZoomGesturesEnabled = false
                        map.isRotateGesturesEnabled = false
                        map.isTiltGesturesEnabled = false
                    }
                },
                update = {
                    it.map.move(
                        CameraPosition(
                            Point(currentLatitude, currentLongitude),
                            13f, 0.0f, 0.0f
                        )
                    )
                    it.map.mapObjects.addPlacemark(Point(endLatitude, endLongitude))
                }

            )

            Text(
                text = name,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = weight,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = dimensions,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium
            )

            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = SECONDARY
                ),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    component.openMaps(currentLatitude, currentLongitude)
                }
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Текущее положение",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = SECONDARY
                ),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    component.openMaps(endLatitude, endLongitude)
                }
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Конечное положение",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            return
        }
        if (state == null && !hasError) {
            Column(
                modifier = modifier
                    .fillMaxHeight()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 14.dp)
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator()
            }
        }
    }
}