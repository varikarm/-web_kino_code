package com.example.myapplication6.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication6.adapter.ListItem;

import java.util.ArrayList;
import java.util.List;

public class MyDbManager {
    private Context context;
    private  MyDBHelper myDBHelper;
    private SQLiteDatabase db;

    public MyDbManager(Context context) {
        this.context = context;
        myDBHelper = new MyDBHelper(context);
    }
    public void openDb(){
         db = myDBHelper.getWritableDatabase();
    }
    public void insertToDb(String title, String disc, String uri ){

        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE, title);
        cv.put(MyConstants.DISC, disc);
        cv.put(MyConstants.URI, uri);
        db.insert(MyConstants.TABLE_NAME, null, cv );

    }

    public void updateItem(String title, String disc, String uri, int id ){
        String selection = MyConstants._ID +  "=" + id;
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE, title);
        cv.put(MyConstants.DISC, disc);
        cv.put(MyConstants.URI, uri);
        db.update(MyConstants.TABLE_NAME, cv, selection,  null);
    }

    public void delete(int id){
        String selection = MyConstants._ID +  "=" + id;
        db.delete(MyConstants.TABLE_NAME, selection, null);
    }

    public void getFromDb(String searchText, OnDataReceived onDataReceived){
        final List<ListItem> tempList = new ArrayList<>();
        String selection = MyConstants.TITLE + " like ?";
        final Cursor cursor = db.query(MyConstants.TABLE_NAME, null, selection,
                new String[]{"%" + searchText + "%"}, null, null, null);


        while (cursor. moveToNext()) {
            ListItem item = new ListItem();
            String title = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE));
            String disc = cursor.getString(cursor.getColumnIndex(MyConstants.DISC));
            String uri = cursor.getString(cursor.getColumnIndex(MyConstants.URI));
            int _id = cursor.getInt(cursor.getColumnIndex(MyConstants. _ID));
            item.setTitle(title);
            item.setDisc(disc);
            item.setUri(uri);
            item.setId(_id);
            tempList.add(item);

        }
        cursor.close();
        onDataReceived.onReceived(tempList);

    }
    public void closeDb(){
        myDBHelper.close();
    }

}


