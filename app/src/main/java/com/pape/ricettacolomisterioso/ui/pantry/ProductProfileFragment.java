package com.pape.ricettacolomisterioso.ui.pantry;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.databinding.FragmentProductProfileBinding;
import com.pape.ricettacolomisterioso.models.Item;
import com.pape.ricettacolomisterioso.models.Product;

import com.pape.ricettacolomisterioso.utils.Functions;
import com.pape.ricettacolomisterioso.viewmodels.ProductProfileViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class ProductProfileFragment extends Fragment {
    private FragmentProductProfileBinding productProfileBinding;
    private ProductProfileViewModel model;
    final static String TAG="ProductProfileFragment";

    public ProductProfileFragment() {

    }

    public static ProductProfileFragment newInstance() {
        ProductProfileFragment fragment = new ProductProfileFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        productProfileBinding = FragmentProductProfileBinding.inflate(getLayoutInflater());
        model = new ViewModelProvider(this).get(ProductProfileViewModel.class);
        View view = productProfileBinding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Product product = ProductProfileFragmentArgs.fromBundle(getArguments()).getProduct();

        updateProductInformation(product, view);
        updateExpiringView(product.getExpirationDate(), product.getPurchaseDate());


    }

    public void updateProductInformation(Product product, View view){
        productProfileBinding.productNameTextView.setText(product.getProduct_name());
        productProfileBinding.categoryValueTextView.setText(Functions.getProductCategoryString(view.getContext(), product.getCategory()));
        productProfileBinding.quantityValueTextView.setText("500g");
        productProfileBinding.brandValueTextView.setText(product.getBrand());
        productProfileBinding.purchaseDateValueTextView.setText(product.getPurchaseDateString());

        // image or category thumbnail
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(((SharedPreferences) sharedPreferences).getBoolean("image_instead_of_thumbnail", false) && product.getImageUrl() != null)
        {
            productProfileBinding.categoryImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            File f = new File(product.getImageUrl());
            Picasso.get().load(f).into(productProfileBinding.categoryImage);
        }
        else{
            productProfileBinding.categoryImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            productProfileBinding.categoryImage.setImageResource(product.getCategoryPreviewId(getContext()));
        }

        // shopping list button
        model.getFindItem().observe(getActivity(), new Observer<Item>() {
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

    private void setColorAddToShoppingListIcon(boolean lightUp){
        if(lightUp)
            productProfileBinding.addShoppingListImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.pink_a200), android.graphics.PorterDuff.Mode.SRC_IN);
        else
            productProfileBinding.addShoppingListImage.setColorFilter(null);
    }
}
