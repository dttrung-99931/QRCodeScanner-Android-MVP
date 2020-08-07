package com.example.barcodescanner.ui.main.scan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.example.barcodescanner.databinding.ActivityMainBinding;
import com.example.barcodescanner.databinding.FragmentScanBinding;
import com.example.barcodescanner.ui.base.BaseFragment;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

/**
 * Created by Trung on 8/7/2020
 */
public class ScanFragment extends BaseFragment implements ScanPresenter.View{

    private static final int REQUEST_CODE_CAMERA_PERMISSION = 1;

    FragmentScanBinding mBinding;

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
        setupView();
        processSubscribingCameraPreview();
    }

    private void setupView() {

    }

    private void processSubscribingCameraPreview() {
        if (isCameraPermissionGranted()) {
            subscribeCameraPreview();
        } else requestCameraPermission();
    }

    private boolean isCameraPermissionGranted() {
        return getBaseActivity().checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        String []permissions = new String[]{Manifest.permission.CAMERA};
        requestPermissions(permissions, REQUEST_CODE_CAMERA_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            processSubscribingCameraPreview();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void subscribeCameraPreview() {
        ListenableFuture<ProcessCameraProvider> cameraSubscriber =
                ProcessCameraProvider.getInstance(requireContext());

        cameraSubscriber.addListener(() -> {
                    try {
                        setupCameraForPreview(cameraSubscriber);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }, ContextCompat.getMainExecutor(requireContext())
        );
    }

    private void setupCameraForPreview (
            ListenableFuture<ProcessCameraProvider> cameraSubscriber)
            throws ExecutionException, InterruptedException {
        // used to the activity lifecycle to the lifecycle of camera
        ProcessCameraProvider cameraProvider = cameraSubscriber.get();

        // attach the cameraPreview to the preview
        Preview preview = new Preview
                .Builder()
                .build();
        preview.setSurfaceProvider(
                mBinding.mCameraPreview.createSurfaceProvider()
        );

        CameraSelector selector = CameraSelector.DEFAULT_BACK_CAMERA;
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, selector, preview);
    }
}
