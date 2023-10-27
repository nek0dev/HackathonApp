package com.nekodev.hackathonapp.di

import androidx.room.Room
import com.google.gson.Gson
import com.nekodev.hackathonapp.room.AppDatabase
import com.nekodev.hackathonapp.room.DatabaseDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomModule = module{
    single<Gson> {
        Gson()
    }
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "database"
        ).build()
    }
    single {
        DatabaseDataSource(get())
    }
}