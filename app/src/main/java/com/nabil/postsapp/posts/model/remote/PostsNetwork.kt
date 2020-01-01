package com.nabil.postsapp.posts.model.remote

import com.nabil.postsapp.posts.model.pojo.Post
import com.nabil.postsapp.posts.model.pojo.Response
import io.reactivex.Observable
import retrofit2.http.*

interface PostsNetwork {

    @GET("posts")
    fun getPosts(): Observable<List<Post>>

    @POST("posts")
    fun addPost(post: Post): Observable<Response>

    @PUT("posts/{id}")
    fun editPost(@Path("id") id: Int): Observable<Response>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Observable<Response>

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    }
}