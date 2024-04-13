package com.example.sacoco.fragments.camera;

import static androidx.camera.view.CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED;

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
import androidx.camera.mlkit.vision.MlKitAnalyzer;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sacoco.R;
import com.example.sacoco.cominterface.DialogInterface;
import com.example.sacoco.dialogs.AddClothDialogFragment;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.List;
import java.util.UUID;

public class CameraFragmentTakeAndScan extends AbstractCameraFragment implements DialogInterface {
    private String lastBarcodeStringResult;
    private Toast currentToast;

    public CameraFragmentTakeAndScan() {
        this.lastBarcodeStringResult = "";
    }

    @Override
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
                    if (this.bagClothViewModel.isClothInCreationSet()) {
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
                    else {
                        if (this.currentToast != null) {
                            this.currentToast.cancel();
                        }
                        this.currentToast = Toast.makeText(this.requireContext(), "Scanner " +
                                "un QR code avant !", Toast.LENGTH_SHORT);
                        this.currentToast.show();
                    }
                }
        );

        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();
        BarcodeScanner barcodeScanner = BarcodeScanning.getClient(options);

        this.cameraController.setImageAnalysisAnalyzer(this.cameraExecutor,
                new MlKitAnalyzer(List.of(barcodeScanner), COORDINATE_SYSTEM_VIEW_REFERENCED,
                        this.cameraExecutor, result -> {
                    List<Barcode> barcodeResults = result.getValue(barcodeScanner);

                    if (barcodeResults != null && barcodeResults.size() == 1 &&
                            barcodeResults.get(0) != null) {
                        String bardcodeResultString = barcodeResults.get(0).getRawValue();

                        if (!this.lastBarcodeStringResult.equals(bardcodeResultString)) {
                            try {
                                UUID clothUUID = UUID.fromString(bardcodeResultString);
                                this.bagClothViewModel.setClothInCreation(clothUUID);

                                Toast.makeText(this.requireContext(), "Vêtement détecté !",
                                        Toast.LENGTH_SHORT).show();
                            }
                            catch (IllegalArgumentException e) {
                                Toast.makeText(this.requireContext(), "Aucun vêtement détecté !",
                                        Toast.LENGTH_SHORT).show();
                            }

                            this.lastBarcodeStringResult = bardcodeResultString;
                        }
                    }
                })
        );

        this.cameraController.bindToLifecycle(this);
    }

    @Override
    protected void onCameraFragmentFinish() {
        this.cameraController.unbind();

        AddClothDialogFragment addClothDialogFragment = new AddClothDialogFragment();
        addClothDialogFragment.setDialogInterface(this);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setReorderingAllowed(true)
                .add(R.id.fragmentContainerView, addClothDialogFragment)
                .commit();
    }

    @Override
    public void onDialogDismiss() {
        this.cameraFragmentButton.setEnabled(true);
        this.lastBarcodeStringResult = "";

        this.cameraController.bindToLifecycle(this);
    }
}
