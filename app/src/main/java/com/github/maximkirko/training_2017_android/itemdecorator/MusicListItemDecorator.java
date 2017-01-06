package com.github.maximkirko.training_2017_android.itemdecorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by MadMax on 06.01.2017.
 */

public class MusicListItemDecorator extends RecyclerView.ItemDecoration {

    private int edgeMargin;
    private int middleMargin;

    public MusicListItemDecorator(int edgeMargin, int middleMargin) {
        this.edgeMargin = edgeMargin;
        this.middleMargin = middleMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        final int position = parent.getChildLayoutPosition(view);
        if (position % 2 == 0) {
            outRect.left = edgeMargin;
        }
        outRect.right =
                (position == parent.getAdapter().getItemCount() - 1) ? edgeMargin : middleMargin;
        outRect.top = middleMargin;
        outRect.bottom = middleMargin;
    }
}
