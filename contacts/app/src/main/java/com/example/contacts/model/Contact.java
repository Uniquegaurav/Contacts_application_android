package com.example.contacts.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "contact_table")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String number;
    private String imageUrl;
    private String emailId;
    private String companyInfo;

    public Contact(String name, String number, String imageUrl, String emailId, String companyInfo) {
        this.name = name;
        this.number = number;
        this.imageUrl = imageUrl;
        this.emailId = emailId;
        this.companyInfo = companyInfo;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setCompanyInfo(String companyInfo) {
        this.companyInfo = companyInfo;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getCompanyInfo() {
        return companyInfo;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", emailId='" + emailId + '\'' +
                ", companyInfo='" + companyInfo + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Contact other = (Contact) obj;
        return Objects.equals(name, other.name) && Objects.equals(number, other.number);
    }
}
