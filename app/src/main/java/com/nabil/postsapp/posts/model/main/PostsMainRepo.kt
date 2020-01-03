package com.nabil.postsapp.posts.model.main

import com.nabil.postsapp.annotations.Local
import com.nabil.postsapp.annotations.Remote
import com.nabil.postsapp.posts.model.Repository
import com.nabil.postsapp.posts.model.pojo.Post
import com.nabil.postsapp.posts.model.pojo.Response
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class PostsMainRepo @Inject constructor(
    @Local val localRepo: Repository<Int, Long>,
    @Remote val remoteRepo: Repository<Observable<Response>, Observable<Response>>
) :
    Repository<Observable<Response>, Observable<Response>> {


    override fun getPosts(internetAvailable: Boolean): Single<List<Post>> {
        return if (internetAvailable)
            remoteRepo.getPosts(internetAvailable).doOnSuccess { posts: List<Post> -> addAllPosts(posts) }
        else
            localRepo.getPosts(internetAvailable)
    }

    override fun addPost(post: Post): Observable<Response> {
        return remoteRepo.addPost(post).doOnComplete { localRepo.addPost(post) }
    }

    override fun editPost(post: Post): Observable<Response> {
        return remoteRepo.editPost(post).doOnComplete { localRepo.editPost(post) }
    }

    override fun deletePost(post: Post): Observable<Response> {
        return remoteRepo.deletePost(post).doOnComplete { localRepo.deletePost(post) }
    }

    override fun addAllPosts(postsList: List<Post>): List<Long> {
        return localRepo.addAllPosts(postsList)
    }
}