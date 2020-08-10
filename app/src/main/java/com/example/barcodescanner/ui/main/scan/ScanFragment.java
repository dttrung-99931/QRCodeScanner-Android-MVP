package com.example.barcodescanner.ui.main.scan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.example.barcodescanner.R;
import com.example.barcodescanner.databinding.FragmentScanBinding;
import com.example.barcodescanner.ui.base.BaseFragment;
import com.example.barcodescanner.ui.main.MainActivity;
import com.example.barcodescanner.util.CommonUtil;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

/**
 * Created by Trung on 8/7/2020
 */
public class ScanFragment extends BaseFragment implements ScanPresenter.View, ImageAnalysis.Analyzer {

    private static final int REQUEST_CODE_CAMERA_PERMISSION = 1;

    private FragmentScanBinding mBinding;
    private BarcodeDetector mBarCodeDetector;
    private CameraControl mCameraControl;
    private boolean mIsFlashOn = false;
    private ProcessCameraProvider mCameraProvider;
    private CameraSelector mCameraSelector;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView();
        return mBinding.getRoot();
    }

    private void bindView() {
        mBinding = FragmentScanBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        processSubscribingCameraPreviewAndAnalysis();
        init();
        setupViews();
    }

    @SuppressLint("RestrictedApi")
    private void setupViews() {
        setupZoomSeekBar();
        setupBtnFlash();
        setupBtnTurnCamera();
    }

    private void setupZoomSeekBar() {
        mBinding.zoomSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCameraControl.setLinearZoom((float)progress/200);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setupBtnFlash() {
        mBinding.btnFlash.setOnClickListener(v -> {
            if (mIsFlashOn) {
                mBinding.btnFlash.setImageResource(R.drawable.ic_flash_off);
            }else {
                mBinding.btnFlash.setImageResource(R.drawable.ic_flash_on);
            }
            mIsFlashOn = !mIsFlashOn;
            mCameraControl.enableTorch(mIsFlashOn);
        });

    }


    private void setupBtnTurnCamera() {
        mBinding.btnTurnCamera.setOnClickListener(v -> {
            if (mCameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                mCameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
            } else {
                mCameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
            }
            mCameraProvider.unbindAll();
            subscribeCameraPreviewAndAnalysis(mCameraSelector);
        });
    }

    private void init() {
        mBarCodeDetector = new BarcodeDetector
                .Builder(requireContext())
                .setBarcodeFormats(
                        com.google.android.gms.vision.barcode.Barcode.DATA_MATRIX
                                | Barcode.QR_CODE
                )
                .build();

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        mBinding.barCodeDetectionView.setPaint(paint);
    }

    private void processSubscribingCameraPreviewAndAnalysis() {
        if (isCameraPermissionGranted()) {
            subscribeCameraPreviewAndAnalysis(CameraSelector.DEFAULT_BACK_CAMERA);
        } else requestCameraPermission();
    }

    private boolean isCameraPermissionGranted() {
        return getBaseActivity().checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        requestPermissions(permissions, REQUEST_CODE_CAMERA_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            processSubscribingCameraPreviewAndAnalysis();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void subscribeCameraPreviewAndAnalysis(CameraSelector cameraSelector) {
        ListenableFuture<ProcessCameraProvider> cameraSubscriber
                = ProcessCameraProvider.getInstance(requireContext());

        cameraSubscriber.addListener(() -> {
                    try {
                        setupCameraForPreviewAndAnalysis(cameraSubscriber, cameraSelector);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }, ContextCompat.getMainExecutor(requireContext())
        );
    }

    @SuppressLint("RestrictedApi")
    private void setupCameraForPreviewAndAnalysis(
            ListenableFuture<ProcessCameraProvider> cameraSubscriber, CameraSelector cameraSelector)
            throws ExecutionException, InterruptedException {
        // used to the activity lifecycle to the lifecycle of camera
        mCameraProvider = cameraSubscriber.get();

        // attach the cameraPreview to the preview
        Preview mCameraPreview = new Preview
                .Builder()
                .build();
        mCameraPreview.setSurfaceProvider(
                mBinding.cameraPreview.createSurfaceProvider()
        );

        ImageAnalysis mCameraAnalyzer = new ImageAnalysis.Builder()
                .build();
        mCameraAnalyzer.setAnalyzer(
                ContextCompat.getMainExecutor(requireContext()),
                this);

        mCameraSelector = cameraSelector;
        mCameraControl = CameraX.getCameraWithCameraSelector(mCameraSelector)
                .getCameraControl();

        mCameraProvider.unbindAll();
        mCameraProvider.bindToLifecycle(this,
                cameraSelector, mCameraPreview, mCameraAnalyzer);
    }

    private boolean mIsShowingResult = false;
    private ImageProxy mCurDetectedImageProxy;

    @Override
    public void analyze(@NonNull ImageProxy image) {
        if (!mIsShowingResult) {
            SparseArray<Barcode> detectedBarcodes =
                    detectBarCodeAndShowDetection(image);
            if (detectedBarcodes.size() != 0) {
                mCurDetectedImageProxy = image;
                MainActivity mainActi = (MainActivity) getBaseActivity();
                mainActi.showBarcodeResultWithDelay(detectedBarcodes);
            } else {  // If there is no detected barcode images
                // Close to detect another frame
                image.close();
            }
        }
    }

    private SparseArray<Barcode> detectBarCodeAndShowDetection(ImageProxy image) {
        @SuppressLint("UnsafeExperimentalUsageError")
        Bitmap bitmap = CommonUtil.toBitmap(image.getImage());
        Frame frame = new Frame.Builder()
                .setBitmap(bitmap)
                .build();

        SparseArray<com.google.android.gms.vision.barcode.Barcode> detectedBarCodes
                = mBarCodeDetector.detect(frame);
        showBarCodeDetection(detectedBarCodes, bitmap);
        return detectedBarCodes;
    }

    private void showBarCodeDetection(
            SparseArray<Barcode> detectedBarCodes, Bitmap bitmap) {

        // Mark detection

        if (detectedBarCodes.size() == 0) {
            mBinding.barCodeDetectionView.removeAllAndInvalidate();
            return;
        }

        setDetectionViewProperties(bitmap);

        for (int i = 0; i < detectedBarCodes.size(); i++) {
            int key = detectedBarCodes.keyAt(0);
            BarCodeDetectionGraphic barCodeDetection = new BarCodeDetectionGraphic(
                    mBinding.barCodeDetectionView, detectedBarCodes.get(key));
            mBinding.barCodeDetectionView.add(barCodeDetection);
        }
        mBinding.barCodeDetectionView.postInvalidate();

        // Overlay the detected barcode imageView on the cameraPreview
        mBinding.imvDetectedBarCode.setImageBitmap(
                mBinding.cameraPreview.getBitmap()
        );
        mBinding.imvDetectedBarCode.setVisibility(View.VISIBLE);
    }

    private void setDetectionViewProperties(Bitmap image) {
        mBinding.barCodeDetectionView.removeAllWithoutInvalidate();
        mBinding.barCodeDetectionView.setCameraInfo(
                image.getWidth(),
                image.getHeight(),
                CameraSource.CAMERA_FACING_BACK
        );
    }

    public void resumeScanningWithDelay() {
        mBinding.imvDetectedBarCode.setVisibility(View.GONE);
        mBinding.barCodeDetectionView.clear();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (mCurDetectedImageProxy != null) {
                mCurDetectedImageProxy.close();
                mCurDetectedImageProxy = null;
            }
        }, 300);
    }
}
