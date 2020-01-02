package com.nabil.postsapp.di

import com.nabil.postsapp.postdetails.view.PostDetailsFragment
import com.nabil.postsapp.posts.view.PostsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * dagger module to provide injection to main Activity
 */
@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    internal abstract fun contributePostsFragmentInjector(): PostsFragment

    @ContributesAndroidInjector
    internal abstract fun contributePostsDetailsFragmentInjector(): PostDetailsFragment
}