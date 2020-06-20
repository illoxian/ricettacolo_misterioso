
package com.pape.ricettacolomisterioso.ui.recipes;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.adapters.RecipeListAdapter;
import com.pape.ricettacolomisterioso.databinding.FragmentRecipeListBinding;
import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.viewmodels.RecipeListViewModel;

import java.util.Arrays;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class RecipeListFragment extends Fragment {

    private static final String TAG = "RecipeListFragment";
    private RecipeListViewModel recipeListViewModel;
    private FragmentRecipeListBinding binding;
    private RecipeListViewModel model;
    private MutableLiveData<List<Recipe>> liveData;
    private RecipeListAdapter mAdapter;

    public RecipeListFragment() {

    }


    @Override
    public void onDetach() {

        super.onDetach();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.VISIBLE);


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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recipeListRecyclerView.setLayoutManager(layoutManager);
        model = new ViewModelProvider(this).get(RecipeListViewModel.class);
        mAdapter = new RecipeListAdapter(getActivity(), model.getRecipes().getValue(), new RecipeListAdapter.OnItemInteractions() {
            @Override
            public void onItemClick(Recipe recipe, View view) {
                Bundle recipeBundle = new Bundle();
                recipeBundle.putParcelable("recipe", recipe);
                RecipeListFragmentDirections.ShowRecipeProfile action = RecipeListFragmentDirections.showRecipeProfile(recipe);
                Navigation.findNavController(view).navigate(action);
            }
        });
        binding.recipeListRecyclerView.setAdapter(mAdapter);

        model.getRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                mAdapter.setData(model.getRecipes().getValue());
            }
        });

        if (res == R.string.recipes_categories_see_all) liveData = model.getAllRecipes();
        else model.getRecipesByCategory(Arrays.asList(getResources().getStringArray(R.array.recipeCategoriesString)).indexOf(getString(res)));


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recipeListRecyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        Recipe deletedItem = null;
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:
                    deletedItem = model.getRecipes().getValue().get(position);
                    model.delete(deletedItem);
                    mAdapter.removeProductAt(position);
                    Snackbar snackbar = Snackbar.make(binding.recipeListRecyclerView,
                            deletedItem.getTitle() + " " + getString(R.string.removed_from_recipes),
                            Snackbar.LENGTH_LONG).setAction(R.string.Undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAdapter.insertProductAt(deletedItem, position);
                            model.addRecipe(deletedItem);
                        }
                    });
                    snackbar.show();
                    break;
/*                 case ItemTouchHelper.RIGHT:

                     break;*/
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.red_400))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                    .addSwipeLeftLabel(getString(R.string.Delete))
                    .setSwipeLeftLabelColor(ContextCompat.getColor(getContext(), R.color.white_50))
                    .setSwipeLeftActionIconTint(ContextCompat.getColor(getContext(), R.color.white_50))
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


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

