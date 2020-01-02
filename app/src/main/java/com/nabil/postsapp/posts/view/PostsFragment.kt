package com.nabil.postsapp.posts.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.nabil.postsapp.R
import com.nabil.postsapp.databinding.PostsFragmentBinding
import com.nabil.postsapp.posts.viewmodel.MainViewModel
import com.nabil.postsapp.posts.viewmodel.ViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.posts_fragment.*
import javax.inject.Inject

class PostsFragment : Fragment(), HasAndroidInjector {

    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel

    private lateinit var postsFragmentBinding: PostsFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        postsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.posts_fragment, container, false)

        return postsFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel =
            ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)

        observeLoadingAndError()
        observeData()
        initAddListener()
        getPosts()

    }

    private fun initAddListener() {
        fabAdd.setOnClickListener {
//            viewModel.addPost()
        }
    }

    private fun getPosts() {
        viewModel.getPosts()
    }

    private fun observeData() {
        viewModel.apply {
            posts.observe(this@PostsFragment, Observer { posts -> })
            addedPost.observe(this@PostsFragment, Observer { post -> })
            editedPost.observe(this@PostsFragment, Observer { post -> })
            deletedPost.observe(this@PostsFragment, Observer { post -> })
        }
    }

    private fun observeLoadingAndError() {
        viewModel.apply {

            listLoading.observe(
                this@PostsFragment,
                Observer { isProgress: Boolean -> postsFragmentBinding.isProgress = isProgress })

            elseLoading.observe(this@PostsFragment, Observer { t: Boolean -> })

            error.observe(this@PostsFragment,
                Observer { t: String -> rvPosts.showSnackBar(t) })
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    private fun View.showSnackBar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG)
            .show()
    }


    companion object {
        fun newInstance() = PostsFragment()
    }


}
