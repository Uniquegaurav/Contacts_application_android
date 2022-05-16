package com.example.contacts.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.contacts.R;
import com.example.contacts.model.Contact;
import com.example.contacts.view.util.ValidationUtil;
import com.example.contacts.viewmodel.ContactViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class ContactDetails extends AppCompatActivity {

    TextInputEditText nameEditText, phoneEditText , emailEditText ,companyInfoEditText;
    private ContactViewModel contactViewModel;
    TextView contactTextView;
    ImageView contactImage;
    Intent intent;
    int idContact;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        imageUri = null;
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText= findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        contactImage = findViewById(R.id.contact_image);
        contactTextView = findViewById(R.id.contactTxtView);
        companyInfoEditText = findViewById(R.id.companyInfoEditText);
        intent = getIntent();
        if(intent.hasExtra("key_1")) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                nameEditText.setText(extras.getString("name"));
                phoneEditText.setText(extras.getString("number"));
                emailEditText.setText(extras.getString("email"));
                companyInfoEditText.setText(extras.getString("companyInfo"));
                contactTextView.setText(extras.getString("name"));
                idContact = extras.getInt("id");
                imageUri = Uri.parse(extras.getString("imageUrl"));
                Glide.with(ContactDetails.this)
                        .load(Uri.parse(extras.getString("imageUrl")))
                        .placeholder(R.drawable.ic_image_add)
                        .into(contactImage);
            }
        }else{
            contactTextView.setText("");
        }
    }
    private void selectImage(Context context) {
//        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        final CharSequence[] options = { "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

//                if (options[item].equals("Take Photo")) {
//                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePicture, 0);
//
//                } else
                if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
//                case 0:
//                    if (resultCode == RESULT_OK && data != null) {
//                        String selectedImage = data.toURI();
//                        try{
//                            if(selectedImage!=null)  imageUri = Uri.parse(selectedImage);
//                            Glide.with(ContactDetails.this)
//                                    .load(imageUri)
//                                    .placeholder(R.drawable.ic_image_add)
//                                    .into(contactImage);
//                        }catch (Exception error1){
//                            error1.printStackTrace();
//                            Toast.makeText(this, "Image upload failed!", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        imageUri  = selectedImage;
                        Glide.with(ContactDetails.this)
                                .load(selectedImage)
                                .placeholder(R.drawable.ic_image_add)
                                .into(contactImage);

                    }
                    break;
            }
        }
    }
    public void saveContactHelper(){
        String name = nameEditText.getText().toString();
        String phoneNumber = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String companyInfo = companyInfoEditText.getText().toString();

        if(!ValidationUtil.isValidName(name)){
            Toast.makeText(this, "Enter a valid name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!email.trim().isEmpty()) {
            if (!ValidationUtil.isValidEmail(email)) {
                Toast.makeText(this, "Enter a valid email Id!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(!ValidationUtil.isValidMobile(phoneNumber)){
            Toast.makeText(this, "Enter a valid phone number!", Toast.LENGTH_SHORT).show();
            return;
        }

        String finalImageUri  = "";
        if(imageUri!=null){
           finalImageUri = imageUri.toString();
        }
        if(intent.hasExtra("key_1")) {
            try{
                Contact updateContact = new Contact(name,phoneNumber,finalImageUri,email,companyInfo);
                updateContact.setId(idContact);
                contactViewModel.update(updateContact);
            }catch (Exception error1){
                Toast.makeText(this, "Can not update the contact!", Toast.LENGTH_SHORT).show();
            }
        }else{
            try{
                contactViewModel.insert(new Contact(name,phoneNumber,finalImageUri,email,companyInfo));
            }catch(Exception error1) {
                error1.printStackTrace();
                Toast.makeText(this, "Can not save the contact!", Toast.LENGTH_SHORT).show();
            }

        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        nameEditText.setText("");
        phoneEditText.setText("");
        emailEditText.setText("");
        companyInfoEditText.setText("");
    }
    public void saveContact(View view) {
       saveContactHelper();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_contact_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        if (id == R.id.saveContactMenu) {
            saveContactHelper();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void uploadImage(View view) {
        selectImage(ContactDetails.this);
    }
}