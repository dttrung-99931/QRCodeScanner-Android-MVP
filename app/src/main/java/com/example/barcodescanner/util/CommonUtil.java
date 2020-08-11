package com.example.barcodescanner.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.util.Log;

import androidx.camera.core.ImageProxy;

import com.example.barcodescanner.data.model.BarCodeData;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 8/6/2020
 */
public class CommonUtil {

    public static void loge(String msg) {
        Log.d("EEEE", msg);
    }

    public static void logd(String msg) {
        Log.d("DDDD", msg);
    }

    public static Bitmap toBitmap2(Image image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        buffer.rewind();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        byte[] clonedBytes = bytes.clone();
        return BitmapFactory.decodeByteArray(clonedBytes, 0, clonedBytes.length, null);
    }

    public static Bitmap toBitmap(Image image) {
        Image.Plane []planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        if (bitmap.getHeight() < bitmap.getWidth()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                    bitmap.getWidth(), bitmap.getHeight(), true);
            bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    /*
    * Map the @param fileName of a class to corresponding R.string.fieldName
    * */
    public static String mapField(String fieldName, Context context) {
        return fieldName;
    }

    public static String compressObjToString(
            Object data, Context context,
            boolean ignoreEmptyOrNullField) throws IllegalAccessException {

        StringBuilder compressedDataStrBuilder = new StringBuilder();

        Field[] fields = data.getClass().getFields();

        for (int i = 0; i< fields.length; i++) {

            Field field = fields[i];
            String fieldName = field.getName();

            if (CommonUtil.isNormalField(fieldName)) {
                    String mappedFieldName = CommonUtil.mapField(fieldName, context);
                String value = field.get(data).toString();

                if (ignoreEmptyOrNullField && value.isEmpty()) continue;

                String end = ",\n";
                if (i == fields.length - 1) end = ".";

                compressedDataStrBuilder.append(mappedFieldName + ": " + value + end);
            }
        }

        return compressedDataStrBuilder.toString();
    }


    /*
     * return true if fileName is a name of a normal field
     * A normal file is a field that is not a final field (like TYPE_QR_CODE)
     * */
    public static boolean isNormalField(String fieldName) {
        return !fieldName.isEmpty() && Character.isLowerCase(fieldName.charAt(0));
    }

    public static String compressObjArrToString(
            Object []objs,
            Context context,
            boolean ignoreEmptyOrNullField) throws IllegalAccessException {
        StringBuilder compressedDataStr = new StringBuilder();
        for (Object dataObj : objs) {
            compressedDataStr.append(
                    compressObjToString(dataObj, context, ignoreEmptyOrNullField) + "\n\n"
            );
        }
        return compressedDataStr.toString();
    }

    public static String joinStrs(String delimiter, String end, String[] strs) {
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<strs.length; i++) {
            if (i != strs.length -1)
                builder.append(strs[i] + delimiter);
            else builder.append(strs[i] + end);
        }
        return builder.toString();
    }

    public static List<BarCodeData.BarCodeField> parseToBarCodeDataFields(
            Object data, Context context, boolean ignoreEmptyField)
            throws IllegalAccessException {

        List<BarCodeData.BarCodeField> barCodeFields = new ArrayList<>();

        for (Field field : data.getClass().getFields()) {

            String fieldName = field.getName();

            if (CommonUtil.isNormalField(fieldName)) {
                String mappedFieldName = CommonUtil.mapField(fieldName, context);
                String value = field.get(data).toString();

                if (value.isEmpty() && ignoreEmptyField) continue;

                barCodeFields.add(new BarCodeData.BarCodeField(
                        mappedFieldName, value
                ));
            }
        }
        return barCodeFields;
    }

    public static void copyToClipBoard(Context context, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copy", text);
        clipboard.setPrimaryClip(clip);
    }
}
