package com.android.fahim.livewallpaper.Models;

/**
 * Created by HSBC on 21-02-2018.
 */

public class CategoryItem {
    String name;
    String imageLink;

    public CategoryItem() {
    }

    public CategoryItem(String name, String imageLink) {
        this.name = name;
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
