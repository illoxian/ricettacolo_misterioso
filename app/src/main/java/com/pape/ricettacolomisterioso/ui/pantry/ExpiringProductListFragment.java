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
import com.pape.ricettacolomisterioso.adapters.ExpiringProductListAdapter;
import com.pape.ricettacolomisterioso.databinding.ExpiringProductListItemBinding;
import com.pape.ricettacolomisterioso.databinding.FragmentExpiringProductListBinding;
import com.pape.ricettacolomisterioso.databinding.FragmentProductListBinding;
import com.pape.ricettacolomisterioso.models.Product;
import com.pape.ricettacolomisterioso.viewmodels.ExpiringProductListViewModel;

import java.util.List;

import static com.pape.ricettacolomisterioso.ui.pantry.ProductProfileActivity.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpiringProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpiringProductListFragment extends Fragment {

    private FragmentExpiringProductListBinding binding;
    private ExpiringProductListViewModel model;
    private MutableLiveData<List<Product>> liveData;
    public ExpiringProductListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpiringProductList.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpiringProductListFragment newInstance(String param1, String param2) {
        ExpiringProductListFragment fragment = new ExpiringProductListFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpiringProductListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.expiringProductListRecyclerView.setLayoutManager(layoutManager);
        model =  new ViewModelProvider(this).get(ExpiringProductListViewModel.class);
        final Observer<List<Product>> observer = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> product) {
                ExpiringProductListAdapter mAdapter = new ExpiringProductListAdapter(getActivity(), product);
                binding.expiringProductListRecyclerView.setAdapter(mAdapter);
                Log.d(TAG, product.toString());
            }
        };
        liveData = model.getAllProductsOrderByExpirationDate();
        liveData.observe(requireActivity(), observer);
    }
}
