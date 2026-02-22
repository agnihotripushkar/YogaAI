package me.pushkaragnihotri.yogaai

import android.app.Application
import me.pushkaragnihotri.yogaai.core.di.featuresModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get()) }
}

class YogaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        startKoin {
            androidLogger()
            androidContext(this@YogaApp)
            modules(
                featuresModule,
                me.pushkaragnihotri.yogaai.core.di.coreDataModule,
                appModule
            )
        }
    }
}
