package com.fahim.smartsold.model;

import java.util.Date;

/**
 * Created by root on 11/4/18.
 */

public class Item {
    private long time;
    private String OwnerName;
    private long OwnerContact;
    private String OwnerEmail;
    private String ownerLocation;
    private String itemImageCover;
    private String itemDescription;
    private double itemPrice;
    private String itemName;
    private String uid;
    private String url2, url3, url4;

    public String getFaulty_features() {
        return faulty_features;
    }

    public void setFaulty_features(String faulty_features) {
        this.faulty_features = faulty_features;
    }

    private String faulty_features;

    public Item() {
        this.time = new Date().getTime();
    }


    public Item(String ownerName, long ownerContact, String ownerEmail, String ownerLocation, String itemImageCover, String itemDescription, double itemPrice, String itemName, String uid, String url2, String url3, String url4) {
        OwnerName = ownerName;
        OwnerContact = ownerContact;
        OwnerEmail = ownerEmail;
        this.ownerLocation = ownerLocation;
        this.itemImageCover = itemImageCover;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemName = itemName;
        this.uid = uid;
        this.url2 = url2;
        this.url3 = url3;
        this.url4 = url4;
        this.time = new Date().getTime();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getUrl4() {
        return url4;
    }

    public void setUrl4(String url4) {
        this.url4 = url4;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public long getOwnerContact() {
        return OwnerContact;
    }

    public void setOwnerContact(long ownerContact) {
        OwnerContact = ownerContact;
    }

    public String getOwnerEmail() {
        return OwnerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        OwnerEmail = ownerEmail;
    }

    public String getOwnerLocation() {
        return ownerLocation;
    }

    public void setOwnerLocation(String ownerLocation) {
        this.ownerLocation = ownerLocation;
    }

    public String getItemImageCover() {
        return itemImageCover;
    }

    public void setItemImageCover(String itemImageCover) {
        this.itemImageCover = itemImageCover;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}

