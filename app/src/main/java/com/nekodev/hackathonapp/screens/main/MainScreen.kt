package com.nekodev.hackathonapp.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nekodev.hackathonapp.R
import com.nekodev.hackathonapp.model.OrderState

val SECONDARY = Color(0xFFEDF1FD)
val PRIMARY = Color(0xFF3D73FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    component: MainScreenComponent,
    modifier: Modifier = Modifier
) {
    val query = remember {
        mutableStateOf("")
    }
    val states by component.states.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "ЗАКАЗЫ")
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )

        },
        contentWindowInsets = WindowInsets(0,0,0,0)
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .navigationBarsPadding()
                .padding(it)
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = query.value,
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        query.value = it
                    }
                },
                shape = RoundedCornerShape(12.dp),
                maxLines = 1,
                label = {
                    Text("Номер заказа")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        component.makeSearch(query.value)
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            component.makeSearch(query.value)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PRIMARY,
                    unfocusedBorderColor = PRIMARY,
                    focusedLabelColor = PRIMARY,
                    unfocusedLabelColor = PRIMARY,
                    focusedTrailingIconColor = PRIMARY,
                    unfocusedTrailingIconColor = PRIMARY,
                    cursorColor = PRIMARY
                )
            )
            val listState = rememberLazyGridState()
            LazyVerticalGrid(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = listState,
                contentPadding = PaddingValues(vertical = 10.dp),
                columns = GridCells.Adaptive(300.dp)
            ) {
                items(states.size) { index ->
                    val state = states[index]
                    if (state is OrderState.OnlyOrder) {
                        val name = remember {
                            "Заказ №${state.orderId}"
                        }
                        ElevatedCard(
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = SECONDARY
                            ),
                            shape = RoundedCornerShape(12.dp),
                            onClick = {
                                component.selectState(orderId = state.orderId)
                            }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(vertical = 20.dp)
                                    .padding(horizontal = 16.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.clock_loader_60_24px),
                                    contentDescription = null,
                                    tint = PRIMARY
                                )

                                Text(
                                    text = name,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    if (state is OrderState.OrderAndState) {
                        val name = remember {
                            "Заказ №${state.orderId}"
                        }
                        ElevatedCard(
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = SECONDARY
                            ),
                            shape = RoundedCornerShape(12.dp),
                            onClick = {
                                component.selectState(orderId = state.orderId)
                            }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(vertical = 20.dp)
                                    .padding(horizontal = 16.dp)
                            ) {
                                if (state.state == "in base"){
                                    Icon(
                                        painter = painterResource(id = R.drawable.check_circle_24px),
                                        contentDescription = null,
                                        tint = PRIMARY
                                    )
                                } else if (state.state == "in delivery") {
                                    Icon(
                                        painter = painterResource(id = R.drawable.clock_loader_60_24px),
                                        contentDescription = null,
                                        tint = PRIMARY
                                    )
                                }

                                Text(
                                    text = name,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}