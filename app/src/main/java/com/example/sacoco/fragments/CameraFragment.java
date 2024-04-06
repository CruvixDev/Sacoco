package com.example.sacoco.fragments;

import static androidx.camera.view.CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED;

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
import androidx.camera.mlkit.vision.MlKitAnalyzer;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.sacoco.R;
import com.example.sacoco.dialogs.AddClothDialogFragment;
import com.example.sacoco.viewmodels.BagClothViewModel;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

public class CameraFragment extends Fragment {
    private BagClothViewModel bagClothViewModel;
    private String lastBarcodeStringResult;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    setupCamera();
                    Log.i(this.getClass().getName(), "Camera permission granted");
                }
                else {
                    Log.i(this.getClass().getName(), "Camera permission denied");
                }
            });

    public CameraFragment() {
        super(R.layout.fragment_camera_layout);
        this.lastBarcodeStringResult = "";
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.bagClothViewModel = new ViewModelProvider(requireActivity()).get(BagClothViewModel.class);

        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {

            setupCamera();
        }
        else {
            this.requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void setupCamera() {
        View view = this.requireView();
        PreviewView previewView = view.findViewById(R.id.viewFinder);
        LifecycleCameraController cameraController = new LifecycleCameraController(
                this.requireActivity().getBaseContext());
        cameraController.setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA);

        Executor cameraExecutor = ContextCompat.getMainExecutor(this.requireContext());

        Button captureImageButton = view.findViewById(R.id.captureImageButton);
        captureImageButton.setOnClickListener(view1 ->
                cameraController.takePicture(cameraExecutor, new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                        super.onCaptureSuccess(image);

                        if (bagClothViewModel.isClothInCreationSet()) {
                            bagClothViewModel.setClothImageTemp(image.toBitmap());
                            image.close();
                            loadAddClothDialogFragment();
                        }
                        else {
                            Toast.makeText(view.getContext(), "Please scan a QR code " +
                                    "before", Toast.LENGTH_SHORT).show();
                        }
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

        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();
        BarcodeScanner barcodeScanner = BarcodeScanning.getClient(options);

        cameraController.setImageAnalysisAnalyzer(cameraExecutor,
                new MlKitAnalyzer(List.of(barcodeScanner), COORDINATE_SYSTEM_VIEW_REFERENCED,
                        cameraExecutor, result -> {
                    List<Barcode> barcodeResults = result.getValue(barcodeScanner);

                    if (barcodeResults != null && barcodeResults.size() == 1 &&
                            barcodeResults.get(0) != null) {
                        String bardcodeResultString = barcodeResults.get(0).getRawValue();

                        if (!this.lastBarcodeStringResult.equals(bardcodeResultString)) {
                            try {
                                UUID clothUUID = UUID.fromString(bardcodeResultString);
                                this.bagClothViewModel.setClothInCreation(clothUUID);

                                Toast.makeText(this.requireContext(), "Cloth detected!",
                                        Toast.LENGTH_SHORT).show();
                            }
                            catch (IllegalArgumentException e) {
                                Toast.makeText(this.requireContext(), "No cloth detected!",
                                        Toast.LENGTH_SHORT).show();
                            }

                            this.lastBarcodeStringResult = bardcodeResultString;
                        }
                    }
                })
        );

        cameraController.bindToLifecycle(this);
        previewView.setController(cameraController);
    }

    private void loadAddClothDialogFragment() {
        AddClothDialogFragment addClothDialogFragment = new AddClothDialogFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setReorderingAllowed(true)
                .add(R.id.fragmentContainerView, addClothDialogFragment)
                .commit();
    }
}
