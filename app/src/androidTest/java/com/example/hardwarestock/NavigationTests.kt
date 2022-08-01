package com.example.hardwarestock

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTests {


    private lateinit var navController: TestNavHostController

    private lateinit var itemListScenario: FragmentScenario<ItemListFragment>


    @Before

    fun setup() {

        //Create a test instance of the navigation controller.
        navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        //Here we specify that we want to launch the ItemListFragment.
        //We have to pass the app's theme so that the UI components know which theme to use or the test may crash.
        itemListScenario = launchFragmentInContainer(themeResId =
        R.style.Theme_HardwareStock)

        //Declare which navigation graph we want the nav controller to use for the fragment launched
        itemListScenario.onFragment { fragment ->

            navController.setGraph(R.navigation.nav_graph)

            Navigation.setViewNavController(fragment.requireView(), navController)
        }



    }

    @Test
    fun navigate_to_itemDetail_nav_component() {

        //Trigger the event that prompts the navigation
        // When you run this test on a device or emulator, you will not see any actual navigation
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        assertEquals(navController.currentDestination?.id, R.id.itemDetailFragment)
    }

    @Test
    fun navigate_to_addItemFragment_nav_component() {

        //Trigger the event that prompts the navigation
        // When you run this test on a device or emulator, you will not see any actual navigation
        onView(withId(R.id.floatingActionButton))
        .perform(click())

        assertEquals(navController.currentDestination?.id, R.id.addItemFragment)
    }

}