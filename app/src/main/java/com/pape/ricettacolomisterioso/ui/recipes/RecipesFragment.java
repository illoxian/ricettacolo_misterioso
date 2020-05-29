package com.pape.ricettacolomisterioso.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.databinding.FragmentRecipesBinding;
import com.pape.ricettacolomisterioso.viewmodels.RecipesViewModel;

public class RecipesFragment extends Fragment {

    private static final String TAG = "RECIPES_FRAGMENT";
    private FragmentRecipesBinding binding;
    private RecipesViewModel recipesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecipesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.recipes_app_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.recipes_app_bar_add) {
            Log.d("RecipeFragment", "onOption:ADD");
            startNewRecipeFragment();
            return true;
        }
        if(id==R.id.recipes_app_bar_search) {
            //TODO search in RecipeFragment
        }
        return super.onOptionsItemSelected(item);
    }

    private void startNewRecipeFragment() {
        Navigation.findNavController(getView()).navigate(R.id.action_add_new_recipe);
    }
}


