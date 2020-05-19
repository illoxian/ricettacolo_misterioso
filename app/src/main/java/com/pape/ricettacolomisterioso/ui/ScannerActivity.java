package com.pape.ricettacolomisterioso.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.viewmodels.ScannerViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

public class ScannerActivity extends AppCompatActivity {

    private static final String TAG = "ScannerActivity";

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private ToneGenerator toneGen1;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private Boolean barcodeFound;
    private ScannerViewModel model;
    private MutableLiveData<Product> liveData;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surfaceView2);
        barcodeFound = false;

        initialiseDetectorsAndSources();

        model = new ViewModelProvider(this).get(ScannerViewModel.class);

        final Observer<Product> observer = new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                // Here we can update the UI
                if(product!=null) Log.d(TAG, "onChanged: " + product.toString());
                createDialog(product);
            }
        };
        liveData = model.getProduct();
        liveData.observe(this, observer);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.clear();
    }

    private void initialiseDetectorsAndSources() {

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.EAN_13 | Barcode.UPC_A | Barcode.EAN_8)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                StartCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {  }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() { }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (!barcodeFound && barcodes.size() != 0) {
                    barcodeFound=true;
                    OnBarcodeFound(barcodes.valueAt(0).displayValue);
                }
            }
        });
    }

    private void OnBarcodeFound(String barcodeData){
        Log.d(TAG, "barcode found:" + barcodeData);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String dataProvider = sharedPref.getString(getResources().getString(R.string.shared_pref_data_provider_key), "Ebay");
        model.getProductInfo(barcodeData, dataProvider);

        //Stop Camera
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                cameraSource.stop();
            }
        };
    }

    private void OnDialogOk(Product product){
        Log.d(TAG, "positive button clicked");
        returnProduct(product);
    }

    private void OnDialogCancel(){
        Log.d(TAG, "negative button clicked");
        barcodeFound = false;
        try{
            cameraSource.start(surfaceView.getHolder());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        // dismiss dialog, start counter again
        Log.d(TAG, "negative button clicked");

    }

    private void StartCamera(){
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


    private void createDialog(Product product){

        if(product == null)
        {
            builder = new AlertDialog.Builder(this, R.style.Theme_MyTheme_Dialog);
            builder.setTitle(getResources().getString(R.string.scanner_alert_dialog_title));
            builder.setMessage("Il codice a barre non è stato trovato nel database");

            String positiveText = "Riprova";//getString(android.R.string.retry);
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OnDialogCancel();
                            dialog.dismiss();
                        }
                    });
        }
        else{
            builder = new AlertDialog.Builder(this, R.style.Theme_MyTheme_Dialog);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_scanner, null);
            builder.setView(dialogView);
            builder.setTitle(getResources().getString(R.string.scanner_alert_dialog_title));

            TextView generic_name_view = dialogView.findViewById(R.id.scanner_dialog_generic_name);
            TextView product_name_view = dialogView.findViewById(R.id.scanner_dialog_product_name);
            TextView brand_view = dialogView.findViewById(R.id.scanner_dialog_brand);
            TextView code_view = dialogView.findViewById(R.id.scanner_dialog_code_name);
            ImageView image_view = dialogView.findViewById(R.id.scanner_dialog_image);
            TextView data_source_view = dialogView.findViewById(R.id.scanner_dialog_data_source);
            product_name_view.setText(product.getProduct_name());
            brand_view.setText(product.getBrand());
            code_view.setText(product.getBarcode());
            String dataSourceText = getResources().getString(R.string.scanner_alert_dialog_data_source_label) + " " + product.getDataSource();
            data_source_view.setText(dataSourceText);
            //handle Image
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    image_view.setImageBitmap(bitmap);
                }
                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    image_view.setImageDrawable(errorDrawable);
                }
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    image_view.setImageDrawable(placeHolderDrawable);
                }
            };
            Picasso.get().load(product.getImageUrl()).into(target);

            String positiveText = getString(android.R.string.ok);
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OnDialogOk(product);
                            dialog.dismiss();
                        }
                    });

            String negativeText = getString(android.R.string.cancel);
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OnDialogCancel();
                            dialog.dismiss();
                        }
                    });
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void returnProduct(Product p){

        Intent returnIntent = new Intent();

        returnIntent.putExtra("product",p);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
