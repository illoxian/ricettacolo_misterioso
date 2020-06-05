package com.pape.ricettacolomisterioso.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Recipe;
import com.pape.ricettacolomisterioso.ui.recipes.RecipeListFragment;
import com.pape.ricettacolomisterioso.ui.recipes.RecipeListFragmentDirections;
import com.pape.ricettacolomisterioso.ui.recipes.RecipesFragment;
import com.pape.ricettacolomisterioso.ui.recipes.RecipesFragmentDirections;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {
    private List<Recipe> recipes;
    private LayoutInflater layoutInflater;
    private static int inflateRecipeItem = R.layout.recipe_list_item;


    public RecipeListAdapter(Context context, List<Recipe> recipes) {
        this.recipes = recipes;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public static class RecipeListViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRecipeTitle;
        TextView textViewRecipeCategory;
        ImageView item_icon;
        public RecipeListViewHolder(View view) {
            super(view);
            textViewRecipeTitle = view.findViewById(R.id.textView_recipe_list_item_name);
            textViewRecipeCategory = view.findViewById(R.id.textView_recipe_list_item_category);
            item_icon = view.findViewById(R.id.image_view_recipe_list_item_category);

        }
    }

    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(inflateRecipeItem, parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        holder.textViewRecipeTitle.setText(recipes.get(position).getRecipe_name());
        holder.textViewRecipeCategory.setText(recipes.get(position).getRecipe_category());

        holder.item_icon.setImageDrawable(holder.itemView.getResources().getDrawable(
                recipes.get(position).getCategoryIconId(holder.itemView.getContext())));


        holder.itemView.setOnClickListener(v->
        {
            Bundle recipe = new Bundle();
            recipe.putParcelable("recipe", recipes.get(position));
            RecipeListFragmentDirections.ShowRecipeProfile action = RecipeListFragmentDirections.showRecipeProfile(recipes.get(position));
            Navigation.findNavController(v).navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

}
