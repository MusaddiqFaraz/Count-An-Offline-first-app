package com.faraz.app.count.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by root on 12/8/18.
 */
@Database(entities = [Data::class],version = 1)
abstract class CountDB:RoomDatabase() {
    abstract fun countDao(): CountDao
}