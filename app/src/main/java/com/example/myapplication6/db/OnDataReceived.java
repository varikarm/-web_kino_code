package com.example.myapplication6.db;

import com.example.myapplication6.adapter.ListItem;

import java.util.List;

public interface OnDataReceived {
    void onReceived(List<ListItem> list);
}
