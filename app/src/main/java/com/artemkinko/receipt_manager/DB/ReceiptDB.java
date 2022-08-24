package com.artemkinko.receipt_manager.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Receipt.class}, version = 1)
public abstract class ReceiptDB extends RoomDatabase {

    private static ReceiptDB receiptDB;

    public static ReceiptDB getInstance(Context context) {
        if (receiptDB == null) {
            receiptDB = Room.databaseBuilder(context.getApplicationContext(), ReceiptDB.class, "noteDB").build();
        }
        return receiptDB;
    }

    private ReceiptDAO receiptDAO;

    public abstract ReceiptDAO getNoteDAO();
}
