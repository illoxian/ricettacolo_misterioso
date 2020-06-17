package com.pape.ricettacolomisterioso.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.repositories.RecipesRepository;
import com.pape.ricettacolomisterioso.ui.recipes.RecipesFragment;

import java.util.List;

public class RecipesViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<Recipe>> recipes;
    public RecipesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        if (recipes == null) {
            recipes = new MutableLiveData<>();
        }
        return recipes;
    }
    public MutableLiveData<List<Recipe>> getRecipesSeached(String product_name){
        RecipesRepository.getInstance().getRecipesSearched(recipes, product_name);
        return recipes;
    }
    public MutableLiveData<List<Recipe>> getAllRecipes(){
        if (recipes == null) {
            recipes = new MutableLiveData<>();
        }
        RecipesRepository.getInstance().getRecipes(recipes);
        return recipes;
    }
}