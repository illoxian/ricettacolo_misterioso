package com.pape.ricettacolomisterioso.ui.pantry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pape.ricettacolomisterioso.ui.new_product.NewProductActivity;
import com.pape.ricettacolomisterioso.R;

public class PantryFragment extends Fragment {

    private PantryViewModel pantryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pantryViewModel =
                ViewModelProviders.of(this).get(PantryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pantry, container, false);

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pantry_app_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.app_bar_add){
            Log.d("PantryFragment", "onOptionsItemSelected: Add");
            startNewProductActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startNewProductActivity(){
        Intent intent = new Intent(this.getActivity(), NewProductActivity.class);
        startActivity(intent);
    }
}
