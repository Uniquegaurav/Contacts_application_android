package com.example.contacts.view.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.contacts.model.Contact;

import java.util.HashSet;

public class ContactData {

    private static HashSet<Contact> contactHashSet = new HashSet<Contact>();

    public static HashSet<Contact> getPhoneContactData() {
        return contactHashSet;
    }

    public static void setPhoneContactData(Context context) {

        try {
            HashSet<Contact> tempContactHashSet = new HashSet<Contact>();
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            Log.i("CONTACT_PROVIDER", "Total contacts : " + Integer.toString(cursor.getCount()));
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    @SuppressLint("Range") String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.i("contacts list", "contactName" + contactName + "contactNumber" + contactNumber);
                    if (contactName == null) contactName = "";
                    if (contactNumber == null) contactNumber = "";
                    contactNumber = contactNumber.replaceAll("\\s+", "");
                    tempContactHashSet.add(new Contact(contactName, contactNumber, "", "", ""));
                    Log.i("contactData", contactNumber);
                }
            }
            contactHashSet = tempContactHashSet;
        } catch (Exception error1) {
            error1.printStackTrace();
        }
    }
}
