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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Product_profile extends AppCompatActivity {
    private ActivityProductProfileBinding productProfileBinding;
    final static long MILLISECOND = 1000;
    final static long SECOND = 60;
    final static long MINUTE = 60;
    final static long HOUR = 24;
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
        expiring = ExcludeTime(expiring);
        purchase_date = ExcludeTime(purchase_date);
        Date today = Calendar.getInstance().getTime();
        Date today_not_time = ExcludeTime(today);

        int daysRemaining = time_in_day_remain(expiring,today_not_time);
        int progress = percentual_for_bar(purchase_date,expiring,today); //in progress bar Today is used with time to prevent progress bar fully empty

        String daysRemainingString = daysRemaining + " " + getString(R.string.remaining_day);
        productProfileBinding.expiringValueTextView.setText(daysRemainingString);
        productProfileBinding.progressBar.setProgress(progress);
    }

    public int time_in_day_remain(Date expiring, Date today){
        long time_in_millisecond = expiring.getTime() -  today.getTime();
        return (int)TimeUnit.DAYS.convert(time_in_millisecond, TimeUnit.MILLISECONDS);
    }


    public int percentual_for_bar(Date purchase, Date expiring, Date today){
        long exp_pur = expiring.getTime() - purchase.getTime();
        long tod_pur = today.getTime() - purchase.getTime();
        double percentual_value_for_progress_bar = ((double)tod_pur/(double)exp_pur)*100;
        return (int)Math.round(percentual_value_for_progress_bar);
    }

    private Date ExcludeTime(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);
        c.clear();
        c.set(year, month, day);
        return c.getTime();
    }
}
