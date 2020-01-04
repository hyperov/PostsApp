package com.nabil.postsapp.local

import com.nabil.postsapp.posts.model.Repository
import com.nabil.postsapp.posts.model.local.PostsDAO
import com.nabil.postsapp.posts.model.local.PostsDatabase
import com.nabil.postsapp.posts.model.local.PostsLocalRepo
import com.nabil.postsapp.posts.model.pojo.Post
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.internal.verification.NoMoreInteractions

class LocalRepoTest {

    private lateinit var database: PostsDatabase
    private lateinit var dao: PostsDAO
    private lateinit var localRepo: Repository<Int, Long>

    @Before
    fun setUp() {
        database = mock(PostsDatabase::class.java)
        `when`(database.postsDAO()).thenReturn(mock(PostsDAO::class.java))
        dao = database.postsDAO()
        localRepo = PostsLocalRepo(database)
    }


    @Test
    fun getPosts_Room_isCalled() {
        `when`(dao.getPosts()).thenReturn(Single.just(arrayListOf()))
        localRepo.getPosts(false)
        verify(dao, times(1)).getPosts()
    }

    @Test
    fun insertPost_Room_isCalled() {
        val post = Post(1, 3, "title", "body")
        `when`(dao.addPost(post)).thenReturn(1L)
        localRepo.addPost(post)
        verify(dao, times(1)).addPost(post)
        verify(dao, NoMoreInteractions()).addPost(post)
    }

    @Test
    fun insertAllPosts_Room_isCalled() {
        val arrayListOfPosts = arrayListOf(
            Post(1, 5, "title1", "body1"),
            Post(2, 5, "title2", "body2")
        )
        `when`(dao.addAllPosts(arrayListOfPosts)).thenReturn(arrayListOf(0, 1))
        localRepo.addAllPosts(arrayListOfPosts)
        verify(dao, times(1)).addAllPosts(arrayListOfPosts)
        verify(dao, NoMoreInteractions()).addAllPosts(arrayListOfPosts)
    }

    @Test
    fun editPost_Room_isCalled() {
        val post = Post(1, 3, "title", "body")
        `when`(dao.editPost(post)).thenReturn(1)
        localRepo.editPost(post)
        verify(dao, times(1)).editPost(post)
        verify(dao, NoMoreInteractions()).editPost(post)
    }

    @Test
    fun deletePost_Room_isCalled() {
        val post = Post(1, 3, "title", "body")
        `when`(dao.deletePost(post)).thenReturn(1)
        localRepo.deletePost(post)
        verify(dao, times(1)).deletePost(post)
        verify(dao, NoMoreInteractions()).deletePost(post)

    }
}