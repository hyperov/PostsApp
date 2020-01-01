package com.nabil.postsapp.posts.model.main

import com.nabil.postsapp.posts.model.Repository
import com.nabil.postsapp.posts.model.pojo.Post
import io.reactivex.Observable
import okhttp3.Response

class PostsMainRepo : Repository<Observable<Response>> {

    var isNetworkConnected: Boolean = true

    override fun getPosts(): Observable<List<Post>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addPost(post: Post): Observable<Response> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editPost(post: Post): Observable<Response> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePost(post: Post): Observable<Response> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addAllPosts(postsList: List<Post>): List<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}