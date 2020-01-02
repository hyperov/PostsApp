package com.nabil.postsapp.posts.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.nabil.postsapp.R
import com.nabil.postsapp.posts.viewmodel.MainViewModel
import com.nabil.postsapp.posts.viewmodel.ViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class PostsFragment : Fragment(), HasAndroidInjector {

    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        fun newInstance() = PostsFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.posts_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        observeLoadingAndError()
        observeData()
        getPosts()

    }

    private fun getPosts() {
        viewModel.getPosts()
    }

    private fun observeData() {
        viewModel.posts.observe(this, Observer { posts -> })
        viewModel.addedPost.observe(this, Observer { post -> })
        viewModel.editedPost.observe(this, Observer { post -> })
        viewModel.deletedPost.observe(this, Observer { post -> })
    }

    private fun observeLoadingAndError() {
        viewModel.listLoading.observe(this, Observer { t: Boolean -> })
        viewModel.elseLoading.observe(this, Observer { t: Boolean -> })
        viewModel.error.observe(this, Observer { t: String -> })
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    private fun View.showSnackBar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG)
            .show()
    }

}
