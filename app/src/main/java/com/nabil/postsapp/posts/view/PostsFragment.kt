package com.nabil.postsapp.posts.view

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.test.espresso.idling.CountingIdlingResource
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.nabil.postsapp.R
import com.nabil.postsapp.databinding.PostsFragmentBinding
import com.nabil.postsapp.posts.model.pojo.Post
import com.nabil.postsapp.posts.viewmodel.MainViewModel
import com.nabil.postsapp.posts.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
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

    private val espressoTestIdlingResource = CountingIdlingResource("Network_Call")

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
                this.editPosition = editPosition
                showDialog(post)
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
        if (espressoTestIdlingResource.isIdleNow)
            espressoTestIdlingResource.increment()
        viewModel.getPosts(isInternetAvailable(activity!!))
    }

    private fun observeData() {
        viewModel.apply {
            posts.observe(
                viewLifecycleOwner,
                Observer { posts ->
                    postAdapter.swapData(posts as ArrayList<Post>)
                    if (espressoTestIdlingResource.isIdleNow.not())
                        espressoTestIdlingResource.decrement()
                })

            addedPost.observe(
                viewLifecycleOwner,
                Observer { post ->
                    Observable.fromCallable { postAdapter.addPost(post) }
                        .subscribe { rvPosts.smoothScrollToPosition(postAdapter.itemCount) }
                })

            editedPost.observe(
                viewLifecycleOwner,
                Observer { post -> postAdapter.notifyItemChanged(editPosition) })

            deletedPost.observe(
                viewLifecycleOwner,
                Observer { post -> postAdapter.notifyItemRemoved(delPosition) })
        }
    }

    private fun observeLoadingAndError() {
        viewModel.apply {

            listLoading.observe(
                viewLifecycleOwner,
                Observer { isProgress: Boolean -> postsFragmentBinding.isProgress = isProgress })

            elseLoading.observe(viewLifecycleOwner, Observer { t: Boolean -> })

            error.observe(viewLifecycleOwner,
                Observer { t: String ->
                    rvPosts.showSnackBar(t)
                    Log.e("error: ", t)
                    if (espressoTestIdlingResource.isIdleNow.not())
                        espressoTestIdlingResource.decrement()

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

    private fun showDialog(post: Post) {
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_post, null, false)
        val etTitle = view?.findViewById<EditText>(R.id.etTitle)
        val etBody = view?.findViewById<EditText>(R.id.etBody)
        val btOk = view?.findViewById<MaterialButton>(R.id.btOk)
        val btCancel = view?.findViewById<MaterialButton>(R.id.btCancel)

        val alertDialog = builder?.setView(view)?.create()


        btCancel?.setOnClickListener { alertDialog?.dismiss() }
        btOk?.setOnClickListener {
            when {
                etTitle?.text?.isBlank()!! -> etTitle.error = "please enter valid title"
                etBody?.text?.isBlank()!! -> etBody.error = "please enter valid body"
                else -> {
                    post.title = etTitle.text.toString()
                    post.body = etBody.text.toString()
                    viewModel.editPost(post)
                    alertDialog?.dismiss()
                }
            }
        }

        alertDialog!!.show()
    }

    fun getEspressoIdlingResource(): CountingIdlingResource {
        return espressoTestIdlingResource
    }


}
