package com.pape.ricettacolomisterioso.ui.recipes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.databinding.FragmentRecipeProfileBinding;
import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.utils.Functions;
import com.pape.ricettacolomisterioso.viewmodels.RecipeProfileViewModel;

import java.util.ArrayList;

public class RecipeProfileFragment extends Fragment {
    final static String TAG = "RecipeProfileFragment";
    private RecipeProfileViewModel model;
    private FragmentRecipeProfileBinding binding;

    public RecipeProfileFragment() {

    }

    public static RecipeProfileFragment newInstance() {
        RecipeProfileFragment fragment = new RecipeProfileFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        Recipe recipe = RecipeProfileFragmentArgs.fromBundle(getArguments()).getRecipe();
        inflateRecipeInfo(recipe);
        binding.deleteRecipeImage.setOnClickListener(v -> {
            showDialogDelete(recipe);

        });

        Log.d(TAG, recipe.toString());

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);


        binding = FragmentRecipeProfileBinding.inflate(getLayoutInflater());
        model = new ViewModelProvider(this).get(RecipeProfileViewModel.class);
       /* Recipe recipe = RecipeProfileFragmentArgs.fromBundle(getArguments()).getRecipe();

        inflateRecipeInfo(recipe);*/
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.GONE);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onDetach() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);

        super.onDetach();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
        bottomNavigationView.setVisibility(View.VISIBLE);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

    }


    public void inflateRecipeInfo(Recipe recipe) {
        binding.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        binding.imageView.setImageResource(recipe.getCategoryPreviewId(getContext()));

        binding.textViewRecipeProfileTitle.setText(recipe.getTitle());
        binding.textViewRecipeProfileCategory.setText(Functions.getRecipeCategoryString(getContext(), recipe.getCategoryId()));
        binding.textViewRecipeProfileDate.setText(recipe.getDateString());

        ArrayList<String> ingredients = (ArrayList) recipe.getIngredients();
        ArrayList<String> steps = (ArrayList) recipe.getSteps();
        for (String ing : ingredients) {
            //inflate a textview
            //assign that textview the text in ing
            TextView toadd = new TextView(getContext());
            toadd.setText(" ● " + ing);
            toadd.setTextAppearance(getContext(), R.style.TextAppearance_AppCompat_Medium);
            toadd.setPadding(0, 0, 0, 8);
            binding.linearLayoutRecipeProfileIngredients.addView(toadd);
        }

        int i = 1;
        for (String step : steps) {
            TextView toadd = new TextView(getContext());
            toadd.setText(" ● " + step);
            toadd.setTextAppearance(getContext(), R.style.TextAppearance_AppCompat_Medium);
            toadd.setPadding(0, 0, 0, 8);
            binding.linearLayoutRecipeProfileSteps.addView(toadd);
            i++;
        }


    }

    private void showDialogDelete(Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MyTheme_Dialog);
        builder.setTitle(R.string.recipe_dialog_delete_title);
        // Set up the buttons
        builder.setPositiveButton(getString(R.string.Delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                model.delete(recipe);
                Snackbar.make(getView(), R.string.removed_from_recipes, Snackbar.LENGTH_LONG).show();

                Navigation.findNavController(getView()).navigateUp();
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
}
