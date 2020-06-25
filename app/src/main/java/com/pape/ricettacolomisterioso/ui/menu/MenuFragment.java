package com.pape.ricettacolomisterioso.ui.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.adapters.MenuListAdapter;
import com.pape.ricettacolomisterioso.databinding.FragmentMenuBinding;
import com.pape.ricettacolomisterioso.models.DailyMenu;
import com.pape.ricettacolomisterioso.models.DailyRecipe;
import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.viewmodels.MenuViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuFragment extends Fragment {

    private static final String TAG = "ShoppingListFragment";

    private MenuViewModel model;
    private MenuListAdapter mAdapter;

    private FragmentMenuBinding binding;

    private List<String> recipesString;

    public MenuFragment() {
    }

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(this).get(MenuViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.menuRecyclerview.setLayoutManager(layoutManager);

        mAdapter = new MenuListAdapter(getActivity(), model.getDailyMenus().getValue(), new MenuListAdapter.OnItemInteractions() {
            final View mView = view;

            @Override
            public void onRecipeClick(DailyMenu dailyMenu, int slot) {
                DailyRecipe dailyRecipe = dailyMenu.getRecipes().get(slot);
                if (dailyRecipe == null) {
                    Log.d(TAG, "onRecipeClick: Aggiungi una ricetta");
                    showDialog(dailyMenu.getDay(), slot);
                } else {
                    Log.d(TAG, "onRecipeClick: " + dailyMenu.getRecipes().get(slot));
                    if (dailyRecipe.getRecipeComplex() != null) {
                        Bundle recipeBundle = new Bundle();
                        recipeBundle.putParcelable("recipe", dailyRecipe.getRecipeComplex());
                        MenuFragmentDirections.ShowRecipeProfileFromMenu action = MenuFragmentDirections.ShowRecipeProfileFromMenu(dailyRecipe.getRecipeComplex());
                        Navigation.findNavController(mView).navigate(action);
                    }
                }
            }

            @Override
            public void onRecipeLongClick(DailyMenu dailyMenu, int slot, int adapterPosition) {
                ShowDialogDelete(dailyMenu, slot, adapterPosition);
            }
        });
        binding.menuRecyclerview.setAdapter(mAdapter);

        model.getDailyMenus().observe(getViewLifecycleOwner(), new Observer<List<DailyMenu>>() {
            @Override
            public void onChanged(@Nullable List<DailyMenu> dailyMenus) {
                Log.d(TAG, "onChanged: Items:" + dailyMenus);
                binding.toolbarTitle.setText(model.getWeekRangeString());
                mAdapter.setData(model.getDailyMenus().getValue());
            }
        });

        binding.imageViewWeekPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeWeek(-1);
            }
        });

        binding.imageViewWeekNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeWeek(1);
            }
        });

        model.getRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                recipesString = new ArrayList<>();
                for (Recipe r : recipes) {
                    recipesString.add(r.getTitle());
                }
            }
        });

        ChangeWeek(0);
        model.getAllRecipes();
    }

    private void showDialog(Date day, int slot) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MyTheme_Dialog);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_menu_add_recipe, null);
        builder.setView(dialogView);
        builder.setTitle(R.string.menu_dialog_title);

        // Set up the inputs
        AutoCompleteTextView recipe_name = dialogView.findViewById(R.id.menu_dialog_input_name);
        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, recipesString);
        recipe_name.setAdapter(adapter);

        // Set up the buttons
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // This behaviour is set after the dialog is shown to handle input errors
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipeName = recipe_name.getText().toString();

                // if itemName is empty, the dialog will not close and an error is shown
                if (recipeName.isEmpty())
                    recipe_name.setError(getString(R.string.error_empty_field));
                else {
                    int recipeId = recipesString.indexOf(recipeName);
                    DailyRecipe recipe_to_insert;
                    if (recipeId < 0)
                        recipe_to_insert = new DailyRecipe(day, recipeName, slot);
                    else
                        recipe_to_insert = new DailyRecipe(day, model.getRecipes().getValue().get(recipeId), slot);
                    model.insert(recipe_to_insert);
                    ChangeWeek(0);
                    dialog.dismiss();
                }
            }
        });
    }

    private void ShowDialogDelete(DailyMenu dailyMenu, int slot, int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MyTheme_Dialog);
        builder.setTitle(R.string.menu_dialog_delete_title);

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.Delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DailyRecipe dailyRecipe = dailyMenu.getRecipes().get(slot);
                model.delete(dailyRecipe);
                dailyMenu.updateRecipe(slot, null);
                mAdapter.update(adapterPosition, dailyMenu);
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ChangeWeek(int offset) {
        model.ChangeWeek(offset);
        binding.toolbarTitle.setText(model.getWeekRangeString());
    }
}
