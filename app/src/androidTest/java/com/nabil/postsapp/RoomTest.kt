package com.nabil.postsapp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nabil.postsapp.posts.model.local.PostsDAO
import com.nabil.postsapp.posts.model.local.PostsDatabase
import com.nabil.postsapp.posts.model.pojo.Post
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class RoomTest {


    lateinit var database: PostsDatabase
    private lateinit var postsDao: PostsDAO

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            PostsDatabase::class.java
        ).allowMainThreadQueries().build()

        postsDao = database.postsDAO()

    }

    @Test
    fun test_database_is_created() {
        assert(database.isOpen)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPostsWithoutInsertSuccessWithEmptyArray() {

        postsDao.getPosts().test().assertValueCount(1).assertResult(arrayListOf())
    }


    @Test
    @Throws(Exception::class)
    fun testInsertPosts_NotEmpty() {
        postsDao.addAllPosts(
            arrayListOf(
                Post(1, 5, "title1", "body1"),
                Post(2, 5, "title2", "body2")
            )
        )

        postsDao.getPosts().test().assertValueCount(1)

    }


    @Test
    @Throws(Exception::class)
    fun testAddAllPostsListSize() {

        assert(
            postsDao.addAllPosts(
                arrayListOf(
                    Post(1, 5, "title1", "body1"),
                    Post(2, 5, "title2", "body2")
                )
            ).size == 2
        )
    }

    @Test
    @Throws(Exception::class)
    fun testGetPosts() {
        postsDao.addAllPosts(
            arrayListOf(
                Post(1, 5, "title1", "body1"),
                Post(2, 5, "title2", "body2")
            )
        )

        postsDao.getPosts().test().assertValue(
            arrayListOf(
                Post(1, 5, "title1", "body1"),
                Post(2, 5, "title2", "body2")
            )
        )

    }

    @Test
    @Throws(Exception::class)
    fun testAddPost() {
        assert(postsDao.addPost(Post(1, 5, "title1", "body1")) == 1L)
    }

    @Test
    @Throws(Exception::class)
    fun testDeletePost() {
        postsDao.addPost(Post(1, 5, "title1", "body1"))

        assert(postsDao.deletePost(Post(1, 5, "title1", "body1")) == 1)
        postsDao.getPosts().test().assertComplete().assertValue(arrayListOf())
    }

    @Test
    @Throws(Exception::class)
    fun testEditPost() {
        postsDao.addPost(Post(1, 5, "title1", "body1"))

        assert(postsDao.editPost(Post(1, 5, "title4", "body4")) == 1)

        postsDao.getPosts().test().assertValue(
            arrayListOf(
                Post(1, 5, "title4", "body4")
            )
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

}