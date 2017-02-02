package com.github.maximkirko.training_2017_android.adapter.itemtype;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by MadMax on 02.02.2017.
 */

public class ItemTypeAware {

    public static final int HEADER_POSITION = 0;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    @Retention(SOURCE)
    @IntDef({TYPE_HEADER, TYPE_ITEM})
    public @interface ItemType {

    }

    @ItemType
    public static int getItemViewType(int position) {
        if (position == HEADER_POSITION) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }
}
