package com.nekodev.hackathonapp.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nekodev.hackathonapp.network.api.DroneApi
import com.nekodev.hackathonapp.network.api.OrderApi
import com.nekodev.hackathonapp.network.datasource.NetworkDataSource
import com.nekodev.hackathonapp.network.datasource.OrdersDataSource
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
val networkModule = module {
    factory<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    single(named("droneEndpoint")) {
        Endpoint("http://91.107.125.237:8001/")
    }
    single(named("orderEndpoint")) {
        Endpoint("http://91.107.125.237:8002/api/v1/")
    }
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            namingStrategy = JsonNamingStrategy.SnakeCase
        }
    }
    single<Converter.Factory> {
        get<Json>().asConverterFactory("application/json".toMediaType())
    }

    single<DroneApi> {
        get<Retrofit>(named("droneRetrofit")).create(DroneApi::class.java)
    }

    single<OrderApi> {
        get<Retrofit>(named("orderRetrofit")).create(OrderApi::class.java)
    }

    single<OrdersDataSource> {
        NetworkDataSource(
            orderApi = get(),
            droneApi = get()
        )
        //FakeDataSource()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder().apply {
            addInterceptor(get<HttpLoggingInterceptor>())
            retryOnConnectionFailure(true)
            followRedirects(true)
        }.build()
    }

    single<Retrofit>(named("droneRetrofit")) {
        Retrofit.Builder().apply {
            baseUrl(get<Endpoint>(named("droneEndpoint")).url)
            addConverterFactory(get())
            client(get<OkHttpClient>())
        }.build()
    }

    single<Retrofit>(named("orderRetrofit")) {
        Retrofit.Builder().apply {
            baseUrl(get<Endpoint>(named("orderEndpoint")).url)
            addConverterFactory(get())
            client(get<OkHttpClient>())
        }.build()

    }
}