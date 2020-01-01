package com.nabil.postsapp.posts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nabil.postsapp.annotations.Main
import com.nabil.postsapp.posts.model.Repository
import com.nabil.postsapp.posts.model.pojo.Response
import com.nabil.postsapp.scheduler.BaseSchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    @Main val repository: Repository<Observable<Response>>, val schedulerProvider: BaseSchedulerProvider
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(
                    repository,
                    schedulerProvider
                ) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}