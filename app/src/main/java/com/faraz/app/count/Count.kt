package com.faraz.app.count

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import com.faraz.app.count.di.AppComponent
import com.faraz.app.count.di.AppInjector
import com.faraz.app.count.di.AppModule
import com.faraz.app.count.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject



val appInstance: Count by lazy {
    Count.appInstance!!
}

class Count: Application() , HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @set:VisibleForTesting
    lateinit var component: AppComponent

    override fun activityInjector() = dispatchingAndroidInjector

    companion object {

        var appInstance: Count? = null
        private val TAG = "Count"
    }

    override fun onCreate() {
        appInstance = this
        super.onCreate()

        component = DaggerAppComponent.builder().appModule(AppModule(applicationContext)).build()
        AppInjector.init(this)
    }
}

val Context.component: AppComponent
    get() = (applicationContext as Count).component

val Fragment.component: AppComponent
    get() = activity!!.component



