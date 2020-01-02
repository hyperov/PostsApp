package com.nabil.postsapp.di

import com.nabil.postsapp.posts.view.PostsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * dagger module to provide injection to main Activity
 */
@Module
abstract class PostsFragmentModule {

    @ContributesAndroidInjector
    internal abstract fun contributePostsFragmentInjector(): PostsFragment
}