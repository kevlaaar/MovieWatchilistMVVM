package me.vuk.moviewatchlistmvvm

import android.app.Application
import me.vuk.moviewatchlistmvvm.db.MovieDatabase

lateinit var db: MovieDatabase

class App : Application() {
    companion object {
        lateinit var INSTANCE: Application
    }

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        db = MovieDatabase.getInstance(this)
        INSTANCE = this
    }
}