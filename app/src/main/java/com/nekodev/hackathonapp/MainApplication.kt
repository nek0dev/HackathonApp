package com.nekodev.hackathonapp

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.nekodev.hackathonapp.BuildConfig.MAP_KEY
import com.nekodev.hackathonapp.di.appModule
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication: Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return get<ImageLoader>()
    }
    
    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey(MAP_KEY)
        startKoin {
            androidContext(applicationContext)
            androidLogger(Level.DEBUG)
            modules(appModule)
        }
    }
}