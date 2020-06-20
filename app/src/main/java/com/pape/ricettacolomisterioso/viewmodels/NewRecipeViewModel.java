package com.pape.ricettacolomisterioso.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.repositories.RecipesRepository;

public class NewRecipeViewModel extends ViewModel {
    private static final String TAG = "NewRecipeViewModel";

    private MutableLiveData<Long> insertId;


    public MutableLiveData<Long> getInsertId() {
        if (insertId == null) {
            insertId = new MutableLiveData<Long>();
        }
        return insertId;
    }

    public MutableLiveData<Long> addRecipe(Recipe recipe){
        RecipesRepository.getInstance().addRecipe(recipe, getInsertId());
        return insertId;
    }


}

