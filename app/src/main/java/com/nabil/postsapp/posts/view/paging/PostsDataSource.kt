package com.nabil.postsapp.posts.view.paging

import androidx.paging.PositionalDataSource
import com.nabil.postsapp.posts.model.pojo.Post

class PostsDataSource : PositionalDataSource<Post>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Post>) {
        
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Post>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}