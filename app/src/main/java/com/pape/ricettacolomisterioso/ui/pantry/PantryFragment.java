package com.pape.ricettacolomisterioso.ui.pantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pape.ricettacolomisterioso.R;

public class PantryFragment extends Fragment {

    private PantryViewModel pantryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pantryViewModel =
                ViewModelProviders.of(this).get(PantryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pantry, container, false);
         return root;
    }
}
