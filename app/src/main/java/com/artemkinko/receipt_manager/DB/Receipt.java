package com.artemkinko.receipt_manager.DB;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.artemkinko.receipt_manager.Model.Product;

import java.util.ArrayList;
import java.util.Calendar;

@Entity
public class Receipt {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "sum")
    public Double sum;

    @ColumnInfo(name = "products")
    public String products;
}
