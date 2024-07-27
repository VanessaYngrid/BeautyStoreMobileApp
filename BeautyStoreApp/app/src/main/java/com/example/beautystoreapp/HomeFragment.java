package com.example.beautystoreapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.beautystoreapp.model.BeautyProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment {

    private ImageView prodPictureImageView;
    private TextView prodNameTextView, prodBrandTextView, prodPriceTextView, prodCategoryTextView;
    private ImageView prevProductButton;
    private ImageView nextProductButton;
    private Button seeAllProductsButton;

    private ImageView productImage1, productImage2, productImage3;
    private TextView bestSellerProductName1, bestSellerProductName2, bestSellerProductName3;
    private TextView bestSellerProductBrand1, bestSellerProductBrand2, bestSellerProductBrand3;
    private TextView bestSellerProductPrice1, bestSellerProductPrice2, bestSellerProductPrice3;
    private TextView bestSellerProductCategory1, bestSellerProductCategory2, bestSellerProductCategory3;

    private List<BeautyProduct> productList = new ArrayList<>();
    private List<BeautyProduct> bestSellersList = new ArrayList<>();
    public static String TAG;
    private int currentProductIndex = 0;
    private DatabaseReference databaseRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize
        databaseRef = FirebaseDatabase.getInstance().getReference("BeautyProducts");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        prodPictureImageView = v.findViewById(R.id.prd_image_image_view);
        prodNameTextView = v.findViewById(R.id.prd_name_text_view);
        prodBrandTextView = v.findViewById(R.id.prd_brand_text_view);
        prodPriceTextView = v.findViewById(R.id.prd_price_text_view);
        prodCategoryTextView = v.findViewById(R.id.prd_category_text_view);
        prevProductButton = v.findViewById(R.id.prev_product_button);
        nextProductButton = v.findViewById(R.id.next_product_button);
        seeAllProductsButton = v.findViewById(R.id.see_all_products_button);

        // Initialize views for best sellers
        productImage1 = v.findViewById(R.id.product_image_1);
        productImage2 = v.findViewById(R.id.product_image_2);
        productImage3 = v.findViewById(R.id.product_image_3);
        bestSellerProductName1 = v.findViewById(R.id.best_seller_product_name1);
        bestSellerProductName2 = v.findViewById(R.id.best_seller_product_name2);
        bestSellerProductName3 = v.findViewById(R.id.best_seller_product_name3);
        bestSellerProductBrand1 = v.findViewById(R.id.best_seller_product1_brand);
        bestSellerProductBrand2 = v.findViewById(R.id.best_seller_product2_brand);
        bestSellerProductBrand3 = v.findViewById(R.id.best_seller_product3_brand);
        bestSellerProductPrice1 = v.findViewById(R.id.best_seller_product1_price);
        bestSellerProductPrice2 = v.findViewById(R.id.best_seller_product2_price);
        bestSellerProductPrice3 = v.findViewById(R.id.best_seller_product3_price);
        bestSellerProductCategory1 = v.findViewById(R.id.best_seller_product1_category);
        bestSellerProductCategory2 = v.findViewById(R.id.best_seller_product2_category);
        bestSellerProductCategory3 = v.findViewById(R.id.best_seller_product3_category);

        // Fetch products and bestsellers from Firebase
        fetchProductsFromFirebase();

        prevProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProductIndex = ((currentProductIndex - 1) + productList.size()) % productList.size();
                displayProducts(currentProductIndex);
            }
        });

        nextProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProductIndex = ((currentProductIndex + 1) % productList.size());
                displayProducts(currentProductIndex);
            }
        });


        seeAllProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass product list data to AllProductsFragment using arguments
                Bundle args = new Bundle();
                args.putParcelableArrayList("productList", new ArrayList<>(productList));
                AllProductsFragment allProductsFragment = new AllProductsFragment();
                allProductsFragment.setArguments(args);

                // Create new fragment and transaction
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, allProductsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return v;
    }

    private void fetchProductsFromFirebase() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                bestSellersList.clear();

                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    BeautyProduct product = productSnapshot.getValue(BeautyProduct.class);

                    product.setProductId(productSnapshot.child("product_id").getValue(String.class));
                    product.setProductName(productSnapshot.child("p_name").getValue(String.class));
                    product.setProductBrand(productSnapshot.child("p_brand").getValue(String.class));
                    product.setProductPrice(productSnapshot.child("p_price").getValue(Double.class));
                    product.setProductCategory(productSnapshot.child("p_category").getValue(String.class));
                    product.setBestSeller(productSnapshot.child("p_best_seller").getValue(String.class));
                    product.setProductImageId(productSnapshot.child("p_image").getValue(String.class));

                    if ("yes".equals(product.getBestSeller())) {
                        bestSellersList.add(product);
                    }
                    productList.add(product);
                }
                // Update UI with the fetched data
                if (!productList.isEmpty()) {
                    displayProducts(currentProductIndex);
                }
                if (!bestSellersList.isEmpty()) {
                    displayBestSellers();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

        private void displayProducts(int index) {
            if (index >= 0 && index < productList.size()) {
                BeautyProduct product = productList.get(index);
                Glide.with(this).load(product.getProductImageId()).into(prodPictureImageView);
                prodNameTextView.setText(product.getProductName());
                prodBrandTextView.setText(product.getProductBrand());
                prodPriceTextView.setText("$" + product.getProductPrice());
                prodCategoryTextView.setText(product.getProductCategory());
            }
        }

    private void displayBestSellers() {
        if (bestSellersList.size() >= 3) {
            setBestSellerDetails(bestSellersList.get(0), bestSellerProductName1, bestSellerProductBrand1, bestSellerProductPrice1, bestSellerProductCategory1, productImage1);
            setBestSellerDetails(bestSellersList.get(1), bestSellerProductName2, bestSellerProductBrand2, bestSellerProductPrice2, bestSellerProductCategory2, productImage2);
            setBestSellerDetails(bestSellersList.get(2), bestSellerProductName3, bestSellerProductBrand3, bestSellerProductPrice3, bestSellerProductCategory3, productImage3);
        }
    }

    private void setBestSellerDetails(BeautyProduct product, TextView name, TextView brand, TextView price, TextView category, ImageView image) {
        name.setText(product.getProductName());
        brand.setText(product.getProductBrand());
        price.setText(String.valueOf(product.getProductPrice()));
        category.setText(product.getProductCategory());
        Glide.with(this).load(product.getProductImageId()).into(image);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}