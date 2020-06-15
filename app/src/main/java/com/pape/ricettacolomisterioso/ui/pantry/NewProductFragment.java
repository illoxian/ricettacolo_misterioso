package com.pape.ricettacolomisterioso.ui.pantry;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.databinding.FragmentNewProductBinding;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.ui.recipes.RecipeProfileFragment;
import com.pape.ricettacolomisterioso.viewmodels.NewProductViewModel;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewProductFragment extends Fragment {

    //singleton impl
    public NewProductFragment() {

    }

    public static NewProductFragment newInstance() {
        NewProductFragment fragment = new NewProductFragment();
        return fragment;
    }



    private static final String TAG = "NewProductFragment";
    private FragmentNewProductBinding binding;
    private NewProductViewModel model;
    private Calendar c;
    private DatePickerDialog datePickerDialog;
    private List<String> CATEGORIES;

    int LAUNCH_SCANNER_ACTIVITY = 1;

    private Product product;
    private Date expirationDate;
    private Date purchaseDate;
    private MutableLiveData<Long> insertId; //livedata for the id returned from the insert to db

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentNewProductBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //livedata observer for the id returned from the insert to db
        model = new ViewModelProvider(this).get(NewProductViewModel.class);
        final Observer<Long> observer = new Observer<Long>() {
            @Override
            public void onChanged(Long insertId) {
                if(insertId>=0){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("insertId",insertId);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
        };
        insertId = model.getInsertId();
        insertId.observe(this, observer);

        initTextInputs();
        initScannerButton();
        initFAB();
        initDatePicker();


    }


    private void initScannerButton() {
        binding.buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startScannerActivity();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new
                            String[]{Manifest.permission.CAMERA}, ScannerActivity.REQUEST_CAMERA_PERMISSION);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && requestCode==ScannerActivity.REQUEST_CAMERA_PERMISSION) {
            startScannerActivity();
        }
    }

    private void startScannerActivity(){
        Intent i = new Intent(this, ScannerActivity.class);
        startActivityForResult(i, LAUNCH_SCANNER_ACTIVITY);
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
        CATEGORIES = Arrays.asList(getResources().getStringArray(R.array.categoriesString));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, CATEGORIES);
        binding.textInputCategory.setAdapter(adapter);
    }

    private void initFAB(){
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }

    private void initDatePicker(){
        //ExpirationDate
        binding.textInputExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textInputLayoutExpirationDate.setError(null);

                c = Calendar.getInstance();

                final int day = c.get(Calendar.DAY_OF_MONTH);
                final int month = c.get(Calendar.MONTH);
                final int year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MyTheme_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        Calendar cPicked = Calendar.getInstance();
                        cPicked.set(mYear, mMonth, mDayOfMonth);
                        expirationDate = cPicked.getTime();
                        String dateString = DateFormat.getDateInstance(DateFormat.SHORT).format(expirationDate);
                        binding.textInputExpirationDate.setText(dateString);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        //PurchaseDate
        purchaseDate = Calendar.getInstance().getTime();
        binding.textInputPurchaseDate.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(purchaseDate));

        binding.textInputPurchaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textInputLayoutPurchaseDate.setError(null);

                c = Calendar.getInstance();

                final int day = c.get(Calendar.DAY_OF_MONTH);
                final int month = c.get(Calendar.MONTH);
                final int year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MyTheme_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        Calendar cPicked = Calendar.getInstance();
                        cPicked.set(mYear, mMonth, mDayOfMonth);
                        purchaseDate = cPicked.getTime();
                        String dateString = DateFormat.getDateInstance(DateFormat.SHORT).format(purchaseDate);
                        binding.textInputPurchaseDate.setText(dateString);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SCANNER_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                product = data.getParcelableExtra("product");
                if(product != null){
                    binding.textInputName.setText(product.getProduct_name());
                    binding.textInputBrand.setText(product.getBrand());
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }






    private void addProduct(){
        if(product==null) product = new Product();

        product.setProduct_name(binding.textInputName.getText().toString());
        String categoryString = binding.textInputCategory.getText().toString();
        int categoryId = CATEGORIES.indexOf(categoryString);
        product.setCategory(categoryId);
        product.setExpirationDate(expirationDate);
        product.setPurchaseDate(purchaseDate);
        product.setBrand(binding.textInputBrand.getText().toString());

        boolean isValid = true;

        //ProductName
        if(product.getProduct_name().equals("")){
            binding.textInputLayoutName.setError(getResources().getString((R.string.error_empty_field)));
            if(isValid) binding.textInputLayoutName.requestFocus();
            isValid = false;
        }
        //Category
        if(categoryString.equals("")){
            binding.textInputLayoutCategory.setError(getResources().getString((R.string.error_empty_field)));
            if(isValid) binding.textInputLayoutCategory.requestFocus();
            isValid = false;
        }
        else if(categoryId<0){
            binding.textInputLayoutCategory.setError(getResources().getString((R.string.error_not_a_category)));
            if(isValid) binding.textInputLayoutCategory.requestFocus();
            isValid = false;
        }
        //ExpirationDate
        if(product.getExpirationDate()==null){
            binding.textInputLayoutExpirationDate.setError(getResources().getString((R.string.error_empty_field)));
            isValid = false;
        }
        //PurchaseDate
        if(product.getPurchaseDate()==null){
            binding.textInputLayoutPurchaseDate.setError(getResources().getString((R.string.error_empty_field)));
            isValid = false;
        }

        if(isValid){
            insertId = model.addProduct(product);
            Log.d(TAG, "Added a product"+ product);
            Log.d(TAG, "id afterInsertion: "+ insertId);        }
    }
}