package com.artemkinko.receipt_manager.View;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.artemkinko.receipt_manager.DB.GSONConverter;
import com.artemkinko.receipt_manager.DB.ReceiptDB;
import com.artemkinko.receipt_manager.Model.Catalog;
import com.artemkinko.receipt_manager.Model.Product;
import com.artemkinko.receipt_manager.Model.QrSaver;
import com.artemkinko.receipt_manager.Model.Receipt;
import com.artemkinko.receipt_manager.R;
import com.artemkinko.receipt_manager.databinding.FragmentAddBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.List;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private int totalProductId;

    private class DBTask extends AsyncTask<com.artemkinko.receipt_manager.DB.Receipt, com.artemkinko.receipt_manager.DB.Receipt, com.artemkinko.receipt_manager.DB.Receipt> {
        @Override
        protected com.artemkinko.receipt_manager.DB.Receipt doInBackground(com.artemkinko.receipt_manager.DB.Receipt... receipts) {
            ReceiptDB.getInstance(getContext()).getNoteDAO().insertAll(receipts[0]);

            List<com.artemkinko.receipt_manager.DB.Receipt> arrayReceipts = ReceiptDB.getInstance(getContext()).getNoteDAO().getAll();
            return receipts[0];
        }

        @Override
        protected void onPostExecute(com.artemkinko.receipt_manager.DB.Receipt s) {
            super.onPostExecute(s);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        totalProductId = 0;
        binding = FragmentAddBinding.inflate(inflater, container, false);
//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fabQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddFragment.this)
                        .navigate(R.id.action_qrScan);
            }
        });


        ArrayList<Product> productsFromQr = QrSaver.getInstance().getProducts();
        if (productsFromQr != null) {
            for (Product product : productsFromQr) {
                LinearLayout myRoot = binding.productItems;
                LayoutInflater infl = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                Space space = new Space(getContext());
                space.setMinimumHeight(20);
                View child = infl.inflate(R.layout.product_card, binding.productItems, false);
                child.setId(totalProductId);

                ((EditText) child.findViewById(R.id.product_name)).setText(product.name);
                ((EditText) child.findViewById(R.id.product_quantity)).setText(product.quantity.toString());
                ((EditText) child.findViewById(R.id.product_price)).setText(product.price.toString());
                ((EditText) child.findViewById(R.id.product_sum)).setText(product.sum.toString());
                Button button = (Button) child.findViewById(R.id.cardRemoveButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myRoot.removeView(child);
                        myRoot.removeView(space);
                    }
                });
                myRoot.addView(child);
                myRoot.addView(space);
                totalProductId++;
            }
            QrSaver.getInstance().clearProducts();
        }

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout myRoot = binding.productItems;
                LayoutInflater infl = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                Space space = new Space(getContext());
                space.setMinimumHeight(20);
                for (int i = 0; i < 1; i++) {
                    View child = infl.inflate(R.layout.product_card, binding.productItems, false);
                    child.setId(totalProductId);
                    Button button = (Button) child.findViewById(R.id.cardRemoveButton);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myRoot.removeView(child);
                            myRoot.removeView(space);
                        }
                    });
                    myRoot.addView(child);
                    myRoot.addView(space);
                    totalProductId++;
                }
            }
        });

        binding.datePickText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        binding.datePickText.setText(year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0": "") + day);
                   }
               };


               DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), 0, dateSetListener, LocalDate.now().getYear(), LocalDate.now().getMonth().getValue() - 1, LocalDate.now().getDayOfMonth());
               datePickerDialog.show();
           }
        });

        binding.fabConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.receiptNameText.getText().toString().equals("")) {
                    Snackbar.make(view, "Перед сохранением введите название чека", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                    return;
                }
                if (binding.datePickText.getText().toString().equals("")) {
                    Snackbar.make(view, "Перед сохранением введите дату совершения покупки", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                Receipt receipt = new Receipt();
                receipt.name = binding.receiptNameText.getText().toString();
                String[] date = binding.datePickText.getText().toString().split("-");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int day = Integer.parseInt(date[2]);
                receipt.calendar = new GregorianCalendar(year, month, day);
                receipt.products = new ArrayList<>();
                receipt.sum = 0.0;

                for (int i = 0; i < binding.productItems.getChildCount() - 1; i+=2) {
                    View child = binding.productItems.getChildAt(i);
                    Product product = new Product();
                    if (((EditText)(child.findViewById(R.id.product_name))).getText().toString().equals("")) {
                        Snackbar.make(view, "Не заполнено поле названия продукта", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }

                    if (((EditText)(child.findViewById(R.id.product_price))).getText().toString().equals("")) {
                        Snackbar.make(view, "Не заполнено поле цены продукта", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }

                    if (((EditText)(child.findViewById(R.id.product_quantity))).getText().toString().equals("")) {
                        Snackbar.make(view, "Не заполнено поле количества продукта", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }

                    if (((EditText)(child.findViewById(R.id.product_sum))).getText().toString().equals("")) {
                        Snackbar.make(view, "Не заполнено поле стоимости продукта", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }

                    product.name = ((EditText)(child.findViewById(R.id.product_name))).getText().toString();
                    product.price = Double.parseDouble(((EditText)(child.findViewById(R.id.product_price))).getText().toString());
                    product.quantity = Double.parseDouble(((EditText)(child.findViewById(R.id.product_quantity))).getText().toString());
                    product.sum = Double.parseDouble(((EditText)(child.findViewById(R.id.product_sum))).getText().toString());

                    receipt.sum += product.sum;

                    receipt.products.add(product);
                }

                Catalog.getInstance().addReceipt(receipt);

                com.artemkinko.receipt_manager.DB.Receipt dbReceipt = new com.artemkinko.receipt_manager.DB.Receipt();
                dbReceipt.name = receipt.name;
                dbReceipt.date = binding.datePickText.getText().toString();
                dbReceipt.sum = receipt.sum;
                dbReceipt.products = GSONConverter.getStringProducts(receipt);


                new AddFragment.DBTask().execute(dbReceipt);


                QrSaver.getInstance().clearProducts();
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String date = QrSaver.getInstance().getDecodedDate();
        binding.datePickText.setText(date);
        QrSaver.getInstance().clearDecodedDate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}