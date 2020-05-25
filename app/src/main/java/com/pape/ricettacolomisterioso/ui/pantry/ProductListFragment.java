package com.pape.ricettacolomisterioso.ui.pantry;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pape.ricettacolomisterioso.R;
import com.pape.ricettacolomisterioso.adapters.ProductListAdapter;
import com.pape.ricettacolomisterioso.databinding.FragmentProductListBinding;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.viewmodels.ProductListViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "FragmentProductList";
    private FragmentProductListBinding binding;
    private ProductListViewModel model;
    private MutableLiveData<List<Product>> liveData;
    public ProductListFragment() {
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
        Integer res = ProductListFragmentArgs.fromBundle(getArguments()).getCategory();
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
