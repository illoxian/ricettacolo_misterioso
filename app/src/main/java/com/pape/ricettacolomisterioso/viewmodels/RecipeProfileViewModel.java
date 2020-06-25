package com.pape.ricettacolomisterioso.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Item;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.repositories.RecipesRepository;

public class RecipeProfileViewModel extends ViewModel {
    private final String TAG = "RecipeProfileViewModel";

    private MutableLiveData<Product> recipe;
    private MutableLiveData<Long> insertId;
    private MutableLiveData<Item> findItem;
    private MutableLiveData<Integer> deleteId;

    public MutableLiveData<Product> getRecipe() {
        if (recipe == null) {
            recipe = new MutableLiveData<>();
        }
        return recipe;
    }

    public MutableLiveData<Long> getInsertId() {
        if (insertId == null) {
            insertId = new MutableLiveData<>();
        }
        return insertId;
    }

    public MutableLiveData<Item> getFindItem() {
        if (findItem == null) {
            findItem = new MutableLiveData<>();
        }
        return findItem;
    }

    public MutableLiveData<Integer> getDeleteId() {
        if (deleteId == null) {
            deleteId = new MutableLiveData<>();
        }
        return deleteId;
    }

    public void delete(Recipe recipe) {
        RecipesRepository.getInstance().delete(recipe, getDeleteId());
    }
}
