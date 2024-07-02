package com.example.agenda

import android.app.Application
import com.example.agenda.data.ContatoDatabase

class Application : Application() {

    companion object {
        var database: ContatoDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        database = ContatoDatabase.getDatabase(this)
    }
}
