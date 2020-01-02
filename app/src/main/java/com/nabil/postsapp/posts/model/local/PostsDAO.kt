package com.nabil.postsapp.posts.model.local

import androidx.room.*
import com.nabil.postsapp.posts.model.pojo.Post
import io.reactivex.Observable

@Dao
interface PostsDAO {

    @Query("SELECT * FROM post")
    fun getPosts(): Observable<List<Post>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPost(post: Post): Long

    @Update
    fun editPost(post: Post): Int

    @Delete
    fun deletePost(post: Post): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAllPosts(postsList: List<Post>): List<Long>
}