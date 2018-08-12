package com.faraz.app.count.data

import io.reactivex.Observable
import org.jsoup.Jsoup
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by root on 11/8/18.
 */
@Singleton
class MainRepo @Inject constructor(private val appRxSchedulers: AppRxSchedulers,
                                   private val countDao: CountDao) {

    //current time
    private val currentTime = Calendar.getInstance().timeInMillis
    //ttl is set to 24 hours, after that it'll load from network and save the new data
    private val ttl = 24 * 60 * 60 * 1000

    fun parseWebsite(url: String): Observable<Resource<String>> =
            Observable
                    //concat takes two sources and returns result of both
                    .concat(getDataFromCache(url), getDataFromNetwork(url))
                    //this is will not take data from db if if time is beyond 24 hours
                    // and will move on to take from network
                    .filter { data: Resource<Data> -> (currentTime - data.data?.ttl!!) < ttl }
                    //map converts observable  from type Resource<Data> to Resource<String> since ViewModel doesn't need to know about ttl value
                    .map { data -> Resource(data.type,data.source,data.data?.string!!) }
                    //this gives result from first observable
                    .first(Resource(TYPE.ERROR,Source.NETWORK,"")).toObservable()
                    .observeOn(appRxSchedulers.main)


    private fun getDataFromCache(url: String): Observable<Resource<Data>> {
        return countDao.getData(url).toObservable().subscribeOn(appRxSchedulers.computation)
                //convert observable from type data to Resource<Data>
                .map { data ->  Resource(TYPE.SUCCESS,Source.Database,data) }
                // first time when data is not there room throws error when used
                // with Single operator we catch the error and write back the dummy data
                .onErrorResumeNext(Observable.just(Resource(TYPE.ERROR,Source.Database,Data(0,"",""))))


    }

    private fun getDataFromNetwork(url: String): Observable<Resource<Data>> {
        //this executes network call in background thread
        return Observable.fromCallable {
            val text = Jsoup.connect(url)
                    .get().text()
            Resource(TYPE.SUCCESS,Source.NETWORK,Data(Calendar.getInstance().timeInMillis,url, text))
        }
                //when the data is ready we write it to db to update it
                .doAfterNext {

            it.data?.let {
                countDao.insertData(it)
            }

        }.onErrorResumeNext(Observable.just(Resource(TYPE.SUCCESS,Source.NETWORK,Data(0,"",""))))
                .subscribeOn(appRxSchedulers.io)
    }

}