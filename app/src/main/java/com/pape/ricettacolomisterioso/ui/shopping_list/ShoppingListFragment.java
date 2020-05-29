package com.pape.ricettacolomisterioso.ui.shopping_list;

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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.adapters.ShoppingListAdapter;
import com.pape.ricettacolomisterioso.databinding.FragmentShoppinglistBinding;
import com.pape.ricettacolomisterioso.models.Item;
import com.pape.ricettacolomisterioso.viewmodels.ShoppingListViewModel;

import java.util.List;

public class ShoppingListFragment extends Fragment {

    private static final String TAG = "ShoppingListFragment";

    private ShoppingListViewModel model;
    private ShoppingListAdapter mAdapter;

    private FragmentShoppinglistBinding binding;

    public ShoppingListFragment() {
    }

    public static ShoppingListFragment newInstance() {
        return new ShoppingListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShoppinglistBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //model = ViewModelProviders.of((FragmentActivity) view.getContext()).get(ShoppingListViewModel.class);
        model =  new ViewModelProvider(this).get(ShoppingListViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.shoppingListRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new ShoppingListAdapter(getActivity(), model.getItems().getValue(), new ShoppingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Log.d(TAG, "onItemClick:"+item.getItemName().toString());
            }
        });
        binding.shoppingListRecyclerView.setAdapter(mAdapter);

        model.getItems().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable List<Item> items) {
                Log.d(TAG, "onChanged: Items:" + items);
                mAdapter.setData(model.getItems().getValue());
            }
        });

        model.getInsertId().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long insertId) {
                Log.d(TAG, "onChanged: InsertId:" + insertId);
            }
        });

        model.getDeleteId().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer deleteId) {
                Log.d(TAG, "onChanged: deleteId:" + deleteId);
            }
        });

        binding.FabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        model.getAllItems();
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MyTheme_Dialog);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_shopping_list, null);
        builder.setView(dialogView);
        builder.setTitle(R.string.shopping_list_dialog_title);

        // Set up the inputs
        TextView item_name = dialogView.findViewById(R.id.shopping_list_dialog_input_name);
        TextView item_quantity = dialogView.findViewById(R.id.shopping_list_dialog_input_quantity);

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
                String itemName = item_name.getText().toString();
                String quantity = item_quantity.getText().toString();

                // if quantity is empty, set it to 1
                if (quantity.isEmpty())
                    quantity = "1";
                // if itemName is empty, the dialog will not close and an error is shown
                if (itemName.isEmpty())
                    item_name.setError(getString(R.string.error_empty_field));
                else {
                    Item item_to_insert = new Item(itemName, Integer.parseInt(quantity), false);
                    model.addItem(item_to_insert);
                    mAdapter.insertItem(item_to_insert);
                    dialog.dismiss();
                }
            }
        });
    }
}