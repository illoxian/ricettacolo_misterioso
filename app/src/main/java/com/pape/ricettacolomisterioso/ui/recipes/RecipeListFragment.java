/*
package com.pape.ricettacolomisterioso.ui.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.adapters.RecipeListAdapter;
import com.pape.ricettacolomisterioso.databinding.FragmentRecipeListBinding;
import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.viewmodels.RecipeListViewModel;

import java.util.List;


public class RecipeListFragment extends Fragment {

    private static final String TAG="RecipeListFragment";
    private RecipeListViewModel recipeListViewModel;
    private FragmentRecipeListBinding binding;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recipeListRecyclerView.setLayoutManager(layoutManager);

        //prendi dal DB la lista di ricette, per categoria oppure prendileTUtte

        List<Recipe> recipes;
        List<String> ingredients;
        List<String> steps;
        ingredients.add("Pasta");
        ingredients.add("Sugo");
        steps.add("Far Bollire la pasta");
        steps.add("Buttare la pasta");
        steps.add("Scolare la pasta");
        steps.add("Aggiungere il sugo preriscaldato");
        steps.add("Aggiungi formaggio a piacere");

        recipes.add(new Recipe(1204, "Penne al Sugo", "CAT1", "Penne al sugo di nonna pina", ingredients, steps));

        RecipeListAdapter recipeListAdapter = new RecipeListAdapter(getActivity(), recipes);
        binding.recipeListRecyclerView.setAdapter(recipeListAdapter);
    }
}
*/
