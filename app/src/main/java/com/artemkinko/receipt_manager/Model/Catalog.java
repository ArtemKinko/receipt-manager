package com.artemkinko.receipt_manager.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Catalog {

    private static Catalog instance;
    private Catalog() {

    }

    private ArrayList<Receipt> receipts;

    public static Catalog getInstance() {
        if (instance == null) {
            instance = new Catalog();
        }
        return instance;
    }

    public void addReceipt(Receipt receipt) {
        if (receipts == null)
            receipts = new ArrayList<>();
        receipts.add(receipt);
    }

    public ArrayList<Receipt> getReceipts() {
        return receipts;
    }
    public void deleteReceiptByNameDate(String name, String date) {
        for (Receipt receipt: receipts) {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String recDate = simpleDateFormat.format(receipt.calendar.getTime());
            if (recDate.equals(date) && name.equals(receipt.name)) {
                receipts.remove(receipt);
                break;
            }
        }
    }

    public void clearReceipts() {if (receipts != null) receipts.clear(); else receipts = new ArrayList<>();}
}
