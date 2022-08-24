package com.artemkinko.receipt_manager.Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Receipt {

    public String name;
    public Calendar calendar;
    public ArrayList<Product> products;
    public Double sum;
}
