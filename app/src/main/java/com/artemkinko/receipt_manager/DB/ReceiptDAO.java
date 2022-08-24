package com.artemkinko.receipt_manager.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReceiptDAO {
    @Query("SELECT * FROM receipt")
    List<Receipt> getAll();

    @Query("DELETE FROM receipt")
    Void deleteAll();

    @Insert
    void insertAll(Receipt... receipts);

    @Query("DELETE FROM receipt WHERE name == :nm")
    Void deleteByNameAndDate(String nm);
}
