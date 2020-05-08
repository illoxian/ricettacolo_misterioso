package com.pape.ricettacolomisterioso;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewProductActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        initCategoriesAutocomplete();
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
        AutoCompleteTextView textView = findViewById(R.id.textInput_category);
        textView.setAdapter(adapter);
    }
}
