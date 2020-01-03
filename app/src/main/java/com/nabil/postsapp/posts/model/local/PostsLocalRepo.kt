package com.nabil.postsapp.posts.model.local

import com.nabil.postsapp.posts.model.Repository
import com.nabil.postsapp.posts.model.pojo.Post
import javax.inject.Inject

class PostsLocalRepo @Inject constructor( val database: PostsDatabase) : Repository<Int,Long> {

    private val databaseDAO = database.postsDAO()

    override fun getPosts(internetAvailable: Boolean) = databaseDAO.getPosts()

    override fun addPost(post: Post) = databaseDAO.addPost(post)

    override fun editPost(post: Post) = databaseDAO.editPost(post)

    override fun deletePost(post: Post) = databaseDAO.deletePost(post)

    override fun addAllPosts(postsList: List<Post>) = databaseDAO.addAllPosts(postsList)


}