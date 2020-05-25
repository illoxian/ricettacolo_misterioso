package com.pape.ricettacolomisterioso.ui.shopping_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.databinding.FragmentProductListBinding;
import com.pape.ricettacolomisterioso.ui.pantry.ProductListFragment;
import com.pape.ricettacolomisterioso.viewmodels.ProductListViewModel;
import com.pape.ricettacolomisterioso.viewmodels.ShoppingListViewModel;

public class ShoppingListFragment extends Fragment {
    private static final String TAG = "ShoppingListFragment";
/**    private ShoppingListFragmentBinding binding;


    private ShoppingListViewModel shoppingListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shoppingListViewModel =
                ViewModelProviders.of(this).get(ShoppingListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        shoppingListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }**/
}