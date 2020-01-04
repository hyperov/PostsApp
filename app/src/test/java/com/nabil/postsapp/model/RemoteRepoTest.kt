package com.nabil.postsapp.model

import com.nabil.postsapp.posts.model.Repository
import com.nabil.postsapp.posts.model.pojo.Post
import com.nabil.postsapp.posts.model.pojo.Response
import com.nabil.postsapp.posts.model.remote.PostsNetwork
import com.nabil.postsapp.posts.model.remote.PostsRemoteRepo
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RemoteRepoTest {

    private var postsNetwork = mock(PostsNetwork::class.java)
    private lateinit var remoteRepo: Repository<Observable<Response>, Observable<Response>>
    private lateinit var post: Post

    @Before
    fun setUp() {
        remoteRepo = PostsRemoteRepo(postsNetwork)
        post = mock(Post::class.java)
    }

    @Test
    fun testRemoteGetPosts() {
        remoteRepo.getPosts(true)  //internet avaliable boolean makes no diff
        verify(postsNetwork, times(1)).getPosts()
    }

    @Test
    fun testRemoteAddPost() {
        remoteRepo.addPost(post)
        verify(postsNetwork, times(1)).addPost(post)
    }

    @Test
    fun testRemoteEditPost() {
        remoteRepo.editPost(post)
        verify(postsNetwork, times(1)).editPost(post.id)
    }

    @Test
    fun testRemoteDeletePost() {
        remoteRepo.deletePost(post)
        verify(postsNetwork, times(1)).deletePost(post.id)
    }

}
