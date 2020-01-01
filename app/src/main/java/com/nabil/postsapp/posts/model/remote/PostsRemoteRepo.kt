package com.nabil.postsapp.posts.model.remote

import com.nabil.postsapp.posts.model.Repository
import com.nabil.postsapp.posts.model.pojo.Post
import com.nabil.postsapp.posts.model.pojo.Response
import io.reactivex.Observable
import javax.inject.Inject

class PostsRemoteRepo @Inject constructor(
    val postsNetwork: PostsNetwork
) :
    Repository<Observable<Response>> {

    override fun getPosts() = postsNetwork.getPosts()

    override fun addPost(post: Post) = postsNetwork.addPost(post)

    override fun editPost(post: Post) = postsNetwork.editPost(post.id)

    override fun deletePost(post: Post) = postsNetwork.deletePost(post.id)

    override fun addAllPosts(postsList: List<Post>) = emptyList<Long>()
}