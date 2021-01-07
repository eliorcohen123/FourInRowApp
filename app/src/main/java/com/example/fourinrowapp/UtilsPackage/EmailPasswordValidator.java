package com.example.fourinrowapp.UtilsPackage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailPasswordValidator {

    // Singleton class
    private static volatile EmailPasswordValidator sInstance;

    private EmailPasswordValidator() {
        if (sInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static EmailPasswordValidator getInstance() {
        if (sInstance == null) {
            synchronized (EmailPasswordValidator.class) {
                if (sInstance == null) sInstance = new EmailPasswordValidator();
            }
        }

        return sInstance;
    }

    // Pattern of email
    private static String EMAIL_PATTERN =
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+";
    private static Pattern pattern; // Set Pattern
    private static Matcher matcher; // Set Matcher

    // Check if the email is valid according to the patten we defined
    public boolean isValidEmail(final String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    // Check if the password is valid according to the patten we defined
    public boolean isValidPassword(final String password) {
        if (password.length() < 8) {
            return false;
        } else {
            return true;
        }
    }

}
