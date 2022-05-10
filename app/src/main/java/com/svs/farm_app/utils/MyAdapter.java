package com.svs.farm_app.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import com.svs.farm_app.main.FarmSelectionActitivty;

import java.util.Comparator;
import java.util.List;

public class MyAdapter<MyData> extends ArrayAdapter {
    private static final String TAG = "MyAdapter";
    Context c;
    public MyAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        c=context;
        Log.e(TAG, "MyAdapter: " );
    }

    public MyAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }


    public MyAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    public MyAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Log.e(TAG, "getFilter: " );
        return super.getFilter();
    }

    @Override
    public void sort(@NonNull Comparator comparator) {
        Log.e(TAG, "sort: "+comparator.toString() );
        super.sort(comparator);
    }
}
