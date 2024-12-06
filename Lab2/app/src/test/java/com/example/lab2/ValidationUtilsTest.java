package com.example.lab2;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidationUtilsTest {
    @Test
    public void testEmptyEmailValidation() {
        assertFalse(ValidationUtils.isEmailValid(""));
    }

    @Test
    public void testInvalidEmailValidation() {
        assertFalse(ValidationUtils.isEmailValid("invalidemail"));
    }

    @Test
    public void testValidEmailValidation() {
        assertTrue(ValidationUtils.isEmailValid("user@example.com"));
    }

    @Test
    public void testEmptyPasswordValidation() {
        assertFalse(ValidationUtils.isPasswordValid(""));
    }

    @Test
    public void testValidPasswordValidation() {
        assertTrue(ValidationUtils.isPasswordValid("password123"));
    }
}

