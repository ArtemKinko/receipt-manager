package com.artemkinko.receipt_manager;

import android.os.AsyncTask;
import android.widget.Toast;

import com.artemkinko.receipt_manager.Model.QrSaver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class RequestTask extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        try {
//            URL weatherEndpoint = new URL(strings[0]);
//            HttpsURLConnection connection = (HttpsURLConnection) weatherEndpoint.openConnection();
//            InputStream responseBody = connection.getInputStream();
//            Scanner s = new Scanner(responseBody).useDelimiter("\\A");
//            result = s.hasNext() ? s.next() : "";
            URL url = new URL("https://proverkacheka.com/api/v1/check/get");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String data = strings[0] + "%22&token=15508.yN7gMkqJu4EOpwyEy";

            byte[] out = data.getBytes(StandardCharsets.UTF_8);

            OutputStream stream = http.getOutputStream();
            stream.write(out);

            System.out.println(http.getResponseCode() + " " + http.getResponseMessage());

            InputStream responseBody = http.getInputStream();
            Scanner s = new Scanner(responseBody).useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";
            http.disconnect();
        } catch (Exception ex) {

        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}