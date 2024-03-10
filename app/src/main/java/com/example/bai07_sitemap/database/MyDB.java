package com.example.bai07_sitemap.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import com.example.bai07_sitemap.model.SiteMap;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class MyDB extends SQLiteOpenHelper {
    private static final String nameDB = "myDB";
    private static final int version = 1;
    private SQLiteDatabase sql;
    public MyDB(Context context) {
        super(context, nameDB, null, version);
        this.sql = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS tblSiteMap(Id INTEGER PRIMARY KEY AUTOINCREMENT, URL TEXT, Images BLOB, Prority REAL, ChangeFrequency TEXT, LastChange TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertData(String url, byte[] img, Float prority, String cFrequency, String lChange) {
        String query = "INSERT INTO tblSiteMap(URL, Images, Prority, ChangeFrequency, LastChange) VALUES (?, ?, ?, ?, ?)";
        SQLiteStatement statement = sql.compileStatement(query);
        statement.bindString(1, url);
        statement.bindBlob(2, img);
        statement.bindDouble(3, prority);
        statement.bindString(4, cFrequency);
        statement.bindString(5, lChange);
        statement.executeInsert();
    }

    public void deleteData() {
        String query = "DELETE FROM tblSiteMap";
        sql.execSQL(query);
    }

    public ArrayList<SiteMap> getSiteMap() {
        Cursor c = sql.rawQuery("SELECT * FROM tblSiteMap" ,null);
        SiteMap siteMap;
        ArrayList<SiteMap> listSiteMap = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                siteMap = new SiteMap();
                siteMap.setUrl(c.getString(1));
                siteMap.setImage(c.getBlob(2));
                siteMap.setPrority(c.getFloat(3));
                siteMap.setChangeFrequency(c.getString(4));
                siteMap.setLastChange(c.getString(5));
                listSiteMap.add(siteMap);
            } while (c.moveToNext() && !c.isAfterLast());
            return listSiteMap;
        }
        return null;
    }}
