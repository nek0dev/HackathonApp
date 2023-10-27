package com.nekodev.hackathonapp.room

import arrow.core.Option
import arrow.core.none
import arrow.core.some
import com.nekodev.hackathonapp.model.State
import com.nekodev.hackathonapp.room.entities.OrderDB
import com.nekodev.hackathonapp.room.entities.StateDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseDataSource(
    private val database: AppDatabase
) {
    suspend fun getOrderById(orderId: Int): Option<State> {
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

    suspend fun getAllStates(): List<State> {
        val result = withContext(Dispatchers.IO) {
            database.orderAndStateDao.getAllStates()
        }
        return result.map {
            it.asDomainModel()
        }
    }

    suspend fun createState(state: State) {
        val orderId = state.order.id
        with(database.orderAndStateDao) {
            with(state.order) {
                insertOrder(OrderDB(id, dimensions, weight, latitude, longitude))
                updateOrder(id, state.state)
            }
            with(state) {
                insertState(
                    StateDB(
                        id = id,
                        serialNumber = serialNumber,
                        orderId = orderId,
                        state = state.state,
                        latitude = latitude,
                        longitude = longitude
                    )
                )
            }
        }
    }

}