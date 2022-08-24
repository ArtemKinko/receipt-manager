package com.artemkinko.receipt_manager.DB;


import com.artemkinko.receipt_manager.Model.Product;
import com.artemkinko.receipt_manager.Model.Receipt;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class GSONConverter {

    private static GSONConverter instance;

    private GSONConverter() {}

    public GSONConverter getInstance() {
        if (instance == null)
            instance = new GSONConverter();
        return instance;
    }

    public static String getStringProducts (Receipt modelReceipt) {
        ArrayList<Product> products = modelReceipt.products;
        if (products != null) {
            return new Gson().toJson(products);
        }
        return "";
    }

    public static ArrayList<Product> getArrayProducts (String jsonProducts) {
        return new Gson().fromJson(jsonProducts, new TypeToken<ArrayList<Product>>(){}.getType());
    }
}
