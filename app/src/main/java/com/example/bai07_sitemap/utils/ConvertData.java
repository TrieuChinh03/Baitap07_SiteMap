package com.example.bai07_sitemap.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertData {
    public static String convertTimeFormat(String inputTimeStr) {
        String inputFormat = "yyyy-MM-dd'T'HH:mm:ssXXX";
        String outputFormat = "yyyy-MM-dd HH:mm:ss";

        try {
            SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
            SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);

            //===   Chuyển đổi chuỗi ngày tháng từ định dạng ban đầu sang Date  ===
            Date inputDate = inputFormatter.parse(inputTimeStr);

            //===   Format lại Date theo định dạng mới  ===
            String outputDateStr = outputFormatter.format(inputDate);

            return outputDateStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static float convertPrority(String prorityStr) {
        return Float.parseFloat(prorityStr);
    }

    public static byte[] convertImg(String urlStr) {
        try {
            URL imageUrl = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            if(bitmap!=null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                return byteArray;
            } else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
