package com.android.fahim.livewallpaper.Database.DataSource;

import com.android.fahim.livewallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by root on 7/3/18.
 */

public interface IRecentsDataSource {
    Flowable<List<Recents>> getAllRecents();

    void insertRecents(Recents... recents);

    void updateRecents(Recents... recents);

    void deleteRecents(Recents... recents);

    void deleteAllRecents();

}
