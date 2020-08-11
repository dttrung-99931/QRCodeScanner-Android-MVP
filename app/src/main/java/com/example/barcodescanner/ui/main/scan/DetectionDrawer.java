package com.example.barcodescanner.ui.main.scan;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.barcodescanner.util.CommonUtil;

/**
 * Created by Trung on 8/8/2020
 */
/*Draw barcode detection. Used with DetectionOverlayView*/
class DetectionDrawer extends DetectionOverlayView.Graphic {
    private com.google.android.gms.vision.barcode.Barcode mBarcode;

    public DetectionDrawer(
            DetectionOverlayView overlay,
            com.google.android.gms.vision.barcode.Barcode barcode) {
        super(overlay);
        mBarcode = barcode;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = getOverlay().getPaint();
        if (paint == null) {
            paint = new Paint();
            CommonUtil.logd("BarCodeDetectionGraphic draw use default Paint");
        }

        Rect rect = mBarcode.getBoundingBox();

        Point[] points = new Point[]{
                new Point(rect.left, rect.top), // top left connor point
                new Point(rect.left, rect.bottom), // ...
                new Point(rect.right, rect.bottom),
                new Point(rect.right, rect.top)
        };
        for (Point point : points) {
            // Map point on from the image frame to on the overlay view
            canvas.drawCircle(
                    scaleX(point.x),
                    scaleY(point.y),
                    4, paint);
        }
    }
}
