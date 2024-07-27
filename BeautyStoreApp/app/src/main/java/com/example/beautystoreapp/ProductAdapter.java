package com.example.beautystoreapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystoreapp.model.BeautyProduct;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<BeautyProduct> productList;
    public ProductAdapter(List<BeautyProduct> productList) {

        this.productList = productList;
    }

    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    public void onBindViewHolder(ProductViewHolder holder, int position) {

        BeautyProduct product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productName, productBrand, productPrice, productCategory;

        public ProductViewHolder(View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.prd_image_image_view);
            productName = itemView.findViewById(R.id.prd_name_text_view);
            productBrand = itemView.findViewById(R.id.prd_brand_text_view);
            productPrice = itemView.findViewById(R.id.prd_price_text_view);
            productCategory = itemView.findViewById(R.id.prd_category_text_view);
        }

        public void bind(BeautyProduct product) {
            productName.setText(product.getProductName());
            productBrand.setText(product.getProductBrand());
            productPrice.setText("$" + product.getProductPrice());
            productCategory.setText(product.getProductCategory());
            Glide.with(itemView.getContext()).load(product.getProductImageId()).into(productImage);

        }

    }
}
