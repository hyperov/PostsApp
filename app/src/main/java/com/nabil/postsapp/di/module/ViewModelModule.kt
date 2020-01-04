package com.nabil.postsapp.di.module

import androidx.lifecycle.ViewModelProvider
import com.nabil.postsapp.posts.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}