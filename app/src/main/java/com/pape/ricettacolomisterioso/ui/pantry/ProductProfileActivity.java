package com.pape.ricettacolomisterioso.ui.pantry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.databinding.ActivityProductProfileBinding;
import com.pape.ricettacolomisterioso.models.Item;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.viewmodels.ProductProfileViewModel;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class ProductProfileActivity extends AppCompatActivity {
    private ActivityProductProfileBinding productProfileBinding;
    private ProductProfileViewModel model;
    final static String TAG = "Product_profile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productProfileBinding = ActivityProductProfileBinding.inflate(getLayoutInflater());
        model = new ViewModelProvider(this).get(ProductProfileViewModel.class);
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

        // image or category thumbnail
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

        // shopping list button
        model.getFindItem().observe(this, new Observer<Item>() {
            @Override
            public void onChanged(Item item) {
                if(item == null) setColorAddToShoppingListIcon(false);
                else setColorAddToShoppingListIcon(true);
            }
        });

        productProfileBinding.addShoppingListImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productProfileBinding.addShoppingListImage.getColorFilter()==null){
                    model.addItemToShoppingList(product.getProduct_name());
                    setColorAddToShoppingListIcon(true);
                    Snackbar.make(v, R.string.product_added_to_shopping_list, Snackbar.LENGTH_LONG).show();
                }
                else{
                    model.deleteItemFromShoppingList(product.getProduct_name());
                    setColorAddToShoppingListIcon(false);
                    Snackbar.make(v, R.string.product_removed_from_shopping_list, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        model.findItemInShoppingList(product.getProduct_name());
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

    private void setColorAddToShoppingListIcon(boolean lightUp){
        if(lightUp)
            productProfileBinding.addShoppingListImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.pink_a200), android.graphics.PorterDuff.Mode.SRC_IN);
        else
            productProfileBinding.addShoppingListImage.setColorFilter(null);
    }
}
