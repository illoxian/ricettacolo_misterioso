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
    private ShoppingListFragmentBinding binding;


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
    }
}

private static final String TAG = "FragmentProductList";
    private FragmentProductListBinding binding;
    private ProductListViewModel model;
    private MutableLiveData<List<Product>> liveData;
    public FragmentProductList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentProductList.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductListFragment newInstance(String param1, String param2) {
        ProductListFragment fragment = new ProductListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Integer res = FragmentProductListArgs.fromBundle(getArguments()).getCategory();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.productListRecyclerView.setLayoutManager(layoutManager);

        model =  new ViewModelProvider(this).get(ProductListViewModel.class);
        final Observer<List<Product>> observer = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> product) {
                ProductListAdapter mAdapter = new ProductListAdapter(getActivity(), product);
                binding.productListRecyclerView.setAdapter(mAdapter);
                Log.d(TAG, product.toString());
            }
        };
        if(res == R.string.pantry_categories_see_all) liveData = model.getAllProducts();
        else liveData = model.getProductsByCategory(getString(res));

        liveData.observe(requireActivity(), observer);

    }
}
