package com.android.fahim.livewallpaper.Helper;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

/**
 * Created by root on 23/2/18.
 */

public class SaveImageHelper {
    public SaveImageHelper() {
    }

    public static String getRootDirPath(Context context) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "WALLPAPER");

        if (!file.mkdirs())
        {
            //return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }

        return file.getAbsolutePath();

    }




    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    private static String getBytesToMBString(long bytes) {
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }

}