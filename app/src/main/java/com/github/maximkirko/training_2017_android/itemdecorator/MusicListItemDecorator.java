package com.github.maximkirko.training_2017_android.itemdecorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by MadMax on 06.01.2017.
 */

public class MusicListItemDecorator extends RecyclerView.ItemDecoration {

    private int edgeMargin;

    public MusicListItemDecorator(int edgeMargin) {
        this.edgeMargin = edgeMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        final int position = parent.getChildLayoutPosition(view);
        if (position % 2 == 0) {
            outRect.right = edgeMargin / 2;
            outRect.left = edgeMargin;
        }
        if (position % 2 != 0) {
            outRect.left = edgeMargin / 2;
            outRect.right = edgeMargin;
        }

        if (position <= 1) {
            outRect.top = edgeMargin;
        }

        outRect.bottom = edgeMargin;
    }
}
