package com.github.maximkirko.training_2017_android.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.github.maximkirko.training_2017_android.R;

/**
 * Created by MadMax on 06.01.2017.
 */

public class ItemSizeUtils {

    private static final int HORIZONTAL_SPACE_NUMBER = 3;
    private static final int HORIZONTAL_ITEM_NUMBER = 2;

    private static int getItemWidth(int spaceHorizontal) {
        return (ScreenUtils.getScreenWidth() - HORIZONTAL_SPACE_NUMBER * spaceHorizontal) / HORIZONTAL_ITEM_NUMBER;
    }

    public static CardView.LayoutParams getLayoutParams(@NonNull Context context) {
        int spaceHorizontal =
                context.getResources().getDimensionPixelSize(R.dimen.margin_musiclist_item_card);

        int itemWidth = ItemSizeUtils.getItemWidth(spaceHorizontal);

        return new CardView.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}
