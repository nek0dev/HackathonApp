package com.nekodev.hackathonapp.di

import com.nekodev.hackathonapp.data.OrderRepository
import org.koin.dsl.module

val dataModule = module {
    includes(networkModule, roomModule)
    single {
        OrderRepository(get(), get())
    }
}