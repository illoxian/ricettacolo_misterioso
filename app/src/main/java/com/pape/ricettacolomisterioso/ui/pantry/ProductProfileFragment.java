package com.pape.ricettacolomisterioso.ui.pantry;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.adapters.ProductListAdapter;
import com.pape.ricettacolomisterioso.databinding.FragmentProductProfileBinding;
import com.pape.ricettacolomisterioso.models.DailyMenu;
import com.pape.ricettacolomisterioso.models.DailyRecipe;
import com.pape.ricettacolomisterioso.models.Item;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.utils.Functions;
import com.pape.ricettacolomisterioso.viewmodels.ProductProfileViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProductProfileFragment extends Fragment {
    private FragmentProductProfileBinding productProfileBinding;
    private ProductProfileViewModel model;
    private ProductListAdapter adapter;
    final static String TAG="ProductProfileFragment";

    public ProductProfileFragment() {

    }

    public static ProductProfileFragment newInstance() {
        ProductProfileFragment fragment = new ProductProfileFragment();
        return fragment;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.GONE);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onDetach() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);

        super.onDetach();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.VISIBLE);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

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
        productProfileBinding.quantityValueTextView.setText(Integer.toString(product.getQuantity()));
        if(product.getBrand().isEmpty()) productProfileBinding.brandValueTextView.setVisibility(View.INVISIBLE);
        else productProfileBinding.brandValueTextView.setText(product.getBrand());
        productProfileBinding.purchaseDateValueTextView.setText(product.getPurchaseDateString());
        productProfileBinding.expiringDateValueTextView.setText(product.getExpirationDateString());
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

        productProfileBinding.addShoppingListImage.setOnClickListener(v -> {
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
        });

        productProfileBinding.deleteIamge.setOnClickListener(v-> {
            ShowDialogDelete(product);

        });

        productProfileBinding.minusQuantity.setOnClickListener(v-> {
            model.minusQuantity(product);

            model.getProduct().observe(getViewLifecycleOwner(), new Observer<Product>() {
                @Override
                public void onChanged(Product product) {
                    productProfileBinding.quantityValueTextView.setText("" + model.getProduct().getValue().getQuantity());
                }
            });
        });


            productProfileBinding.plusQuantity.setOnClickListener(v -> {
                model.plusQuantity(product);

                model.getProduct().observe(getViewLifecycleOwner(), new Observer<Product>() {
                    @Override
                    public void onChanged(Product product) {
                        productProfileBinding.quantityValueTextView.setText("" + model.getProduct().getValue().getQuantity());
                    }
                });

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

/*    private void initFAB(){
        productProfileBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }*/

    private void refreshQuantity(Product product) {
        productProfileBinding.quantityValueTextView.setText(Integer.toString(product.getQuantity()));
    }

    private void ShowDialogDelete(Product product){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MyTheme_Dialog);
        builder.setTitle(R.string.product_dialog_delete_title);
        // Set up the buttons
        builder.setPositiveButton(getString(R.string.Delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                model.delete(product);
                Snackbar.make(getView(), R.string.removed_from_products, Snackbar.LENGTH_LONG).show();

                Navigation.findNavController(getView()).navigateUp();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
