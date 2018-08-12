package com.faraz.app.count.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single

/**
 * Created by root on 12/8/18.
 */
@Dao
abstract class CountDao {

    @Query("SELECT * FROM DATA WHERE key = :url")
    abstract fun getData(url:String): Single<Data>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertData(data: Data): Long
}