package com.artemkinko.receipt_manager.Model;

import java.util.ArrayList;

public class QrSaver {

    private static QrSaver instance;
    private QrSaver() {

    }

    private ArrayList<Product> products;

    public static QrSaver getInstance() {
        if (instance == null) {
            instance = new QrSaver();
        }
        return instance;
    }

    private String decodedText;
    private String decodedDate;

    public void setDecodedDate(String date) {
        decodedDate = date;
    }

    public String getDecodedDate() {
        return decodedDate;
    }


    public void addProduct(Product product) {
        if (products == null)
            products = new ArrayList<>();
        products.add(product);
    }

    public void clearProducts() {
        if (products != null) products.clear();
    }
    public void clearDecodedDate() {decodedDate = "";}

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setDecodedText(String text) {decodedText = text;}
    public String getDecodedtext() {return decodedText;}
}
