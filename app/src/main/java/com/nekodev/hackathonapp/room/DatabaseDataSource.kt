package com.nekodev.hackathonapp.room

import arrow.core.Option
import arrow.core.none
import arrow.core.some
import com.nekodev.hackathonapp.model.OrderState
import com.nekodev.hackathonapp.room.entities.OrderDB
import com.nekodev.hackathonapp.room.entities.StateDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseDataSource(
    private val database: AppDatabase
) {
    suspend fun getOrderById(orderId: Int): Option<OrderState> {
        return try {
            val result = withContext(Dispatchers.IO) {
                database.orderAndStateDao.getOrderAndState(
                    orderId
                )
            }
            result.asDomainModel().some()
        } catch (e: Exception) {
            none()
        }
    }

    suspend fun getAllStates(): List<OrderState> {
        val result = withContext(Dispatchers.IO) {
            database.orderAndStateDao.getAllStates()
        }
        return result.map {
            it.asDomainModel()
        }
    }

    suspend fun createOrderState(orderState: OrderState) {
        with(database.orderAndStateDao) {
            if (orderState is OrderState.OnlyOrder) {
                with(orderState) {
                    insertOrder(OrderDB(orderId, dimensions, weight, endLatitude, endLongitude))
                }
                return
            }
            if (orderState is OrderState.OrderAndState) {
                with(orderState) {
                    insertState(
                        StateDB(
                            id = stateId,
                            serialNumber = serialNumber,
                            orderId = orderId,
                            state = state,
                            latitude = currentLatitude,
                            longitude = currentLongitude
                        )
                    )
                }
            }

        }
    }

}