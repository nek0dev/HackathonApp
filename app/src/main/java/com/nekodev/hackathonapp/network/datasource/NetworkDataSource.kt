package com.nekodev.hackathonapp.network.datasource

import arrow.core.Option
import arrow.core.none
import arrow.core.some
import com.nekodev.hackathonapp.model.State
import com.nekodev.hackathonapp.network.api.NetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkDataSource(
    private val api: NetworkApi
): OrdersDataSource {
    override suspend fun getStateByOrderId(orderId: Int): Option<State>{
        val response = withContext(Dispatchers.IO){ api.getStateById(orderId) }
        if (!response.isSuccessful) {
            return none()
        }
        val body = response.body()!!
        return body.asDomainModel().some()
    }
}