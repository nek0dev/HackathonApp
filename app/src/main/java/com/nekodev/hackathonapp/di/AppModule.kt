package com.nekodev.hackathonapp.di

import org.koin.dsl.module

val appModule = module {
    includes(imageModule, dataModule, dataStoreModule, networkModule, roomModule)
}

