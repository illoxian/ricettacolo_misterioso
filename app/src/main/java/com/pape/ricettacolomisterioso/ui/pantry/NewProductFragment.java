package com.pape.ricettacolomisterioso.ui.pantry;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.databinding.FragmentNewProductBinding;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.utils.Functions;
import com.pape.ricettacolomisterioso.viewmodels.NewProductViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

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

    private static final int LAUNCH_SCANNER_ACTIVITY = 1;
    private static final int LAUNCH_LOAD_IMAGE_ACTIVITY = 2;
    private static final int LAUNCH_TAKE_PHOTO_ACTIVITY = 3;

    private Product product;
    private Date expirationDate;
    private Date purchaseDate;
    private Bitmap bitmapProduct;
    private MutableLiveData<Long> insertId; //livedata for the id returned from the insert to db


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewProductBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setHasOptionsMenu(true);

        //livedata observer for the id returned from the insert to db
        model = new ViewModelProvider(this).get(NewProductViewModel.class);
        final Observer<Long> observer = new Observer<Long>() {
            @Override
            public void onChanged(Long insertId) {
                if(insertId>=0){
                    Toast.makeText(getContext(), R.string.new_product_toast_success, Toast.LENGTH_LONG).show();
                    Navigation.findNavController(getView()).popBackStack();
                }
            }
        };
        insertId = model.getInsertId();
        insertId.observe(this.getActivity(), observer);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTextInputs();
        initScannerButton();
        initFAB();
        initDatePicker();
        initButtonImage();

    }

    private void initButtonImage() {
        binding.buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(v.getContext());
                View sheetView = getLayoutInflater().inflate(R.layout.new_product_bottom_sheet_dialog, null);
                mBottomSheetDialog.setContentView(sheetView);

                LinearLayout dialog_take_photo = (LinearLayout) sheetView.findViewById(R.id.dialog_button_take_photo);
                LinearLayout dialog_pick_image = (LinearLayout) sheetView.findViewById(R.id.dialog_button_pick_image);

                dialog_take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            startTakePhotoActivity();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), new
                                    String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, LAUNCH_TAKE_PHOTO_ACTIVITY);
                        }
                        mBottomSheetDialog.dismiss();
                    }
                });

                dialog_pick_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            startPickImageActivity();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), new
                                    String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, LAUNCH_LOAD_IMAGE_ACTIVITY);
                        }
                        mBottomSheetDialog.dismiss();
                    }
                });

                mBottomSheetDialog.show();
            }
        });
    }

    private void initScannerButton() {
        binding.buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startScannerActivity();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new
                            String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, LAUNCH_SCANNER_ACTIVITY);
                }
            }
        });
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case LAUNCH_SCANNER_ACTIVITY:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    startScannerActivity();
                }
                break;

            case LAUNCH_LOAD_IMAGE_ACTIVITY:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startPickImageActivity();
                }
                break;

            case LAUNCH_TAKE_PHOTO_ACTIVITY:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    startTakePhotoActivity();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SCANNER_ACTIVITY) {
            if(resultCode == RESULT_OK){
                product = data.getParcelableExtra("product");

                if(product != null){
                    binding.textInputName.setText(product.getProduct_name());
                    binding.textInputBrand.setText(product.getBrand());

                    String image_path = product.getImageUrl();

                    if(image_path != null){
                        File file = new File(image_path);
                        Picasso.get().load(file).into(binding.imageView2);
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        else if (requestCode == LAUNCH_LOAD_IMAGE_ACTIVITY) {
            if(resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Bitmap bmp = BitmapFactory.decodeFile(picturePath);
                binding.imageView2.setImageBitmap(bmp);
                bitmapProduct = bmp;
                cursor.close();
            }
        }
        else if (requestCode == LAUNCH_TAKE_PHOTO_ACTIVITY) {
            if(resultCode == RESULT_OK && data != null) {
                Bundle extras = data.getExtras();
                // Get the returned image from extra
                Bitmap bmp = (Bitmap) extras.get("data");
                binding.imageView2.setImageBitmap(bmp);
                bitmapProduct = bmp;
            }
        }
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.new_recipe_app_bar_menu, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id==R.id.new_recipe_app_bar_add) {
            addProduct();
            return true;
        }
        if(id==android.R.id.home) {
            Navigation.findNavController(getView()).popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.GONE);


    }


    @Override
    public void onDetach() {

        super.onDetach();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.VISIBLE);


    }




    private void startScannerActivity(){
        // insert navigate?
        Intent i = new Intent(getActivity(), ScannerActivity.class);
        startActivityForResult(i, LAUNCH_SCANNER_ACTIVITY);
    }

    private void startPickImageActivity(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, LAUNCH_LOAD_IMAGE_ACTIVITY);
    }

    private void startTakePhotoActivity() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, LAUNCH_TAKE_PHOTO_ACTIVITY);
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
        if(binding.textInputQuantity.getText().toString()!="") product.setQuantity(Integer.parseInt(binding.textInputQuantity.getText().toString()));
        else product.setQuantity(1);

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
        //Image from "add image"
        if(bitmapProduct != null){
            String image_path = Functions.SaveImage(bitmapProduct);
            if(image_path != null)
                product.setImageUrl(image_path);
        }

        if(isValid){
            insertId = model.addProduct(product);
            Log.d(TAG, "Added a product"+ product);
            Log.d(TAG, "id afterInsertion: "+ insertId);        }
    }
}
