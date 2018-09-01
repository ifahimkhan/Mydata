package com.android.fahim.livewallpaper.Models;

/**
 * Created by root on 22/2/18.
 */

public class WallpaperItem {
    String imageUrl;
    String categoryId;
    long viewCount;

    public WallpaperItem() {
    }

    public WallpaperItem(String imageUrl, String categoryId) {
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}
