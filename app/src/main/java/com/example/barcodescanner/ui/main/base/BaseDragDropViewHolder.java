package com.example.barcodescanner.ui.main.base;

import android.view.View;

import androidx.annotation.NonNull;

import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter;

/**
 * Created by Trung on 8/19/2020
 */
public abstract class BaseDragDropViewHolder extends DragDropSwipeAdapter.ViewHolder {
    public BaseDragDropViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bindView(int position);
}
