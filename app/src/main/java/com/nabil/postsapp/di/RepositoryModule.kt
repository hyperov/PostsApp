package com.nabil.postsapp.di

import com.nabil.postsapp.posts.model.Repository
import com.nabil.postsapp.posts.model.local.PostsLocalRepo
import com.nabil.postsapp.posts.model.main.PostsMainRepo
import com.nabil.postsapp.posts.model.pojo.Response
import com.nabil.postsapp.posts.model.remote.PostsRemoteRepo
import dagger.Binds
import dagger.Module
import io.reactivex.Observable

@Module
abstract class RepositoryModule {

    @Binds
    internal abstract fun getLocalRepo(localRepo: PostsLocalRepo): Repository<Long>

    @Binds
    internal abstract fun getRemoteRepo(remoteRepo: PostsRemoteRepo): Repository<Observable<Response>>

    @Binds
    internal abstract fun getMainRepo(mainRepo: PostsMainRepo): Repository<Observable<Response>>

}