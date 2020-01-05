package com.nabil.postsapp


import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nabil.postsapp.postdetails.view.PostDetailsFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock


@LargeTest
@RunWith(AndroidJUnit4::class)
class PostDetailsFragmentTest {

//    @Rule
//    @JvmField
//    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    lateinit var mainActivityIdlingResource: CountingIdlingResource


    @Before
    fun clickItem() {

        IdlingRegistry.getInstance().register(mainActivityIdlingResource)

    }

    @Test
    fun postDetailsFragmentTest() {
        val mockNavController = mock(NavController::class.java)
        val fragmentScenario = launchFragmentInContainer<PostDetailsFragment>()

        // Set the NavController property on the fragment
        fragmentScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        val espressoTestIdlingResource =
            CountingIdlingResource("Network_Call")


//        onView(withId(R.id.rvPosts)).perform(
//            RecyclerViewActions.actionOnItemAtPosition<PostsAdapter.PostsViewHolder>(
//                2,
//                click()
//            )
//        )

        onView(withId(R.id.tvTitle)).check(matches(isDisplayed()))

        onView(withId(R.id.tvDetails)).check(matches(isDisplayed()))

        IdlingRegistry.getInstance().unregister(mainActivityIdlingResource)
    }


}
