package com.ivi.launcher

import android.app.Application
import com.ivi.carservice.CarServiceManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class IVIApplication : Application() {

    @Inject
    lateinit var carServiceManager: CarServiceManager

    override fun onCreate() {
        super.onCreate()
        carServiceManager.connect()
    }
}
