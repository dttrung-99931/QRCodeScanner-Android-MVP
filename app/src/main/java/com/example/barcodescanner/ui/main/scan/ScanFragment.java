package com.example.barcodescanner.ui.main.scan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.example.barcodescanner.R;
import com.example.barcodescanner.data.local.model.RelationBarcodeData;
import com.example.barcodescanner.databinding.FragmentScanBinding;
import com.example.barcodescanner.ui.base.BaseFragment;
import com.example.barcodescanner.ui.main.MainActivity;
import com.example.barcodescanner.ui.main.result.ResultFragment;
import com.example.barcodescanner.util.ViewUtil;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.appbar.AppBarLayout;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

/**
 * Created by Trung on 8/7/2020
 */
public class ScanFragment extends BaseFragment implements
        ScanPresenter.View, ImageAnalysis.Analyzer {

    private static final int REQUEST_CODE_CAMERA_PERMISSION = 1;

    private FragmentScanBinding mBinding;
    private CameraControl mCameraControl;
    private boolean mIsFlashOn = false;
    private ProcessCameraProvider mCameraProvider;
    private CameraSelector mCameraSelector;

    private ScanPresenter mPresenter;

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
        setupBtnRefresh();
    }

    private void setupBtnRefresh() {
        mBinding.btnRefresh.setOnClickListener(v -> {
            if (mIsShowingResult) {
                resumeScanningWithDelay();
                mIsShowingResult = false;
            }
        });
    }

    private void setCollapsingToolbarScrollFlags(int scrollFlagNoScroll) {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)
                mBinding.collapsingToolbar.getLayoutParams();
        params.setScrollFlags(scrollFlagNoScroll);
    }

    private void setupZoomSeekBar() {
        mBinding.zoomSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCameraControl.setLinearZoom((float) progress / 200);
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
            } else {
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

    @Override
    public void onDestroyView() {
        mPresenter.onDetached();
        super.onDestroyView();
    }

    private void init() {
        mPresenter = new ScanPresenter();
        mPresenter.onAttached(this);
        mPresenter.initBarcodeDetector(requireContext().getApplicationContext());

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

    private boolean mIsShowingResult = false, mIsStopScanning = false;
    private ImageProxy mCurAnalyzedImageProxy;

    @SuppressLint("UnsafeExperimentalUsageError")
    @Override
    public void analyze(@NonNull ImageProxy image) {
        mCurAnalyzedImageProxy = image;
        if (!mIsShowingResult && !mIsStopScanning) {
            mPresenter.detectBarcode(image.getImage());
        }
    }

    private void showDetectedBarcodeResult(RelationBarcodeData relBarcodeData) {
        mIsShowingResult = true;
        mBinding.btnRefresh.setVisibility(View.VISIBLE);
        mBinding.layoutResult.setVisibility(View.VISIBLE);

        // Enable collapsing scroll when result is available
        setCollapsingToolbarScrollFlags(
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
        );

        getBaseActivity().replaceFragment(
                R.id.layout_result,
                new ResultFragment(relBarcodeData, ViewGroup.LayoutParams.MATCH_PARENT)
        );
    }

    /*
     * Show only one detection barcode that is in the scan area
     * */
    private void showBarCodeDetection(Barcode barcode) {
        // Draw barcode detection on barCodeDetectionView
        DetectionDrawer barCodeDetection = new DetectionDrawer(
                mBinding.barCodeDetectionView, barcode);
        mBinding.barCodeDetectionView.add(barCodeDetection);
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
        mBinding.btnRefresh.setVisibility(View.GONE);
        mBinding.layoutResult.setVisibility(View.GONE);

        // Disable collapsing scroll on scan
        setCollapsingToolbarScrollFlags(
                AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
        );
        translateAppBarLayoutVertically(
                ViewUtil.sdpToPx(getResources(), R.dimen._40sdp)
        );

        mBinding.imvDetectedBarCode.setVisibility(View.GONE);
        mBinding.barCodeDetectionView.clear();
        closeDetectedImgProxyWithDelay();
    }

    private void closeDetectedImgProxyWithDelay() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (mCurAnalyzedImageProxy != null) {
                mCurAnalyzedImageProxy.close();
                mCurAnalyzedImageProxy = null;
            }
        }, 300);
    }

    @Override
    public void showBarcodeDetectionResult(Pair<Bitmap, SparseArray<Barcode>> result) {
        if (result.second == null) return;

        Barcode barcodeInScanArea = ViewUtil.getOneDetectedBarCodeInScanArea(
                result.second,
                mBinding.barCodeDetectionView.getScanAreaInOverlayView(),
                mBinding.barCodeDetectionView.getWidthScaleFactor(),
                mBinding.barCodeDetectionView.getHeightScaleFactor()
        );

        if (barcodeInScanArea != null) {
            RelationBarcodeData relBarcodeData =
                    RelationBarcodeData.fromBarcode(barcodeInScanArea);

            setDetectionViewProperties(result.first);
            showBarCodeDetection(barcodeInScanArea);

            if (relBarcodeData == null) {
                showToastMsg(R.string.format_not_support);
                mIsShowingResult = true;
                mBinding.btnRefresh.setVisibility(View.VISIBLE);
                return;
            }

            showDetectedBarcodeResult(relBarcodeData);
            scrollResultUp();

            mPresenter.onShowBarcodeResultComplete(relBarcodeData);
        } else {  // If there is no detected barcode images
            // Close to detect another frame
            mCurAnalyzedImageProxy.close();
            mBinding.barCodeDetectionView.removeAllAndInvalidate();
        }

    }

    @Override
    public void notifyRefreshHistoryFragment() {
        ((MainActivity) getBaseActivity()).notifyRefreshHistoryFragment();
    }

    private void scrollResultUp() {
        translateAppBarLayoutVertically(
                -ViewUtil.sdpToPx(getResources(), R.dimen._40sdp)
        );
    }

    private void translateAppBarLayoutVertically(int additionalPx) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                mBinding.appBarLayout.getLayoutParams();

        TranslateAnimation translateAnimation = new TranslateAnimation(
                mBinding.appBarLayout.getX(),
                mBinding.appBarLayout.getX(),
                mBinding.appBarLayout.getY(),
                mBinding.appBarLayout.getY() + additionalPx
        );
        translateAnimation.setDuration(500);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                params.topMargin = params.topMargin + additionalPx;
                mBinding.appBarLayout.setLayoutParams(params);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBinding.appBarLayout.startAnimation(translateAnimation);
    }

    public void stopDetecting() {
        if (!mIsShowingResult) {
            mIsStopScanning = true;
        }
    }

    public void resumeDetecting() {
        if (!mIsShowingResult) {
            mIsStopScanning = false;
            mCurAnalyzedImageProxy.close();
        }
    }
}
