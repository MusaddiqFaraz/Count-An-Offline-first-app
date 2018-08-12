package com.faraz.app.count.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import io.reactivex.Observable

/**
 * Created by root on 11/8/18.
 */

enum class Source{
    Database,
    NETWORK
}

enum class TYPE {
    SUCCESS,
    ERROR
}
data class Resource<out T>(val type: TYPE,val source: Source,val data: T?)


@Entity
data class Data(val ttl:Long,@PrimaryKey val key: String,val string: String)

data class ListData(val source: Source,val map:Map<String, Int>)
data class Filter(var removeCommonWords:Boolean,var wordsLimit: Int,var removeShortWords:Boolean)