package com.svs.farm_app.farmersearch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.authentication.activity.FingerprintActivityLeft;
import com.authentication.activity.TPS350FingerprintVerificationActivity;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.svs.farm_app.R;
import com.svs.farm_app.asynctask.uploads.UploadShowsIntent;
import com.svs.farm_app.asynctask.uploads.UploadSignedDocs;
import com.svs.farm_app.entities.RegisteredFarmer;
import com.svs.farm_app.entities.ShowIntent;
import com.svs.farm_app.entities.SignedDoc;
import com.svs.farm_app.main.FarmSelectionActitivty;
import com.svs.farm_app.main.recovery.FarmerHistoryActivity;
import com.svs.farm_app.main.registration.UpdateFarmerActivity;
import com.svs.farm_app.utils.CardUtils;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.MyArrayAdapter;
import com.svs.farm_app.utils.Preferences;
import com.svs.farm_app.utils.ToastUtil;
import com.svs.farm_app.searchable.SearchableSpinner;
//import com.svs.farm_app.searchable.SearchableSpinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.ndeftools.Message;
import org.ndeftools.util.activity.NfcReaderActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
//import info.tomaszminiach.superspinner.SuperSpinner;

public class FarmerSearchActivity extends NfcReaderActivity implements OnClickListener {

    private static final String TAG = FarmerSearchActivity.class.getName();
    public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString();
    public static final int progress_bar_type = 0;
    public ImageView fingerprintImage;
    public String fingerPrintImageFromServer;
    ImageView my_image;
    private String[] m;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String filePath;
    private String filePath2;
    private TextView tveriId;
    RegisteredFarmer selectedFarmer;
    private EditText veriId;
    private Button register;
    private Button getFingerServer;
    private ProgressDialog pDialog;
    private Button validate;
    private Button calibration;
    private Button back;
    private ProgressDialog progressDialog;
    private byte[] model;

    private String farmer_id;
    private long lastBackTime = 0;
    private String path = Environment.getExternalStorageDirectory() + File.separator + "fingerprint_image";
    private String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String farmerImageFromServer;
    private String next2;
    private String next1;
    private boolean running = false;
    private String toWhichClass = null;
    private AlertDialog.Builder search;
    private boolean match;
    private String assfid;
    protected String farmerId;
    private String genId;
    private Bitmap famerPhoto;
    private String orderId;
    private DatabaseHandler db;
    private FarmerSearchActivity mContext;
    private String lastName;
    private String firstName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private AppCompatDelegate delegate;
    private TextView tvInstructions;
    private ImageView ivInstructions;
    private RegisteredFarmer searchedFarmer = null;
    private MyFarmerData[] farmerData;
    private SearchableSpinner spFarmers;
//    private CustomSpinnerAdapter farmersAdapter;
    private MyArrayAdapter farmersAdapter;
    private View searchFarmerDialog;
    private List<RegisteredFarmer> registeredFarmerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //let's create the delegate, passing the activity at both arguments (Activity, AppCompatCallback)
        delegate = AppCompatDelegate.create(this, this);

        //we need to call the onCreate() of the AppCompatDelegate
        delegate.onCreate(savedInstanceState);

        //we use the delegate to inflate the layout
        //delegate.setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        try {
            farmer_id = intent.getStringExtra("farmer_id");
            toWhichClass = intent.getStringExtra(Config.TO_ACTIVITY).trim();
            Log.e(TAG, "toWhich: " + toWhichClass);
            orderId = intent.getStringExtra(DatabaseHandler.KEY_ORDER_ID);
            genId = intent.getStringExtra(DatabaseHandler.KEY_GEN_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (toWhichClass != null) {
            delegate.setContentView(R.layout.activity_farmer_search);
        } else {
            delegate.setContentView(R.layout.fingerprint);
        }

        ButterKnife.bind(this);

        delegate.setSupportActionBar(toolbar);
        delegate.getSupportActionBar().setDisplayShowHomeEnabled(true);
        delegate.getSupportActionBar().setDisplayShowTitleEnabled(true);

        toolbar.setTitleTextColor(Color.WHITE);

        mContext = FarmerSearchActivity.this;

        initView();
        initListeners();
        initData();
        // lets start detecting NDEF message using foreground mode
        setDetecting(true);

    }

    private void initView() {
        spinner = (Spinner) findViewById(R.id.spinner);
        tveriId = (TextView) findViewById(R.id.tVeriId);
        tvInstructions = (TextView) findViewById(R.id.tvInstructions);
        veriId = (EditText) findViewById(R.id.etVeriId);
        register = (Button) findViewById(R.id.register);
        getFingerServer = (Button) findViewById(R.id.bgetFingerServer);
        validate = (Button) findViewById(R.id.validate);
        calibration = (Button) findViewById(R.id.calibration);
        back = (Button) findViewById(R.id.backRegister);
        fingerprintImage = (ImageView) findViewById(R.id.fingerprintImage);
        ivInstructions = (ImageView) findViewById(R.id.ivInstructions);

        spinner.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
        validate.setVisibility(View.GONE);
        back.setVisibility(View.GONE);

        if (toWhichClass != null) {
            tvInstructions.setVisibility(View.VISIBLE);
            ivInstructions.setVisibility(View.VISIBLE);
        }

    }

    private void initData() {
        db = new DatabaseHandler(mContext);
    }

    private void writeToFile(byte[] data) {
        String dir = rootPath + File.separator + "fingerprint_image";
        File dirPath = new File(dir);
        if (!dirPath.exists()) {
            dirPath.mkdir();
        }

        filePath = dir + "/" + System.currentTimeMillis() + ".bmp";
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initListeners() {
        // getFingerServer.setOnClickListener(this);
        register.setOnClickListener(this);
        validate.setOnClickListener(this);
        calibration.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.register:
                // asyncFingerprintVeri.register();
                break;
            case R.id.bgetFingerServer:
                final String searchedGenID = veriId.getText().toString();
                if (searchedGenID.trim().length() == Config.FARMER_ID_LENGTH) {
                    List<RegisteredFarmer> registeredFarmerList;

                    registeredFarmerList = getRegisteredFarmers(toWhichClass);

                    RegisteredFarmer registeredFarmer = searchFarmer(searchedGenID, registeredFarmerList);

                    match = (registeredFarmer != null);
                    firstName = registeredFarmer.getFirstName();
                    lastName = registeredFarmer.getLastName();
                    farmerId = registeredFarmer.getFarmerId();
                    genId = registeredFarmer.getGenId();

                    if (match) {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                showAlert2(searchedGenID, searchedGenID, firstName, lastName);

                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                String farmerNotFound = getString(R.string.card_no_not_found, searchedGenID);
                                MyAlerts.genericDialog(mContext, farmerNotFound);
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            MyAlerts.genericDialog(mContext, R.string.invalid_farmer_id);
                        }
                    });
                }

                break;
            case R.id.validate:
                break;
            case R.id.backRegister:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * @param toWhichClass
     * @return
     */
    private List<RegisteredFarmer> getRegisteredFarmers(String toWhichClass) {
        Log.i(TAG, "toWhichClass: " + toWhichClass);

        List<RegisteredFarmer> registeredFarmerList;
        if (toWhichClass.equals("fingerprint_register")) {

            registeredFarmerList = db.getUnFingerprintedFarmers();
            for (RegisteredFarmer cn : registeredFarmerList) {
                Log.i("Farmer ID: " + cn.getFarmerId(), "gen_id: " + cn.getGenId());
            }
        } else if (toWhichClass.equals("show_intent")) {
            registeredFarmerList = db.getNotContractedFarmers();
        } else if (toWhichClass.equals(Config.SIGN_ACTIVITY)) {
            registeredFarmerList = db.getShowIntentAndNotContractedFarmers();
        } else if (toWhichClass.equals("upload_inputs")) {
            registeredFarmerList = db.getCreditFarmers();
        } else {

            registeredFarmerList = db.getAllRegisteredFarmers();
        }
        return registeredFarmerList;
    }

    /**
     * Showing Dialog
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Fetching fingerprint. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            ;
            return true;

        }
        return super.onKeyDown(keyCode, event);
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

    private class UploadCollectedInputs extends AsyncTask<Void, Void, Void> {

        public UploadCollectedInputs() {
        }

        private String result2;
        ConnectionDetector cd2;

        @Override
        protected Void doInBackground(Void... arg0) {

            cd2 = new ConnectionDetector(mContext);
            if (cd2.isConnectingToInternet()) {
                // download all inputs from server
                // if (db.getSignedDocsCount() > 0) {
                InputStream is = null;
                // Log.e("companyID: " + doc.getCompanyId() + "FID: " + doc.getGradeId(),
                // "Date: " + doc.getSignDate() + "UID: " + doc.getUserId());
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    Log.e("", Config.UPLOAD_COLLECTED_INPUTS);
                    HttpPost httppost = new HttpPost(Config.UPLOAD_COLLECTED_INPUTS);
                    // send companyId

                    List<NameValuePair> nameValuePairs = null;
                    nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("assfid", assfid));
                    nameValuePairs
                            .add(new BasicNameValuePair(DatabaseHandler.KEY_USER_ID, Preferences.USER_ID));
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    nameValuePairs.add(new BasicNameValuePair(DatabaseHandler.KEY_COLLECT_DATE, dateFormat.format(date)));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();
                } catch (Exception e) {
                    Log.e("Exception: ", e.toString());
                    // Toast.makeText(mContext,
                    // "Invalid IP Address",Toast.LENGTH_LONG).show();
                    // finish();
                }
                result2 = null;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result2 = sb.toString();
                    //Log.e("Collect Response server:", result2);
                } catch (Exception e) {
                    Log.e("Ex Sign: ", e.toString());
                }
                // }

            } else {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result); // To change body of generated methods,
            // choose Tools | Templates.
            showAlert(result2);
        }

    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void showAlert2(String cardNo, final String genID, String firstName, String lastName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        ImageView farmerImage = new ImageView(mContext);

        String imageLocation = Config.SD_CARD_PATH + "/farmer_details/" + genID;

        if (new File(imageLocation).exists()) {

            famerPhoto = BitmapFactory.decodeFile(imageLocation);
            farmerImage.setImageBitmap(famerPhoto);
        } else {

            farmerImage.setImageResource(R.drawable.ic_account_circle_black);
        }

        builder.setView(farmerImage);

        String finalMessage = "FARMER ID:  " + genID + "\nCARD NO: " + cardNo + "\nFIRST NAME: " + firstName + "\nLAST NAME:  " + lastName;

        builder.setMessage(finalMessage).setTitle("Farmer Details").setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (toWhichClass.equals("upload_inputs")) {
                            Log.e("Towhich: ", toWhichClass);

                            Date date = new Date();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String collectDate = dateFormat.format(date);
                            Log.i("gen_id: " + genID, "genId: " + genID);
                            if (genID.equals(genID)) {
                                // to fingerprint
                                startActivity(new Intent(mContext, TPS350FingerprintVerificationActivity.class)
                                        .putExtra(DatabaseHandler.KEY_ORDER_ID, orderId).putExtra(DatabaseHandler.KEY_COLLECT_DATE, collectDate)
                                        .putExtra(DatabaseHandler.KEY_USER_ID, Preferences.USER_ID).putExtra(Config.TO_ACTIVITY, Config.FINGERPRINT_VERIFICATION)
                                        .putExtra(DatabaseHandler.KEY_GEN_ID, genID));
                            } else {
                                ToastUtil.showToast(mContext, "Wrong Farmer");
                            }
                        } else if (toWhichClass.equals("editFarmer")) {
                            Intent mapping = new Intent(mContext, FarmSelectionActitivty.class);

                            mapping.putExtra("to_which", "editFarmer");
                            mapping.putExtra("farmer_id", farmerId);
                            mapping.putExtra("farmer_info", farmerId);
                            mapping.putExtra(DatabaseHandler.KEY_GEN_ID, genID);

                            startActivity(mapping);
                        }  else if (toWhichClass.equals("mapping")) {
                            Intent mapping = new Intent(mContext, FarmSelectionActitivty.class);

                            mapping.putExtra("to_which", "mapping");
                            mapping.putExtra("farmer_id", farmerId);
                            mapping.putExtra(DatabaseHandler.KEY_GEN_ID, genID);

                            startActivity(mapping);
                        } else if (toWhichClass.equals("forms")) {
                            Intent allForms = new Intent(mContext, FarmSelectionActitivty.class);
                            // allForms.putExtra("farmer_id",genId);
                            SaveValFarmerID(mContext, "val_gen_id", genID);
                            startActivity(allForms);
                        } else if (toWhichClass.equals("farm_update")) {
                            Intent farmUpdate = new Intent(mContext, FarmSelectionActitivty.class);
                            farmUpdate.putExtra("to_which", "farm_update");
                            farmUpdate.putExtra("farmer_id", farmerId);
                            farmUpdate.putExtra(DatabaseHandler.KEY_GEN_ID, genID);

                            startActivity(farmUpdate);
                        } else if (toWhichClass.equals(Config.FINGERPRINT_CAPTURE)) {
                            //
                            Intent fingerprintRegister = new Intent(mContext, FingerprintActivityLeft.class)
                                    .putExtra(Config.TO_ACTIVITY, Config.FINGERPRINT_CAPTURE);
                            fingerprintRegister.putExtra(DatabaseHandler.KEY_GEN_ID, genID);
                            fingerprintRegister.putExtra("farmer_id", farmerId);
                            startActivity(fingerprintRegister);
                        } else if (toWhichClass.equals(Config.SIGN_ACTIVITY)) {
                            ConnectionDetector cd = new ConnectionDetector(mContext);
                            if (cd.isConnectingToInternet()) {

                                SignedDoc signedDoc = new SignedDoc(farmerId, Preferences.USER_ID, Preferences.COMPANY_ID);
                                uploadSignedDocOnline(signedDoc);

                            } else {

                                db.addSignedDoc(new SignedDoc(farmerId, Preferences.USER_ID, Preferences.COMPANY_ID));
                                MyAlerts.backToDashboardDialog(mContext, R.string.saved_offline);
                            }
                        }
                        if (toWhichClass.equals("show_intent")) {
                            ConnectionDetector cd = new ConnectionDetector(mContext);
                            if (cd.isConnectingToInternet()) {

                                ShowIntent showIntent = new ShowIntent(farmerId, Preferences.USER_ID, Preferences.COMPANY_ID);
                                uploadShowIntentOnline(showIntent);

                            } else {

                                db.addShowIntent(new ShowIntent(farmerId, Preferences.USER_ID, Preferences.COMPANY_ID));
                                MyAlerts.backToDashboardDialog(mContext, R.string.saved_offline);
                            }
                        }
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Upload SignedDoc online
     *
     * @param signedDoc
     */
    private void uploadSignedDocOnline(SignedDoc signedDoc) {
        new UploadSignedDocs(mContext, signedDoc).execute();
    }

    /**
     * Upload ShowIntent online
     *
     * @param showIntent
     */
    private void uploadShowIntentOnline(ShowIntent showIntent) {
        new UploadShowsIntent(mContext, showIntent).execute();
    }


    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message).setTitle("NOTICE").setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void SaveValFarmerID(Context ctx, String Key, String value) {
        ctx.getSharedPreferences("mypref", Context.MODE_PRIVATE).edit().putString(Key, value).commit();
    }

    public static String getPrefrence(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        String result = pref.getString(key, null);
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_farmer_search, menu);
        menu.findItem(R.id.menu_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    //@Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_farmer_search, menu);
        menu.findItem(R.id.menu_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_search:

                registeredFarmerList = getRegisteredFarmers(toWhichClass);//TODO:Benson Update to remove redundancy on line 674

                Log.i(TAG, "farmer list size: " + registeredFarmerList.size());

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                searchFarmerDialog = inflater.inflate(R.layout.search_farmer_dialog, null);

                spFarmers = (SearchableSpinner) searchFarmerDialog.findViewById(R.id.spFarmers);


                spFarmers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
//                        selectedFarmer = registeredFarmerList.get(position);
                        selectedFarmer = (RegisteredFarmer) parent.getAdapter().getItem(position);

                        Log.e(TAG, "onItemSelected1: "+selectedFarmer.toString() );
                        Log.e(TAG, "onItemSelected2: "+parent.getSelectedItem().toString());
                        Log.e(TAG, "onItemSelected3: "+parent.getItemAtPosition(position).toString());
                        Log.e(TAG, "onItemSelected4: "+parent.getAdapter().getClass());
                        Log.e(TAG, "onItemSelected4: "+((TextView)view.findViewById(android.R.id.text1)).getText());
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

//        farmersAdapter = new CustomSpinnerAdapter(mContext, R.layout.search_farmer_item, registeredFarmers);
        farmersAdapter = new MyArrayAdapter(mContext, android.R.layout.simple_spinner_item);
        farmersAdapter.addAll(registeredFarmers);
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
                        Log.e(TAG, "Ok-selected: "+selectedFarmer.toString() );
                        if (selectedFarmer == null) {
                            MyAlerts.genericDialog(mContext, R.string.select_farmer_first);
                            return;
                        }

                        String genID = selectedFarmer.getGenId();
                        String farmerId = selectedFarmer.getFarmerId();
                        Log.e("Towhich: ", toWhichClass);

                        if (toWhichClass.equals("upload_inputs")) {


                            Date date = new Date();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String collectDate = dateFormat.format(date);
                            Log.i("gen_id: " + genID, "genId: " + genID);
                            if (genID.equals(genID)) {
                                // to fingerprint
                                startActivity(new Intent(mContext, TPS350FingerprintVerificationActivity.class)
                                        .putExtra(DatabaseHandler.KEY_ORDER_ID, orderId).putExtra(DatabaseHandler.KEY_COLLECT_DATE, collectDate)
                                        .putExtra(DatabaseHandler.KEY_USER_ID, Preferences.USER_ID).putExtra(Config.TO_ACTIVITY, Config.FINGERPRINT_VERIFICATION)
                                        .putExtra(DatabaseHandler.KEY_GEN_ID, genID));
                            } else {
                                ToastUtil.showToast(mContext, "Wrong Farmer");
                            }
                        } else if (toWhichClass.equals("editFarmer")) {
                            Intent mapping = new Intent(mContext, UpdateFarmerActivity.class);

                            mapping.putExtra("to_which", "editFarmer");
                            mapping.putExtra("farmer_id", farmerId);
                            mapping.putExtra("farmer_info", selectedFarmer);
                            mapping.putExtra(DatabaseHandler.KEY_GEN_ID, genID);

                            startActivity(mapping);
                        }  else if (toWhichClass.equals("mapping")) {
                            Intent mapping = new Intent(mContext, FarmSelectionActitivty.class);

                            mapping.putExtra("to_which", "mapping");
                            mapping.putExtra("farmer_id", farmerId);
                            mapping.putExtra(DatabaseHandler.KEY_GEN_ID, genID);

                            startActivity(mapping);
                        } else if (toWhichClass.equals("forms")) {
                            Intent allForms = new Intent(mContext, FarmSelectionActitivty.class);
                            // allForms.putExtra("farmer_id",genId);
                            SaveValFarmerID(mContext, "val_gen_id", genID);
                            startActivity(allForms);
                        } else if (toWhichClass.equals("farm_update")) {
                            Intent farmUpdate = new Intent(mContext, FarmSelectionActitivty.class);
                            farmUpdate.putExtra("to_which", "farm_update");
                            farmUpdate.putExtra("farmer_id", farmerId);
                            farmUpdate.putExtra(DatabaseHandler.KEY_GEN_ID, genID);

                            startActivity(farmUpdate);
                        } else if (toWhichClass.equals(Config.FINGERPRINT_CAPTURE)) {
                            //
                            Intent fingerprintRegister = new Intent(mContext, FingerprintActivityLeft.class)
                                    .putExtra(Config.TO_ACTIVITY, Config.FINGERPRINT_CAPTURE);
                            fingerprintRegister.putExtra(DatabaseHandler.KEY_GEN_ID, genID);
                            fingerprintRegister.putExtra("farmer_id", farmerId);
                            startActivity(fingerprintRegister);
                        } else if (toWhichClass.equals(Config.SIGN_ACTIVITY)) {
                            ConnectionDetector cd = new ConnectionDetector(mContext);
                            if (cd.isConnectingToInternet()) {

                                SignedDoc signedDoc = new SignedDoc(farmerId, Preferences.USER_ID, Preferences.COMPANY_ID);
                                uploadSignedDocOnline(signedDoc);

                            } else {

                                db.addSignedDoc(new SignedDoc(farmerId, Preferences.USER_ID, Preferences.COMPANY_ID));
                                MyAlerts.backToDashboardDialog(mContext, R.string.saved_offline);
                            }
                        } else if (toWhichClass.equals("recovery")) {
                            Intent farmerHistory = new Intent(mContext, FarmerHistoryActivity.class);
                            farmerHistory.putExtra("farmer_id", farmerId);
                            startActivity(farmerHistory);
                        }
                        if (toWhichClass.equals("show_intent")) {
                            ConnectionDetector cd = new ConnectionDetector(mContext);
                            if (cd.isConnectingToInternet()) {

                                ShowIntent showIntent = new ShowIntent(farmerId, Preferences.USER_ID, Preferences.COMPANY_ID);
                                uploadShowIntentOnline(showIntent);

                            } else {

                                db.addShowIntent(new ShowIntent(farmerId, Preferences.USER_ID, Preferences.COMPANY_ID));
                                MyAlerts.backToDashboardDialog(mContext, R.string.saved_offline);
                            }
                        }
                    }
                }).show();
    }

    private RegisteredFarmer searchFarmer(String searchedCardNo, List<RegisteredFarmer> registeredFarmerList) {
        RegisteredFarmer farmer = null;
        for (RegisteredFarmer registeredFarmer : registeredFarmerList) {
            Log.i(TAG, "SEARCH FARMER: " + searchedCardNo + " | " + registeredFarmer.getCardNo());
            if (searchedCardNo.equals(registeredFarmer.getCardNo())) {
                Log.i(TAG, "MATCHED FARMER: " + searchedCardNo + " | " + registeredFarmer.getCardNo());
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
        //
        // clearList();
    }

    /**
     * Something was read via NFC, but it was not an NDEF message.
     * <p>
     * Handling this situation is out of scope of this project.
     */

    @Override
    protected void readNonNdefMessage(Tag tag) {
        // toast(getString(R.string.readNonNDEFMessage));

        String[] techList = tag.getTechList();
        String searchedTech = Ndef.class.getName();

        // for (String tech : techList) {
        Log.d(TAG, "tech type1: " + searchedTech);
        if (searchedTech.equals("android.nfc.tech.Ndef")) {
            Log.d(TAG, "tech type2: " + searchedTech);
            new NdefReaderTask().execute(tag);
            // break;
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
        private SimpleDateFormat timeFormat;

    /*    public void writeTag(Tag tag, String tagText) {
            MifareUltralight ultralight = MifareUltralight.get(tag);
            try {

                ultralight.connect();
                // ultralight.writePage(4,
                // "BA14".getBytes(Charset.forName("US-ASCII")));
                ultralight.writePage(4, "0NY5".getBytes(Charset.forName("US-ASCII")));
                ultralight.writePage(5, "5D07".getBytes(Charset.forName("US-ASCII")));
                ultralight.writePage(6, "45F6".getBytes(Charset.forName("US-ASCII")));
                ultralight.writePage(7, "50C6".getBytes(Charset.forName("US-ASCII")));
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing MifareUltralight...", e);
            } finally {
                try {
                    ultralight.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException while closing MifareUltralight...", e);
                }
            }
        }*/

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
            Log.d(TAG, "async task running");
            Tag tag = params[0];

            return readTag(tag);
        }

        @Override
        protected void onPostExecute(String dirtyCardNo) {
            try {
                Log.d("NFC Data:", dirtyCardNo + " | " + dirtyCardNo.trim().length());

                String cardNo = CardUtils.cleanCardNo(dirtyCardNo);

                if (cardNo.length() >= Config.CARD_NO_LENGTH) {

                    toActivitySelection(cardNo);

                } else {
                    MyAlerts.genericDialog(mContext, R.string.invalid_card_no);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }


        private void toActivitySelection(String NFCCardNo) {
            List<RegisteredFarmer> registeredFarmerList = null;

            registeredFarmerList = getRegisteredFarmers(toWhichClass);

            RegisteredFarmer registeredFarmer = searchFarmer(NFCCardNo, registeredFarmerList);

            match = (registeredFarmer != null);
            Log.i(TAG, "NFC Match: " + match);

            String cardNo = null;

            if (match) {
                firstName = registeredFarmer.getFirstName();
                lastName = registeredFarmer.getLastName();
                farmerId = registeredFarmer.getFarmerId();
                genId = registeredFarmer.getGenId();
                cardNo = registeredFarmer.getCardNo();

                showAlert2(cardNo, genId, firstName, lastName);

            } else {
                if (toWhichClass.equals("show_intent")) {
                    MyAlerts.genericDialog(mContext, getString(R.string.farmer_may_have_already_shown_intent));
                } else if (toWhichClass.equals(Config.SIGN_ACTIVITY)) {
                    MyAlerts.genericDialog(mContext, getString(R.string.farmer_may_have_already_signed_doc));
                } else {
                    String farmerNotFound = getString(R.string.card_no_not_found, cardNo);
                    MyAlerts.genericDialog(mContext, farmerNotFound);
                }
            }


        }
    }

    /**
     * NFC feature was found and is currently enabled
     */

    @Override
    protected void onNfcStateEnabled() {
        ToastUtil.showToast(getApplicationContext(), R.string.nfc_available_and_enabled);
    }

    /**
     * NFC feature was found but is currently disabled
     */

    @Override
    protected void onNfcStateDisabled() {
        ToastUtil.showToast(getApplicationContext(), R.string.nfc_available_but_disabled);
    }

    /**
     * NFC setting changed since last check. For example, the user enabled NFC
     * in the wireless settings.
     */

    @Override
    protected void onNfcStateChange(boolean enabled) {
        if (enabled) {
            ToastUtil.showToast(getApplicationContext(), R.string.nfc_enabled);
        } else {
            ToastUtil.showToast(getApplicationContext(), R.string.nfc_disabled);
        }
    }

    /**
     * This device does not have NFC hardware
     */

    @Override
    protected void onNfcFeatureNotFound() {
        ToastUtil.showToast(getApplicationContext(), R.string.nfc_not_found);
    }

    @Override
    protected void onTagLost() {
        // toast(getString(R.string.tagLost));
    }

    @Override
    protected void readNdefMessage(Message message) {

    }

    private class MyFarmerData {
        private String farmerId;
        private String firstName;
        private String lastName;
        private String cardNo;

        public MyFarmerData(String farmerId, String firstName, String lastName, String cardNo) {
            this.farmerId = farmerId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.cardNo = cardNo;
        }

        public String getFarmerId() {
            return farmerId;
        }

        public void setFarmerId(String farmerId) {
            this.farmerId = farmerId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }
    }

    public class CustomSpinnerAdapter extends ArrayAdapter<RegisteredFarmer> {
//    public class CustomSpinnerAdapter extends MyArrayAdapter<RegisteredFarmer> {

        private final LayoutInflater mInflater;
        private Context context = null;
        private List<RegisteredFarmer> registeredFarmers = null;
        private final int mResource;

    List<RegisteredFarmer> filteredList = new ArrayList<>();

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
