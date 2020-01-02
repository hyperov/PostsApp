package com.nabil.postsapp.di

import com.nabil.postsapp.annotations.Local
import com.nabil.postsapp.annotations.Main
import com.nabil.postsapp.annotations.Remote
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
    @Local
    internal abstract fun getLocalRepo(localRepo: PostsLocalRepo): Repository<Int, Long>

    @Binds
    @Remote
    internal abstract fun getRemoteRepo(remoteRepo: PostsRemoteRepo): Repository<Observable<Response>, Observable<Response>>

    @Binds
    @Main
    internal abstract fun getMainRepo(mainRepo: PostsMainRepo): Repository<Observable<Response>, Observable<Response>>

}