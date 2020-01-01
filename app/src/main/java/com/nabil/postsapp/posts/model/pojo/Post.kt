package com.nabil.postsapp.posts.model.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post")
data class Post(

    @PrimaryKey
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)