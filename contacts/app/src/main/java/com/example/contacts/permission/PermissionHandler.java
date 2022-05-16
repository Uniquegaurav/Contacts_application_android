package com.example.contacts.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHandler {
    private static PermissionHandler mPermissionHandler;
    private View mView;
    private PermissionHandler(View view) {
        this.mView = view;
    }
    public static PermissionHandler getInstance(View view)
    {
        if (mPermissionHandler == null) {
            mPermissionHandler = new PermissionHandler(view);

        }
        mPermissionHandler.mView = view;
        return mPermissionHandler;
    }
    public void requestPermission() {

        if (ContextCompat.checkSelfPermission(mView.getViewActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mView.getViewActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        else {
            mView.permissionGranted();
        }
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mView.permissionGranted();
                } else {
                    requestPermission();
                }
                return;
            }
        }
    }
    public interface View {
        Activity getViewActivity();
        void permissionGranted();
    }
}