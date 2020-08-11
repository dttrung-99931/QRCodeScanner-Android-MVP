package com.example.barcodescanner.ui.main.scan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.barcodescanner.R;
import com.example.barcodescanner.util.ViewUtil;
import com.google.android.gms.vision.CameraSource;

import java.util.ArrayList;
import java.util.List;

/**
 * A view which renders a series of custom graphics to be overlayed on top of an associated preview
 * (i.e., the camera preview). The creator can add graphics objects, update the objects, and remove
 * them, triggering the appropriate drawing and invalidation within the view.
 *
 * <p>Supports scaling and mirroring of the graphics relative the camera's preview properties. The
 * idea is that detection items are expressed in terms of a preview size, but need to be scaled up
 * to the full view size, and also mirrored in the case of the front-facing camera.
 *
 * <p>Associated {@link Graphic} items should use the following methods to convert to view
 * coordinates for the graphics that are drawn:
 *
 * <ol>
 *   <li>{@link Graphic#scaleX(float)} and {@link Graphic#scaleY(float)} adjust the size of the
 *       supplied value from the preview scale to the view scale.
 *   <li>{@link Graphic#translateX(float)} and {@link Graphic#translateY(float)} adjust the
 *       coordinate from the preview's coordinate system to the view coordinate system.
 * </ol>
 */
public class DetectionOverlayView extends View {
    private final Object lock = new Object();
    private int previewWidth;
    private float widthScaleFactor = 1.0f;
    private int previewHeight;
    private float heightScaleFactor = 1.0f;
    private int facing = CameraSource.CAMERA_FACING_BACK;
    private final List<Graphic> graphics = new ArrayList<>();
    private Paint mDetectionPaint, mScanFramePaint, mSquareMarkPaint;
    private int mScanFrameSize, mSquareMarkSize, mSquareMarkStrokeWidth;

    public void removeAllWithoutInvalidate() {
        synchronized (lock) {
            graphics.clear();
        }
    }

    public void removeAllAndInvalidate() {
        synchronized (lock) {
            graphics.clear();
        }
        postInvalidate();
    }

    public Rect getScanAreaInOverlayView() {
        return new Rect(
                getLeft() + mScanFrameSize,
                getTop() + mScanFrameSize,
                getRight() - mScanFrameSize,
                getBottom() - mScanFrameSize * 3 / 2
        );
    }

    /*
     * Convert rect from an camera prevew to rect in the overlay view
     * by used heightScaleFactor and widthScaleFactor
     * */
    public Rect scaleRectToOverlayRatio(Rect rect) {
        return new Rect(
                (int) scaleX(rect.left),
                (int) scaleY(rect.top),
                (int) scaleX(rect.right),
                (int) scaleY(rect.bottom)
        );
    }

    /*
     * Convert rect from an camera prevew to rect in the overlay view
     * by used heightScaleFactor and widthScaleFactor
     * */
    public Rect scaleRectToPreviewRatio(Rect rect) {
        float widthFactorOverlayToPreview = (float) previewWidth / getWidth();
        float heightFactorOverlayToPreview = (float) previewHeight / getHeight();
        return ViewUtil.scaleRect(rect, widthFactorOverlayToPreview, heightFactorOverlayToPreview);
    }

    public float getWidthScaleFactor(){
        return widthScaleFactor;
    }

    public float getHeightScaleFactor(){
        return heightScaleFactor;
    }

    /**
     * Base class for a custom graphics object to be rendered within the graphic overlay. Subclass
     * this and implement the {@link Graphic#draw(Canvas)} method to define the graphics element. Add
     * instances to the overlay using {@link DetectionOverlayView#add(Graphic)}.
     */
    public abstract static class Graphic {
        private DetectionOverlayView overlay;

        public Graphic(DetectionOverlayView overlay) {
            this.overlay = overlay;
        }

        /**
         * Draw the graphic on the supplied canvas. Drawing should use the following methods to convert
         * to view coordinates for the graphics that are drawn:
         *
         * <ol>
         *   <li>{@link Graphic#scaleX(float)} and {@link Graphic#scaleY(float)} adjust the size of the
         *       supplied value from the preview scale to the view scale.
         *   <li>{@link Graphic#translateX(float)} and {@link Graphic#translateY(float)} adjust the
         *       coordinate from the preview's coordinate system to the view coordinate system.
         * </ol>
         *
         * @param canvas drawing canvas
         */
        public abstract void draw(Canvas canvas);

        /**
         * Adjusts a horizontal value of the supplied value from the preview scale to the view scale.
         */
        public float scaleX(float horizontal) {
            return overlay.scaleX(horizontal);
        }

        /**
         * Adjusts a vertical value of the supplied value from the preview scale to the view scale.
         */
        public float scaleY(float vertical) {
            return overlay.scaleY(vertical);
        }

        /**
         * Returns the application context of the app.
         */
        public Context getApplicationContext() {
            return overlay.getContext().getApplicationContext();
        }

        /**
         * Adjusts the x coordinate from the preview's coordinate system to the view coordinate system.
         */
        public float translateX(float x) {
            if (overlay.facing == CameraSource.CAMERA_FACING_FRONT) {
                return overlay.getWidth() - scaleX(x);
            } else {
                return scaleX(x);
            }
        }

        /**
         * Adjusts the y coordinate from the preview's coordinate system to the view coordinate system.
         */
        public float translateY(float y) {
            return scaleY(y);
        }

        public void postInvalidate() {
            overlay.postInvalidate();
        }

        public DetectionOverlayView getOverlay() {
            return overlay;
        }
    }

    private float scaleY(float vertical) {
        return vertical * heightScaleFactor;
    }

    private float scaleX(float horizontal) {
        return horizontal * widthScaleFactor;
    }

    public DetectionOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initScanFramePaint();
        initSquareMarksGraphic();
    }

    private void initSquareMarksGraphic() {
        mSquareMarkStrokeWidth = getResources().getDimensionPixelSize(R.dimen._5sdp);
        mSquareMarkSize = getResources().getDimensionPixelSize(R.dimen._22sdp);

        mSquareMarkPaint = new Paint();
        mSquareMarkPaint.setColor(
                ContextCompat.getColor(getContext(), android.R.color.holo_purple)
        );
        mSquareMarkPaint.setStrokeWidth(mSquareMarkStrokeWidth);
        mSquareMarkPaint.setStyle(Paint.Style.STROKE);
    }

    private void initScanFramePaint() {
        mScanFramePaint = new Paint();
        mScanFramePaint.setColor(
                ContextCompat.getColor(getContext(), R.color.backgroundScanFrame)
        );
        mScanFramePaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Removes all graphics from the overlay.
     */
    public void clear() {
        synchronized (lock) {
            graphics.clear();
        }
        postInvalidate();
    }

    /**
     * Adds a graphic to the overlay.
     */
    public void add(Graphic graphic) {
        synchronized (lock) {
            graphics.add(graphic);
        }
    }

    /**
     * Removes a graphic from the overlay.
     */
    public void remove(Graphic graphic) {
        synchronized (lock) {
            graphics.remove(graphic);
        }
        postInvalidate();
    }

    /**
     * Sets the camera attributes for size and facing direction, which informs how to transform image
     * coordinates later.
     */
    public void setCameraInfo(int previewWidth, int previewHeight, int facing) {
        synchronized (lock) {
            this.previewWidth = previewWidth;
            this.previewHeight = previewHeight;
            this.facing = facing;
        }
        postInvalidate();
    }

    /**
     * Draws the overlay with its associated graphic objects.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mScanFrameSize = (int) (((getWidth() + getHeight()) / 2) * 0.15);

        drawScanFrame(canvas);
        drawSquareMarks(canvas);
        synchronized (lock) {
            if ((previewWidth != 0) && (previewHeight != 0)) {
                widthScaleFactor = (float) getWidth() / previewWidth;
                heightScaleFactor = (float) getHeight() / previewHeight;
            }

            for (Graphic graphic : graphics) {
                graphic.draw(canvas);
            }
        }
    }

    /**
     * Draw the shape marked by dots
     * ************
     * *..******..*
     * *.        .*
     * **        **
     * *.        .*
     * *..******..*
     * ************
     */
    private void drawSquareMarks(Canvas canvas) {
        Path path = new Path();

        // Draw the top left
        path.moveTo(
                getLeft() + mScanFrameSize,
                getTop() + mScanFrameSize + mSquareMarkSize
        );
        path.lineTo(
                getLeft() + mScanFrameSize,
                getTop() + mScanFrameSize
        );
        path.lineTo(
                getLeft() + mScanFrameSize + mSquareMarkSize,
                getTop() + mScanFrameSize
        );
        canvas.drawPath(path, mSquareMarkPaint);
        path.reset();

        /**
         * Draw the shape marked by dots
         * ************
         * *..******..*
         * *.        .*
         * **        **
         * *.        .*
         * *..******..*
         * ************
         */
        // Draw the top right
        path.moveTo(
                getRight() - mScanFrameSize,
                getTop() + mScanFrameSize + mSquareMarkSize
        );
        path.lineTo(
                getRight() - mScanFrameSize,
                getTop() + mScanFrameSize
        );
        path.lineTo(
                getRight() - mScanFrameSize - mSquareMarkSize,
                getTop() + mScanFrameSize
        );
        canvas.drawPath(path, mSquareMarkPaint);
        path.reset();

/**
 * Draw the shape marked by dots
 * ************
 * *..******..*
 * *.        .*
 * **        **
 * *.        .*
 * *..******..*
 * ************
 */
        // Draw the bottom right
        path.moveTo(
                getRight() - mScanFrameSize,
                getBottom() - mScanFrameSize * 3 / 2 - mSquareMarkSize
        );
        path.lineTo(
                getRight() - mScanFrameSize,
                getBottom() - mScanFrameSize * 3 / 2
        );
        path.lineTo(
                getRight() - mScanFrameSize - mSquareMarkSize,
                getBottom() - mScanFrameSize * 3 / 2
        );
        canvas.drawPath(path, mSquareMarkPaint);
        path.reset();

/**
 * Draw the shape marked by dots
 * ************
 * *..******..*
 * *.        .*
 * **        **
 * *.        .*
 * *..******..*
 * ************
 */
        // Draw the bottom left
        path.moveTo(
                getLeft() + mScanFrameSize,
                getBottom() - mScanFrameSize * 3 / 2 - mSquareMarkSize
        );
        path.lineTo(
                getLeft() + mScanFrameSize,
                getBottom() - mScanFrameSize * 3 / 2
        );
        path.lineTo(
                getLeft() + mScanFrameSize + mSquareMarkSize,
                getBottom() - mScanFrameSize * 3 / 2
        );
        canvas.drawPath(path, mSquareMarkPaint);
        path.reset();
    }

    /**
     * Draw the frame like that
     * ********
     * ********
     * **    **
     * **    **
     * ********
     * ********
     */
    private void drawScanFrame(Canvas canvas) {
        // Draw the top rect
        canvas.drawRect(
                getLeft(), getTop(),
                getRight(), getTop() + mScanFrameSize,
                mScanFramePaint
        );
        // Draw the bottom rect
        canvas.drawRect(
                getLeft(), getBottom() - mScanFrameSize * 3 / 2,
                getRight(), getBottom(),
                mScanFramePaint
        );
//        /*
//         * ********
//         * ********
//         * **    **
//         * **    **
//         * ********
//         * ********
//         */
        // Draw the left rect
        canvas.drawRect(
                getLeft(), getTop() + mScanFrameSize,
                getLeft() + mScanFrameSize, getBottom() - mScanFrameSize * 3 / 2,
                mScanFramePaint
        );
        // Draw the left rect
        canvas.drawRect(
                getRight() - mScanFrameSize, getTop() + mScanFrameSize,
                getRight(), getBottom() - mScanFrameSize * 3 / 2,
                mScanFramePaint
        );

    }

    public Paint getPaint() {
        return mDetectionPaint;
    }

    public void setPaint(Paint mPaint) {
        this.mDetectionPaint = mPaint;
    }
}