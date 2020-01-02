package com.nabil.postsapp.posts.model

import com.nabil.postsapp.posts.model.pojo.Post
import io.reactivex.Observable

interface Repository<T,U> {

    fun getPosts(): Observable<List<Post>>

    fun addPost(post: Post): U

    fun editPost(post: Post): T

    fun deletePost(post: Post): T

    fun addAllPosts(postsList: List<Post>): List<Long>
}