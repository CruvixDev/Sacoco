package com.example.sacoco.fragments.camera;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sacoco.R;
import com.example.sacoco.viewmodels.BagClothViewModel;

import java.util.concurrent.Executor;

public abstract class AbstractCameraFragment extends Fragment {
    protected BagClothViewModel bagClothViewModel;
    protected LifecycleCameraController cameraController;
    protected Executor cameraExecutor;
    protected PreviewView previewView;
    protected Button cameraFragmentButton;
    protected final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    setupCameraUseCases();
                    Log.i(this.getClass().getName(), "Camera permission granted");
                }
                else {
                    Log.i(this.getClass().getName(), "Camera permission denied");
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View fragmentView = inflater.inflate(R.layout.fragment_camera_layout, container, false);

        this.bagClothViewModel = new ViewModelProvider(this.requireActivity()).get(BagClothViewModel.class);
        this.cameraFragmentButton = fragmentView.findViewById(R.id.captureImageButton);
        this.cameraController = new LifecycleCameraController(this.requireActivity().getBaseContext());
        this.cameraController.setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA);
        this.cameraExecutor = ContextCompat.getMainExecutor(this.requireContext());
        this.previewView = fragmentView.findViewById(R.id.viewFinder);
        this.previewView.setController(this.cameraController);

        return fragmentView;
    }

    protected abstract void setupCameraUseCases();
    protected abstract void onCameraFragmentFinish();
}
