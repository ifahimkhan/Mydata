package com.fahim.myapplication.Common;

import com.fahim.myapplication.model.AlbumModel;

/**
 * Created by root on 17/7/18.
 */

public class Common {

    public static void setAlbumModel1(AlbumModel albumModel1) {
        Common.albumModel1 = albumModel1;
    }

    public static AlbumModel getAlbumModel1() {
        return albumModel1;
    }

    private static AlbumModel albumModel1;


}
