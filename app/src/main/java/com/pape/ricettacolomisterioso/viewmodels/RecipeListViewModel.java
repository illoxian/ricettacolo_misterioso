package com.pape.ricettacolomisterioso.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.repositories.RecipesRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {
    private static final String TAG = "RecipeListViewModel";

    private MutableLiveData<List<Recipe>> recipes;
    private MutableLiveData<Integer> deleteId;
    private MutableLiveData<Long> insertId;


    public MutableLiveData<List<Recipe>> getRecipes() {
        if (recipes == null) {
            recipes = new MutableLiveData<>();
        }
        return recipes;
    }

    public MutableLiveData<Integer> getDeleteId() {
        if (deleteId == null) {
            deleteId = new MutableLiveData<>();
        }
        return deleteId;
    }

    public MutableLiveData<Long> getInsertId() {
        if (insertId == null) {
            insertId = new MutableLiveData<>();
        }
        return insertId;
    }

    public MutableLiveData<List<Recipe>> getRecipesByCategory(String category) {
        if (recipes == null) {
            recipes = new MutableLiveData<>();
        }


        Log.d(TAG, "getProductsByCategory: getProductsUpdate");
        RecipesRepository.getInstance().getRecipesByCategory(recipes, category);
        return recipes;
    }

    public MutableLiveData<List<Recipe>> getAllRecipes(){
        if (recipes == null) {
            recipes = new MutableLiveData<>();
        }

        Log.d(TAG, "getAllrecipes: getRecipesUpdate");
        RecipesRepository.getInstance().getRecipes(recipes);
        return recipes;
    }

    public void delete(Recipe recipe){
        RecipesRepository.getInstance().delete(recipe, getDeleteId());
    }

    public void addRecipe(Recipe recipe){
        RecipesRepository.getInstance().addRecipe(recipe, getInsertId());
    }
}
