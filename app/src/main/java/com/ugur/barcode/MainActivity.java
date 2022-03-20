package com.ugur.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends Activity implements ZXingScannerView.ResultHandler {
    private static final String TAG = "";
    private ZXingScannerView mScannerView;
    int MY_PERMISSION_REQUEST_CAMERA=0;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);// Set the scanner view as the content view

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},MY_PERMISSION_REQUEST_CAMERA);
        }

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();


    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        try {
            Log.v(TAG, rawResult.getText()); // Prints scan results
            Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
            Toast.makeText(this, rawResult.getText().toString(), Toast.LENGTH_SHORT).show();


        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rawResult.getText()));
        startActivity(browserIntent);

            // If you would like to resume scanning, call this method below:
            mScannerView.resumeCameraPreview(this);
        }catch (Exception e){
            Toast.makeText(this, "HATA: Geçersiz QR kodu.", Toast.LENGTH_SHORT).show();
    }
    }

}