package com.nekodev.hackathonapp.network.datasource

import arrow.core.Option
import arrow.core.none
import arrow.core.some
import com.nekodev.hackathonapp.model.OrderState
import com.nekodev.hackathonapp.network.api.DroneApi
import com.nekodev.hackathonapp.network.api.OrderApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkDataSource(
    private val droneApi: DroneApi,
    private val orderApi: OrderApi
): OrdersDataSource {
    override suspend fun getStateByOrderId(orderId: Int): Option<OrderState>{
        // Получаем ответ на запрос получения заказа по его ID
        val orderResponse = withContext(Dispatchers.IO) {
            orderApi.getOrderById(orderId)
        }
        // Ответ не с кодом 2**
        if (!orderResponse.isSuccessful) {
            // Возвращаем ничего
            return none()
        }
        val orderBody = orderResponse.body()!!

        // Получаем ответ на запрос получения состояния по её ID
        val stateResponse = withContext(Dispatchers.IO){
            droneApi.getStateById(orderId)
        }
        // Ответ не с кодом 2**
        if (!stateResponse.isSuccessful) {
            // Возвращаем только заказ
            return OrderState.OnlyOrder(
                orderId = orderId,
                dimensions = orderBody.dimensions,
                weight = orderBody.weight,
                endLatitude = orderBody.latitude,
                endLongitude = orderBody.longitude
            ).some()
        }
        // Десериализуем тело ответа
        val stateBody = stateResponse.body()!!

        return OrderState.OrderAndState(
            orderId = orderId,
            dimensions = orderBody.dimensions,
            weight = orderBody.weight,
            endLatitude = orderBody.latitude,
            endLongitude = orderBody.longitude,
            stateId = stateBody.id,
            serialNumber = stateBody.serialNumber,
            state = stateBody.state,
            currentLatitude = stateBody.latitude,
            currentLongitude = stateBody.longitude
        ).some()
    }
}