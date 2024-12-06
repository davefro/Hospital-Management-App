package com.example.lab2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EncryptionTest {

    @Test
    public void testEncryptionDecryption()  throws Exception{
        String originalText = "Test Reason";
        String encryptedText = AESEncryptionHelper.encrypt(originalText);
        String decryptedText = AESEncryptionHelper.decrypt(encryptedText);

        assertEquals(originalText, decryptedText);
    }

    @Test
    public void testSpecialCharacters() throws Exception {
        String originalText = "!@#$%^&*()";
        String encryptedText = AESEncryptionHelper.encrypt(originalText);
        String decryptedText = AESEncryptionHelper.decrypt(encryptedText);

        assertEquals(originalText, decryptedText);
    }

    @Test
    public void testLongText() throws Exception {
        String originalText = "This is a very long test reason with more than 300 characters. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus varius ante ut mi vulputate convallis. Integer nec libero bibendum, auctor nulla ut, venenatis orci.";
        String encryptedText = AESEncryptionHelper.encrypt(originalText);
        String decryptedText = AESEncryptionHelper.decrypt(encryptedText);

        assertEquals(originalText, decryptedText);
    }

    @Test
    public void testEmptyString() throws Exception {
        String originalText = "";
        String encryptedText = AESEncryptionHelper.encrypt(originalText);
        String decryptedText = AESEncryptionHelper.decrypt(encryptedText);

        assertEquals(originalText, decryptedText);
    }

    @Test
    public void testValidBase64() {
        String base64Encoded = "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24=";
        assertTrue(AESEncryptionHelper.isValidBase64(base64Encoded));
    }

    @Test
    public void testInvalidBase64() {
        String invalidBase64 = "NotAValidBase64String==";
        boolean result = AESEncryptionHelper.isValidBase64(invalidBase64);
        assertFalse(result);
    }

}
