package com.pape.ricettacolomisterioso.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.repositories.RecipesRepository;

import java.util.List;

public class RecipesViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<Recipe>> recipes;
    private MutableLiveData<Recipe> recipe;
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
    public MutableLiveData<List<Recipe>> getRecipesSearched(String recipe_name){
        RecipesRepository.getInstance().getRecipesSearched(recipes, recipe_name);
        return recipes;
    }
    public MutableLiveData<List<Recipe>> getAllRecipes(){
        if (recipes == null) {
            recipes = new MutableLiveData<>();
        }
        RecipesRepository.getInstance().getRecipes(recipes);
        return recipes;
    }
    public MutableLiveData<Recipe> getRandomRecipe(){
        if (recipe == null) {
            recipe = new MutableLiveData<>();
        }
        RecipesRepository.getInstance().getRandRecipe(recipe);
        return recipe;
    }

}