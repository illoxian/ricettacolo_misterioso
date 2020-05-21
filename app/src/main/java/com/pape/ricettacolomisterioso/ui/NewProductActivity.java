package com.pape.ricettacolomisterioso.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.databinding.ActivityNewProductBinding;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.viewmodels.NewProductViewModel;

public class NewProductActivity extends AppCompatActivity {

    private static final String TAG = "NewProductActivity";
    private ActivityNewProductBinding binding;
    private NewProductViewModel model;
    private Calendar c;
    private DatePickerDialog datePickerDialog;
    List<String> CATEGORIES;
    private Date timeDatePicker;
    int LAUNCH_SCANNER_ACTIVITY = 1;

    LiveData<List<Product>> liveData;

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewProductBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initTextInputs();
        initScannerButton();
        initFAB();
        initCategoriesAutocomplete();
        initDatePicker();

        model = new ViewModelProvider(this).get(NewProductViewModel.class);

        final Observer<List<Product>> observer = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                for (int i = 0; i < products.size(); i++) {
                    Log.d(TAG, "Product: " + i + " " + products.get(i).toString());
                }
            }
        };
        liveData = model.getProducts();
        liveData.observe(this, observer);
    }

    private void initScannerButton() {
        binding.buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScannerActivity();
            }
        });
    }

    private void startScannerActivity(){
        Intent i = new Intent(this, ScannerActivity.class);
        startActivityForResult(i, LAUNCH_SCANNER_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SCANNER_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                product = data.getParcelableExtra("product");
                binding.textInputName.setText(product.getProduct_name());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initTextInputs(){
        binding.textInputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.textInputLayoutName.setError(null);
            }
        });

        binding.textInputCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.textInputLayoutCategory.setError(null);
            }
        });
    }

    private void initFAB(){
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }

    private void initCategoriesAutocomplete(){
        CATEGORIES = new ArrayList<>();

        CATEGORIES.add(getResources().getString(R.string.pantry_categories_fruits_and_vegetables));
        CATEGORIES.add(getResources().getString(R.string.pantry_categories_meat));
        CATEGORIES.add(getResources().getString(R.string.pantry_categories_fish));
        CATEGORIES.add(getResources().getString(R.string.pantry_categories_pasta_and_rice));
        CATEGORIES.add(getResources().getString(R.string.pantry_categories_milk_and_cheese));
        CATEGORIES.add(getResources().getString(R.string.pantry_categories_ice_cream_and_frozen_food));
        CATEGORIES.add(getResources().getString(R.string.pantry_categories_bread_and_pizza));
        CATEGORIES.add(getResources().getString(R.string.pantry_categories_breakfast_and_sweets));
        CATEGORIES.add(getResources().getString(R.string.pantry_categories_oil_and_condiments));
        CATEGORIES.add(getResources().getString(R.string.pantry_categories_water_and_drinks));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, CATEGORIES);
        binding.textInputCategory.setAdapter(adapter);
    }

    private void initDatePicker(){
        binding.textInputExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textInputLayoutExpirationDate.setError(null);

                c = Calendar.getInstance();

                final int day = c.get(Calendar.DAY_OF_MONTH);
                final int month = c.get(Calendar.MONTH);
                final int year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(NewProductActivity.this, R.style.Theme_MyTheme_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        Calendar cPicked = Calendar.getInstance();
                        cPicked.set(mYear, mMonth, mDayOfMonth);
                        timeDatePicker = cPicked.getTime();
                        String dateString = DateFormat.getDateInstance(DateFormat.SHORT).format(timeDatePicker);
                        binding.textInputExpirationDate.setText(dateString);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void addProduct(){
        if(product==null) product = new Product();

        product.setExpirationDate(timeDatePicker);
        product.setProduct_name(binding.textInputName.getText().toString());
        product.setCategory(binding.textInputCategory.getText().toString());
        boolean isValid = true;

        //ProductName
        if(product.getProduct_name().equals("")){
            binding.textInputLayoutName.setError(getResources().getString((R.string.error_empty_field)));
            if(isValid) binding.textInputLayoutName.requestFocus();
            isValid = false;
        }
        //Category
        if(product.getCategory().equals("")){
            binding.textInputLayoutCategory.setError(getResources().getString((R.string.error_empty_field)));
            if(isValid) binding.textInputLayoutCategory.requestFocus();
            isValid = false;
        }
        else if(!CATEGORIES.contains(product.getCategory())){
            binding.textInputLayoutCategory.setError(getResources().getString((R.string.error_not_a_category)));
            if(isValid) binding.textInputLayoutCategory.requestFocus();
            isValid = false;
        }
        //Date
        if(product.getExpirationDate()==null){
            binding.textInputLayoutExpirationDate.setError(getResources().getString((R.string.error_empty_field)));
            isValid = false;
        }

        if(isValid){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    addProductAsync();
                }
            });
        }
    }

    private void addProductAsync(){
        int id = (int)MainActivity.db.productDao().insertProduct(product);
        Log.d(TAG, "addProductAsync: "+id);
        liveData = model.getProducts();
    }
}
