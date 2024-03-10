package com.example.bai07_sitemap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bai07_sitemap.database.MyDB;
import com.example.bai07_sitemap.model.SiteMap;
import com.example.bai07_sitemap.service.SiteMapServiceImpl;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private byte[] a;
    private Button btUpdateData;
    private ListView lvListSiteMap;
    private ArrayAdapter<String> adapter;
    private ArrayList<SiteMap> listSiteMap;
    private MyDB myDB;
    private boolean click = true;
    private boolean updated = false;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //===   Ánh xạ view ===
        btUpdateData = findViewById(R.id.btUpdateData);
        lvListSiteMap = findViewById(R.id.lvListSiteMap);

        progressDialog = new Dialog(this);
        myDB = new MyDB(this);
        getListSiteMap();

        btUpdateData.setOnClickListener(view -> {
            if(listSiteMap == null) {
                updateData();
            } else {
                openUpdate();
            }
        });

        lvListSiteMap.setOnItemClickListener((parent, view, position, id) -> {
            String url = parent.getItemAtPosition(position).toString().trim();
            if(click && url.startsWith("http") )
                openWebsite(url);
        });

        lvListSiteMap.setOnItemLongClickListener((parent, view, position, id) -> {
            click = false;
            if(listSiteMap!=null)
                openDialogInformation(listSiteMap.get(position));
            return false;
        });
    }

    public void getListSiteMap() {
        listSiteMap = myDB.getSiteMap();
        ArrayList<String> listUrl = new ArrayList<>();
        if (listSiteMap != null) {
            for (int i = 0; i < listSiteMap.size(); i++) {
                listUrl.add(listSiteMap.get(i).getUrl());
            }
        } else {
            listUrl.add("Danh sách rỗng");
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listUrl);
        lvListSiteMap.setAdapter(adapter);
    }

    private void openDialogProgressBar() {
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.show();
        progressDialog.setOnDismissListener(dialog -> {
            getListSiteMap();
        });
    }

    private void openDialogInformation(SiteMap siteMap) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_information);
        Window window = dialog.getWindow();
        if(window == null) {return;}
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        TextView    tvURL = dialog.findViewById(R.id.tvURL),
                    tvPrority = dialog.findViewById(R.id.tvPrority),
                    tvCFrequency = dialog.findViewById(R.id.tvCFrequency),
                    tvLChange = dialog.findViewById(R.id.tvLChange);
        ImageView   img = dialog.findViewById(R.id.img);
        tvURL.setText("Url: " + siteMap.getUrl());
        tvPrority.setText("Ưu tiên: "+siteMap.setPrority());
        tvLChange.setText("Lần cập nhật cuối: "+siteMap.getLastChange());
        tvCFrequency.setText("Tần suất cập nhật: "+siteMap.getChangeFrequency());
        Bitmap bitmap = BitmapFactory.decodeByteArray(siteMap.getImage(), 0, siteMap.getImage().length);
        img.setImageBitmap(bitmap);
        dialog.show();
        dialog.setOnDismissListener(dialog1 -> {
            click = true;
        });
    }

    public void openWebsite(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn truy cập trang web không?");

        builder.setPositiveButton("Yes", (dialog, id) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Không thể truy cập!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void openUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn cập nhật dữ liệu mới không?");

        builder.setPositiveButton("Yes", (dialog, id) -> updateData());

        builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void updateData() {
        openDialogProgressBar();
        new SiteMapServiceImpl(this,progressDialog).execute();
    }
}