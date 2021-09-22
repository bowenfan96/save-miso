package com.example.snapcycle


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class BottomNavBarTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.snapcycle", appContext.packageName)
    }

    @Test
    fun testHomeTab() {
        onView(withContentDescription(R.string.title_home))
            .perform(click())
        onView(withId(R.id.textView)).check(matches(isDisplayed()))
    }

    @Test
    fun testGreenTab() {
        onView(withContentDescription(R.string.title_dashboard))
            .perform(click())
        onView(withId(R.id.pager)).check(matches(isDisplayed()))
    }

    @Test
    fun testUserTab() {
        onView(withContentDescription(R.string.title_account))
            .perform(click())
        onView(withId(R.id.NewUser)).check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclingTab() {
        onView(withContentDescription(R.string.title_myrecycling))
            .perform(click())
        onView(withId(R.id.tasksRecyclerView)).check(matches(isDisplayed()))
    }
}