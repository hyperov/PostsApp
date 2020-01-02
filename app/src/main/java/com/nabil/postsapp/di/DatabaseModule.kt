package com.nabil.postsapp.di

import android.content.Context
import androidx.room.Room
import com.nabil.postsapp.posts.model.local.PostsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun getDatabase(context: Context): PostsDatabase {
        return Room.databaseBuilder(
            context,
            PostsDatabase::class.java, "database-posts"
        ).fallbackToDestructiveMigration().build()
    }
}