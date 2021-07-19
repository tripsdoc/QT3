package com.hsc.qda.utilities.tileView;

import android.content.Context;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;

import com.qozix.tileview.geom.FloatMathHelper;

public class ScalingMarkerLayout extends MarkerLayout {

    private float mOriginalAtScale;

    public ScalingMarkerLayout(Context context) {
        this(context, 1f);
    }

    public ScalingMarkerLayout(@NonNull Context context, float originalAtScale) {
        super(context);
        mOriginalAtScale = originalAtScale;
    }

    @Override
    protected LayoutParams populateLayoutParams(View child) {
        MarkerLayout.LayoutParams layoutParams = (MarkerLayout.LayoutParams) child.getLayoutParams();
        if (child.getVisibility() != View.GONE) {
            // actual sizes of children
            int measuredWidth;
            int measuredHeight;
            if (layoutParams.mode == 0) {
                measuredWidth = FloatMathHelper.scale((child.getMeasuredWidth() * 10 / 100), mScale);
                measuredHeight = FloatMathHelper.scale((child.getMeasuredHeight() * 10 / 100), mScale);
                // calculate combined anchor offsets
                float widthOffset = measuredWidth * (layoutParams.relativeAnchorX + layoutParams.absoluteAnchorX);
                float heightOffset = measuredHeight * (layoutParams.relativeAnchorY + layoutParams.absoluteAnchorY);
                // get offset position
                int scaledX = (int) (layoutParams.x * mScale);
                int scaledY = (int) (layoutParams.y * mScale);
                // save computed values
                layoutParams.mLeft = (int) (scaledX + widthOffset);
                layoutParams.mTop = (int) (scaledY + heightOffset);
                layoutParams.mRight = layoutParams.mLeft + measuredWidth;
                layoutParams.mBottom = layoutParams.mTop + measuredHeight;
            } else {
                measuredWidth = child.getMeasuredWidth();
                measuredHeight = child.getMeasuredHeight();
                // get anchor offsets
                float widthMultiplier = (layoutParams.relativeAnchorX == null) ? layoutParams.absoluteAnchorX : layoutParams.relativeAnchorX;
                float heightMultiplier = (layoutParams.relativeAnchorY == null) ? layoutParams.absoluteAnchorY : layoutParams.relativeAnchorY;
                // calculate combined anchor offsets
                float widthOffset = measuredWidth * widthMultiplier + layoutParams.absoluteAnchorX;
                float heightOffset = measuredHeight * heightMultiplier + layoutParams.absoluteAnchorY;
                // get offset position
                int scaledX = FloatMathHelper.scale( layoutParams.x, mScale );
                int scaledY = FloatMathHelper.scale( layoutParams.y, mScale );
                // save computed values
                layoutParams.mLeft = (int) (scaledX + widthOffset);
                layoutParams.mTop = (int) (scaledY + heightOffset);
                layoutParams.mRight = layoutParams.mLeft + measuredWidth;
                layoutParams.mBottom = layoutParams.mTop + measuredHeight;
            }
        }
        return layoutParams;
    }

    public void setFixedScale(float scale) {
        this.mOriginalAtScale = scale;
        requestLayout();
    }
}
