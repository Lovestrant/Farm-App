package com.svs.farm_app.main.registration;

/**
 * Created by user on 12/27/2014.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.Farmers;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.AndroidMultiPartEntity;
import com.svs.farm_app.utils.AndroidMultiPartEntity.ProgressListener;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.GPSTracker;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadActivity extends BaseClass {
    private static final String TAG = UploadActivity.class.getSimpleName();

    long totalSize = 0;
    String fname, lname, idNo, phone,  email, villageName,
            subVillageName, postalAddress, gender,  estimateFarmAreaOne;

    private String farmerPic;
    private double lat, longt;
    private GPSTracker gpsTracker;
    private String latitude, longitude;
    private String villageID;
    private String subVillageID;
    private String estimateFarmAreaTwo;
    private String estimateFarmAreaThree;
    private String estimateFarmAreaFour;
    private String showIntent;
    private String farmVillageIDOne, farmVillageIDTwo, farmVillageIDThree, farmVillageIDFour;
    private String otherCropsNameOne;
    private String otherCropsIDOne;
    private String otherCropsNameTwo;
    private String otherCropsIDTwo;
    private String otherCropsNameThree;
    private String otherCropsIDThree;
    private boolean isUploading;

    @BindView(R.id.tPercentage)
    TextView tPercentage;
    @BindView(R.id.btnUpload)
    Button btnUpload;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.imgPreview)
    ImageView imgPreview;
    @BindView(R.id.tFName)
    TextView tFName;
    @BindView(R.id.tLName)
    TextView tLName;
    @BindView(R.id.tGender)
    TextView tGender;
    @BindView(R.id.tID)
    TextView tID;
    @BindView(R.id.tPhone)
    TextView tPhone;
    @BindView(R.id.tEmail)
    TextView tEmail;
    @BindView(R.id.tPost)
    TextView tPost;
    @BindView(R.id.tVillage)
    TextView tVillage;
    @BindView(R.id.tSubVillage)
    TextView tSubVillage;
    @BindView(R.id.tvShowIntent)
    TextView tvShowIntent;
    @BindView(R.id.tEstFarmArea)
    TextView tEstFarmArea;
    @BindView(R.id.tEstFarmArea2)
    TextView tEstFarmArea2;
    @BindView(R.id.tEstFarmArea3)
    TextView tEstFarmArea3;
    @BindView(R.id.tEstFarmArea4)
    TextView tEstFarmArea4;
    @BindView(R.id.tvOtherCropsOne)
    TextView tvOtherCropsOne;
    @BindView(R.id.tvOtherCropsTwo)
    TextView tvOtherCropsTwo;
    @BindView(R.id.tvOtherCropsThree)
    TextView tvOtherCropsThree;
    @BindView(R.id.tvLat)
    TextView tvLat;
    @BindView(R.id.tvLongt)
    TextView tvLongt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private UploadActivity mContext;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);

        mContext = UploadActivity.this;

        db = new DatabaseHandler(mContext);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initData();

        Log.d("Other CROPS 1:", otherCropsIDOne);
        Log.d("Other CROPS 2:", otherCropsIDTwo);
        Log.d("Other CROPS 3:", otherCropsIDThree);

        if (estimateFarmAreaTwo == null) {
            estimateFarmAreaTwo = "0";
        }
        if (estimateFarmAreaThree == null) {
            estimateFarmAreaThree = "0";
        }
        if (estimateFarmAreaFour == null) {
            estimateFarmAreaFour = "0";
        }

        if (gpsTracker.canGetLocation) {
            lat = gpsTracker.getLatitude();
            longt = gpsTracker.getLongitude();

            latitude = String.valueOf(lat);
            longitude = String.valueOf(longt);

            tvLat.setText(latitude);
            tvLongt.setText(longitude);

            Log.e("latitude: ", latitude);
            Log.e("long: ", longitude);

        } else {
            gpsTracker.showSettingsAlert(UploadActivity.this);
            lat = gpsTracker.getLatitude();
            longt = gpsTracker.getLongitude();

            latitude = String.valueOf(lat);
            longitude = String.valueOf(longt);

            tvLat.setText(latitude);
            tvLongt.setText(longitude);

            Log.e("latitude: ", latitude);
            Log.e("long: ", longitude);

        }

       setData();
        if (farmerPic != null) {
            previewFarmerPic();
        } else {
            Toast.makeText(mContext,"Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ConnectionDetector cdd = new ConnectionDetector(
                        mContext);

                if (cdd.isConnectingToInternet()) {
                    new UploadFarmerToServer().execute();
                } else {

                    if (gpsTracker.canGetLocation) {
                        // do {
                        lat = gpsTracker.getLatitude();
                        longt = gpsTracker.getLongitude();
                        // }while(latitude==0.0 || longitude==0.0);

                            try {
                                Intent i = new Intent(mContext,UploadActivity.class);

                                farmerPic = MyPrefrences.getPrefrence(mContext, "farmer_pic");

                                DatabaseHandler db = new DatabaseHandler(mContext);

                                Log.e("Insert: ", "Inserting .." + villageID + "|" + subVillageID);

                                db.addFarmer(new Farmers(fname, lname, gender,
                                        idNo, phone, email, postalAddress, villageID, subVillageID,
                                        farmerPic, MyPrefrences.getPrefrence(mContext, "left_thumb"), MyPrefrences.getPrefrence(mContext, "right_thumb"), String.valueOf(lat), String
                                        .valueOf(longt), showIntent, estimateFarmAreaOne, farmVillageIDOne,
                                        estimateFarmAreaTwo, farmVillageIDTwo, estimateFarmAreaThree, farmVillageIDThree,
                                        estimateFarmAreaFour, farmVillageIDFour, otherCropsIDOne, otherCropsIDTwo, otherCropsIDThree, Preferences.COMPANY_ID,Preferences.USER_ID,null));

                            } catch (Exception ex) {
                                Log.e("Exception:", ex.getMessage());
                            }

                        Log.e("latitude: ", String.valueOf(lat));
                        Log.e("long: ", String.valueOf(longt));

                        gpsTracker.stopUsingGPS();

                        new MaterialStyledDialog.Builder(UploadActivity.this)
                                .setTitle(R.string.app_name)
                                .setStyle(Style.HEADER_WITH_TITLE)
                                .setDescription(R.string.saved_offline)
                                .setCancelable(false)
                                .setPositiveText(R.string.ok)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent newf = new Intent(mContext,
                                                DashBoardActivity.class);
                                        newf.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(newf);
                                    }
                                }).show();

                    } else {
                        // turn on gps
                        gpsTracker.showSettingsAlert(UploadActivity.this);
                        lat = gpsTracker.getLatitude();
                        longt = gpsTracker.getLongitude();

                            try {
                                Intent i = new Intent(mContext,
                                        UploadActivity.class);
                                farmerPic = MyPrefrences.getPrefrence(
                                        mContext, "farmerPic");
                                DatabaseHandler db = new DatabaseHandler(
                                        mContext);

                                Log.e("Insert: ", "Inserting .." + villageID + "|" + subVillageID);

                                db.addFarmer(new Farmers(fname, lname, gender,
                                        idNo, phone, email, postalAddress, villageID, subVillageID,
                                        farmerPic, MyPrefrences.getPrefrence(mContext, "left_thumb"), MyPrefrences.getPrefrence(mContext, "right_thumb"), String.valueOf(lat), String
                                        .valueOf(longt), showIntent, estimateFarmAreaOne, farmVillageIDOne,
                                        estimateFarmAreaTwo, farmVillageIDTwo, estimateFarmAreaThree, farmVillageIDThree,
                                        estimateFarmAreaFour, farmVillageIDFour, otherCropsIDOne, otherCropsIDTwo, otherCropsIDThree, Preferences.COMPANY_ID,Preferences.USER_ID,null));

                                Log.e("Reading: ", "Reading all farmers..");
                                List<Farmers> farmers = db.getAllFarmers();
                                db.close();
                            } catch (Exception e) {
                                Log.e("Exception:", e.getMessage());
                            }

                        Log.e("latitude: ", String.valueOf(lat));
                        Log.e("long: ", String.valueOf(longt));
                        Intent r = new Intent(
                                "android.location.GPS_ENABLED_CHANGE");
                        r.putExtra("enabled", false);
                        sendBroadcast(r);
                        gpsTracker.stopUsingGPS();

                        new MaterialStyledDialog.Builder(UploadActivity.this)
                                .setTitle(R.string.app_name)
                                .setStyle(Style.HEADER_WITH_TITLE)
                                .setDescription(R.string.saved_offline)
                                .setCancelable(false)
                                .setPositiveText(R.string.ok)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent newf = new Intent(mContext,
                                                DashBoardActivity.class);
                                        newf.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(newf);
                                    }
                                }).show();

                    }
                }
            }
        });

    }

    private void setData() {
        tFName.setText(fname);
        tLName.setText(lname);
        tGender.setText(gender);
        tID.setText(idNo);
        tPhone.setText(phone);
        tEmail.setText(email);
        tPost.setText(postalAddress);
        tVillage.setText(villageName);
        tSubVillage.setText(subVillageName);
        tvShowIntent.setText(showIntent);
        tEstFarmArea.setText(estimateFarmAreaOne);
        tEstFarmArea2.setText(estimateFarmAreaTwo);
        tEstFarmArea3.setText(estimateFarmAreaThree);
        tEstFarmArea4.setText(estimateFarmAreaFour);
        tvOtherCropsOne.setText(otherCropsNameOne);
        tvOtherCropsTwo.setText(otherCropsNameTwo);
        tvOtherCropsThree.setText(otherCropsNameThree);

    }

    private void initData() {
        gpsTracker = new GPSTracker(mContext);

        Intent intent = getIntent();

        farmerPic = MyPrefrences.getPrefrence(mContext, "farmer_pic");

         //isImage = intent.getBooleanExtra("isImage", true);

        fname = intent.getStringExtra("fname");
        lname = intent.getStringExtra("lname");
        gender = intent.getStringExtra("gender");
        idNo = intent.getStringExtra("id_no");
        phone = intent.getStringExtra("phone");
        email = intent.getStringExtra("email");
        postalAddress = intent.getStringExtra("postal_address");
        villageName = intent.getStringExtra("village_name");
        villageID = intent.getStringExtra("village_id");
        subVillageName = intent.getStringExtra("sub_village_name");
        subVillageID = intent.getStringExtra("sub_village_id");

        farmVillageIDOne = intent.getStringExtra("farm_village_id_one");
        farmVillageIDTwo = intent.getStringExtra("farm_village_id_two");
        farmVillageIDThree = intent.getStringExtra("farm_village_id_three");
        farmVillageIDFour = intent.getStringExtra("farm_village_id_four");

        showIntent = intent.getStringExtra("show_intent");

        estimateFarmAreaOne = intent.getStringExtra("farm_area_one");
        estimateFarmAreaTwo = intent.getStringExtra("farm_area_two");
        estimateFarmAreaThree = intent.getStringExtra("farm_area_three");
        estimateFarmAreaFour = intent.getStringExtra("farm_area_four");

        otherCropsNameOne = intent.getStringExtra("other_crops_name_one");
        otherCropsIDOne = intent.getStringExtra("other_crops_id_one");
        otherCropsNameTwo = intent.getStringExtra("other_crops_name_two");
        otherCropsIDTwo = intent.getStringExtra("other_crops_id_two");
        otherCropsNameThree = intent.getStringExtra("other_crops_name_three");
        otherCropsIDThree = intent.getStringExtra("other_crops_id_three");
    }

    /**
     * Displaying captured image on the screen
     */
    private void previewFarmerPic() {
        //if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(farmerPic, options);

            imgPreview.setImageBitmap(bitmap);
//        } else {
//            imgPreview.setVisibility(View.GONE);
//
//        }
    }

    /**
     * Uploading the farmers details to server
     */
    private class UploadFarmerToServer extends AsyncTask<Void, Integer, String> {

        String responseString = null;

        @Override
        protected void onPreExecute() {
            btnUpload.setEnabled(false);

            isUploading = true;

            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setVisibility(View.VISIBLE);

            progressBar.setProgress(progress[0]);

            tPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FARMER_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File leftThumbFile = new File(MyPrefrences.getPrefrence(mContext, "left_thumb"));
                File rightThumbFile = new File(MyPrefrences.getPrefrence(mContext, "right_thumb"));
                File sourceFile2 = new File(farmerPic);

                entity.addPart(Config.LEFT_THUMB, new FileBody(leftThumbFile));
                entity.addPart(Config.RIGHT_THUMB, new FileBody(rightThumbFile));
                entity.addPart("image", new FileBody(sourceFile2));

                entity.addPart("fname", new StringBody(fname));
                entity.addPart("lname", new StringBody(lname));
                entity.addPart("gender", new StringBody(gender));
                entity.addPart("id_no", new StringBody(idNo));
                entity.addPart("phone", new StringBody(phone));
                entity.addPart("email", new StringBody(email));
                entity.addPart("post", new StringBody(postalAddress));
                entity.addPart("village_id", new StringBody(villageID));
                entity.addPart("subvillage_id", new StringBody(subVillageID));
                entity.addPart("est_farm_area_one", new StringBody(estimateFarmAreaOne));
                entity.addPart("contract_status", new StringBody(showIntent));
                entity.addPart("est_farm_area_two", new StringBody(estimateFarmAreaTwo));
                entity.addPart("est_farm_area_three", new StringBody(estimateFarmAreaThree));
                entity.addPart("est_farm_area_four", new StringBody(estimateFarmAreaFour));
                entity.addPart("farm_village_id_one", new StringBody(farmVillageIDOne));
                entity.addPart("farm_village_id_two", new StringBody(farmVillageIDTwo));
                entity.addPart("farm_village_id_three", new StringBody(farmVillageIDThree));
                entity.addPart("farm_village_id_four", new StringBody(farmVillageIDFour));
                entity.addPart("other_crops_id_one", new StringBody(otherCropsIDOne));
                entity.addPart("other_crops_id_two", new StringBody(otherCropsIDTwo));
                entity.addPart("other_crops_id_three", new StringBody(otherCropsIDThree));
                entity.addPart("latitude", new StringBody(latitude));
                entity.addPart("longitude", new StringBody(longitude));
                entity.addPart("company_id", new StringBody(Preferences.COMPANY_ID));
                entity.addPart(DatabaseHandler.KEY_USER_ID, new StringBody(Preferences.USER_ID));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (Exception e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("SERVER: ", "Response from server: " + responseString);
            gpsTracker.stopUsingGPS();

            if (responseString.contains("ok")) {
                new MaterialStyledDialog.Builder(UploadActivity.this)
                        .setTitle(R.string.app_name)
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .setDescription(R.string.saved_online)
                        .setCancelable(false)
                        .setPositiveText(R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent newf = new Intent(mContext,
                                        DashBoardActivity.class);
                                newf.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(newf);
                            }
                        }).show();
            } else {
                farmerPic = MyPrefrences.getPrefrence(mContext, "farmer_pic");

                Log.e("Insert: ", "Inserting .." + villageID + "|" + subVillageID);

                db.addFarmer(new Farmers(fname, lname, gender,
                        idNo, phone, email, postalAddress, villageID, subVillageID,
                        farmerPic, MyPrefrences.getPrefrence(mContext, "left_thumb"), MyPrefrences.getPrefrence(mContext, "right_thumb"), String.valueOf(lat), String
                        .valueOf(longt), showIntent, estimateFarmAreaOne, farmVillageIDOne,
                        estimateFarmAreaTwo, farmVillageIDTwo, estimateFarmAreaThree, farmVillageIDThree,
                        estimateFarmAreaFour, farmVillageIDFour, otherCropsIDOne, otherCropsIDTwo, otherCropsIDThree, Preferences.COMPANY_ID,Preferences.USER_ID,null));

                new MaterialStyledDialog.Builder(UploadActivity.this)
                        .setTitle(R.string.app_name)
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .setDescription(R.string.try_save_offline)
                        .setCancelable(false)
                        .setPositiveText(R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                startActivity(new Intent(mContext,
                                        DashBoardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        }).show();

            }
        }

    }

    @Override
    public void onBackPressed() {
        if (!isUploading) {
            super.onBackPressed();
        }
    }

}
