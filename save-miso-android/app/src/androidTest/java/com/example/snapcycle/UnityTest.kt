package com.example.snapcycle


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class UnityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun unityTest() {
        val appCompatImageView = onView(
            allOf(
                withId(R.id.butForest), withContentDescription("miso's forest"),
                childAtPosition(
                    childAtPosition(
                        allOf(
                            withId(R.id.nav_host_fragment),
                            childAtPosition(
                                childAtPosition(
                                    allOf(
                                        withId(android.R.id.content),
                                        childAtPosition(
                                            allOf(
                                                withId(R.id.action_bar_root),
                                                childAtPosition(
                                                    childAtPosition(
                                                        withClassName(`is`("android.widget.LinearLayout")),
                                                        1
                                                    ),
                                                    0
                                                )
                                            ),
                                            1
                                        )
                                    ),
                                    0
                                ),
                                0
                            )
                        ),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val view = onView(
            allOf(
                withId(R.id.unitySurfaceView), withContentDescription("Game view"),
                withParent(
                    withParent(
                        allOf(
                            withId(R.id.frameLayoutForUnity),
                            withParent(
                                withParent(
                                    allOf(
                                        withId(R.id.nav_host_fragment),
                                        withParent(
                                            withParent(
                                                allOf(
                                                    withId(android.R.id.content),
                                                    withParent(withId(R.id.action_bar_root))
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                ),
                isDisplayed()
            )
        )
        view.check(matches(isDisplayed()))

        val frameLayout = onView(
            allOf(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java), isDisplayed())
        )
        frameLayout.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
