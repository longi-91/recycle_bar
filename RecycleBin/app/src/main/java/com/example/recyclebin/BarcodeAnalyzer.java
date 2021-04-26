package com.example.recyclebin;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.mlkit.vision.common.InputImage;

class BarcodeAnalyzer implements ImageAnalysis.Analyzer {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override

    public void analyze(ImageProxy imageProxy) {
        @SuppressLint("UnsafeExperimentalUsageError") Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
        }
    }
}