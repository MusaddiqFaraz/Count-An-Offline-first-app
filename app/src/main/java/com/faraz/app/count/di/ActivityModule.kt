package com.faraz.app.count.di

import com.faraz.app.count.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by root on 11/8/18.
 */
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun getMainActivity(): MainActivity
}