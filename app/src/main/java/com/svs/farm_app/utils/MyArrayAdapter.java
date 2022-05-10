package com.svs.farm_app.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.Nullable;

import com.svs.farm_app.entities.RegisteredFarmer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class MyArrayAdapter<T> extends ArrayAdapter {
    ArrayList<T> mList=new ArrayList<>();
    private List<T> mFilteredList = new ArrayList<>();
    private static final String TAG = "MyArrayAdapter";
    public MyArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public MyArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public MyArrayAdapter(Context context, int resource, Object[] objects) {
        super(context, resource, objects);
    }

    public MyArrayAdapter(Context context, int resource, int textViewResourceId, Object[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ArrayList<T> getOriginalList(){
        return mList;
    }

    public MyArrayAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        mList.clear();
        mList.addAll(objects);
        mFilteredList.addAll(objects);
        Log.e(TAG, "MyArrayAdapter: "+objects.toString() );
    }

    public MyArrayAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public void addAll(Collection collection) {
        super.addAll(collection);
        Log.e(TAG, "addAll: "+collection.toString() );
//        mList.clear();
        mList.addAll(collection);
        mFilteredList.addAll(collection);
    }

//    @Nullable
//    @Override
//    public Object getItem(int position) {
//        Log.e(TAG, "getItem: "+mFilteredList.get(position).toString() );
//        return mFilteredList.get(position);
//    }



    @Override
    public Filter getFilter() {
        Log.e(TAG, "getFilter: " );
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Log.e(TAG, "performFiltering: start-"+charSequence );
                ArrayList<T> filteredList = new ArrayList<>();
                if (charSequence==null||charSequence.toString().isEmpty()) {
                    mFilteredList.addAll(mList);
                    filteredList.addAll(mList);
                    Log.e(TAG, "performFiltering: ch is empty-"+mFilteredList.size() );
                } else {
                    mFilteredList.clear();
                    String charString = charSequence.toString();

//                    final ArrayList newValues = new ArrayList<>();
                    for (T listItem : mList) {
                        if (listItem instanceof RegisteredFarmer){
                                String rv = ((RegisteredFarmer) listItem).toSearchable();
                                Log.e(TAG, "performFiltering: searchable available" );
                                if (rv.toString().toLowerCase().contains(charString.toLowerCase())) {
                                    Log.e(TAG, "performFiltering-contains: "+rv.toString() );
                                    filteredList.add(listItem);
                                }else{

                                    Log.e(TAG, "performFiltering-non contain: "+listItem.toString() );
                                }

                        }else{

                            if (listItem.toString().toLowerCase().contains(charString.toLowerCase())) {
                                Log.e(TAG, "performFiltering-contains: "+listItem.toString() );
                                filteredList.add(listItem);
                            }else{

                                Log.e(TAG, "performFiltering-non contain: "+listItem.toString() );
                            }
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
                return filterResults;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                clear();
                mFilteredList = (ArrayList)  filterResults.values;
                Object[] x=new Object[mFilteredList.size()];
                for (int i = 0; i < mFilteredList.size(); i++) {
                    Object v=mFilteredList.get(i);
                    x[i]=v;
                }
                addAll(x);
//                addAll(filterResults.values);
//                Log.e(TAG, "publishResults: "+filterResults.values );
                if (filterResults.count > 0) {
                    notifyDataSetChanged();
                } else {
                    Log.e(TAG, "publishResults: count is 0 mlist:"+mList.size() );
//                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
