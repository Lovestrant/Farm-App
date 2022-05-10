package com.svs.farm_app.main.recapture_details;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.authentication.activity.FP05FingerprintCaptureActivity;
import com.authentication.activity.FingerprintActivityLeft;
import com.authentication.activity.TPS350FingerprintCaptureActivity;
import com.authentication.activity.TPS900FingerprintCaptureActivity;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.Village;
import com.svs.farm_app.entities.WackFarmer;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReRegistrationActivity extends BaseClass {
    private DatabaseHandler db;
    private ReRegArray syncedArray;
    List<ReRegArray> list;
    private CustomListAdapter adapter;
    private String mCurrentPhotoPath;
    private File image;
    private static final int REQUEST_TAKE_PHOTO = 1;
    @BindView(R.id.list)
    ListView listView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synced_data);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = getApplicationContext();

        db = new DatabaseHandler(mContext);
        list = new ArrayList<>();
        CreateList();

        adapter = new CustomListAdapter(this, list);
        (listView).setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                captureImageReRegister();
                MyPrefrences.savePrefrence(mContext, "wack_fid", String.valueOf(adapter.getItem(pos).getFarmerID()));
                MyPrefrences.savePrefrence(mContext, "wack_gen_id", String.valueOf(adapter.getItem(pos).getGenID()));
            }
        });
    }


    private void captureImageReRegister() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFileReReg();
            } catch (IOException ex) {
                Log.e(TAG, ex.getMessage());
            }
            if (photoFile != null) {

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            } else {
                Log.e(TAG, "path is null");
            }
        }
    }


    @SuppressLint("NewApi")
    private File createImageFileReReg() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Config.IMAGE_DIRECTORY_NAME2);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        MyPrefrences.savePrefrence(mContext, Config.NEW_FARMER_PICTURE, image.getAbsolutePath());
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Log.e("Image Path: ", MyPrefrences.getPrefrence(mContext, Config.NEW_FARMER_PICTURE));

                image = new File(MyPrefrences.getPrefrence(mContext, Config.NEW_FARMER_PICTURE));

                if (image.exists()) {
                    if (Preferences.DEVICE_MODEL.equals(Config.TPS350)) {

                        Intent intent = new Intent(this, TPS350FingerprintCaptureActivity.class);
                        intent.putExtra(Config.WHICH_THUMB, Config.NEW_LEFT_THUMB).putExtra(Config.TO_ACTIVITY,Config.RECAPTURE_FARMER_DETAILS);
                        startActivity(intent);

                    }else if (Preferences.DEVICE_MODEL.equals(Config.TPS900)) {

                        Intent intent = new Intent(this, TPS900FingerprintCaptureActivity.class);
                        intent.putExtra(Config.WHICH_THUMB, Config.NEW_LEFT_THUMB).putExtra(Config.TO_ACTIVITY,Config.RECAPTURE_FARMER_DETAILS);
                        startActivity(intent);

                    } else if (Preferences.DEVICE_MODEL.equals(Config.COREWISE_V0)) {

                        Intent intent = new Intent(this, FingerprintActivityLeft.class);
                        intent.putExtra(Config.TO_ACTIVITY, Config.RECAPTURE_FARMER_DETAILS);
                        startActivity(intent);
                    }else if (Preferences.DEVICE_MODEL.equals(Config.FP05)) {

                        Intent intent = new Intent(this, FP05FingerprintCaptureActivity.class);
                        intent.putExtra(Config.WHICH_THUMB, Config.NEW_LEFT_THUMB).putExtra(Config.TO_ACTIVITY,Config.RECAPTURE_FARMER_DETAILS);
                        startActivity(intent);

                    }

                    Log.e(TAG, MyPrefrences.getPrefrence(mContext, Config.NEW_FARMER_PICTURE));

                } else {
                    captureImageReRegister();
                }
            }
        }
    }

    public void CreateList() {
        DatabaseHandler db = new DatabaseHandler(mContext);
        List<Village> villageList = db.getVillages();
        List<WackFarmer> farmersList = db.getWackFarmers();
        Log.i("List Length: ", "" + farmersList.size());
        for (WackFarmer cn : farmersList) {
            for (Village v : villageList) {
                if (cn.getVillageId().equals(v.getVillageID())) {
                    CreateItem(cn.getGenId(), cn.getFarmerId(), v.getVillageName(), cn.getFirstName(), cn.getLastName());
                }
            }
        }


    }

    public void CreateItem(String genID, String farmerID, String villageName, String fName, String lName) {
        Log.e("Data", genID + " " + farmerID);
        syncedArray = new ReRegArray();
        syncedArray.setGenID(genID);
        syncedArray.setFarmerID(farmerID);
        syncedArray.setVillageName(villageName);
        syncedArray.setFName(fName);
        syncedArray.setLName(lName);
        list.add(syncedArray);
    }

    private class ReRegArray {
        String Name;
        String Count;
        private String lastName;
        private String firstName;
        private String villageName;

        public String getGenID() {
            return Name;
        }

        public void setGenID(String name) {
            Name = name;
        }

        public String getFarmerID() {
            return Count;
        }

        public void setFarmerID(String count) {
            Count = count;
        }

        public String getLName() {
            return this.lastName;
        }

        public void setLName(String lName) {
            this.lastName = lName;
        }

        public String getFName() {
            return this.firstName;
        }

        public void setFName(String fName) {
            this.firstName = fName;
        }

        public String getVillageName() {
            return villageName;
        }

        public void setVillageName(String villageName) {
            this.villageName = villageName;
        }


    }

    public class CustomListAdapter extends ArrayAdapter<ReRegArray> {

        private final Activity context;
        private final List<ReRegArray> items;

        public CustomListAdapter(Activity context, List<ReRegArray> items) {
            super(context, R.layout.re_registration_list_item, items);
            this.context = context;
            this.items = items;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.re_registration_list_item, null,
                    true);

            TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
            TextView tvFarmerName = (TextView) rowView.findViewById(R.id.tvFarmerName);
            TextView tvVillageName = (TextView) rowView.findViewById(R.id.tvVillageName);
            TextView tvCount = (TextView) rowView.findViewById(R.id.tvCount);

            tvName.setText(items.get(position).getGenID());
            tvFarmerName.setText(items.get(position).getFName() + " " + items.get(position).getLName());
            tvVillageName.setText("Village: " + items.get(position).getVillageName());
            tvCount.setText(items.get(position).getFarmerID() + "");
            return rowView;

        }

    }
}
