package com.pape.ricettacolomisterioso.ui.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.adapters.MenuListAdapter;
import com.pape.ricettacolomisterioso.databinding.FragmentMenuBinding;
import com.pape.ricettacolomisterioso.models.DailyMenu;
import com.pape.ricettacolomisterioso.models.DailyRecipe;
import com.pape.ricettacolomisterioso.viewmodels.MenuViewModel;

import java.util.Date;
import java.util.List;

public class MenuFragment extends Fragment {

    private static final String TAG = "ShoppingListFragment";

    private MenuViewModel model;
    private MenuListAdapter mAdapter;

    private FragmentMenuBinding binding;



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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        model =  new ViewModelProvider(this).get(MenuViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.menuRecyclerview.setLayoutManager(layoutManager);

        mAdapter = new MenuListAdapter(getActivity(), model.getDailyMenus().getValue(), new MenuListAdapter.OnItemInteractions() {
            @Override
            public void onRecipeClick(String recipe, Date day, int slot) {
                if(recipe==null){
                    Log.d(TAG, "onRecipeClick: Aggiungi una ricetta");
                    showDialog(day, slot);
                }
                else{
                    Log.d(TAG, "onRecipeClick: "+ recipe);
                }
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
                model.ChangeWeek(-1);
            }
        });

        binding.imageViewWeekNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.ChangeWeek(1);
            }
        });

        model.ChangeWeek(0);
    }

    private void showDialog(Date day, int slot){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MyTheme_Dialog);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_menu_add_recipe, null);
        builder.setView(dialogView);
        builder.setTitle(R.string.menu_dialog_title);

        // Set up the inputs
        TextView recipe_name = dialogView.findViewById(R.id.menu_dialog_input_name);

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
                    DailyRecipe recipe_to_insert = new DailyRecipe(day, recipeName, slot);
                    model.insert(recipe_to_insert);
                    model.ChangeWeek(0);
                    dialog.dismiss();
                }
            }
        });
    }
}
