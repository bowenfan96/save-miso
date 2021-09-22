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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class MyRecyclingTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun myRecyclingTest() {
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

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(300)

        val appCompatSpinner = onView(
            allOf(
                withId(R.id.type_spinner),
                withContentDescription("dropdown menu for item selection"),
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
                    3
                ),
                isDisplayed()
            )
        )
        appCompatSpinner.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(250)

        val materialButton = onView(
            allOf(
                withId(R.id.next), withText("next"),
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
                    1
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val bottomNavigationItemView = onView(
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
        bottomNavigationItemView.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.scoreMyRecycling),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("0g")))

        val materialCheckBox = onView(
            allOf(
                withId(R.id.todoCheckBox), withText("Can"),
                childAtPosition(
                    childAtPosition(
                        childAtPosition(
                            allOf(
                                withId(R.id.tasksRecyclerView),
                                childAtPosition(
                                    childAtPosition(
                                        allOf(
                                            withId(R.id.nav_host_fragment),
                                            childAtPosition(
                                                childAtPosition(
                                                    withId(android.R.id.content),
                                                    0
                                                ),
                                                0
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
                    0
                ),
                isDisplayed()
            )
        )
        materialCheckBox.perform(click())

        val materialCheckBox2 = onView(
            allOf(
                withId(R.id.todoCheckBox), withText("Can"),
                childAtPosition(
                    childAtPosition(
                        childAtPosition(
                            allOf(
                                withId(R.id.tasksRecyclerView),
                                childAtPosition(
                                    childAtPosition(
                                        allOf(
                                            withId(R.id.nav_host_fragment),
                                            childAtPosition(
                                                childAtPosition(
                                                    withId(android.R.id.content),
                                                    0
                                                ),
                                                0
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
                    0
                ),
                isDisplayed()
            )
        )
        materialCheckBox2.perform(click())

        val materialCheckBox3 = onView(
            allOf(
                withId(R.id.todoCheckBox), withText("Can"),
                childAtPosition(
                    childAtPosition(
                        childAtPosition(
                            allOf(
                                withId(R.id.tasksRecyclerView),
                                childAtPosition(
                                    childAtPosition(
                                        allOf(
                                            withId(R.id.nav_host_fragment),
                                            childAtPosition(
                                                childAtPosition(
                                                    withId(android.R.id.content),
                                                    0
                                                ),
                                                0
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
                    0
                ),
                isDisplayed()
            )
        )
        materialCheckBox3.perform(click())

        val floatingActionButton = onView(
            allOf(
                withId(R.id.fabRecycled),
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
                    2
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val textView2 = onView(
            allOf(
                withId(R.id.scoreMyRecycling), withText("Score: 125"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("120g")))
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
