package com.pape.ricettacolomisterioso.ui.pantry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.databinding.ActivityProductProfileBinding;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.utils.Functions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ProductProfileActivity extends AppCompatActivity {
    private ActivityProductProfileBinding productProfileBinding;
    final static String TAG = "Product_profile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productProfileBinding = ActivityProductProfileBinding.inflate(getLayoutInflater());
        View view = productProfileBinding.getRoot();
        setContentView(view);

        Product product = getIntent().getBundleExtra("product").getParcelable("product");

        updateProductInformation(product);
        updateExpiringView(product.getExpirationDate(), product.getPurchaseDate());

        Log.d(TAG, product.toString());

    }

    public void updateProductInformation(Product product){
        productProfileBinding.productNameTextView.setText(product.getProduct_name());
        productProfileBinding.categoryValueTextView.setText(product.getCategory());
        productProfileBinding.quantityValueTextView.setText("500g");
        productProfileBinding.brandValueTextView.setText(product.getBrand());
        productProfileBinding.purchaseDateValueTextView.setText(product.getPurchaseDateString());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("image_instead_of_thumbnail", false) && product.getImageUrl() != null)
        {
            productProfileBinding.categoryImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.get().load(product.getImageUrl()).into(productProfileBinding.categoryImage);
        }
        else{
            productProfileBinding.categoryImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            productProfileBinding.categoryImage.setImageResource(product.getCategoryPreviewId(this));
        }

    }

    public void updateExpiringView(Date expiring, Date purchase_date){
        expiring = Functions.ExcludeTime(expiring);
        purchase_date = Functions.ExcludeTime(purchase_date);
        Date today = Calendar.getInstance().getTime();
        Date today_not_time = Functions.ExcludeTime(today);

        int daysRemaining = Functions.time_in_day_remain(expiring,today_not_time);

        if(daysRemaining > 0) {
            int progress = Functions.percentual_for_bar(purchase_date,expiring,today); //in progress bar Today is used with time to prevent progress bar fully empty
            String daysRemainingString = daysRemaining + " " + getString(R.string.remaining_day);
            productProfileBinding.expiringValueTextView.setText(daysRemainingString);
            productProfileBinding.progressBar.setProgress(progress);
        }else if(daysRemaining == 0) {
            productProfileBinding.expiringValueTextView.setText(getString(R.string.product_expired_today));
            productProfileBinding.progressBar.setProgress(100);
        }else {
            String ExpiredFromXDays = getString(R.string.product_expired_from) + Math.abs(daysRemaining) + getString(R.string.days);
            productProfileBinding.expiringValueTextView.setText(ExpiredFromXDays);
            productProfileBinding.progressBar.setProgress(100);
        }
    }

}
