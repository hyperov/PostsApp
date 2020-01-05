package com.nabil.postsapp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nabil.postsapp.posts.view.PostsAdapter
import com.nabil.postsapp.posts.view.PostsFragment
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@LargeTest
@RunWith(AndroidJUnit4::class)
class PostFragmentTest {

    lateinit var mainActivityIdlingResource: CountingIdlingResource

    @Test
    fun postFragmentTest() {


        val mockNavController = Mockito.mock(NavController::class.java)
        val scenario = launchFragmentInContainer<PostsFragment>()

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
            mainActivityIdlingResource = fragment.getEspressoIdlingResource()
            IdlingRegistry.getInstance().register(mainActivityIdlingResource)
        }
        onView(withId(R.id.fabAdd)).check(matches(isDisplayed()))
        onView(withId(R.id.rvPosts)).check(matches(isDisplayed()))

        onView(withId(R.id.rvPosts)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PostsAdapter.PostsViewHolder>(
                2,
                click()
            )
        )
        IdlingRegistry.getInstance().unregister(mainActivityIdlingResource)    }
}