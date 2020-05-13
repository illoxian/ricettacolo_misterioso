package com.pape.ricettacolomisterioso.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.viewmodels.RecipesViewModel;

public class RecipesFragment extends Fragment {

    private static final String TAG = "RECIPES_FRAGMENT";
    private RecipesViewModel recipesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        recipesViewModel =
                ViewModelProviders.of(this).get(RecipesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);

        return root;
    }
}
