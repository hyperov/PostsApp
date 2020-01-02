package com.nabil.postsapp.posts.model.main

import com.nabil.postsapp.annotations.Local
import com.nabil.postsapp.annotations.Remote
import com.nabil.postsapp.posts.model.Repository
import com.nabil.postsapp.posts.model.pojo.Post
import com.nabil.postsapp.posts.model.pojo.Response
import io.reactivex.Observable
import javax.inject.Inject

class PostsMainRepo @Inject constructor(
    @Local val localRepo: Repository<Int, Long>,
    @Remote val remoteRepo: Repository<Observable<Response>, Observable<Response>>
) :
    Repository<Observable<Response>, Observable<Response>> {

    var isNetworkConnected: Boolean = true

    override fun getPosts(): Observable<List<Post>> {
        return if (isNetworkConnected)
            remoteRepo.getPosts().doOnNext { posts: List<Post> -> addAllPosts(posts) }
        else
            localRepo.getPosts()
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