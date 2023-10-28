package com.nekodev.hackathonapp.network.api

import com.nekodev.hackathonapp.network.dto.OrderDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderApi {
    @GET("orders/get/{order_id}")
    suspend fun getOrderById(
        @Path("order_id") id: Int
    ): Response<OrderDTO>
}