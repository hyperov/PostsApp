package com.nabil.postsapp.posts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nabil.postsapp.posts.model.Repository
import com.nabil.postsapp.posts.model.pojo.Post
import com.nabil.postsapp.posts.model.pojo.Response
import com.nabil.postsapp.scheduler.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainViewModel @Inject constructor(
    val repository: Repository<Observable<Response>, Observable<Response>>,
    val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val selectedPost = MutableLiveData<Post>()

    private val mutableListLoading = MutableLiveData<Boolean>()
    val listLoading: LiveData<Boolean>
        get() = mutableListLoading

    private val mutableElseLoading = MutableLiveData<Boolean>()
    val elseLoading: LiveData<Boolean>
        get() = mutableElseLoading

    private val mutableError = MutableLiveData<String>()
    val error: LiveData<String>
        get() = mutableError


    private val mutablePosts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = mutablePosts

    private val mutableAddedPost = MutableLiveData<Post>()
    val addedPost: LiveData<Post>
        get() = mutableAddedPost

    private val mutableEditedPost = MutableLiveData<Post>()
    val editedPost: LiveData<Post>
        get() = mutableEditedPost

    private val mutableDeletedPost = MutableLiveData<Post>()
    val deletedPost: LiveData<Post>
        get() = mutableDeletedPost


    fun getPosts(internetAvailable: Boolean) {

        mutableListLoading.value = true

        compositeDisposable.add(

            repository.getPosts(internetAvailable)
                .doAfterTerminate { mutableListLoading.postValue(false) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { posts: List<Post> -> mutablePosts.value = posts },
                    { t: Throwable? -> mutableError.value = "getposts: ${t?.message}" }
                )
        )
    }

    fun addPost(post: Post) {

        mutableElseLoading.value = true

        compositeDisposable.add(
            repository.addPost(post)
                .doAfterTerminate { mutableElseLoading.postValue(false) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { response: Response? -> mutableAddedPost.value = post },
                    { t: Throwable? -> mutableError.value = "addpost: ${t?.message}" }
                )
        )
    }


    fun editPost(post: Post) {

        mutableElseLoading.value = true

        compositeDisposable.add(
            repository.editPost(post)
                .doAfterTerminate { mutableElseLoading.postValue(false) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { response: Response? -> mutableEditedPost.value = post },
                    { t: Throwable? -> mutableError.value = "editpost: ${t?.message}" }
                )
        )
    }

    fun deletePost(post: Post) {

        mutableElseLoading.value = true

        compositeDisposable.add(
            repository.deletePost(post)
                .doAfterTerminate { mutableElseLoading.postValue(false) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { response: Response? -> mutableDeletedPost.value = post },
                    { t: Throwable? -> mutableError.value = "deletepost: ${t?.message}" }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
