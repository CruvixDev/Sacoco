package com.example.sacoco.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sacoco.R;

import java.util.concurrent.Executor;

public class CameraFragment extends Fragment {
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.i(this.getClass().getName(), "Camera permission granted");
                }
                else {
                    Log.i(this.getClass().getName(), "Camera permission denied");
                }
            });

    public CameraFragment() {
        super(R.layout.fragment_camera_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {

            PreviewView previewView = view.findViewById(R.id.viewFinder);
            LifecycleCameraController cameraController = new LifecycleCameraController(
                    requireActivity().getBaseContext());
            cameraController.bindToLifecycle(this);
            cameraController.setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA);
            previewView.setController(cameraController);

            Executor cameraExecutor = ContextCompat.getMainExecutor(this.requireContext());

            Button captureImageButton = view.findViewById(R.id.captureImageButton);
            captureImageButton.setOnClickListener(view1 ->
                    cameraController.takePicture(cameraExecutor, new ImageCapture.OnImageCapturedCallback() {
                        @Override
                        public void onCaptureSuccess(@NonNull ImageProxy image) {
                            super.onCaptureSuccess(image);
                            Toast.makeText(view.getContext(), "Image captured",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            super.onError(exception);
                            Toast.makeText(view.getContext(), "Failed to capture image",
                                    Toast.LENGTH_SHORT).show();
                            Log.e(view.getContext().getClass().getName(), exception.toString());
                        }
                    })
            );
        }
        else {
            this.requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }
}
