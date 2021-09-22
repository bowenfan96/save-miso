package com.example.snapcycle


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class PagerFragmentTest {

//    @Before
//    fun setUp() {
//        try {
//            AccessibilityChecks.enable()
//        } catch(e: IllegalStateException) {
//            // If accessibility checks are already enabled, do nothing
//        }
//    }


    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)


    @Test
    fun testTab1() {
        onView(withContentDescription(R.string.title_dashboard)).perform(click())
        onView(withContentDescription(R.string.instructionsMenu1))
            .perform(click())
        onView(withContentDescription(R.string.instructionsText1)).check(matches(isDisplayed())) //
    }

    @Test
    fun testTab2() {
        onView(withContentDescription(R.string.instructionsMenu2))
            .perform(click())
        onView(withContentDescription(R.string.instructionsText2)).check(matches(isDisplayed()))
    }

    @Test
    fun testTab3() {
        onView(withContentDescription(R.string.instructionsMenu3))
            .perform(click())
        onView(withContentDescription(R.string.instructionsText3)).check(matches(isDisplayed()))
    }
}