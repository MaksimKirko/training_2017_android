package com.github.maximkirko.training_2017_android.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.CardView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.util.ScreenUtils;

/**
 * Created by MadMax on 06.01.2017.
 */

public class ItemSizeAware {

    private static final int HORIZONTAL_SPACE_NUMBER = 3;
    private static final int VERTICAL_SPACE_NUMBER = 6;

    private static final int HORIZONTAL_ITEM_NUMBER = 2;
    private static final int VERTICAL_ITEM_NUMBER = 5;

    private static int getItemWidth(int spaceHorizontal) {
        return (ScreenUtils.getScreenWidth() - HORIZONTAL_SPACE_NUMBER * spaceHorizontal) / HORIZONTAL_ITEM_NUMBER;
    }

    private static int getItemHeight(int spaceVertical) {
        return (ScreenUtils.getScreenHeight() - VERTICAL_SPACE_NUMBER * spaceVertical) / VERTICAL_ITEM_NUMBER;
    }

    public static CardView.LayoutParams getLayoutParams(Context context) {
        int spaceHorizontal =
                context.getResources().getDimensionPixelSize(R.dimen.music_recycler_view_item_card_layout_margin);

        int spaceVertical =
                context.getResources().getDimensionPixelSize(R.dimen.music_recycler_view_item_card_layout_margin);

        int itemWidth = ItemSizeAware.getItemWidth(spaceHorizontal);
        int itemHeight = ItemSizeAware.getItemHeight(spaceVertical);

        CardView.LayoutParams params = new CardView.LayoutParams(itemWidth, itemHeight);
        return params;
    }

}
