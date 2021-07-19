package com.hsc.qda.utilities.tileView;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

public class MarkerLayout extends ViewGroup {

    protected float mScale = 1;

    private MarkerLayout.MarkerTapListener mMarkerTapListener;

    public MarkerLayout(Context context ) {
        super( context );
        setClipChildren( false );
    }

    public void setScale( float scale ) {
        mScale = scale;
        refreshPositions();
    }

    public float getScale() {
        return mScale;
    }

    public View addLayoutMarker( View view, int x, int y, Float relativeX, Float relativeY, float absoluteX, float absoluteY ) {
        ViewGroup.LayoutParams defaultLayoutParams = view.getLayoutParams();
        LayoutParams markerLayoutParams = (defaultLayoutParams != null)
                ? generateLayoutParams(defaultLayoutParams)
                : generateDefaultLayoutParams();
        markerLayoutParams.x = x;
        markerLayoutParams.y = y;
        markerLayoutParams.relativeAnchorX = relativeX;
        markerLayoutParams.relativeAnchorY = relativeY;
        markerLayoutParams.absoluteAnchorX = absoluteX;
        markerLayoutParams.absoluteAnchorY = absoluteY;
        markerLayoutParams.mode = 1;
        return addMarker( view, markerLayoutParams );
    }

    public View addMarker( View view, int x, int y, Float relativeX, Float relativeY, float absoluteX, float absoluteY ) {
        LayoutParams layoutParams = new MarkerLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                x, y,
                relativeX, relativeY,
                absoluteX, absoluteY);
        return addMarker( view, layoutParams );
    }

    public View addMarker( View view, MarkerLayout.LayoutParams params ) {
        addView( view, params );
        return view;
    }

    public void moveMarker( View view, int x, int y ) {
        MarkerLayout.LayoutParams lp = (MarkerLayout.LayoutParams) view.getLayoutParams();
        if (lp == null) {
            return;
        }
        if (lp.mode == 0) {
            lp.x = x;
            lp.y = y;
            populateLayoutParams(view);
            view.setLeft(lp.mLeft);
            view.setTop(lp.mTop);
        } else {
            MarkerLayout.LayoutParams layoutParams = (MarkerLayout.LayoutParams) view.getLayoutParams();
            layoutParams.x = x;
            layoutParams.y = y;
            moveMarker( view, layoutParams );
        }
    }

    public void moveMarker( View view, MarkerLayout.LayoutParams params ) {
        if( indexOfChild( view ) > -1 ) {
            view.setLayoutParams( params );
            requestLayout();
        }
    }

    public void removeMarker( View view ) {
        if (view.getParent() == this) {
            removeView( view );
        }
    }

    public void setMarkerTapListener( MarkerLayout.MarkerTapListener markerTapListener ) {
        mMarkerTapListener = markerTapListener;
    }

    private View getViewFromTap( int x, int y ) {
        for( int i = getChildCount() - 1; i >= 0; i-- ) {
            View child = getChildAt( i );
            MarkerLayout.LayoutParams layoutParams = (MarkerLayout.LayoutParams) child.getLayoutParams();
            Rect hitRect = layoutParams.getHitRect();
            if( hitRect.contains( x, y ) ) {
                return child;
            }
        }
        return null;
    }

    public void processHit( int x, int y ) {
        if( mMarkerTapListener != null ) {
            View view = getViewFromTap( x, y );
            if( view != null ) {
                mMarkerTapListener.onMarkerTap( view, x, y );
            }
        }
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        measureChildren( widthMeasureSpec, heightMeasureSpec );
        for( int i = 0; i < getChildCount(); i++ ) {
            View child = getChildAt( i );
            populateLayoutParams(child);
        }
        int availableWidth = View.MeasureSpec.getSize( widthMeasureSpec );
        int availableHeight = View.MeasureSpec.getSize( heightMeasureSpec );
        setMeasuredDimension( availableWidth, availableHeight );
    }

    @Override
    protected void onLayout( boolean changed, int l, int t, int r, int b ) {
        for( int i = 0; i < getChildCount(); i++ ) {
            View child = getChildAt( i );
            if( child.getVisibility() != GONE ) {
                MarkerLayout.LayoutParams layoutParams = (MarkerLayout.LayoutParams) child.getLayoutParams();
                child.layout( layoutParams.mLeft, layoutParams.mTop, layoutParams.mRight, layoutParams.mBottom );
            }
        }
    }

    protected LayoutParams populateLayoutParams(View child) {
        MarkerLayout.LayoutParams layoutParams = (MarkerLayout.LayoutParams) child.getLayoutParams();
        if (child.getVisibility() != View.GONE) {
            // actual sizes of children
            int actualWidth = child.getMeasuredWidth();
            int actualHeight = child.getMeasuredHeight();
            // calculate combined anchor offsets
            float widthOffset = actualWidth * layoutParams.relativeAnchorX + layoutParams.absoluteAnchorX;
            float heightOffset = actualHeight * layoutParams.relativeAnchorY + layoutParams.absoluteAnchorY;
            // get offset position
            int scaledX = (int) (layoutParams.x * mScale);
            int scaledY = (int) (layoutParams.y * mScale);
            // save computed values
            layoutParams.mLeft = (int) (scaledX + widthOffset);
            layoutParams.mTop = (int) (scaledY + heightOffset);
            layoutParams.mRight = layoutParams.mLeft + actualWidth;
            layoutParams.mBottom = layoutParams.mTop + actualHeight;
        }
        return layoutParams;
    }

    public void refreshPositions() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutParams layoutParams = populateLayoutParams(child);
                child.setLeft(layoutParams.mLeft);
                child.setTop(layoutParams.mTop);
                child.setRight(layoutParams.mRight);
                child.setBottom(layoutParams.mBottom);
            }
        }
    }

    @Override
    protected MarkerLayout.LayoutParams generateDefaultLayoutParams() {
        return new MarkerLayout.LayoutParams( MarkerLayout.LayoutParams.WRAP_CONTENT, MarkerLayout.LayoutParams.WRAP_CONTENT, 0, 0 );
    }

    @Override
    protected MarkerLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams ) {
        return new MarkerLayout.LayoutParams( layoutParams );
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public int x = 0;
        public int y = 0;
        public Float relativeAnchorX = null;
        public Float relativeAnchorY = null;
        public float absoluteAnchorX;
        public float absoluteAnchorY;

        public int mode = 0;

        public int mTop;
        public int mLeft;
        public int mBottom;
        public int mRight;

        private Rect mHitRect;

        private Rect getHitRect() {
            if( mHitRect == null ) {
                mHitRect = new Rect();
            }
            mHitRect.left = mLeft;
            mHitRect.top = mTop;
            mHitRect.right = mRight;
            mHitRect.bottom = mBottom;
            return mHitRect;
        }

        public LayoutParams( ViewGroup.LayoutParams source ) {
            super( source );
        }

        public LayoutParams( int width, int height, int left, int top ) {
            super( width, height );
            x = left;
            y = top;
        }

        public LayoutParams( int width, int height, int left, int top, Float relativeAnchorLeft, Float relativeAnchorTop, float absoluteAnchorLeft, float absoluteAnchorTop ) {
            super( width, height );
            x = left;
            y = top;
            relativeAnchorX = relativeAnchorLeft;
            relativeAnchorY = relativeAnchorTop;
            absoluteAnchorX = absoluteAnchorLeft;
            absoluteAnchorY = absoluteAnchorTop;
        }

    }

    public interface MarkerTapListener {
        void onMarkerTap( View view, int x, int y );
    }
}
