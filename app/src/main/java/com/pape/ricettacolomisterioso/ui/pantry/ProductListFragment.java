package com.pape.ricettacolomisterioso.ui.pantry;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.adapters.ProductListAdapter;
import com.pape.ricettacolomisterioso.databinding.FragmentProductListBinding;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.viewmodels.ProductListViewModel;

import java.util.Arrays;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ProductListFragment extends Fragment {

    private static final String TAG = "FragmentProductList";

    private ProductListViewModel model;
    private ProductListAdapter mAdapter;

    private FragmentProductListBinding binding;

    public ProductListFragment() {
    }

    public static ProductListFragment newInstance() {
        return new ProductListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        Integer res = ProductListFragmentArgs.fromBundle(getArguments()).getCategory();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.productListRecyclerView.setLayoutManager(layoutManager);
        model = new ViewModelProvider(this).get(ProductListViewModel.class);


        mAdapter = new ProductListAdapter(getActivity(), model.getProducts().getValue(), new ProductListAdapter.OnItemInteractions() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(getContext(), ProductProfileActivity.class);
                Bundle productBundle = new Bundle();
                productBundle.putParcelable("product", product);
                intent.putExtra("product", productBundle);
                startActivity(intent);
            }

            @Override
            public void onItemClickAddToShoppingList(Product product) {
                model.addProductToShoppingList(product.getProduct_name());
                Snackbar snackbar = Snackbar.make(view, R.string.product_added_to_shopping_list, Snackbar.LENGTH_LONG);
                snackbar.setAnchorView(getActivity().findViewById(R.id.nav_view));
                snackbar.show();
            }
        });
        binding.productListRecyclerView.setAdapter(mAdapter);

        model.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                mAdapter.setData(model.getProducts().getValue());
                Log.d(TAG, products.toString());
            }
        });

        if(res == R.string.pantry_categories_see_all) model.getAllProducts();
        else{
            model.getProductsByCategory(Arrays.asList(getResources().getStringArray(R.array.categoriesString)).indexOf(getString(res)));
        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.productListRecyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }


        Product deletedItem = null;
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:
                    deletedItem = model.getProducts().getValue().get(position);
                    model.delete(deletedItem);
                    mAdapter.removeProductAt(position);
                    Snackbar snackbar = Snackbar.make(binding.productListRecyclerView,
                            deletedItem.getProduct_name() + " " + getString(R.string.removed_from_products),
                            Snackbar.LENGTH_LONG).setAction(R.string.Undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAdapter.insertProductAt(deletedItem, position);
                            model.addProduct(deletedItem);
                        }
                    });
                    snackbar.show();
                    break;
/*                 case ItemTouchHelper.RIGHT:

                     break;*/
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.red_400))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                    .addSwipeLeftLabel(getString(R.string.Delete))
                    .setSwipeLeftLabelColor(ContextCompat.getColor(getContext(), R.color.white_50))
                    .setSwipeLeftActionIconTint(ContextCompat.getColor(getContext(), R.color.white_50))
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Navigation.findNavController(getView()).navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
