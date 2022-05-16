package com.example.contacts.view.util;



public class ValidationUtil {
    public static boolean isValidName(String name) {
        if(name.trim().isEmpty()) return  false;
        if(name.length()<2) return false;
        return  true;

    }
    public static boolean isValidMobile(String phone) {
        if(phone.trim().isEmpty()) return false;
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches();
    }
}
