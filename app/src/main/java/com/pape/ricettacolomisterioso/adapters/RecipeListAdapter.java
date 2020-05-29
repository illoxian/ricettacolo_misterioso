package com.pape.ricettacolomisterioso.adapters;

import android.content.Context;
import android.print.PrintDocumentAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Recipe;
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

        public RecipeListViewHolder(View view) {
            super(view);
            textViewRecipeTitle = view.findViewById(R.id.recipe_list_item_title);
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
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

}
