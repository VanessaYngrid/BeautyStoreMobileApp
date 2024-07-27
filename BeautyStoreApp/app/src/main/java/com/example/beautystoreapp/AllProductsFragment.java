package com.example.beautystoreapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.beautystoreapp.model.BeautyProduct;

import java.util.List;


public class AllProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;

    private List<BeautyProduct> productList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            productList = getArguments().getParcelableArrayList("productList");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_all_products, container, false);

        recyclerView = v.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        return v;
    }
}