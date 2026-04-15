package com.example.ecolog

import android.app.Application

class EcoLogApplication : Application() {
    lateinit var dataStoreManager: DataStoreManager
        private set

    override fun onCreate() {
        super.onCreate()
        dataStoreManager = DataStoreManager(this)
    }
}
