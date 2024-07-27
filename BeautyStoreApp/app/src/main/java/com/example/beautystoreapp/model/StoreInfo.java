package com.example.beautystoreapp.model;

public class StoreInfo {

    private String store_email;
    private String store_phoneNumber;
    private String store_address;

    public StoreInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

    public StoreInfo(String store_email, String store_phoneNumber, String store_address) {
        this.store_email = store_email;
        this.store_phoneNumber = store_phoneNumber;
        this.store_address = store_address;
    }


    public String getStore_email() {
        return store_email;
    }

    public void setStore_email(String store_email) {
        this.store_email = store_email;
    }

    public String getStore_phoneNumber() {
        return store_phoneNumber;
    }

    public void setStore_phoneNumber(String store_phoneNumber) {
        this.store_phoneNumber = store_phoneNumber;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    @Override
    public String toString() {
        return "StoreInformation{" +
                "store_email='" + store_email + '\'' +
                ", store_phoneNumber='" + store_phoneNumber + '\'' +
                ", store_address='" + store_address + '\'' +
                '}';
    }
}
