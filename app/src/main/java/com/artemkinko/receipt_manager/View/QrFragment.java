package com.artemkinko.receipt_manager.View;

import android.Manifest;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.artemkinko.receipt_manager.Model.Product;
import com.artemkinko.receipt_manager.Model.QrSaver;
import com.artemkinko.receipt_manager.R;
import com.artemkinko.receipt_manager.RequestTask;
import com.artemkinko.receipt_manager.databinding.FragmentAddBinding;
import com.artemkinko.receipt_manager.databinding.FragmentCatalogBinding;
import com.artemkinko.receipt_manager.databinding.FragmentQrBinding;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class QrFragment extends Fragment {

    private FragmentQrBinding binding;
    private CodeScanner mCodeScanner;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        final Activity activity = getActivity();

        int requestCode = 1;
        ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.CAMERA}, requestCode);
        View root = inflater.inflate(R.layout.fragment_qr, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "QR-код успешно отсканирован! Обработка...", Toast.LENGTH_SHORT).show();
                        try {
                            String decodedText = new RequestTask().execute(result.getText()).get();
//                            String decodedText = "{\n" +
//                                    "    \"code\": 1,\n" +
//                                    "    \"first\": 0,\n" +
//                                    "    \"data\": {\n" +
//                                    "        \"json\": {\n" +
//                                    "            \"code\": 3,\n" +
//                                    "            \"user\": \"ООО \\\"ДВ НЕВАДА\\\"\",\n" +
//                                    "            \"items\": [{\n" +
//                                    "                \"nds\": 2,\n" +
//                                    "                \"sum\": 34487,\n" +
//                                    "                \"name\": \"Филе грудки куриное Михайловский бройлер подл охл\",\n" +
//                                    "                \"price\": 35999,\n" +
//                                    "                \"ndsSum\": 3135,\n" +
//                                    "                \"quantity\": 0.958,\n" +
//                                    "                \"paymentType\": 4,\n" +
//                                    "                \"productType\": 1,\n" +
//                                    "                \"itemsQuantityMeasure\": 0\n" +
//                                    "            }, {\n" +
//                                    "                \"nds\": 2,\n" +
//                                    "                \"sum\": 10698,\n" +
//                                    "                \"name\": \"Молоко Грин Агро 2,5% 1л т\\/п ГМЗ Артем\",\n" +
//                                    "                \"price\": 10698,\n" +
//                                    "                \"ndsSum\": 973,\n" +
//                                    "                \"quantity\": 1,\n" +
//                                    "                \"paymentType\": 4,\n" +
//                                    "                \"productType\": 33,\n" +
//                                    "                \"productCodeNew\": {\n" +
//                                    "                    \"gs1m\": {\n" +
//                                    "                        \"gtin\": \"04607103820700\",\n" +
//                                    "                        \"sernum\": \"55kTHg\",\n" +
//                                    "                        \"productIdType\": 6,\n" +
//                                    "                        \"rawProductCode\": \"01046071038207002155kTHg\"\n" +
//                                    "                    }\n" +
//                                    "                },\n" +
//                                    "                \"labelCodeProcesMode\": 0,\n" +
//                                    "                \"itemsQuantityMeasure\": 0,\n" +
//                                    "                \"checkingProdInformationResult\": 15\n" +
//                                    "            }, {\n" +
//                                    "                \"nds\": 1,\n" +
//                                    "                \"sum\": 8999,\n" +
//                                    "                \"name\": \"Майонез с перепелиным яйцом 67% 380г д\\/п Махеев\",\n" +
//                                    "                \"price\": 8999,\n" +
//                                    "                \"ndsSum\": 1500,\n" +
//                                    "                \"quantity\": 1,\n" +
//                                    "                \"paymentType\": 4,\n" +
//                                    "                \"productType\": 1,\n" +
//                                    "                \"itemsQuantityMeasure\": 0\n" +
//                                    "            }, {\n" +
//                                    "                \"nds\": 2,\n" +
//                                    "                \"sum\": 3016,\n" +
//                                    "                \"name\": \"Помидор мини красный КНР, кг\",\n" +
//                                    "                \"price\": 12999,\n" +
//                                    "                \"ndsSum\": 274,\n" +
//                                    "                \"quantity\": 0.232,\n" +
//                                    "                \"paymentType\": 4,\n" +
//                                    "                \"productType\": 1,\n" +
//                                    "                \"itemsQuantityMeasure\": 0\n" +
//                                    "            }, {\n" +
//                                    "                \"nds\": 1,\n" +
//                                    "                \"sum\": 19998,\n" +
//                                    "                \"name\": \"Вафли С какао 180г Самбери\",\n" +
//                                    "                \"price\": 9999,\n" +
//                                    "                \"ndsSum\": 3333,\n" +
//                                    "                \"quantity\": 2,\n" +
//                                    "                \"paymentType\": 4,\n" +
//                                    "                \"productType\": 1,\n" +
//                                    "                \"itemsQuantityMeasure\": 0\n" +
//                                    "            }, {\n" +
//                                    "                \"nds\": 2,\n" +
//                                    "                \"sum\": 4290,\n" +
//                                    "                \"name\": \"Хлеб Заливной Зерновой 400 г\",\n" +
//                                    "                \"price\": 10725,\n" +
//                                    "                \"ndsSum\": 390,\n" +
//                                    "                \"quantity\": 0.4,\n" +
//                                    "                \"paymentType\": 4,\n" +
//                                    "                \"productType\": 1,\n" +
//                                    "                \"itemsQuantityMeasure\": 0\n" +
//                                    "            }, {\n" +
//                                    "                \"nds\": 2,\n" +
//                                    "                \"sum\": 6998,\n" +
//                                    "                \"name\": \"Напиток Моя Семья 0,95л ягода-сочнягода т\\/п\",\n" +
//                                    "                \"price\": 6998,\n" +
//                                    "                \"ndsSum\": 636,\n" +
//                                    "                \"quantity\": 1,\n" +
//                                    "                \"paymentType\": 4,\n" +
//                                    "                \"productType\": 1,\n" +
//                                    "                \"itemsQuantityMeasure\": 0\n" +
//                                    "            }, {\n" +
//                                    "                \"nds\": 1,\n" +
//                                    "                \"sum\": 3996,\n" +
//                                    "                \"name\": \"Вода негазированная 0,5л пэт\",\n" +
//                                    "                \"price\": 1998,\n" +
//                                    "                \"ndsSum\": 666,\n" +
//                                    "                \"quantity\": 2,\n" +
//                                    "                \"paymentType\": 4,\n" +
//                                    "                \"productType\": 1,\n" +
//                                    "                \"itemsQuantityMeasure\": 0\n" +
//                                    "            }, {\n" +
//                                    "                \"nds\": 1,\n" +
//                                    "                \"sum\": 3999,\n" +
//                                    "                \"name\": \"Резинка жеват Дирол Мята 13,6г\",\n" +
//                                    "                \"price\": 3999,\n" +
//                                    "                \"ndsSum\": 667,\n" +
//                                    "                \"quantity\": 1,\n" +
//                                    "                \"paymentType\": 4,\n" +
//                                    "                \"productType\": 1,\n" +
//                                    "                \"itemsQuantityMeasure\": 0\n" +
//                                    "            }, {\n" +
//                                    "                \"nds\": 1,\n" +
//                                    "                \"sum\": 7495,\n" +
//                                    "                \"name\": \"Кофе MacCoffee Cappuccino Di Torino корица 25,5г\",\n" +
//                                    "                \"price\": 1499,\n" +
//                                    "                \"ndsSum\": 1249,\n" +
//                                    "                \"quantity\": 5,\n" +
//                                    "                \"paymentType\": 4,\n" +
//                                    "                \"productType\": 1,\n" +
//                                    "                \"itemsQuantityMeasure\": 0\n" +
//                                    "            }],\n" +
//                                    "            \"nds10\": 5408,\n" +
//                                    "            \"nds18\": 7415,\n" +
//                                    "            \"fnsUrl\": \"www.nalog.ru\",\n" +
//                                    "            \"region\": \"25\",\n" +
//                                    "            \"userInn\": \"2723205733  \",\n" +
//                                    "            \"dateTime\": \"2022-08-21T19:45:00\",\n" +
//                                    "            \"kktRegId\": \"0005744605027747    \",\n" +
//                                    "            \"metadata\": {\n" +
//                                    "                \"id\": 4380115252201735169,\n" +
//                                    "                \"ofdId\": \"ofd1\",\n" +
//                                    "                \"address\": \"690065,Россия,Приморский край,Владивостокский г.о.,,Владивосток г,,Крыгина ул,,д. 23,,,,\",\n" +
//                                    "                \"subtype\": \"receipt\",\n" +
//                                    "                \"receiveDate\": \"2022-08-21T09:46:52Z\"\n" +
//                                    "            },\n" +
//                                    "            \"operator\": \"Мелиева\",\n" +
//                                    "            \"totalSum\": 103976,\n" +
//                                    "            \"creditSum\": 0,\n" +
//                                    "            \"numberKkt\": \"0128011997\",\n" +
//                                    "            \"fiscalSign\": 48207649,\n" +
//                                    "            \"prepaidSum\": 0,\n" +
//                                    "            \"retailPlace\": \"Самбери-15\",\n" +
//                                    "            \"shiftNumber\": 363,\n" +
//                                    "            \"cashTotalSum\": 0,\n" +
//                                    "            \"provisionSum\": 0,\n" +
//                                    "            \"ecashTotalSum\": 103976,\n" +
//                                    "            \"operationType\": 1,\n" +
//                                    "            \"redefine_mask\": 0,\n" +
//                                    "            \"requestNumber\": 452,\n" +
//                                    "            \"fiscalDriveNumber\": \"9960440300337839\",\n" +
//                                    "            \"messageFiscalSign\": 9.29736047707122e+18,\n" +
//                                    "            \"retailPlaceAddress\": \"690065, г.Владивосток, ул.Крыгина, 23\",\n" +
//                                    "            \"appliedTaxationType\": 1,\n" +
//                                    "            \"fiscalDocumentNumber\": 95345,\n" +
//                                    "            \"fiscalDocumentFormatVer\": 4,\n" +
//                                    "            \"checkingLabeledProdResult\": 0\n" +
//                                    "        },\n" +
//                                    "        \"html\": \"<table class=\\\"b-check_table table\\\"><tbody><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">ООО \\\"ДВ НЕВАДА\\\"<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">690065, г.Владивосток, ул.Крыгина, 23<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">ИНН 2723205733  <\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">&nbsp;<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">21.08.2022 19:45<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">Чек № 452<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">Смена № 363<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle b-check_center\\\"><td colspan=\\\"5\\\">Кассир Мелиева<\\/td><\\/tr><tr class=\\\"b-check_vblock-last b-check_center\\\"><td colspan=\\\"5\\\">Приход<\\/td><\\/tr><tr><td><strong>№<\\/strong><\\/td><td><strong>Название<\\/strong><\\/td><td><strong>Цена<\\/strong><\\/td><td><strong>Кол.<\\/strong><\\/td><td><strong>Сумма<\\/strong><\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>1<\\/td><td>Филе грудки куриное Михайловский бройлер подл охл<\\/td><td>359.99<\\/td><td>0.958<\\/td><td>344.87<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>2<\\/td><td>Молоко Грин Агро 2,5% 1л т\\/п ГМЗ Артем<\\/td><td>106.98<\\/td><td>1<\\/td><td>106.98<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>3<\\/td><td>Майонез с перепелиным яйцом 67% 380г д\\/п Махеев<\\/td><td>89.99<\\/td><td>1<\\/td><td>89.99<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>4<\\/td><td>Помидор мини красный КНР, кг<\\/td><td>129.99<\\/td><td>0.232<\\/td><td>30.16<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>5<\\/td><td>Вафли С какао 180г Самбери<\\/td><td>99.99<\\/td><td>2<\\/td><td>199.98<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>6<\\/td><td>Хлеб Заливной Зерновой 400 г<\\/td><td>107.25<\\/td><td>0.4<\\/td><td>42.90<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>7<\\/td><td>Напиток Моя Семья 0,95л ягода-сочнягода т\\/п<\\/td><td>69.98<\\/td><td>1<\\/td><td>69.98<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>8<\\/td><td>Вода негазированная 0,5л пэт<\\/td><td>19.98<\\/td><td>2<\\/td><td>39.96<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>9<\\/td><td>Резинка жеват Дирол Мята 13,6г<\\/td><td>39.99<\\/td><td>1<\\/td><td>39.99<\\/td><\\/tr><tr class=\\\"b-check_item\\\"><td>10<\\/td><td>Кофе MacCoffee Cappuccino Di Torino корица 25,5г<\\/td><td>14.99<\\/td><td>5<\\/td><td>74.95<\\/td><\\/tr><tr class=\\\"b-check_vblock-first\\\"><td colspan=\\\"3\\\" class=\\\"b-check_itogo\\\">ИТОГО:<\\/td><td><\\/td><td class=\\\"b-check_itogo\\\">1039.76<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"3\\\">Наличные<\\/td><td><\\/td><td>0.00<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"3\\\">Карта<\\/td><td><\\/td><td>1039.76<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"3\\\">НДС итога чека со ставкой 20%<\\/td><td><\\/td><td>74.15<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"3\\\">НДС итога чека со ставкой 10%<\\/td><td><\\/td><td>54.08<\\/td><\\/tr><tr class=\\\"b-check_vblock-last\\\"><td colspan=\\\"5\\\">ВИД НАЛОГООБЛОЖЕНИЯ: ОСН<\\/td><\\/tr><tr class=\\\"b-check_vblock-first\\\"><td colspan=\\\"5\\\">РЕГ.НОМЕР ККТ: 0005744605027747    <\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"5\\\">ЗАВОД. №: <\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"5\\\">ФН: 9960440300337839<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"5\\\">ФД: 95345<\\/td><\\/tr><tr class=\\\"b-check_vblock-middle\\\"><td colspan=\\\"5\\\">ФПД#: 48207649<\\/td><\\/tr><tr class=\\\"b-check_vblock-last\\\"><td colspan=\\\"5\\\" class=\\\"b-check_center\\\"><img src=\\\"\\/qrcode\\/generate?text=t%3D20220821T1945%26s%3D1039.76%26fn%3D9960440300337839%26i%3D95345%26fp%3D48207649%26n%3D1\\\" alt=\\\"QR код чека\\\" width=\\\"35%\\\" loading=\\\"lazy\\\" decoding=\\\"async\\\"><\\/td><\\/tr><\\/tbody><\\/table>\"\n" +
//                                    "    }\n" +
//                                    "}";
                            JSONObject jsonDecoded = new JSONObject(decodedText);

                            JSONObject data = jsonDecoded.getJSONObject("data");
                            JSONObject jsondata = data.getJSONObject("json");

                            JSONArray items = jsondata.getJSONArray("items");

                            String dateTime = jsondata.getString("dateTime");
                            dateTime = dateTime.substring(0, 10);
                            QrSaver.getInstance().setDecodedDate(dateTime);

                            for (int i = 0; i < items.length(); i++) {
                                JSONObject product = items.getJSONObject(i);

                                String stringProduct = "";
                                Product productArray = new Product();
                                productArray.name = product.getString("name");
                                productArray.price = ((double)product.getInt("price") / (double)100);
                                productArray.quantity = product.getDouble("quantity");
                                productArray.sum = (double)product.getInt("sum") / (double)100;

                                stringProduct += "Название: " + product.getString("name") + "\n" +
                                        "Цена: " + ((double)product.getInt("price") / (double)100) + "\n" +
                                        "Количество: " + product.getDouble("quantity") + "\n" +
                                        "Стоимость: " + (double)product.getInt("sum") / (double)100 + "\n\n";
                                QrSaver.getInstance().setDecodedText(QrSaver.getInstance().getDecodedtext() + stringProduct);
                                QrSaver.getInstance().addProduct(productArray);

                                System.out.println("Название: " + product.getString("name"));
                                System.out.println("Цена: " + (double)product.getInt("price") / (double)100);
                                System.out.println("Количество: " + product.getDouble("quantity"));
                                System.out.println("Стоимость: " + (double)product.getInt("sum") / (double)100);
                            }


                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                        getActivity().onBackPressed();
                    }
                });

            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}