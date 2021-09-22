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
class ScanItemTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun scanItemTest() {
        val appCompatImageView = onView(
            allOf(
                withId(R.id.butCamera), withContentDescription("scanner for recyclables"),
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
                    4
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.descriptionRec),
                withText("You will save 5g CO? per Paper, 5g in total!"),
                withParent(
                    withParent(
                        allOf(
                            withId(R.id.nav_host_fragment),
                            withParent(
                                withParent(
                                    allOf(
                                        withId(android.R.id.content),
                                        withParent(
                                            allOf(
                                                withId(R.id.action_bar_root),
                                                withParent(
                                                    withParent(
                                                        IsInstanceOf.instanceOf(
                                                            android.widget.LinearLayout::class.java
                                                        )
                                                    )
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
        textView.check(matches(isDisplayed()))

        val imageView = onView(
            allOf(
                withId(R.id.defaultRecyclable),
                withParent(
                    withParent(
                        allOf(
                            withId(R.id.nav_host_fragment),
                            withParent(
                                withParent(
                                    allOf(
                                        withId(android.R.id.content),
                                        withParent(
                                            allOf(
                                                withId(R.id.action_bar_root),
                                                withParent(
                                                    withParent(
                                                        IsInstanceOf.instanceOf(
                                                            android.widget.LinearLayout::class.java
                                                        )
                                                    )
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
        imageView.check(matches(isDisplayed()))

        val imageView2 = onView(
            allOf(
                withId(R.id.defaultRecyclable),
                withParent(
                    withParent(
                        allOf(
                            withId(R.id.nav_host_fragment),
                            withParent(
                                withParent(
                                    allOf(
                                        withId(android.R.id.content),
                                        withParent(
                                            allOf(
                                                withId(R.id.action_bar_root),
                                                withParent(
                                                    withParent(
                                                        IsInstanceOf.instanceOf(
                                                            android.widget.LinearLayout::class.java
                                                        )
                                                    )
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
        imageView2.check(matches(isDisplayed()))
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
