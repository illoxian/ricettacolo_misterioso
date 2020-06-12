package com.pape.ricettacolomisterioso.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.pape.ricettacolomisterioso.models.AppDatabase;
import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.ui.MainActivity;

import java.util.List;

public class RecipesRepository {
    private static final String TAG = "RecipesRepository";

    private static RecipesRepository instance;
    private AppDatabase appDatabase;

    private RecipesRepository() {
        appDatabase = MainActivity.db;
    }

    public static synchronized RecipesRepository getInstance() {
        if (instance == null) {
            instance = new RecipesRepository();
        }
        return instance;
    }

    public void addRecipe(Recipe recipe, MutableLiveData<Long> insertId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    long id = appDatabase.recipeDao().insert(recipe);
                    insertId.postValue(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void getRecipes(MutableLiveData<List<Recipe>> recipes) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "getRecipes(): ");
                    recipes.postValue(appDatabase.recipeDao().getAll());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        new Thread(runnable).start();

    }

   public void getRecipesByCategory(MutableLiveData<List<Recipe>> recipes, String category) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "getRecipesByCategory(" + category + "): ");
                    recipes.postValue(appDatabase.recipeDao().findByCategory(category));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
    public void delete(Recipe recipe, MutableLiveData<Integer> deleteId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    int id = appDatabase.recipeDao().delete(recipe);
                    Log.d(TAG, "run: deleteId:"+id);
                    deleteId.postValue(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }


    public void getRecipesSearched(MutableLiveData<List<Recipe>> recipes, String recipe_name){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: getRecipesSearched() ");
                    recipes.postValue(appDatabase.recipeDao().getSearchedRecipes(recipe_name));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}
