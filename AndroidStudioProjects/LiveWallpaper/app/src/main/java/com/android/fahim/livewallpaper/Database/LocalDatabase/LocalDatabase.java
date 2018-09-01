package com.android.fahim.livewallpaper.Database.LocalDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.android.fahim.livewallpaper.Database.Recents;

import static com.android.fahim.livewallpaper.Database.LocalDatabase.LocalDatabase.DATABASE_VERSION;

/**
 * Created by root on 7/3/18.
 */

@Database(entities = Recents.class, version = DATABASE_VERSION, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LiveWallpaper";

    public abstract RecentsDAO recentsDAO();

    private static LocalDatabase instance;

    public static LocalDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context, LocalDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
