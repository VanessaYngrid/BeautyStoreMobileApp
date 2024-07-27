package com.example.beautystoreapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BeautyProduct implements Parcelable {

    private String productId;
    private String productName;
    private String productBrand;
    private double productPrice;
    private String productCategory;
    private String bestSeller;
    private String productImageId;

    public BeautyProduct() {
        super();
        // TODO Auto-generated constructor stub
    }

    public BeautyProduct(String productId, String productName, String productBrand, double productPrice, String productCategory, String bestSeller, String productImageId) {
        this.productId = productId;
        this.productName = productName;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.bestSeller = bestSeller;
        this.productImageId = productImageId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(String bestSeller) {
        this.bestSeller = bestSeller;
    }

    public String getProductImageId() {
        return productImageId;
    }

    public void setProductImageId(String productImageId) {
        this.productImageId = productImageId;
    }

    @Override
    public String toString() {
        return "BeautyProduct{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productBrand='" + productBrand + '\'' +
                ", productPrice=" + productPrice +
                ", productCategory='" + productCategory + '\'' +
                ", bestSeller='" + bestSeller + '\'' +
                ", productImageId=" + productImageId +
                '}';
    }

    protected BeautyProduct(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        productBrand = in.readString();
        productPrice = in.readDouble();
        productCategory = in.readString();
        bestSeller = in.readString();
        productImageId = in.readString();
    }

    public static final Parcelable.Creator<BeautyProduct> CREATOR = new Parcelable.Creator<BeautyProduct>() {
        @Override
        public BeautyProduct createFromParcel(Parcel in) {
            return new BeautyProduct(in);
        }

        @Override
        public BeautyProduct[] newArray(int size) {
            return new BeautyProduct[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(productBrand);
        dest.writeDouble(productPrice);
        dest.writeString(productCategory);
        dest.writeString(bestSeller);
        dest.writeString(productImageId);
    }
}
