package com.faraz.app.count.di

import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.Room
import android.content.Context
import android.support.v4.util.LruCache
import com.faraz.app.count.data.AppRxSchedulers
import com.faraz.app.count.data.CountDB
import com.faraz.app.count.data.CountDao
import com.faraz.app.count.data.Data
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

/**
 * Created by root on 11/8/18.
 */
@Module
class AppModule(private val  context: Context) {

    @Singleton @Provides fun providesAppContext() = context

    @Singleton
    @Provides
    fun provideRxSchedulers() : AppRxSchedulers = AppRxSchedulers(
            io = Schedulers.io(),
            computation = Schedulers.computation(),
            main = AndroidSchedulers.mainThread()
    )

    @Singleton
    @Provides
    fun providesDB(context: Context): CountDB = Room.databaseBuilder(context, CountDB::class.java,"CountDB").build()


    @Singleton
    @Provides
    fun providesCountDao(countDB: CountDB): CountDao = countDB.countDao()

}