package com.nabil.postsapp.posts.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.nabil.postsapp.R
import com.nabil.postsapp.databinding.PostsFragmentBinding
import com.nabil.postsapp.posts.model.pojo.Post
import com.nabil.postsapp.posts.viewmodel.MainViewModel
import com.nabil.postsapp.posts.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.posts_fragment.*
import javax.inject.Inject

class PostsFragment : DaggerFragment() {


    private var delPosition: Int = -1
    private var editPosition: Int = -1
    private lateinit var postAdapter: PostsAdapter

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

        initAdapter()
        initUI()
        observeLoadingAndError()
        observeData()
        initAddListener()
        getPosts()

    }

    private fun initUI() {
        rvPosts.apply {
            itemAnimator = DefaultItemAnimator()
            rvPosts.adapter = postAdapter
        }
    }

    private fun initAdapter() {
        postAdapter = PostsAdapter(
            arrayListOf(),
            onClick = {
                findNavController().navigate(R.id.postDetailsFragment)
                viewModel.selectedPost.value = it
            },
            onEditPost = { post, editPosition ->
                viewModel.editPost(post)
                this.editPosition = editPosition
            },
            onDeletePost = { post, delPosition ->
                viewModel.deletePost(post)
                this.delPosition = delPosition
            })
    }

    private fun initAddListener() {
        fabAdd.setOnClickListener {
            viewModel.addPost(
                Post(
                    ++DEFAULT_POSTS_SIZE,
                    1,
                    "This is added title ${++DEFAULT_POSTS_ADDED}",
                    "THIS IS BODY"
                )
            )
        }
    }

    private fun getPosts() {
        viewModel.getPosts(isInternetAvailable(activity!!))
    }

    private fun observeData() {
        viewModel.apply {
            posts.observe(
                this@PostsFragment,
                Observer { posts -> postAdapter.swapData(posts as ArrayList<Post>) })

            addedPost.observe(
                this@PostsFragment,
                Observer { post ->
                    Observable.fromCallable { postAdapter.addPost(post) }
                        .subscribe { rvPosts.smoothScrollToPosition(postAdapter.itemCount) }
                })

            editedPost.observe(
                this@PostsFragment,
                Observer { post -> postAdapter.notifyItemChanged(editPosition) })

            deletedPost.observe(
                this@PostsFragment,
                Observer { post -> postAdapter.notifyItemRemoved(delPosition) })
        }
    }

    private fun observeLoadingAndError() {
        viewModel.apply {

            listLoading.observe(
                this@PostsFragment,
                Observer { isProgress: Boolean -> postsFragmentBinding.isProgress = isProgress })

            elseLoading.observe(this@PostsFragment, Observer { t: Boolean -> })

            error.observe(this@PostsFragment,
                Observer { t: String ->
                    rvPosts.showSnackBar(t)
                    Log.e("error: ", t)
                })
        }
    }

    private fun View.showSnackBar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG)
            .show()
    }


    companion object {
        fun newInstance() = PostsFragment()
        private var DEFAULT_POSTS_SIZE = 100
        private var DEFAULT_POSTS_ADDED = 0
    }

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }


}
