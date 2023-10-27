package com.nekodev.hackathonapp.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nekodev.hackathonapp.network.api.NetworkApi
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
            level = HttpLoggingInterceptor.Level.HEADERS
        }
    }
    single(named("mainEndpoint")){
        Endpoint("http://91.107.125.237:8001/")
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

    single<NetworkApi> {
        get<Retrofit>(named("noAuthRetrofit")).create(NetworkApi::class.java)
    }

    single<OrdersDataSource> {
        NetworkDataSource(get())
        //FakeDataSource()
    }

    single<OkHttpClient>(named("noAuthClient")) {
        val builder = OkHttpClient.Builder()
        with(builder){
            addInterceptor(get<HttpLoggingInterceptor>())
            retryOnConnectionFailure(true)
            followRedirects(true)
        }
        builder.build()
    }

    single<Retrofit>(named("noAuthRetrofit")) {
        Retrofit.Builder()
            .baseUrl(get<Endpoint>(named("mainEndpoint")).url)
            .addConverterFactory(get())
            .client(get<OkHttpClient>(named("noAuthClient")))
            .build()
    }
}