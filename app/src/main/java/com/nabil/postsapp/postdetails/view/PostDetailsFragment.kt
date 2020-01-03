package com.nabil.postsapp.postdetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.nabil.postsapp.R
import com.nabil.postsapp.databinding.PostDetailsFragmentBinding
import com.nabil.postsapp.posts.viewmodel.MainViewModel
import com.nabil.postsapp.posts.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class PostDetailsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel

    private lateinit var postDetailsFragmentBinding: PostDetailsFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        postDetailsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.post_details_fragment, container, false)

        return postDetailsFragmentBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel =
            ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)

        viewModel.selectedPost.observe(
            this,
            Observer { post -> postDetailsFragmentBinding.post = post })

    }

    companion object {
        fun newInstance() = PostDetailsFragment()
    }


}
