package com.svs.farm_app.main.training_attendance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.AssignedTrainings;
import com.svs.farm_app.entities.FarmerTime;
import com.svs.farm_app.entities.RegisteredFarmer;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.GPSTracker;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;
import com.svs.farm_app.utils.ToastUtil;
import com.svs.farm_app.searchable.SearchableSpinner;
//import com.svs.farm_app.searchable.SearchableSpinner;

import org.ndeftools.Message;
import org.ndeftools.MimeRecord;
import org.ndeftools.Record;
import org.ndeftools.externaltype.ExternalTypeRecord;
import org.ndeftools.util.activity.NfcReaderActivity;
import org.ndeftools.wellknown.TextRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PDFViewerTrainingActivity extends NfcReaderActivity {

    private static final String TAG = PDFViewerTrainingActivity.class.getSimpleName();
    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String PDFPath;
    private PDFViewerTrainingActivity mContext;
    private DatabaseHandler db;
    private GPSTracker gps;
    private AlertDialog.Builder mAlertBuilder;
    private String companyId;
    private String userId;
    private String trainTime;
    private Intent intent;
    private String trainCatId;
    private String extTrainId;
    private SimpleDateFormat dateFormat;
    private String dateTime;
    private AlertDialog.Builder search;
    private boolean match;
    private String farmerId;
    private int count2;
    private List<String> swipeCount;
    private AppCompatDelegate delegate;
    private List<RegisteredFarmer> registeredFarmerList;
    private View searchFarmerDialog;
    private SearchableSpinner spFarmers;
    private CircleImageView ivFarmerImage;
    private CustomSpinnerAdapter farmersAdapter;
    private RegisteredFarmer selectedFarmer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        delegate = AppCompatDelegate.create(this, this);
        //we need to call the onCreate() of the AppCompatDelegate
        delegate.onCreate(savedInstanceState);
        //we use the delegate to inflate the layout
        delegate.setContentView(R.layout.activity_pdfviewer);
        ButterKnife.bind(this);

        delegate.setSupportActionBar(toolbar);
        delegate.getSupportActionBar().setDisplayShowHomeEnabled(true);
        delegate.getSupportActionBar().setDisplayShowTitleEnabled(true);

        intent = getIntent();
        PDFPath = intent.getStringExtra("pdf_path");
        Log.i(TAG,"pdf path: "+PDFPath+" Exists: "+new File(PDFPath).exists());

        pdfView.fromFile(new File(PDFPath)).load();

        initData();

        setDetecting(true);
        //SavePrefrence(getApplicationContext(), "where", "2");
    }

    private void initData() {
        mContext = PDFViewerTrainingActivity.this;
        db = new DatabaseHandler(mContext);
        gps = new GPSTracker(mContext);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        swipeCount = new ArrayList<>();

        mAlertBuilder = new AlertDialog.Builder(this);

        companyId = Preferences.COMPANY_ID;
        userId = Preferences.USER_ID;

        trainTime = intent.getStringExtra("train_time");
        trainCatId = intent.getStringExtra("train_cat_id");
        extTrainId = intent.getStringExtra("ext_train_id");

        String latitude = String.valueOf(gps.getLatitude());
        String longitude = String.valueOf(gps.getLongitude());

        Log.i(TAG,"saveEarlyFarmers before "+new Gson().toJson(TrainingUtils.trackFarmers));

        saveEarlyFarmers(TrainingUtils.trackFarmers, extTrainId, trainCatId, latitude, longitude,
                trainTime, "0", userId, companyId);
    }

    private void saveEarlyFarmers(List<String> earlyFarmers, String extTrainId, String trainCatId,String latitude, String longitude,
                                  String farmerTimeIn, String  farmerTimeOut, String userId,String  companyId) {

        for (String farmerId : earlyFarmers) {

            Log.i(TAG,"Early farmers: FarmerId: "+ farmerId+" trainCatId: "+trainCatId+" trainCatId: "+trainCatId);

            db.addFarmerTimes(new FarmerTime(farmerId, extTrainId, trainCatId, latitude, longitude,
                    farmerTimeIn, farmerTimeOut, userId, companyId));
        }

        //TrainingUtils.trackFarmers.clear();
        //Log.i(TAG,"saveEarlyFarmers after "+new Gson().toJson(TrainingUtils.trackFarmers));
    }

    @Override
    public void onBackPressed() {

            SimpleDateFormat timeFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String stopTime = timeFormat.format(date);

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            /*// auto tap out
            List<String> notTappedOut = db.getAllFarmersNotTappedOut(MyPrefrences.getPrefrence(getApplicationContext(), "not_tapped_out"));

            String joined = TextUtils.join(",", notTappedOut);
            MyPrefrences.savePrefrence(mContext, "not_tapped_out", joined);*/

            db.autoTapOut(trainCatId,stopTime);

            db.addAssignedTrainings(new AssignedTrainings(extTrainId, trainCatId,
                    trainTime, stopTime, String.valueOf(latitude), String
                    .valueOf(longitude), userId, companyId));

            TrainingUtils.trackFarmers.clear();

        Log.i(TAG,"onBackPressed "+new Gson().toJson(TrainingUtils.trackFarmers));

            finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_farmer_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                //toTransparentActivity();
                registeredFarmerList = db.getAllRegisteredFarmers();

                Log.i(TAG, "farmer list size: " + registeredFarmerList.size());

                //selectedFarmer = registeredFarmerList.get(0);

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                searchFarmerDialog = inflater.inflate(R.layout.search_farmer_dialog, null);

                spFarmers = (SearchableSpinner) searchFarmerDialog.findViewById(R.id.spFarmers);
                ivFarmerImage = (CircleImageView) searchFarmerDialog.findViewById(R.id.ivFarmerImage);

                spFarmers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {

                        selectedFarmer = registeredFarmerList.get(position);

                        String farmersImage = Config.SD_CARD_PATH + "/farmer_details/" + selectedFarmer.getGenId();

                        if (new File(farmersImage).exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(farmersImage);

                            ivFarmerImage.setImageBitmap(myBitmap);

                            ivFarmerImage.setVisibility(View.VISIBLE);
                        } else {
                            //TODO: Show default image
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //selectedFarmer = registeredFarmerList.get(0);
                    }
                });

                loadFarmers(registeredFarmerList);
                showSearchDialog(searchFarmerDialog);

                break;
        }
        return true;
    }

    private void loadFarmers(List<RegisteredFarmer> registeredFarmers) {

        farmersAdapter = new CustomSpinnerAdapter(mContext, R.layout.search_farmer_item, registeredFarmers);
        spFarmers.setAdapter(farmersAdapter);
    }

    private void showSearchDialog(View customView) {

        new MaterialStyledDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription("Search for farmer by name/Card No \n # of farmers " + registeredFarmerList.size())
                .setCustomView(customView, 10, 20, 10, 40)
                .setCancelable(true)
                .setNegativeText("CANCEL")
                .setPositiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        if (selectedFarmer == null) {
                            MyAlerts.genericDialog(mContext, R.string.select_farmer_first);
                            return;
                        }

                        trackFarmer(selectedFarmer);

                    }
                }).show();
    }

    private void trackFarmer(RegisteredFarmer selectedFarmer) {
        farmerId = selectedFarmer.getFarmerId();

        Date date = new Date();

        dateTime = dateFormat.format(date);

        String farmerName = "Name: "+selectedFarmer.getFirstName()+" "+selectedFarmer.getLastName();

        if (TrainingUtils.isInTraining(farmerId)) {
            String message = farmerName+"\nTime out: "+dateTime;
            String farmerTimeOut = dateTime;
            String farmerTimeIn = "0";
            trackSessionDialog(selectedFarmer,extTrainId,trainCatId,farmerTimeIn,farmerTimeOut,message);

        } else {
            String message = farmerName+"\nTime In: "+dateTime;
            String farmerTimeIn = dateTime;
            String farmerTimeOut = "0";
            trackSessionDialog(selectedFarmer,extTrainId,trainCatId,farmerTimeIn,farmerTimeOut,message);

        }
    }

    private void trackSessionDialog(RegisteredFarmer selectedFarmer, final String extTrainId, final String trainCatId, final String farmerTimeIn, final String farmerTimeOut, String message) {
        final String farmerId = selectedFarmer.getFarmerId();
        new MaterialStyledDialog.Builder(mContext)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(message)
                .setCancelable(false)
                .setPositiveText(R.string.ok)
                .setNegativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        if (TrainingUtils.isInTraining(farmerId)) {

                            db.updateFarmerTimes(farmerId,extTrainId, trainCatId, farmerTimeOut,
                                    userId, companyId);

                            TrainingUtils.removeFarmer(farmerId);

                        } else{

                            String latitude="";
                            String longitude = "";
                            latitude = String.valueOf(gps.getLatitude());
                            longitude = String.valueOf(gps.getLongitude());
                            db.addFarmerTimes(new FarmerTime(farmerId,extTrainId, trainCatId,latitude,longitude,
                                    farmerTimeIn, farmerTimeOut, userId, companyId));

                            if(!TrainingUtils.isInTraining(farmerId)){
                                TrainingUtils.addFarmer(farmerId);
                            }

                        }

                        Log.i(TAG,"trackSessionDialog"+new Gson().toJson(TrainingUtils.trackFarmers));

                    }
                }).show();
    }

    public int inOrOut(String searchedGenID){
        count2 = 0;

        for (String cn : swipeCount) {
            if (cn.equals(searchedGenID)) {
                count2++;
                Log.e("match found: " + cn, "counter|" + count2);
            }
        }
        return count2;
    }

    private void searchFarmer(final String searchedGenID) {
        if (searchedGenID.length() == 16) {

            List<RegisteredFarmer> registeredFarmerList = null;

            registeredFarmerList = db.getAllRegisteredFarmers();

            RegisteredFarmer registeredFarmer = searchFarmerInList(searchedGenID, registeredFarmerList);

            match = (registeredFarmer != null);
            farmerId = registeredFarmer.getFarmerId();

            if (match) {
                runOnUiThread(new Runnable() {

                    public void run() {

                        //count2 = swipeFunction(swipeCount, searchedGenID);

                        if (count2 % 2 == 0) {
                            Intent trans = new Intent(getApplicationContext(), TransparentActivity.class);
                            trans.putExtra("time", "Time Out: " + dateTime);
                            trans.putExtra("farmer_time_in", "0");
                            trans.putExtra("farmer_time_out", dateTime);
                            trans.putExtra("farmer_id", farmerId);
                            trans.putExtra(DatabaseHandler.KEY_GEN_ID, searchedGenID);
                            trans.putExtra("train_cat_id", trainCatId);
                            trans.putExtra("ext_train_id", trainCatId);
                            match = false;
                            startActivity(trans);

                        } else {
                            Intent trans = new Intent(getApplicationContext(), TransparentActivity.class);
                            try {

                                trans.putExtra("time", "Time In: " + dateTime);
                            } catch (Exception ex) {
                                trans.putExtra("time", "Time In: " + dateTime);
                            }
                            trans.putExtra("farmer_time_in", dateTime);
                            trans.putExtra("farmer_time_out", "0");
                            trans.putExtra("farmer_id", farmerId);
                            trans.putExtra(DatabaseHandler.KEY_GEN_ID, searchedGenID);
                            trans.putExtra("train_cat_id", trainCatId);
                            trans.putExtra("ext_train_id", trainCatId);
                            match = false;
                            startActivity(trans);

                        }

                    }


                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Intent trans = new Intent(
                                getApplicationContext(),
                                TransparentActivity.class);
                        trans.putExtra("time", "Time In: "
                                + dateTime);
                        trans.putExtra("farmer_id", "Unknown");
                        startActivity(trans);
                    }
                });
            }
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    // Toast.makeText(getApplicationContext(),
                    // "Farmer Not searchedGenID",
                    // Toast.LENGTH_LONG).show();
                    // alert

                    MyAlerts.genericDialog(mContext,R.string.verify_farmer_id);
                }
            });
        }
    }

    private RegisteredFarmer searchFarmerInList(String searchedGenID, List<RegisteredFarmer> registeredFarmerList) {
        RegisteredFarmer farmer = null;
        for (RegisteredFarmer registeredFarmer : registeredFarmerList) {
            if (searchedGenID.equals(registeredFarmer.getGenId())) {
                farmer = registeredFarmer;
            }
        }
        return farmer;
    }

    /**
     * An NDEF message was read and parsed. This method prints its contents to
     * log and then shows its contents in the GUI.
     *
     * @param message the message
     */
    public void readNdefMessage(Message message) {

        this.setMessage(message);

        // process message

        // show in log
        // iterate through all records in message
        Log.d(TAG, "searchedGenID " + message.size() + " NDEF records");

        for (int k = 0; k < message.size(); k++) {
            Record record = message.get(k);

            Log.d(TAG, "Record " + k + " type "
                    + record.getClass().getSimpleName());

            // your own code here, for example:
            if (record instanceof MimeRecord) {
                // ..
            } else if (record instanceof ExternalTypeRecord) {
                // ..
            } else if (record instanceof TextRecord) {
                // ..
            } else { // more else
                // ..
            }
        }

    }

    /**
     * An empty NDEF message was read.
     */

    @Override
    protected void readEmptyNdefMessage() {

    }

    /**
     * Something was read via NFC, but it was not an NDEF message.
     * <p>
     * Handling this situation is out of scope of this project.
     */

    @Override
    protected void readNonNdefMessage(Tag tag) {

        String searchedTech = Ndef.class.getName();

        Log.d(TAG, "tech type: " + searchedTech);
        if (searchedTech.equals("android.nfc.tech.Ndef")) {
            new NdefReaderTask().execute(tag);
        }
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    /**
     * Background task for reading the data. Do not block the UI thread while
     * reading.
     *
     * @author Ralf Wondratschek
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
        private boolean match;
        private SimpleDateFormat timeFormat;
        protected String genID;

        public void writeTag(Tag tag, String tagText) {
            MifareUltralight ultralight = MifareUltralight.get(tag);
            try {

                ultralight.connect();
                ultralight.writePage(4,
                        "BA14".getBytes(Charset.forName("US-ASCII")));
                ultralight.writePage(5,
                        "0NY5".getBytes(Charset.forName("US-ASCII")));
                ultralight.writePage(6,
                        "5D07".getBytes(Charset.forName("US-ASCII")));
                ultralight.writePage(7,
                        "45F6".getBytes(Charset.forName("US-ASCII")));
                ultralight.writePage(8,
                        "50C6".getBytes(Charset.forName("US-ASCII")));
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing MifareUltralight...", e);
            } finally {
                try {
                    ultralight.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException while closing MifareUltralight...",
                            e);
                }
            }
        }

        public String readTag(Tag tag) {
            MifareUltralight mifare = MifareUltralight.get(tag);
            try {
                mifare.connect();
                byte[] payload = mifare.readPages(4);
                return new String(payload, Charset.forName("US-ASCII"))
                        .replace("000", "");
            } catch (IOException ex) {
                Log.e(TAG, ex.getMessage());
            } finally {
                if (mifare != null) {
                    try {
                        mifare.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing tag...", e);
                    }
                }
            }
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Tag... params) {
            Log.d(TAG, "async task running");
            Tag tag = params[0];

            // write to tag
            // writeTag(tag, "BEnosn");
            return readTag(tag);
        }

        @Override
        protected void onPostExecute(String result) {

            toTransparentActivity(result);
        }

        /**
         * @param result
         */
        private void toTransparentActivity(final String result) {
            Log.d("NFC Data:", result);
            // show dialog
                // launch dialog to search for farmer manually
                timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date date = new Date();

                dateTime = timeFormat.format(date);

                search = new AlertDialog.Builder(mContext);
                search.setTitle("Attendance");
                search.setMessage("Search for Farmer Using ID");

                search.setPositiveButton("Validate",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                final String searchedGenID = result.trim();

                                if (searchedGenID.length() == 16) {
                                    match = false;

                                    List<RegisteredFarmer> registeredFarmerList = null;
                                    registeredFarmerList = db.getAllRegisteredFarmers();

                                    RegisteredFarmer registeredFarmer = searchFarmerInList(searchedGenID, registeredFarmerList);

                                    match = (registeredFarmer != null);
                                    farmerId = registeredFarmer.getFarmerId();
                                    genID = registeredFarmer.getGenId();


                                    if (match) {
                                        runOnUiThread(new Runnable() {
                                            public void run() {

                                                count2 = swipeFunction(swipeCount, genID);

                                                Log.e("List Size: ", String.valueOf(swipeCount.size()));
                                                Log.e("Match Count: ",String.valueOf(count2));

                                                if (count2 % 2 == 0) {
                                                    Intent trans = new Intent(getApplicationContext(),TransparentActivity.class);
                                                    trans.putExtra("time","Time Out: "+ dateTime);
                                                    trans.putExtra("farmer_time_in","0");
                                                    trans.putExtra("farmer_time_out",dateTime);
                                                    trans.putExtra("farmer_id",farmerId);
                                                    trans.putExtra(DatabaseHandler.KEY_GEN_ID,genID);
                                                    trans.putExtra("train_cat_id", trainCatId);
                                                    trans.putExtra("ext_train_id", trainCatId);

                                                    match = false;
                                                    startActivity(trans);

                                                } else {
                                                    Intent trans = new Intent(getApplicationContext(),TransparentActivity.class);
                                                    try {

                                                        trans.putExtra("time","Time In: "+ dateTime);
                                                    } catch (Exception ex) {
                                                        trans.putExtra("time","Time In: "+ dateTime);
                                                    }
                                                    trans.putExtra("farmer_time_in",dateTime);
                                                    trans.putExtra("farmer_time_out","0");
                                                    trans.putExtra("farmer_id",farmerId);
                                                    trans.putExtra(DatabaseHandler.KEY_GEN_ID,genID);
                                                    trans.putExtra("train_cat_id", trainCatId);
                                                    trans.putExtra("ext_train_id", trainCatId);

                                                    match = false;
                                                    startActivity(trans);

                                                }

                                            }

                                            private int swipeFunction(List<String> swipeCount,String searchedGenID) {
                                                Log.d("", "work");

                                                String swipe_pref = MyPrefrences.getPrefrence(mContext,"swipe_count");
                                                boolean check = false;
                                                if (swipe_pref == null) {
                                                    check = true;
                                                    // add swipe count
                                                    swipeCount.add(searchedGenID);

                                                    MyPrefrences.savePrefrence(getApplicationContext(),"swipe_count",searchedGenID);
                                                    Log.d("SWIPE COUNT: null",swipeCount.size()+ "|"+ searchedGenID);
                                                } else if (swipe_pref.length() > 16) {
                                                    // clear current swipe counts
                                                    swipeCount.clear();
                                                    // add data to swipe count
                                                    String[] savedSwipes = TextUtils .split(swipe_pref,",");
                                                    for (String cn : savedSwipes) {
                                                        swipeCount.add(cn);
                                                    }

                                                    // add swipe count
                                                    swipeCount.add(searchedGenID);
                                                    String swipeCountCSV = TextUtils.join(",",swipeCount);

                                                    MyPrefrences.savePrefrence(getApplicationContext(),"swipe_count", swipeCountCSV);

                                                    Log.d("SWIPE COUNT: elseif1",swipeCount.size()+ "|"+ searchedGenID);

                                                } else if (!check
                                                        && swipe_pref.length() == 16) {
                                                    // clear current swipe counts
                                                    swipeCount.clear();
                                                    // add swipe count
                                                    swipeCount.add(searchedGenID);
                                                    swipeCount.add(swipe_pref);

                                                    String[] temp = new String[2];
                                                    temp[0] = swipe_pref;
                                                    temp[1] = searchedGenID;

                                                    String swipeCountCSV = TextUtils.join(",",swipeCount);

                                                    MyPrefrences.savePrefrence(getApplicationContext(),"swipe_count",swipeCountCSV);

                                                    Log.d("SWIPE COUNT: elseif2",swipeCount.size()+ "|"+ searchedGenID);
                                                }

                                                count2 = 0;

                                                for (String cn : swipeCount) {
                                                    if (swipeCount.contains(searchedGenID)) {
                                                        count2++;
                                                        Log.e("match searchedGenID: "+ cn,"counter|"+ count2);
                                                    }
                                                }
                                                return count2;
                                            }

                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Intent trans = new Intent(getApplicationContext(),TransparentActivity.class);
                                                trans.putExtra("time","Time In: " + dateTime);
                                                trans.putExtra("farmer_id", "Unknown");
                                                startActivity(trans);
                                            }
                                        });
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            MyAlerts.genericDialog(mContext,R.string.verify_farmer_id);
                                        }
                                    });
                                }
                            }
                        });

                search.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        });

                search.show();
            }
    }

    /**
     * NFC feature was searchedGenID and is currently enabled
     */

    @Override
    protected void onNfcStateEnabled() {
        ToastUtil.showToast(getApplicationContext(),
                "NFC is available and enabled");
    }

    /**
     * NFC feature was searchedGenID but is currently disabled
     */

    @Override
    protected void onNfcStateDisabled() {

        ToastUtil.showToast(getApplicationContext(),
                "NFC is available but disabled");
    }

    /**
     * NFC setting changed since last check. For example, the user enabled NFC
     * in the wireless settings.
     */

    @Override
    protected void onNfcStateChange(boolean enabled) {
        if (enabled) {
            ToastUtil.showToast(getApplicationContext(), "NFC is enabled");
        } else {
            ToastUtil.showToast(getApplicationContext(), "NFC is disabled");
        }
    }

    /**
     * This device does not have NFC hardware
     */

    @Override
    protected void onNfcFeatureNotFound() {
        ToastUtil.showToast(getApplicationContext(), "NFC not found");
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 0);
        toast.show();
    }

    @Override
    protected void onTagLost() {
        ToastUtil.showToast(getApplicationContext(), "Tag Lost");
    }

    public class CustomSpinnerAdapter extends ArrayAdapter<RegisteredFarmer> {

        private final LayoutInflater mInflater;
        private Context context = null;
        private List<RegisteredFarmer> registeredFarmers = null;
        private final int mResource;

        public CustomSpinnerAdapter(Context context, int resource, List registeredFarmers) {
            super(context, resource, 0, registeredFarmers);

            this.context = context;
            this.mInflater = LayoutInflater.from(context);
            this.mResource = resource;
            this.registeredFarmers = registeredFarmers;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        @Override
        public
        @NonNull
        View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        private View createItemView(int position, View convertView, ViewGroup parent) {
            final View view = mInflater.inflate(mResource, parent, false);

            TextView tvFarmerName = (TextView) view.findViewById(R.id.tvFarmerName);
            TextView tvCardNo = (TextView) view.findViewById(R.id.tvCardNo);

            RegisteredFarmer registeredFarmer = registeredFarmers.get(position);

            tvFarmerName.setText(registeredFarmer.getFirstName() + " " + registeredFarmer.getLastName());

            String cardNo = (registeredFarmer.getCardNo() == null) ? "N/A" : registeredFarmer.getCardNo();
            tvCardNo.setText(cardNo);

            return view;
        }
    }

}
