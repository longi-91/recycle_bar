package com.example.gretabin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.example.gretabin.API.APIActivity;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity{
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private TextView textView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        previewView = findViewById(R.id.previewView);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        textView = findViewById(R.id.txtBarcodeValue);
        cameraProviderFuture.addListener(() -> {
            try{
                ProcessCameraProvider cameraProvider =
                        cameraProviderFuture.get();
                bindImageAnalysis(cameraProvider);
            } catch (ExecutionException | InterruptedException e){
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void bindImageAnalysis (@NonNull ProcessCameraProvider cameraProvider){
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        BarcodeScanner scanner = BarcodeScanning.getClient();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this),
                imageProxy -> {
            processImageProxy(scanner, imageProxy);
        });

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.createSurfaceProvider());
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageAnalysis, preview);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void processImageProxy(BarcodeScanner scanner, ImageProxy imageProxy){
        @SuppressLint("UnsafeExperimentalUsageError")
        InputImage inputImage = InputImage.fromMediaImage(imageProxy.getImage(),imageProxy.getImageInfo().getRotationDegrees());

        Task<List<Barcode>> result = scanner.process(inputImage)
                .addOnSuccessListener(barcodes -> {
                    for (Barcode barcode: barcodes) {
                        textView.setText(barcode.getRawValue());
                        Intent intent = new Intent(this, APIActivity.class);
                        //This feeds in all the info we have so far about the barcode and image.
                        //startActivity(intent);
                        startActivity(intent.putExtra("Barcode + Image",barcode.getRawValue()));
                    }
                })
                .addOnFailureListener(e -> textView.setText("Not able to read Barcode"))
                .addOnCompleteListener(task -> { imageProxy.close();})
                ;
    }

}
