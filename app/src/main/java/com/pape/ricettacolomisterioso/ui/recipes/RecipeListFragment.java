
package com.pape.ricettacolomisterioso.ui.recipes;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.adapters.RecipeListAdapter;
import com.pape.ricettacolomisterioso.databinding.FragmentRecipeListBinding;
import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.viewmodels.RecipeListViewModel;

import java.util.List;


public class RecipeListFragment extends Fragment {

    private static final String TAG = "RecipeListFragment";
    private RecipeListViewModel recipeListViewModel;
    private FragmentRecipeListBinding binding;
    private RecipeListViewModel model;
    private MutableLiveData<List<Recipe>> liveData;
    private RecipeListAdapter mAdapter;

    public RecipeListFragment() {

    }

    public static RecipeListFragment newInstance(String param1, String param2) {
        RecipeListFragment fragment = new RecipeListFragment();
        return fragment;
    }


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
        setHasOptionsMenu(true);

        Integer res = RecipeListFragmentArgs.fromBundle(getArguments()).getCategory();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recipeListRecyclerView.setLayoutManager(layoutManager);

        model = new ViewModelProvider(this).get(RecipeListViewModel.class);
        final Observer<List<Recipe>> observer = new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipe) {
                mAdapter = new RecipeListAdapter(getActivity(), recipe);
                binding.recipeListRecyclerView.setAdapter(mAdapter);
                Log.d(TAG, recipe.toString());
            }
        };

        if (res == R.string.recipes_categories_see_all) liveData = model.getAllRecipes();
        else liveData = model.getRecipesByCategory(getString(res));

        liveData.observe(requireActivity(), observer);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Navigation.findNavController(getView()).navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

