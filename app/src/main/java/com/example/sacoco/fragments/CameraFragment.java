package com.example.sacoco.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;

import com.example.sacoco.R;

public class CameraFragment extends Fragment {

    public CameraFragment() {
        super(R.layout.fragment_camera_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PreviewView previewView = view.findViewById(R.id.viewFinder);
        LifecycleCameraController cameraController = new LifecycleCameraController(
                requireActivity().getBaseContext());
        cameraController.bindToLifecycle(this);
        cameraController.setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA);
        previewView.setController(cameraController);
    }
}
