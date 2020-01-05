package com.nabil.postsapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nabil.postsapp.posts.model.Repository
import com.nabil.postsapp.posts.model.main.PostsMainRepo
import com.nabil.postsapp.posts.model.pojo.Post
import com.nabil.postsapp.posts.model.pojo.Response
import com.nabil.postsapp.posts.viewmodel.MainViewModel
import com.nabil.postsapp.scheduler.TrampolineSchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*


class PostsViewModelTest {

    private lateinit var postsRepository: Repository<Observable<Response>, Observable<Response>>
    private lateinit var mainViewModel: MainViewModel

    private lateinit var progressObserver: Observer<Boolean>
    private lateinit var progressElseObserver: Observer<Boolean>
    private lateinit var errorObserver: Observer<String>

    private lateinit var postsObserver: Observer<List<Post>>
    private lateinit var postEditedObserver: Observer<Post>
    private lateinit var postAddedObserver: Observer<Post>
    private lateinit var postDeletedObserver: Observer<Post>

    private lateinit var post: Post

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        post = Post(1, 5, "title", "body")

        postsRepository = mock(PostsMainRepo::class.java)
        mainViewModel = MainViewModel(postsRepository, TrampolineSchedulerProvider())

        progressObserver = mock(Observer<Boolean> {}.javaClass)
        progressElseObserver = mock(Observer<Boolean> {}.javaClass)

        errorObserver = mock(Observer<String> {}.javaClass)

        postsObserver = mock(Observer<List<Post>> {}.javaClass)
        postEditedObserver = mock(Observer<Post> {}.javaClass)
        postAddedObserver = mock(Observer<Post> {}.javaClass)
        postDeletedObserver = mock(Observer<Post> {}.javaClass)

        mainViewModel.listLoading.observeForever(progressObserver)
        mainViewModel.elseLoading.observeForever(progressElseObserver)
        mainViewModel.error.observeForever(errorObserver)

        mainViewModel.posts.observeForever(postsObserver)

        mainViewModel.addedPost.observeForever(postAddedObserver)
        mainViewModel.editedPost.observeForever(postEditedObserver)
        mainViewModel.deletedPost.observeForever(postDeletedObserver)

        `when`(postsRepository.getPosts(true))
            .thenReturn(Single.just(arrayListOf(post)))

        `when`(postsRepository.getPosts(false))
            .thenReturn(Single.just(arrayListOf(post)))

        `when`(postsRepository.addPost(post))
            .thenReturn(Observable.just(Response(post.id)))

        `when`(postsRepository.editPost(post))
            .thenReturn(Observable.just(Response(post.id)))

        `when`(postsRepository.deletePost(post))
            .thenReturn(Observable.just(Response(post.id)))


    }

    @After
    fun tearDown() {
    }

    @Test
    fun test_getPosts() {
        mainViewModel.getPosts(false)
        verify(postsRepository).getPosts(false)
    }

    @Test
    fun test_addPost() {
        mainViewModel.addPost(post)
        verify(postsRepository).addPost(post)
    }

    @Test
    fun test_editPost() {
        mainViewModel.editPost(post)
        verify(postsRepository).editPost(post)
    }

    @Test
    fun test_deletePost() {
        mainViewModel.deletePost(post)
        verify(postsRepository).deletePost(post)
    }

    @Test
    fun test_listLoadingLiveData_from_getPosts() {
        mainViewModel.getPosts(true)

        verify(progressObserver).onChanged(true)
        verify(progressObserver).onChanged(false)
    }

    @Test
    fun test_listLoadingLiveData_value_from_addPost() {

        mainViewModel.addPost(post)
        verify(progressElseObserver).onChanged(true)
        verify(progressElseObserver).onChanged(false)
    }

    @Test
    fun test_listLoadingLiveData_value_from_editPost() {

        mainViewModel.editPost(post)
        verify(progressElseObserver).onChanged(true)
        verify(progressElseObserver).onChanged(false)
    }

    @Test
    fun test_listLoadingLiveData_value_from_deletePost() {

        mainViewModel.deletePost(post)
        verify(progressElseObserver).onChanged(true)
        verify(progressElseObserver).onChanged(false)
    }

    @Test
    fun test_errorLiveData_value() {

        `when`(postsRepository.getPosts(true))
            .then { Single.error<Any>(Exception("error")) }

        `when`(postsRepository.addPost(post))
            .then { Observable.error<Any>(Exception("error")) }

        `when`(postsRepository.editPost(post))
            .then { Observable.error<Any>(Exception("error")) }

        `when`(postsRepository.deletePost(post))
            .then { Observable.error<Any>(Exception("error")) }

        mainViewModel.getPosts(true)
        verify(errorObserver).onChanged("getposts: error")

        mainViewModel.addPost(post)
        verify(errorObserver).onChanged("addpost: error")

        mainViewModel.editPost(post)
        verify(errorObserver).onChanged("editpost: error")

        mainViewModel.deletePost(post)
        verify(errorObserver).onChanged("deletepost: error")
    }

    @Test
    fun test_postsLiveData_getPosts() {
        mainViewModel.getPosts(false)
        verify(postsObserver).onChanged(arrayListOf(post))
        verify(postsObserver).onChanged(arrayListOf(post))
    }

    @Test
    fun test_addedPostsLiveData_addPost() {
        mainViewModel.addPost(post)
        verify(postAddedObserver).onChanged(post)
        verify(postAddedObserver).onChanged(post)
    }

    @Test
    fun test_editPostsLiveData_editPost() {
        mainViewModel.editPost(post)
        verify(postEditedObserver).onChanged(post)
        verify(postEditedObserver).onChanged(post)
    }

    @Test
    fun test_deletePostsLiveData_deletePost() {
        mainViewModel.deletePost(post)
        verify(postDeletedObserver).onChanged(post)
        verify(postDeletedObserver).onChanged(post)
    }

}