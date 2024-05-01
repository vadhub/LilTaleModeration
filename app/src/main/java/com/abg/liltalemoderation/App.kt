package com.abg.liltalemoderation

import android.app.Application
import com.abg.liltalemoderation.data.local.AppDatabase

class App : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}