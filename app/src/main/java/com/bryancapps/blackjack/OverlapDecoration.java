package com.bryancapps.blackjack;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by bryancapps on 6/24/16.
 */
public class OverlapDecoration extends RecyclerView.ItemDecoration {
    private final static int OVERLAP_IN_DP = -50;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) > 0) {
            DisplayMetrics metrics = view.getContext().getResources().getDisplayMetrics();
            int overlapInPx = (int) ((OVERLAP_IN_DP * metrics.density) + 0.5);

            outRect.set(overlapInPx, 0, 0, 0);
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }
}
