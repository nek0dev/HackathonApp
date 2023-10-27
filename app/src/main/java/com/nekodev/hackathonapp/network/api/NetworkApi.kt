package com.nekodev.hackathonapp.network.api

import com.nekodev.hackathonapp.network.dto.StateDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {
    @GET("state/get/{order_id}")
    suspend fun getStateById(
        @Path("order_id") id: Int
    ): Response<StateDTO>
}