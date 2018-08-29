package com.example.khale.baking;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.Toolbar;
import com.example.khale.baking.UI.MainRecipeActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;




import org.junit.Rule;
import org.junit.Test;

public class ActivityBasicTest {

    @Rule
    public ActivityTestRule<MainRecipeActivity> activityTestRule = new ActivityTestRule<MainRecipeActivity>(MainRecipeActivity.class);

    @Test
    public void clickRecipe(){
        try {
            Thread.sleep(3000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        onView(withId(R.id.recipe_recycler))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText("Cheesecake")), click()));

                equalsToolbarTitle("Cheesecake");
    }

    private static ViewInteraction equalsToolbarTitle(CharSequence title){
        return onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is(title))));
    }

    private static Matcher<Object> withToolbarTitle(final Matcher<CharSequence> textMatcher){
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }
        };
    }
}
