package com.nabil.postsapp


import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nabil.postsapp.postdetails.view.PostDetailsFragment
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock


@LargeTest
@RunWith(AndroidJUnit4::class)
class PostDetailsFragmentTest {

    @Test
    fun postDetailsFragmentTest() {
        val mockNavController = mock(NavController::class.java)
        val fragmentScenario = launchFragmentInContainer<PostDetailsFragment>()

        // Set the NavController property on the fragment
        fragmentScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        onView(withId(R.id.tvTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.tvDetails)).check(matches(isDisplayed()))
    }


}
