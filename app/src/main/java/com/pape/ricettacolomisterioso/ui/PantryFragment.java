package com.pape.ricettacolomisterioso.ui;

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
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.$Gson$Preconditions;
import com.pape.ricettacolomisterioso.FragmentProductList;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.viewmodels.PantryViewModel;
import com.pape.ricettacolomisterioso.viewmodels.ProductListViewModel;

import java.util.List;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CardView c = view.findViewById(R.id.primo);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //andare avanti aprendo fragmentProductList
                Navigation.findNavController(view).navigate(R.id.fragmentProductList);
            }
        });
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
