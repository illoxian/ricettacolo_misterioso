package com.pape.ricettacolomisterioso.ui.pantry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.databinding.ActivityProductProfileBinding;
import com.pape.ricettacolomisterioso.models.Product;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        updateTopImage(product.getCategory());

        Date purchase_date= null;
        try {
            purchase_date = new SimpleDateFormat("dd/MM/yyyy").parse("01/05/2020");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        product.setPurchaseDate(purchase_date);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String purchase_date_string = dateFormat.format(product.getPurchaseDate());



        updateProductInformation(product.getProduct_name(),
                                product.getCategory(),
                                "500g",
                                product.getBrand(),
                                purchase_date_string);



        Log.d(TAG, product.toString());
        updateExpiringView(product.getExpirationDate(), product.getPurchaseDate());
    }

    public void updateTopImage(final String product_category){
        if(product_category.equals(getString(R.string.pantry_categories_bread_and_pizza)))
            productProfileBinding.categoryImage.setImageResource(R.drawable.previews_bread_and_bakery);
        else if(product_category.equals(getString(R.string.pantry_categories_breakfast_and_sweets)))
            productProfileBinding.categoryImage.setImageResource(R.drawable.previews_breakfast_and_sweets);
        else if(product_category.equals(getString(R.string.pantry_categories_fish)))
            productProfileBinding.categoryImage.setImageResource(R.drawable.previews_fish);
        else if(product_category.equals(getString(R.string.pantry_categories_fruits_and_vegetables)))
            productProfileBinding.categoryImage.setImageResource(R.drawable.previews_fruits_and_vegetables);
        else if(product_category.equals(getString(R.string.pantry_categories_ice_cream_and_frozen_food)))
            productProfileBinding.categoryImage.setImageResource(R.drawable.previews_ice_cream_and_frozen_food);
        else if(product_category.equals(getString(R.string.pantry_categories_meat)))
            productProfileBinding.categoryImage.setImageResource(R.drawable.previews_meat);
        else if(product_category.equals(getString(R.string.pantry_categories_milk_and_cheese)))
            productProfileBinding.categoryImage.setImageResource(R.drawable.previews_milk_and_cheese);
        else if(product_category.equals(getString(R.string.pantry_categories_oil_and_condiments)))
            productProfileBinding.categoryImage.setImageResource(R.drawable.previews_oil_and_condiments);
        else if(product_category.equals(getString(R.string.pantry_categories_pasta_and_rice)))
            productProfileBinding.categoryImage.setImageResource(R.drawable.previews_pasta_and_rice);
        else if(product_category.equals(getString(R.string.pantry_categories_water_and_drinks)))
            productProfileBinding.categoryImage.setImageResource(R.drawable.previews_water_and_drinks);
    }

    public void updateProductInformation(final String product_name,
                                         final String product_category,
                                         final String product_quantity,
                                         final String product_brand,
                                         final String product_purchase_date){
        productProfileBinding.productNameTextView.setText(product_name);
        productProfileBinding.categoryValueTextView.setText(product_category);
        productProfileBinding.quantityValueTextView.setText(product_quantity);
        productProfileBinding.brandValueTextView.setText(product_brand);
        productProfileBinding.purchaseDateValueTextView.setText(product_purchase_date);
    }

    /*public void updateExpiringView(String expiring, String purchase_date){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        Date todayWithZeroTime = null;
        try {
            todayWithZeroTime = formatter.parse(formatter.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date expiring_date= null;
        try {
            expiring_date = new SimpleDateFormat("dd/MM/yyyy").parse(expiring);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date purchase= null;
        try {
            purchase = new SimpleDateFormat("dd/MM/yyyy").parse(purchase_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d(TAG, expiring_date.getTime() + " " + purchase.toString());
        String remaining_day = time_in_day_remain(expiring_date,todayWithZeroTime)+getString(R.string.remaining_day);
        productProfileBinding.expiringValueTextView.setText(remaining_day);
        productProfileBinding.progressBar.setProgress(percentual_for_bar(purchase,expiring_date,todayWithZeroTime));
    }*/



    public void updateExpiringView(Date expiring, Date purchase_date){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        Date todayWithZeroTime = null;
        try {
            todayWithZeroTime = formatter.parse(formatter.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            expiring = formatter.parse(formatter.format(expiring));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            purchase_date = formatter.parse(formatter.format(purchase_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String remaining_day = time_in_day_remain(expiring,todayWithZeroTime)+getString(R.string.remaining_day);
        productProfileBinding.expiringValueTextView.setText(remaining_day);
        productProfileBinding.progressBar.setProgress(percentual_for_bar(purchase_date,expiring,todayWithZeroTime));
    }

    public long time_in_day_remain(Date expiring, Date today){
        long time_in_millisecond = expiring.getTime() -  today.getTime();
        return TimeUnit.DAYS.convert(time_in_millisecond, TimeUnit.MILLISECONDS);
    }


    public int percentual_for_bar(Date purchase, Date expiring, Date today){
        long exp_pur = expiring.getTime() - purchase.getTime();
        long tod_pur = today.getTime() - purchase.getTime();
        double percentual_value_for_progress_bar = ((double)tod_pur/(double)exp_pur)*100;
        return (int)Math.round(percentual_value_for_progress_bar);
    }
}
