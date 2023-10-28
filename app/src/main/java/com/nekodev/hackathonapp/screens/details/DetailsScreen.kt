package com.nekodev.hackathonapp.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import com.nekodev.hackathonapp.model.OrderState
import com.nekodev.hackathonapp.screens.main.PRIMARY
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
    val listState = rememberScrollState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "ЗАКАЗ")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            component.gotoMain()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )

        },
        modifier = modifier,
        contentWindowInsets = WindowInsets(0,0,0,0)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    //.fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 14.dp)
                    .verticalScroll(listState)
                    .width(IntrinsicSize.Max)
                    .height(IntrinsicSize.Max),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (state != null && !hasError) {
                    if (state is OrderState.OrderAndState) {
                        val stateLol = (state as OrderState.OrderAndState)
                        val name = remember {
                            "Заказ №${stateLol.orderId}"
                        }
                        val weight = remember {
                            "Вес: ${stateLol.weight} г"
                        }
                        val dimensions = remember {
                            val dimensions = stateLol.dimensions
                            "Размеры груза: ${dimensions[0]} см X ${dimensions[1]} см X ${dimensions[2]} см"
                        }
                        val currentLongitude = stateLol.currentLongitude

                        val currentLatitude = stateLol.currentLatitude


                        val endLongitude = stateLol.endLongitude

                        val endLatitude = stateLol.endLatitude

                        Text(
                            text = "Текущее положение",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            fontSize = 22.sp,
                            modifier = Modifier.fillMaxWidth()
                        )

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
                                it.map.mapObjects.addPlacemark(Point(currentLatitude, currentLongitude))
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
                    }
                    if (state is OrderState.OnlyOrder) {
                        val stateLol = (state as OrderState.OnlyOrder)
                        val name = remember {
                            "Заказ №${stateLol.orderId}"
                        }
                        val weight = remember {
                            "Вес: ${stateLol.weight} г"
                        }
                        val dimensions = remember {
                            val dimensions = stateLol.dimensions
                            if (dimensions.size < 3 || dimensions.size > 3) {
                                "Не удалось получить размеры груза"
                            } else {
                                "Размеры груза: ${dimensions[0]} см X ${dimensions[1]} см X ${dimensions[2]} см"
                            }

                        }

                        val endLongitude = stateLol.endLongitude

                        val endLatitude = stateLol.endLatitude

                        Text(
                            text = "Конечное положение",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            fontSize = 22.sp,
                            modifier = Modifier.fillMaxWidth()
                        )

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
                                        Point(endLatitude, endLongitude),
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
                                containerColor = Color(250, 137, 137, 255)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(vertical = 20.dp)
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = "Дрон пока не назначен!",
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

                    }
                }
                if (state == null && !hasError) {
                    Column(
                        modifier = modifier
                            .sizeIn(maxWidth = 600.dp)
                            .fillMaxSize()
                            .width(IntrinsicSize.Max)
                            .height(IntrinsicSize.Max)
                        ,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(56.dp),
                            color = PRIMARY
                        )
                    }
                }
            }
        }

    }

}