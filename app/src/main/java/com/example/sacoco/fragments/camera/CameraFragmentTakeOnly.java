package com.example.sacoco.fragments.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.core.content.ContextCompat;

import com.example.sacoco.MainActivity;
import com.example.sacoco.fragments.ClothDetailsFragment;

public class CameraFragmentTakeOnly extends AbstractCameraFragment {
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            setupCameraUseCases();
        }
        else {
            this.requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    @Override
    protected void setupCameraUseCases() {
        View view = this.requireView();
        this.cameraFragmentButton.setOnClickListener(
                buttonView -> {
                    this.cameraFragmentButton.setEnabled(false);
                    this.cameraController.takePicture(this.cameraExecutor, new ImageCapture.OnImageCapturedCallback() {
                        @Override
                        public void onCaptureSuccess(@NonNull ImageProxy image) {
                            super.onCaptureSuccess(image);

                            bagClothViewModel.setClothImageTemp(image.toBitmap());
                            image.close();
                            onCameraFragmentFinish();
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            super.onError(exception);
                            Toast.makeText(view.getContext(), "Erreur lors de la capture" +
                                    " de l'image !", Toast.LENGTH_SHORT).show();
                            Log.e(view.getContext().getClass().getName(), exception.toString());
                        }
                    });
                }
        );

        this.cameraController.bindToLifecycle(this);
    }

    @Override
    protected void onCameraFragmentFinish() {
        this.cameraController.unbind();
        MainActivity mainActivity = (MainActivity) this.requireActivity();
        mainActivity.loadFragment(ClothDetailsFragment.class);
    }
}
