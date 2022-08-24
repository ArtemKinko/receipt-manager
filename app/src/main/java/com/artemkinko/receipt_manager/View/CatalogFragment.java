package com.artemkinko.receipt_manager.View;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.artemkinko.receipt_manager.DB.GSONConverter;
import com.artemkinko.receipt_manager.DB.ReceiptDB;
import com.artemkinko.receipt_manager.Model.Catalog;
import com.artemkinko.receipt_manager.Model.Product;
import com.artemkinko.receipt_manager.Model.Receipt;
import com.artemkinko.receipt_manager.R;
import com.artemkinko.receipt_manager.databinding.FragmentCatalogBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CatalogFragment extends Fragment {

    private FragmentCatalogBinding binding;
    private boolean isDbLoaded;

    private class DBRequest extends AsyncTask<String, Void, Void> {

        private List<com.artemkinko.receipt_manager.DB.Receipt> receipts;

        @Override
        protected Void doInBackground(String... str) {

            receipts =  ReceiptDB.getInstance(getContext()).getNoteDAO().getAll();
            Catalog.getInstance().clearReceipts();
            for (com.artemkinko.receipt_manager.DB.Receipt receipt: receipts) {
                Receipt modelReceipt = new Receipt();
                modelReceipt.name = receipt.name;
                modelReceipt.sum = receipt.sum;
                modelReceipt.products = GSONConverter.getArrayProducts(receipt.products);

                String[] date = receipt.date.split("-");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int day = Integer.parseInt(date[2]);
                modelReceipt.calendar = new GregorianCalendar(year, month, day);

                Catalog.getInstance().addReceipt(modelReceipt);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            createReceipts();
            super.onPostExecute(s);
        }
    }

    private class DBDelete extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... str) {

            ReceiptDB.getInstance(getContext()).getNoteDAO().deleteByNameAndDate(str[0]);
            System.out.println(ReceiptDB.getInstance(getContext()).getNoteDAO().getAll().size());
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
        }
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        new DBRequest().execute("");
        binding = FragmentCatalogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }



    public void createReceipts() {
        binding.emptyText.setVisibility(View.VISIBLE);
        ArrayList<Receipt> receipts = Catalog.getInstance().getReceipts();
        if (receipts != null) {
            int curRow = 1;
            for (Receipt receipt: receipts) {
                binding.emptyText.setVisibility(View.INVISIBLE);
                LinearLayout myRoot = binding.cardContainer;
                LayoutInflater infl = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                Space space = new Space(getContext());
                space.setMinimumHeight(20);
                View child = infl.inflate(R.layout.receipt_card, myRoot, false);

                GridLayout gridLayout = child.findViewById(R.id.productContainer);
                String name = "";

                for (Product product: receipt.products) {
                    TextView productName = new TextView(getContext());
                    productName.setGravity(Gravity.START);
                    name = "";
                    int i = 0;
                    while (i < product.name.length()) {
                        if (i + 20 > product.name.length())
                            if (product.name.charAt(i) == ' ')
                                name += product.name.substring(i + 1, product.name.length());
                            else
                                name += product.name.substring(i, product.name.length());
                        else {
                            if (product.name.charAt(i) == ' ')
                                name += product.name.substring(i + 1, i + 20) + "\n";
                            else
                                name += product.name.substring(i, i + 20) + "\n";
                        }
                        i+=20;
                    }

                    productName.setText(name);
                    productName.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.grid_border));

                    TextView productQuantity = new TextView(getContext());
                    productQuantity.setLayoutParams(new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
                    productQuantity.setText(product.quantity.toString());
                    productQuantity.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.grid_border));

                    TextView productSum = new TextView(getContext());
                    productSum.setLayoutParams(new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
                    productSum.setText(new DecimalFormat("0.00").format(product.sum));
                    productSum.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.grid_border));

                    gridLayout.addView(productName, new GridLayout.LayoutParams(
                            GridLayout.spec(curRow, GridLayout.FILL),
                            GridLayout.spec(0, GridLayout.FILL))
                    );

                    gridLayout.addView(productQuantity, new GridLayout.LayoutParams(
                            GridLayout.spec(curRow, GridLayout.FILL),
                            GridLayout.spec(1, GridLayout.FILL))
                    );

                    gridLayout.addView(productSum, new GridLayout.LayoutParams(
                            GridLayout.spec(curRow, GridLayout.FILL),
                            GridLayout.spec(2, GridLayout.FILL))
                    );

                    curRow++;
                }

                ((TextView)(child.findViewById(R.id.sumText))).setText(new DecimalFormat("0.00").format(receipt.sum));
                ((EditText)(child.findViewById(R.id.receiptName))).setText(receipt.name);
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String simpleDateString = simpleDateFormat.format(receipt.calendar.getTime());
                ((EditText)(child.findViewById(R.id.receiptDate))).setText(simpleDateString);

                ((Button)child.findViewById(R.id.deleteReceiptButton)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Catalog.getInstance().deleteReceiptByNameDate(receipt.name, simpleDateString);
                        myRoot.removeView(child);

                        if (Catalog.getInstance().getReceipts().size() == 0) {
                            binding.emptyText.setVisibility(View.VISIBLE);
                        }
                        new DBDelete().execute(receipt.name, simpleDateString);
                    }
                });

                myRoot.addView(child);
            }
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CatalogFragment.this)
                        .navigate(R.id.action_addReceipt);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}