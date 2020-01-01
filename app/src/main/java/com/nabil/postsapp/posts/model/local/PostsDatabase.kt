package com.nabil.postsapp.posts.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nabil.postsapp.posts.model.pojo.Post

@Database(entities = [Post::class], version = 1, exportSchema = false)
abstract class PostsDatabase : RoomDatabase() {
    abstract fun postsDAO(): PostsDAO
}