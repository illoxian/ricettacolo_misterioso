package com.pape.ricettacolomisterioso.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.models.ProductFromApi;
import com.pape.ricettacolomisterioso.viewmodels.ScannerViewModel;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScannerActivity extends AppCompatActivity {

    private static final String TAG = "ScannerActivity";
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;
    Boolean barcodeFound;
    ScannerViewModel model;
    MutableLiveData<ProductFromApi> liveData;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,     100);
        surfaceView = findViewById(R.id.surfaceView2);
        barcodeText = findViewById(R.id.barcode_textview);initialiseDetectorsAndSources();
        barcodeFound = false;

        model = new ViewModelProvider(this).get(ScannerViewModel.class);

        // The observer associated with the LiveData object that holds the list of articles.
        final Observer<ProductFromApi> observer = new Observer<ProductFromApi>() {
            @Override
            public void onChanged(ProductFromApi product) {
                // Here we can update the UI
                Log.d(TAG, "onChanged: " + product.toString());
                createDialog(product);
            }
        };
        liveData = model.getProduct();
        liveData.observe(this, observer);
    }

    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.EAN_13 | Barcode.UPC_A)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannerActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    Log.d(TAG, "barcode found ");
                    if(!barcodeFound) {
                        barcodeFound = true;

                        barcodeText.post(new Runnable() {

                            @Override
                            public void run() {
                                cameraSource.stop();
                                barcodeData = barcodes.valueAt(0).displayValue;
                                Log.d(TAG, "run: " + barcodeData);
                                model.getProductInfo(barcodeData);
                            /*barcodeText.setText(barcodeData);
                            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);*/
                            }
                        });
                    }

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        getSupportActionBar().hide();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().hide();
        initialiseDetectorsAndSources();
    }

    private void createDialog(ProductFromApi product){
        builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.scanner_alert_dialog_title));
        String message = "Product Name: " + product.getProduct_name_it() + "\n"+
                         "Generic Name: " + product.getGeneric_name_it() + "\n"+
                         "Brand: " + product.getBrands() + "\n"+
                          "Code: " + product.getCode();
        builder.setMessage(message);
        model.getProductInfo(barcodeData);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss alert dialog, update preferences with game score and restart play fragment
                        Log.d(TAG, "positive button clicked");
                        dialog.dismiss();
                        returnProduct(product);
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        barcodeFound = false;
                        try{
                        cameraSource.start(surfaceView.getHolder());
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                        // dismiss dialog, start counter again
                        Log.d(TAG, "negative button clicked");
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
// display dialog
        dialog.show();
    }
    private void returnProduct(ProductFromApi p){

        Product pReturn = new Product(p.getProduct_name_it(), null, null);

        Intent returnIntent = new Intent();

        returnIntent.putExtra("product",pReturn);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
