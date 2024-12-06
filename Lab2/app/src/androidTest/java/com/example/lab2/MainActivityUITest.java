package com.example.lab2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.example.lab2.TextInputLayoutMatchers.hasTextInputLayoutErrorText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testEmailFieldIsDisplayed() {
        // Check if the email input field is displayed
        onView(withId(R.id.email)).check(matches(isDisplayed()));
    }

    @Test
    public void testPasswordFieldIsDisplayed() {
        // Check if the password input field is displayed
        onView(withId(R.id.password)).check(matches(isDisplayed()));
    }

    @Test
    public void testSignInButtonIsClickable() {
        // Check if the "Sign In" button is clickable
        onView(withId(R.id.sign_in)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyEmailShowsError() {
        // Leave email empty, enter password, and click Sign In
        onView(withId(R.id.password)).perform(typeText("password123"));
        onView(withId(R.id.sign_in)).perform(click());

        // Verify the error message on email field
        onView(withId(R.id.emailInputLayout)).check(matches(hasTextInputLayoutErrorText("Email cannot be empty")));
    }

    @Test
    public void testInvalidEmailShowsError() {
        // Enter invalid email, leave password, and click Sign In
        onView(withId(R.id.email)).perform(typeText("invalid email"));
        onView(withId(R.id.sign_in)).perform(click());

        // Verify the error message on email field
        onView(withId(R.id.emailInputLayout)).check(matches(hasTextInputLayoutErrorText("Invalid email address")));
    }
}
