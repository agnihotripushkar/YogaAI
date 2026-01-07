package me.pushkaragnihotri.yogaai

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class YogaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@YogaApp)
            modules(me.pushkaragnihotri.yogaai.features.di.featuresModule)
        }
    }
}
