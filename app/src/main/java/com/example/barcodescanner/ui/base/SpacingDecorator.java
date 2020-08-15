package com.example.barcodescanner.ui.base;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Trung on 8/13/2020
 */
public class SpacingDecorator extends RecyclerView.ItemDecoration {
    private int mTopMargin, mBottomMargin, mRightMargin, mLeftMargin;

    public SpacingDecorator(int mTopMargin, int mBottomMargin, int mRightMargin, int mLeftMargin) {
        this.mTopMargin = mTopMargin;
        this.mBottomMargin = mBottomMargin;
        this.mRightMargin = mRightMargin;
        this.mLeftMargin = mLeftMargin;
    }

    public SpacingDecorator(int topMargin) {
        mTopMargin = topMargin;
    }


    public void setTopMargin(int mTopMargin) {
        this.mTopMargin = mTopMargin;
    }

    public void setBottomMargin(int mBottomMargin) {
        this.mBottomMargin = mBottomMargin;
    }

    public void setRightMargin(int mRightMargin) {
        this.mRightMargin = mRightMargin;
    }

    public void setLeftMargin(int mLeftMargin) {
        this.mLeftMargin = mLeftMargin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.top += mTopMargin;
        outRect.left += mLeftMargin;
        outRect.bottom += mBottomMargin;
        outRect.right += mRightMargin;
    }

}
