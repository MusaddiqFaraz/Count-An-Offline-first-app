package com.faraz.app.count.di

import com.faraz.app.count.Count
import com.faraz.app.count.ui.MainVM
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by root on 11/8/18.
 */
@Singleton
@Component(modules = [AppModule::class,ActivityModule::class,AndroidSupportInjectionModule::class])
interface AppComponent {

    val mainVM: MainVM

    fun inject(count:Count)
}