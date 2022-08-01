package com.example.hardwarestock




import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith





@RunWith(AndroidJUnit4::class)
class DataTransactionTests {

    @get:Rule
    val scenario = ActivityScenarioRule(MainActivity::class.java)

    private val testItemName = "Test Item"
    private val testItemType = "Test Type"
    private val testItemPrice = "5.00"

    //I added NBSP (space and coma to match the string)
    private val testItemPriceTransformed = " 5,00"
    private val testItemInitialCount = "1"
    private val quantityHeader = "Quantity\nIn Stock"

/*
 * Add item to the list and checked that is displayed
 */
    @Test
    fun added_item_displays_in_list() {
        onView(withId(R.id.floatingActionButton)).perform(click())

        //Add items
        onView(withId(R.id.item_name)).perform(typeText(testItemName))
        onView(withId(R.id.item_type)).perform(typeText(testItemType))
        onView(withId(R.id.item_price)).perform(typeText(testItemPrice))
        onView(withId(R.id.item_count)).perform(typeText(testItemInitialCount))

            //Close soft keyboard to reach save button
            .perform(closeSoftKeyboard())

        //Click save button
        onView(withId(R.id.save_action)).perform(click())



        // Make sure we are back in the list fragment by checking that a header is displayed
        onView(withText(quantityHeader)).check(matches(isDisplayed()))

        // Make sure item name is displayed
        onView(withText(testItemName)).check(matches(isDisplayed()))

        // Make sure item type is displayed
        onView(withText(testItemType)).check(matches(isDisplayed()))

        //Check price with coma instead of dot due to price format in Spanish
        //To avoid this issue I could use .check(matches(withText(containsString("5,00"))))
        onView(withText("$$testItemPriceTransformed")).check(matches(isDisplayed()))
        onView(withText(testItemInitialCount)).check(matches(isDisplayed()))
    }

 /*
  * Add item to the list then delete it and check it was deleted
  */
    @Test
    fun list_empty_after_item_deletion() {
        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withId(R.id.item_name)).perform(typeText(testItemName))
        onView(withId(R.id.item_type)).perform(typeText(testItemType))
        onView(withId(R.id.item_price)).perform(typeText(testItemPrice))
        onView(withId(R.id.item_count)).perform(typeText(testItemInitialCount))

            //Close soft keyboard to reach save button
            .perform(closeSoftKeyboard())

        //Click save button
        onView(withId(R.id.save_action)).perform(click())

        // Make sure we are back in the list fragment by checking that a header is displayed
        onView(withText(quantityHeader)).check(matches(isDisplayed()))

        //Go to first item in the recyclerView an click on it
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions
                .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        //Click delete button
        onView(withId(R.id.delete_item)).perform(click())

         //Click yes
        onView(withText("Yes")).perform(click())

        //Check testItemName does not exist
        onView(withText(testItemName)).check(doesNotExist())
    }




}