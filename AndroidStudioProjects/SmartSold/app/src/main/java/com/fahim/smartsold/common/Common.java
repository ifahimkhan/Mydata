package com.fahim.smartsold.common;

import com.fahim.smartsold.model.Item;

/**
 * Created by root on 11/4/18.
 */

public class Common {
    public static final String STR_FEED_REF = "items";
    public static final int PICK_IMAGE_REQ = 1000;
    public static final int PICK_IMAGE_REQ2 = 1001;
    public static final int PICK_IMAGE_REQ3 = 1002;
    public static final int PICK_IMAGE_REQ4 = 1003;
    public static String ITEM_ID_SELECTED;
    public static String ITEM_ID_USER;

    public static Item select_properties = new Item();

    public static boolean sellThisItem = false;
}
