package com.nekodev.hackathonapp.di

import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val imageModule = module {
    single {
        ImageLoader.Builder(androidContext())
            .memoryCache(get<MemoryCache>())
            .diskCache(get<DiskCache>())
            .respectCacheHeaders(false)
            .build()
    }

    single<MemoryCache> {
        MemoryCache.Builder(androidContext())
            .maxSizePercent(0.25)
            .build()
    }

    single<DiskCache> {
        DiskCache.Builder()
            .directory(androidContext().cacheDir.resolve("image_cache"))
            .maxSizePercent(0.02)
            .build()
    }
}