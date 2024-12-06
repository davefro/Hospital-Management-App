package com.example.lab2;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.espresso.matcher.BoundedMatcher;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class TextInputLayoutMatchers {
    public static Matcher<View> hasTextInputLayoutErrorText(@NonNull final String expectedError) {
        return new BoundedMatcher<View, TextInputLayout>(TextInputLayout.class) {
            @Override
            protected boolean matchesSafely(TextInputLayout textInputLayout) {
                CharSequence error = textInputLayout.getError();
                return error != null && error.toString().equals(expectedError);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with error: ").appendValue(expectedError);
            }
        };
    }
}
