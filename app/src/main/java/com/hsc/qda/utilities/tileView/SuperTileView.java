package com.hsc.qda.utilities.tileView;

import android.content.Context;
import android.util.AttributeSet;

import com.qozix.tileview.TileView;

public class SuperTileView extends TileView {
    public SuperTileView(Context context) {
        super(context);
    }

    public SuperTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuperTileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onScaleChanged(float scale, float previous) {
        super.onScaleChanged(scale, previous);
    }
}
