package com.github.maximkirko.training_2017_android.itemdecorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DefaultItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public DefaultItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        final int position = parent.getChildLayoutPosition(view);
        if (position <= 1) {
            outRect.top = space;
        }
        outRect.right = space;
        outRect.left = space;
        outRect.bottom = space;
    }
}