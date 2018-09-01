package com.android.fahim.livewallpaper.common;

import android.content.Context;
import android.content.Intent;

import com.android.fahim.livewallpaper.Models.WallpaperItem;
import com.android.fahim.livewallpaper.UploadActivity;

/**
 * Created by HSBC on 21-02-2018.
 */

public class Common {

    public static final String STR_WALLPAPERS = "Wallpapers";
    public static final int PICK_IMAGE_REQ = 102;
    public static String STR_CATEGORY_BACKGROUND = "CategoryBackground";
    public static String CATEGORY_SELECTED;
    public static String CATEGORY_ID_SELECTED;
    public static WallpaperItem select_background = new WallpaperItem();
    public static final int PERMISSION_REQUEST_CODE = 100;
    public static String select_background_key;

    public static final int SIGN_IN_REQUEST_CODE = 101;


}
