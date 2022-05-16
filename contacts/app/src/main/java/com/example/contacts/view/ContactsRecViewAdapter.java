package com.example.contacts.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contacts.R;
import com.example.contacts.model.Contact;
import com.example.contacts.viewmodel.ContactViewModel;

import java.util.ArrayList;
import java.util.List;

public class ContactsRecViewAdapter extends RecyclerView.Adapter<ContactsRecViewAdapter.ViewHolder> {

    private List<Contact> contacts = new ArrayList<>();
    private Context context;
    ContactViewModel contactViewModel;

    public ContactsRecViewAdapter(Context context, ContactViewModel contactViewModel) {
        this.context = context;
        this.contactViewModel = contactViewModel;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ContactsRecViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Contact contact = contacts.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactNumber.setText(contact.getNumber());
        if (contact.getImageUrl().equals("")) {

            String contactText = "";
            try {
                String[] splitContactName = contact.getName().trim().split(" ");
                if (splitContactName.length > 1) {
                    contactText = Character.toString(splitContactName[0].charAt(0)) + Character.toString(splitContactName[1].charAt(0));
                } else {
                    contactText = Character.toString(splitContactName[0].charAt(0));
                }
            } catch (Exception error1) {
                contactText = "AB";
                error1.printStackTrace();
            }

            holder.contactIconText.setText(contactText);
            holder.contactImage.setVisibility(View.GONE);
            holder.contactIconText.setVisibility(View.VISIBLE);

        } else {
            holder.contactImage.setVisibility(View.VISIBLE);
            holder.contactIconText.setVisibility(View.GONE);
            try {
                Glide.with(context)
                        .load(Uri.parse(contact.getImageUrl()))
                        .placeholder(R.drawable.ic_user)
                        .into(holder.contactImage);
            } catch (Exception error1) {
                error1.printStackTrace();
            }

        }
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ContactDetails.class);
                intent.putExtra("key_1", "update_contact");
                intent.putExtra("id", contact.getId());
                intent.putExtra("name", contact.getName());
                intent.putExtra("number", contact.getNumber());
                intent.putExtra("imageUrl", contact.getImageUrl());
                intent.putExtra("email", contact.getEmailId());
                intent.putExtra("companyInfo", contact.getCompanyInfo());
                view.getContext().startActivity(intent);
            }
        });
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    contactViewModel.delete(contact);
                    notifyDataSetChanged();
                } catch (Exception error1) {
                    error1.printStackTrace();
                    Toast.makeText(context, "Can not delete contact!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }
    public void filterList(ArrayList<Contact>filterList) {
        contacts = filterList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView contactName, contactNumber, contactIconText;
        private LinearLayout parent;
        private RelativeLayout backgroundImage;
        private ImageView deleteIcon, contactImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactNumber = itemView.findViewById(R.id.contactNumber);
            contactIconText = itemView.findViewById(R.id.contactIconText);
            deleteIcon = itemView.findViewById(R.id.deleteBtn);
            parent = itemView.findViewById(R.id.parent);
            contactImage = itemView.findViewById(R.id.contactImage);
        }
    }
}
