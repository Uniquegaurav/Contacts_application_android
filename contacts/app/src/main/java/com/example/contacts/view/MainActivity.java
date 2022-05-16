package com.example.contacts.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.model.Contact;
import com.example.contacts.permission.PermissionHandler;
import com.example.contacts.view.util.ContactData;
import com.example.contacts.viewmodel.ContactViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PermissionHandler.View {

    private RecyclerView contactsRecView;
    private ContactViewModel contactViewModel;
    private PermissionHandler permissionHandler;
    ContactsRecViewAdapter adapter;
    SharedPreferences prefs;
    ProgressBar pb;
    List<Contact> allContacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactsRecView = findViewById(R.id.contactsRecView);
        pb = (ProgressBar) findViewById(R.id.idPBLoading);
        contactsRecView.setLayoutManager(new LinearLayoutManager(this));
        contactsRecView.setHasFixedSize(true);
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        adapter = new ContactsRecViewAdapter(this, contactViewModel);
        permissionHandler = PermissionHandler.getInstance(this);
        permissionHandler.requestPermission();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public Activity getViewActivity() {
        return this;
    }

    @Override
    public void permissionGranted() {
        new storeToDatabaseTask().execute();
        contactsRecView.setAdapter(adapter);
        contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                allContacts = contacts;
                adapter.setContacts((ArrayList<Contact>) contacts);
                Log.i("contact", String.valueOf(contacts));
            }
        });
    }


    public void addNewContact(View view) {
        Intent intent = new Intent(MainActivity.this, ContactDetails.class);
        startActivity(intent);
    }

    class storeToDatabaseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            storePhoneContact();
            ContactData.setPhoneContactData(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    private void filter(String text) {
        List<Contact> filteredList = new ArrayList<>();
        for (Contact item : allContacts) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Contact Found", Toast.LENGTH_SHORT).show();
        } else {

            adapter.filterList((ArrayList<Contact>) filteredList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contact, menu);
        try {
            MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filter(newText.toLowerCase());
                    return false;
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addContactMenu) {
            Intent intent = new Intent(MainActivity.this, ContactDetails.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
