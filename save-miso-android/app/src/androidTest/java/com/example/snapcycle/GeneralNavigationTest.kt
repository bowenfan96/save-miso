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
class GeneralNavigationTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun generalNavigationTest() {
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navHome), withContentDescription("Home"),
                childAtPosition(
                    childAtPosition(
                        allOf(
                            withId(R.id.navView),
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
                                1
                            )
                        ),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val imageView = onView(
            allOf(
                withId(R.id.butForest), withContentDescription("miso's forest"),
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
                withId(R.id.butCamera), withContentDescription("scanner for recyclables"),
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

        val imageView3 = onView(
            allOf(
                withId(R.id.butCamera), withContentDescription("scanner for recyclables"),
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
        imageView3.check(matches(isDisplayed()))

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.navInstructions), withContentDescription("Green Guide"),
                childAtPosition(
                    childAtPosition(
                        allOf(
                            withId(R.id.navView),
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
                                1
                            )
                        ),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val textView = onView(
            allOf(
                withText("GENERAL"),
                withParent(
                    allOf(
                        withContentDescription("General"),
                        withParent(
                            withParent(
                                allOf(
                                    withId(R.id.tabs),
                                    withParent(
                                        withParent(
                                            allOf(
                                                withId(R.id.nav_host_fragment),
                                                withParent(withParent(withId(android.R.id.content)))
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
        textView.check(matches(withText("GENERAL")))

        val textView2 = onView(
            allOf(
                withText("RECYCLING LOCATIONS"),
                withParent(
                    allOf(
                        withContentDescription("Recycling Locations"),
                        withParent(
                            withParent(
                                allOf(
                                    withId(R.id.tabs),
                                    withParent(
                                        withParent(
                                            allOf(
                                                withId(R.id.nav_host_fragment),
                                                withParent(withParent(withId(android.R.id.content)))
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
        textView2.check(matches(withText("RECYCLING LOCATIONS")))

        val bottomNavigationItemView3 = onView(
            allOf(
                withId(R.id.navNotifications), withContentDescription("Account"),
                childAtPosition(
                    childAtPosition(
                        allOf(
                            withId(R.id.navView),
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
                                1
                            )
                        ),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView3.perform(click())

        val textView3 = onView(
            allOf(
                withId(R.id.instructions), withText("Welcome to Save Miso!"),
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
        textView3.check(matches(withText("Welcome to Save Miso!")))

        val bottomNavigationItemView4 = onView(
            allOf(
                withId(R.id.navMyRecycling), withContentDescription("My Recycling"),
                childAtPosition(
                    childAtPosition(
                        allOf(
                            withId(R.id.navView),
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
                                1
                            )
                        ),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView4.perform(click())

        val imageButton = onView(
            allOf(
                withId(R.id.fabRecycled),
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
        imageButton.check(matches(isDisplayed()))

        val textView4 = onView(
            allOf(
                withId(R.id.tasksText), withText("Items"),
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
        textView4.check(matches(withText("Items")))
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
