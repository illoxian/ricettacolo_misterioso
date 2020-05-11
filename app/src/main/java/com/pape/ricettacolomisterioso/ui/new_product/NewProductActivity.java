package com.pape.ricettacolomisterioso.ui.new_product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.pape.ricettacolomisterioso.MainActivity;
import com.pape.ricettacolomisterioso.ui.database.Product;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.databinding.ActivityNewProductBinding;

public class NewProductActivity extends AppCompatActivity {

    private ActivityNewProductBinding binding;
    private NewProductViewModel model;
    private Calendar c;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewProductBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initCategoriesAutocomplete();
        initDatePicker();
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        MainActivity.db.productDao().getAll().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                String s = "";
                for(int i=0; i<products.size(); i++)
                {
                    s+=products.get(i).productName + " ";
                }
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCategoriesAutocomplete(){
        List<String> CATEGORIES = new ArrayList<>();

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
                c = Calendar.getInstance();

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(NewProductActivity.this, R.style.Theme_MyTheme_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        String dateString = mDayOfMonth + "/" + (mMonth+1) + "/" + mYear;
                        binding.textInputExpirationDate.setText(dateString);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
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
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                addProductAsync();
            }
        });

    }
    private void addProductAsync(){
        Product p = new Product();
        p.productName = binding.textInputName.getText().toString();
        p.category = binding.textInputCategory.getText().toString();
        MainActivity.db.productDao().insertAll(p);
    }
}
