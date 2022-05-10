package com.svs.farm_app.main.training_attendance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.OfficerTraining;
import com.svs.farm_app.entities.RegisteredFarmer;
import com.svs.farm_app.utils.CardUtils;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;
import com.svs.farm_app.utils.ToastUtil;
import com.svs.farm_app.searchable.SearchableSpinner;

import org.ndeftools.Message;
import org.ndeftools.util.activity.NfcReaderActivity;

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

/**
 * Created by Benson on 1/26/2015.
 */
public class TrainingActivity extends NfcReaderActivity {

    public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString();
    private Button stopTrainAttendance;
    private String companyID;
    private DatabaseHandler db;
    private SimpleDateFormat timeFormat;
    private String trainCatID;
    private DigitalClock timer;
    private String currentDateTime;
    private ArrayList<String> swipeCount;
    private AlertDialog.Builder search;
    private boolean match;
    private int countTimesInTraining = 0;
    private String farmerID;
    private String userID;
    private ArrayList<String> earlyFarmers;
    final private String TAG = TrainingActivity.class.getSimpleName();
    private String extTrainID;
    private Bitmap famerPhoto;
    private TrainingActivity mContext;
    private boolean running;
    private TextView trainings;
    private ListView lvTraining;
    private TrainingAdapter trainingAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private List<OfficerTraining> officerTrainings;
    private AppCompatDelegate delegate;
    private View searchFarmerDialog;
    private SearchableSpinner spFarmers;
    private RegisteredFarmer selectedFarmer;
    private CustomSpinnerAdapter farmersAdapter;
    private List<RegisteredFarmer> registeredFarmerList;
    private CircleImageView ivFarmerImage;

    public TrainingActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_training);
        //let's create the delegate, passing the activity at both arguments (Activity, AppCompatCallback)
        delegate = AppCompatDelegate.create(this, this);
        //we need to call the onCreate() of the AppCompatDelegate
        delegate.onCreate(savedInstanceState);
        //we use the delegate to inflate the layout
        delegate.setContentView(R.layout.activity_training);
        ButterKnife.bind(this);

        delegate.setSupportActionBar(toolbar);
        delegate.getSupportActionBar().setDisplayShowHomeEnabled(true);
        delegate.getSupportActionBar().setDisplayShowTitleEnabled(true);

        mContext = TrainingActivity.this;

        initView();
        initData();
        initListeners();

        startService2();

        MyAlerts.genericDialog(mContext, getString(R.string.start_taking_attendance));

        swipeCount = new ArrayList<>();
        // clear swipe prefrence
        MyPrefrences.removePrefrence(mContext, "swipe_count");
        // remove early farmers prefrence
        MyPrefrences.removePrefrence(mContext, "early_farmers");

        MyPrefrences.savePrefrence(mContext, "where", "1");
        // lets start detecting NDEF message using foreground mode
        setDetecting(true);
    }

    private void startService2() {
        running = true;
        Log.e("Starting Service", "");
    }

    private void initView() {
        lvTraining = (ListView) findViewById(R.id.lvTrainings);
        trainings = (TextView) findViewById(R.id.lblListItem);
        timer = (DigitalClock) findViewById(R.id.digitalClock);
        timer.setVisibility(View.VISIBLE);
        stopTrainAttendance = (Button) findViewById(R.id.bStopTrainAtendance);

    }

    private void initListeners() {

        lvTraining.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                extTrainID = officerTrainings.get(position).getExtTrainID();
                trainCatID = officerTrainings.get(position).getTrainCatID();

                showAlertStartLesson("Do You Want To Start Lesson?");
            }
        });

        stopTrainAttendance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopService2();

            }
        });

    }

    private void stopService2() {
        running = false;
        MyAlerts.backToDashboardDialog(mContext, R.string.attendance_stopped);
    }

    private void initData() {
        companyID = Preferences.COMPANY_ID;
        userID = Preferences.USER_ID;

        db = new DatabaseHandler(mContext);

        officerTrainings = db.getOfficerTrainingByUserId(userID);

        TrainingUtils.trackFarmers.clear();

        Log.i(TAG, "AssignedTrainings count: " + officerTrainings.size());

        trainingAdapter = new TrainingAdapter(mContext, R.layout.list_item_training, officerTrainings);

        lvTraining.setAdapter(trainingAdapter);

        earlyFarmers = new ArrayList<>();
    }


    private void showVerificationAlert(String message, RegisteredFarmer selectedFarmer, final String action) {
        String genID = selectedFarmer.getGenId();
        final String farmerId = selectedFarmer.getFarmerId();

        /*famerPhoto = BitmapFactory.decodeFile("/sdcard/farmer_details/"
                + genID);
        farmerImage.setImageBitmap(famerPhoto);
        builder.setView(farmerImage);

        ImageView farmerImage = new ImageView(mContext);*/

        new MaterialStyledDialog.Builder(mContext)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(message)
                .setNegativeText(R.string.cancel)
                .setPositiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (action.equals("save")) {
                            TrainingUtils.addFarmer(farmerId);
                        } else if (action.equals("remove")) {
                            TrainingUtils.removeFarmer(farmerId);
                        }
                    }}).show();


    }

    private void showAlertStartLesson(String message) {

        new MaterialStyledDialog.Builder(mContext)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(message)
                .setCancelable(true)
                .setPositiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        String language = getLanguagePrefrence();

                        Uri uri = setTrainingMaterialLanguage(language);

                        //Intent tab = new Intent(mContext, MuPDFActivity.class);
                        Intent PDFViewer = new Intent(mContext, PDFViewerTrainingActivity.class);

                        timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        Date date = new Date();

                        String trainStartTime = timeFormat.format(date);

                        //reTapFarmers(trainStartTime);

                        PDFViewer.setAction(Intent.ACTION_VIEW);
                        PDFViewer.setData(uri);
                        //tab.putExtra("where_from", "training");

                        String pdfPath = Config.SD_CARD_PATH + "/train_mats/"
                                + "coach_the_farmer_swa" + ".pdf";
                        PDFViewer.putExtra("pdf_path", pdfPath);

                        //setTrainingPrefrences(earlyFarmers);

                        PDFViewer.putExtra("train_time", trainStartTime);
                        PDFViewer.putExtra("train_cat_id", trainCatID);
                        PDFViewer.putExtra("ext_train_id", extTrainID);

                        startActivity(PDFViewer);
                    }
                }).show();
    }

    private void reTapFarmers(String trainStartTime) {
        try {
            String not_tapped_out = MyPrefrences.getPrefrence(mContext, "not_tapped_out");
            if (not_tapped_out.length() > 0) {
                db.reTapFarmers(not_tapped_out, extTrainID, trainCatID, trainStartTime, userID, companyID);
                MyPrefrences.removePrefrence(mContext, "not_tapped_out");
                MyPrefrences.savePrefrence(mContext, "timeout", "timeout");
            }
        } catch (Exception ex) {
            Log.e(TAG, "NOT TAPPED OUT: " + ex.getMessage());
        }
    }

    private void setTrainingPrefrences(List<String> earlyFarmers) {
        String earlyFarmerCSV = TextUtils.join(",", earlyFarmers);

        Log.i(TAG, "Early Farmers:" + earlyFarmerCSV);

        MyPrefrences.savePrefrence(mContext, "early_farmers", earlyFarmerCSV);
    }

    @NonNull
    private String getLanguagePrefrence() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        return SP.getString("language", "NULL");
    }

    private Uri setTrainingMaterialLanguage(String language) {
        Uri uri;
        if (language.equals("1")) {
            uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/train_mats/"
                    + "coach_the_farmer_swa" + ".pdf");
        } else {
            uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/train_mats/"
                    + "coach_the_farmer_en" + ".pdf");
        }
        return uri;
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        MyPrefrences.removePrefrence(mContext, "early_farmers");

        MyPrefrences.savePrefrence(mContext, "where", "1");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public String getFarmerID(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences("approved_or_not_farmers", Context.MODE_PRIVATE);
        String result = pref.getString(key, null);
        return result;
    }

    @Override
    public void onBackPressed() {

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

                registeredFarmerList = db.getAllRegisteredFarmers();

                Log.i(TAG, "farmer list size: " + registeredFarmerList.size());

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

                    }
                });

                loadFarmers(registeredFarmerList);
                showSearchDialog(searchFarmerDialog);

                break;
        }
        return true;
    }

    private void loadFarmers(List<RegisteredFarmer> registeredFarmers) {

        farmersAdapter = new TrainingActivity.CustomSpinnerAdapter(mContext, R.layout.search_farmer_item, registeredFarmers);
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

                        logInEarlyFarmers(selectedFarmer);

                    }
                }).show();
    }

    private void logInEarlyFarmers(RegisteredFarmer selectedFarmer) {
        timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();

        currentDateTime = timeFormat.format(date);

        if (TrainingUtils.isInTraining(selectedFarmer.getFarmerId())) {

            showVerificationAlert("Remove Early Farmer", selectedFarmer, "remove");

            Log.i(TAG, "OUT ");

        } else {

            showVerificationAlert("Save Early Farmer", selectedFarmer, "save");

            Log.i(TAG, "IN ");

        }
    }

    private int swipeFunction(List<String> swipeCount, String genID) {

        String swipe_pref = MyPrefrences.getPrefrence(mContext, "swipe_count");
        boolean check = false;
        if (swipe_pref == null) {
            check = true;
            // add swipe count
            swipeCount.add(genID);

            MyPrefrences.savePrefrence(mContext, "swipe_count", genID);
            Log.d("SWIPE COUNT: null", swipeCount.size() + "|" + genID);
        } else if (swipe_pref.length() > 16) {
            // clear current swipe
            // counts
            swipeCount.clear();
            // add data to swipe count
            String[] saved_swipes = TextUtils.split(swipe_pref, ",");
            for (String cn : saved_swipes) {
                swipeCount.add(cn);
            }

            // add swipe count
            swipeCount.add(genID);
            String temp = TextUtils.join(",", swipeCount);
            MyPrefrences.savePrefrence(mContext, "swipe_count", temp);
            Log.d("SWIPE COUNT: elseif1", swipeCount.size() + "|" + genID);
        } else if (!check && swipe_pref.length() == 16) {
            // clear current swipe
            // counts
            swipeCount.clear();
            // add swipe count
            swipeCount.add(genID);
            swipeCount.add(swipe_pref);

            String temp_swipe_count = TextUtils.join(",", swipeCount);

            MyPrefrences.savePrefrence(mContext, "swipe_count", temp_swipe_count);

            Log.d("SWIPE COUNT: elseif2", swipeCount.size() + "| currentid: " + genID + "|pref:" + temp_swipe_count);
        }

        countTimesInTraining = 0;

        for (String cn : swipeCount) {
            if (cn.equals(genID)) {
                countTimesInTraining++;
                Log.e("match found: " + cn, "counter|" + countTimesInTraining);
            }
        }
        return countTimesInTraining;
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
     * An empty NDEF message was read.
     */
    @Override
    protected void readEmptyNdefMessage() {
        // toast(getString(R.string.readEmptyMessage));

    }

    /**
     * Something was read via NFC, but it was not an NDEF message.
     * <p>
     * Handling this situation is out of scope of this project.
     */
    @Override
    protected void readNonNdefMessage(Tag tag) {
        String searchedTech = Ndef.class.getName();

        if (searchedTech.equals("android.nfc.tech.Ndef")) {
            new NdefReaderTask().execute(tag);
        }

    }

    /**
     * Background task for reading the data. Do not block the UI thread while
     * reading.
     *
     * @author Ralf Wondratschek
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        private boolean match;

        public String readTag(Tag tag) {
            MifareUltralight mifare = MifareUltralight.get(tag);
            try {
                mifare.connect();
                byte[] payload = mifare.readPages(4);
                return new String(payload, Charset.forName("US-ASCII")).replace("000", "");
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
            Log.i(TAG, "async task running");
            Tag tag = params[0];
            return readTag(tag);
        }

        @Override
        protected void onPostExecute(String dirtyCardNo) {
            try {
                Log.d("NFC Data:", dirtyCardNo + " | " + dirtyCardNo.trim().length());

                String cardNo = CardUtils.cleanCardNo(dirtyCardNo);

                if (cardNo.length() >= Config.CARD_NO_LENGTH) {

                    RegisteredFarmer farmer = db.getFarmerByCardNo(cardNo);

                    logInEarlyFarmers(farmer);

                } else {
                    MyAlerts.genericDialog(mContext, R.string.invalid_card_no);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * NFC feature was found and is currently enabled
     */
    @Override
    protected void onNfcStateEnabled() {
        ToastUtil.showToast(getApplicationContext(),
                "NFC is available and enabled");
    }

    /**
     * NFC feature was found but is currently disabled
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

    @Override
    protected void readNdefMessage(Message message) {

    }

    @Override
    protected void onTagLost() {

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
