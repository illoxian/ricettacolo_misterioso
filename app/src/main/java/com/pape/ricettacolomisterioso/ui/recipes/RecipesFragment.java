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
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.adapters.RecipeListAdapter;
import com.pape.ricettacolomisterioso.databinding.FragmentRecipesBinding;
import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.ui.pantry.PantryFragmentDirections;
import com.pape.ricettacolomisterioso.viewmodels.RecipesViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment {

    private static final String TAG = "RecipesFragment";
    private FragmentRecipesBinding binding;
    private RecipesViewModel recipesViewModel;
    private RecipeListAdapter searchAdapter;
    private RecipesViewModel model;
    private View view;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecipesBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model =  new ViewModelProvider(this).get(RecipesViewModel.class);

        List<CardView> cardViews = new ArrayList<>();
        cardViews.add(binding.recipesAppetizersCardView);
        cardViews.add(binding.recipesFirstCoursesCardView);
        cardViews.add(binding.recipesMainCoursesCardView);
        cardViews.add(binding.recipesDessertsCardView);
        cardViews.add(binding.recipesSideDishesCardView);
        cardViews.add(binding.recipesSingleCoursesCardView);
        cardViews.add(binding.recipesCondimentsCardView);
        cardViews.add(binding.recipesBreadAndPizzaCardView);
        cardViews.add(binding.recipesDrinksCardView);
        cardViews.add(binding.recipesSeeAllCardView);

        for(CardView cardView: cardViews)
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int res = getCardViewStringRes(cardView);
                    RecipesFragmentDirections.ShowRecipeslistFragmentAction action = RecipesFragmentDirections.showRecipeslistFragmentAction(res);

                    if (Navigation.findNavController(view).getCurrentDestination().getId() == R.id.navigation_recipes) {
                        Navigation.findNavController(view).navigate(action);
                    }
                }
            });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.recipes_app_bar_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.recipes_app_bar_search).getActionView();
        searchView.setIconified(true);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                binding.recipesCategoriesTablelayout.setVisibility(View.VISIBLE);
                binding.recipesCategoriesTitle.setVisibility(View.VISIBLE);
                binding.recipesMenuTitle.setVisibility(View.VISIBLE);
                binding.recipesMenuCard1.setVisibility(View.VISIBLE);
                binding.recipesFragmentRecyclerView.setVisibility(View.GONE);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        onSearched(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        binding.recipesCategoriesTablelayout.setVisibility(View.GONE);
                        binding.recipesCategoriesTitle.setVisibility(View.GONE);
                        binding.recipesMenuTitle.setVisibility(View.GONE);
                        binding.recipesMenuCard1.setVisibility(View.GONE);
                        binding.recipesFragmentRecyclerView.setVisibility(View.VISIBLE);

                        onSearched(newText);
                        return false;
                    }
                });
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.recipesCategoriesTablelayout.setVisibility(View.VISIBLE);
                binding.recipesCategoriesTitle.setVisibility(View.VISIBLE);
                binding.recipesMenuTitle.setVisibility(View.VISIBLE);
                binding.recipesMenuCard1.setVisibility(View.VISIBLE);
                binding.recipesFragmentRecyclerView.setVisibility(View.GONE);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        onSearched(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        binding.recipesCategoriesTablelayout.setVisibility(View.GONE);
                        binding.recipesCategoriesTitle.setVisibility(View.GONE);
                        binding.recipesMenuTitle.setVisibility(View.GONE);
                        binding.recipesMenuCard1.setVisibility(View.GONE);
                        binding.recipesFragmentRecyclerView.setVisibility(View.VISIBLE);

                        onSearched(newText);
                        return false;
                    }
                });

            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.recipes_app_bar_add) {
            startNewRecipeFragment();
            return true;
        }
        if(id==R.id.recipes_app_bar_search) {
            //TODO search in RecipeFragment
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSearched(String newString){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recipesFragmentRecyclerView.setLayoutManager(layoutManager);

        searchAdapter = new RecipeListAdapter(getActivity(), model.getRecipes().getValue(), new RecipeListAdapter.OnItemInteractions() {
            @Override
            public void onItemClick(Recipe recipe, View view) {
                Bundle recipeBundle = new Bundle();
                recipeBundle.putParcelable("recipe", recipe);
                RecipesFragmentDirections.ShowRecipeProfileFromNavigationPantry action = RecipesFragmentDirections.showRecipeProfileFromNavigationPantry(recipe);
                Navigation.findNavController(view).navigate(action);
            }
        });
        binding.recipesFragmentRecyclerView.setAdapter(searchAdapter);

        model.getRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                searchAdapter.setData(model.getRecipes().getValue());
            }
        });

        model.getRecipesSeached(newString);
    }



    private void startNewRecipeFragment() {
        Navigation.findNavController(getView()).navigate(R.id.action_add_new_recipe);
    }

    public int getCardViewStringRes(CardView cardView){
        int res = 0;
        switch (cardView.getId()){
            case R.id.recipes_appetizers_cardView:
                res =  R.string.recipes_categories_appetizers;
                break;
            case R.id.recipes_first_courses_cardView:
                res = R.string.recipes_categories_first_courses;
                break;
            case R.id.recipes_main_courses_cardView:
                res = R.string.recipes_categories_main_courses;
                break;
            case R.id.recipes_desserts_cardView:
                res = R.string.recipes_categories_desserts;
                break;
            case R.id.recipes_side_dishes_cardView:
                res = R.string.recipes_categories_side_dishes;
                break;
            case R.id.recipes_single_courses_cardView:
                res = R.string.recipes_categories_single_courses;
                break;
            case R.id.recipes_condiments_cardView:
                res = R.string.recipes_categories_condiments;
                break;
            case R.id.recipes_bread_and_pizza_cardView:
                res = R.string.recipes_categories_bread_and_pizza;
                break;
            case R.id.recipes_drinks_cardView:
                res = R.string.recipes_categories_drinks;
                break;
            case R.id.recipes_see_all_cardView:
                res = R.string.recipes_categories_see_all;
        }
        return res;
    }
}
