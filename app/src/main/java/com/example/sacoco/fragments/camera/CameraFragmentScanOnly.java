package com.example.sacoco.fragments.camera;

import static androidx.camera.view.CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.mlkit.vision.MlKitAnalyzer;
import androidx.core.content.ContextCompat;


import com.example.sacoco.MainActivity;
import com.example.sacoco.fragments.BagDetailsFragment;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class CameraFragmentScanOnly extends AbstractCameraFragment{
    private String lastBarcodeStringResult;
    private final CompositeDisposable compositeDisposable;

    public CameraFragmentScanOnly() {
        this.compositeDisposable = new CompositeDisposable();
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
        this.cameraFragmentButton.setOnClickListener(buttonView -> {
            Disposable disposable = this.bagClothViewModel.verifyClothInBag().subscribe(
                    () -> {
                        Toast.makeText(this.requireContext(), "Le sac a été vérifié avec " +
                            "succès !", Toast.LENGTH_SHORT).show();
                        onCameraFragmentFinish();
                    },
                    throwable -> {
                        Toast.makeText(this.requireContext(), "Le sac n'a pas pu " +
                                "être vérifié !", Toast.LENGTH_SHORT).show();
                        onCameraFragmentFinish();
                    }
            );
            this.compositeDisposable.add(disposable);
        });

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

                                if (this.bagClothViewModel.addScannedCloth(clothUUID)) {
                                    Toast.makeText(this.requireContext(), "Vêtement détecté !",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(this.requireContext(), "Vêtement déjà " +
                                            "détecté !", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (IllegalArgumentException e) {
                                Toast.makeText(this.requireContext(), "Aucun vêtement détecté !",
                                        Toast.LENGTH_SHORT).show();
                            }

                            this.lastBarcodeStringResult = bardcodeResultString;
                        }
                    }
                }));

        this.cameraController.bindToLifecycle(this);
    }

    @Override
    protected void onCameraFragmentFinish() {
        this.cameraController.unbind();
        this.compositeDisposable.dispose();

        MainActivity mainActivity = (MainActivity) this.requireActivity();
        mainActivity.loadFragment(BagDetailsFragment.class);
    }
}
