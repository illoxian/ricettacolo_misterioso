package com.pape.ricettacolomisterioso.ui.recipes;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.vision.text.Line;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.databinding.FragmentNewRecipeBinding;
import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.viewmodels.NewRecipeViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewRecipeFragment extends Fragment {
    private static final String TAG = "NewRecipeFragment";
    private List<String> CATEGORIES;
    private FragmentNewRecipeBinding binding;

    private Recipe recipe;
    private MutableLiveData<Long> insertId;
    private NewRecipeViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewRecipeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        initAddIngredient();
        initAddStep();

        setHasOptionsMenu(true);
        initTextInputs();
        initFAB();


        model = new ViewModelProvider(this).get(NewRecipeViewModel.class);
        final Observer<Long> observer = new Observer<Long>() {
            @Override
            public void onChanged(Long insertId) {
                if(insertId>=0) {
                    Toast.makeText(getContext(), R.string.new_recipe_toast_succes, Toast.LENGTH_LONG).show();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();
                }
            }
        };
        insertId = model.getInsertId();
        insertId.observe(this.getActivity(), observer);

        return view;
    }


    private void initTextInputs(){
        binding.textInputRecipeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.textInputLayoutRecipeName.setError(null);
            }
        });

        binding.textInputRecipeCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.textInputLayoutRecipeCategory.setError(null);
            }
        });
        CATEGORIES = Arrays.asList(getResources().getStringArray(R.array.recipeCategoriesString));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, CATEGORIES);
        binding.textInputRecipeCategory.setAdapter(adapter);

    }


    private void initFAB(){
        binding.newRecipeFab.setOnClickListener(v -> addRecipe());
    }
    private void initAddIngredient() {
        binding.newRecipeIngredientAdd.setOnClickListener(v -> {

        });

    }
    private void initAddStep(){
        EditText textIn = binding.newRecipeStepText;
        binding.newRecipeStepAdd.setOnClickListener(v -> {
            Log.d(TAG, "ButtonClicked");
            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = inflater.inflate(R.layout.new_recipe_item, null);
            Button buttonRemove = addView.findViewById(R.id.new_recipe_step_remove);
 /*
            EditText tw = addView.findViewById(R.id.new_recipe_step_text_new);
            tw.setText(textIn.getText().toString());

            final View.OnClickListener thisListener = v1 -> ((LinearLayout)addView.getParent()).removeView(addView);

            buttonRemove.setOnClickListener(thisListener);
 */          LinearLayout container = binding.newRecipeStepsContainer;
            container.addView(buttonRemove);
            Log.d(TAG, container.toString());




        });

    }


    private void addRecipe(){
        if(recipe==null) recipe = new Recipe();

        recipe.setRecipe_name(binding.textInputRecipeName.getText().toString());
        recipe.setRecipe_category(binding.textInputRecipeCategory.getText().toString());

        List<String> ingredients = new ArrayList<String>();
        List<String> steps = new ArrayList<String>();
        int nIngredients=0;
        int nSteps=0;
        for (int i =0; i<nIngredients; i++) {
            String curr = binding.newRecipeIngredientText.getText().toString();
            ingredients.add(curr);

        }
/*        for (int i=0; i<nSteps; i++) {
            String curr = binding.newRecipeStepText.getText().toString();
            steps.add(curr);
        }*/
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);
        boolean isValid = true;

        //ProductName
        if(recipe.getRecipe_name().equals("")){
            binding.textInputLayoutRecipeName.setError(getResources().getString((R.string.error_empty_field)));
            binding.textInputLayoutRecipeName.requestFocus();
            isValid = false;
        }
        //Category
        if(recipe.getRecipe_category().equals("")){
            binding.textInputLayoutRecipeCategory.setError(getResources().getString((R.string.error_empty_field)));
            if(isValid) binding.textInputLayoutRecipeCategory.requestFocus();
            isValid = false;
        }
        else if(!CATEGORIES.contains(recipe.getRecipe_category())) {
            binding.textInputLayoutRecipeCategory.setError(getResources().getString((R.string.error_not_a_category)));
            if (isValid) binding.textInputLayoutRecipeCategory.requestFocus();
            isValid = false;
        }

        if(isValid){
            insertId = model.addRecipe(recipe);
         }
    }


}
