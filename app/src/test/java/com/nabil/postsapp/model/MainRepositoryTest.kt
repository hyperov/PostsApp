package com.nabil.postsapp.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nabil.postsapp.posts.model.local.PostsLocalRepo
import com.nabil.postsapp.posts.model.main.PostsMainRepo
import com.nabil.postsapp.posts.model.pojo.Post
import com.nabil.postsapp.posts.model.pojo.Response
import com.nabil.postsapp.posts.model.remote.PostsRemoteRepo
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*


class MainRepositoryTest {


    private lateinit var localRepo: PostsLocalRepo
    private lateinit var remoteRepo: PostsRemoteRepo
    private lateinit var mainRepo: PostsMainRepo

    private lateinit var post: Post

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        localRepo = mock(PostsLocalRepo::class.java)
        remoteRepo = mock(PostsRemoteRepo::class.java)
        mainRepo = PostsMainRepo(localRepo, remoteRepo)

        post = Post(1, 5, "title", "body")

    }

    @Test
    fun getPosts_ReadFromRemote_if_connected() {
        doReturn(Single.just(arrayListOf(post))).`when`(remoteRepo).getPosts(true)
        mainRepo.getPosts(true)
        verify(remoteRepo, times(1)).getPosts(true)
        verify(localRepo, never()).getPosts(true)
    }

    @Test
    fun getPosts_ReadFromLocal_if_not_connected() {
        mainRepo.getPosts(false)
        verify(localRepo, times(1)).getPosts(false)
        verify(remoteRepo, never()).getPosts(false)
    }

    @Test
    fun addPost() {
        doReturn(Observable.just(Response(post.id))).`when`(remoteRepo).addPost(post)
        mainRepo.addPost(post)
        verify(remoteRepo, times(1)).addPost(post)
        verify(localRepo, never()).addPost(post)
    }

    @Test
    fun addAllPosts_From_Local() {
        mainRepo.addAllPosts(arrayListOf(post))
        verify(localRepo, times(1)).addAllPosts(arrayListOf(post))
        verify(remoteRepo, never()).addAllPosts(arrayListOf(post))
    }

    @Test
    fun editPost() {
        doReturn(Observable.just(Response(post.id))).`when`(remoteRepo).editPost(post)
        mainRepo.editPost(post)
        verify(remoteRepo, times(1)).editPost(post)
        verify(localRepo, never()).editPost(post)
    }

    @Test
    fun deletePost() {
        doReturn(Observable.just(Response(post.id))).`when`(remoteRepo).deletePost(post)
        mainRepo.deletePost(post)
        verify(remoteRepo, times(1)).deletePost(post)
        verify(localRepo, never()).deletePost(post)
    }
}