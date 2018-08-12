package com.faraz.app.count.data

import io.reactivex.Scheduler

/**
 * Created by root on 11/8/18.
 */
data class AppRxSchedulers(
        val io: Scheduler,
        val computation: Scheduler,
        val main: Scheduler
)