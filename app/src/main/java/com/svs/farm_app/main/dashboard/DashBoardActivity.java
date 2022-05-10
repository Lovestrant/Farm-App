package com.svs.farm_app.main.dashboard;

import static com.svs.farm_app.utils.Config.LEFT_THUMB;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.artifex.mupdfdemo.ChoosePDFActivity;
import com.authentication.activity.FP05FingerprintCaptureActivity;
import com.authentication.activity.FingerprintActivityLeft;
import com.svs.farm_app.main.Survey.Survey;
import com.authentication.activity.TPS350FingerprintCaptureActivity;
import com.authentication.activity.TPS900FingerprintCaptureActivity;
import com.authentication.utils.VolleySingleton;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.svs.farm_app.R;
import com.svs.farm_app.asynctask.downloads.DownloadDataCollected;
import com.svs.farm_app.asynctask.downloads.DownloadFoliarFeed;
import com.svs.farm_app.asynctask.downloads.DownloadHerbicides;
import com.svs.farm_app.asynctask.downloads.DownloadOfficerTraining;
import com.svs.farm_app.asynctask.downloads.DownloadOtherCrops;
import com.svs.farm_app.asynctask.downloads.DownloadPesticides;
import com.svs.farm_app.asynctask.downloads.DownloadSubVillages;
import com.svs.farm_app.asynctask.downloads.DownloadTrainingTypes;
import com.svs.farm_app.asynctask.downloads.DownloadUserVillages;
import com.svs.farm_app.asynctask.downloads.DownloadVillages;
import com.svs.farm_app.asynctask.downloads.DownloadWackFarmers;
import com.svs.farm_app.asynctask.downloads.DownloadWards;
import com.svs.farm_app.asynctask.uploads.UploadAssignedTrainings;
import com.svs.farm_app.asynctask.uploads.UploadCollectedInputs;
import com.svs.farm_app.asynctask.uploads.UploadFarmAreaUpdate;
import com.svs.farm_app.asynctask.uploads.UploadFarmAssMajor;
import com.svs.farm_app.asynctask.uploads.UploadFarmAssMedium;
import com.svs.farm_app.asynctask.uploads.UploadFarmIncome;
import com.svs.farm_app.asynctask.uploads.UploadFarmOtherCrops;
import com.svs.farm_app.asynctask.uploads.UploadFarmProduction;
import com.svs.farm_app.asynctask.uploads.UploadFarmerTimes;
import com.svs.farm_app.asynctask.uploads.UploadFarmers;
import com.svs.farm_app.asynctask.uploads.UploadFingerFiveForm;
import com.svs.farm_app.asynctask.uploads.UploadFingerFourForm;
import com.svs.farm_app.asynctask.uploads.UploadFingerOneForm;
import com.svs.farm_app.asynctask.uploads.UploadFingerThreeForm;
import com.svs.farm_app.asynctask.uploads.UploadFingerTwoForm;
import com.svs.farm_app.asynctask.uploads.UploadGerminations;
import com.svs.farm_app.asynctask.uploads.UploadInputCollectionRecapture;
import com.svs.farm_app.asynctask.uploads.UploadMappedFarms;
import com.svs.farm_app.asynctask.uploads.UploadMolassesTrapCatches;
import com.svs.farm_app.asynctask.uploads.UploadPlantingRains;
import com.svs.farm_app.asynctask.uploads.UploadProductPurchase;
import com.svs.farm_app.asynctask.uploads.UploadReRegisteredFarmers;
import com.svs.farm_app.asynctask.uploads.UploadScouting;
import com.svs.farm_app.asynctask.uploads.UploadShowsIntent;
import com.svs.farm_app.asynctask.uploads.UploadSignedDocs;
import com.svs.farm_app.asynctask.uploads.UploadTransportHouseToMarket;
import com.svs.farm_app.asynctask.uploads.UploadUpdatedFarmers;
import com.svs.farm_app.asynctask.uploads.UploadYieldEstimates;
import com.svs.farm_app.entities.AssignedTrainings;
import com.svs.farm_app.entities.CollectedInputs;
import com.svs.farm_app.entities.Farm;
import com.svs.farm_app.entities.FarmAssFormsMajor;
import com.svs.farm_app.entities.FarmAssFormsMedium;
import com.svs.farm_app.entities.FarmIncome;
import com.svs.farm_app.entities.FarmOtherCrops;
import com.svs.farm_app.entities.FarmProduction;
import com.svs.farm_app.entities.FarmerTime;
import com.svs.farm_app.entities.Farmers;
import com.svs.farm_app.entities.FingerFive;
import com.svs.farm_app.entities.FingerFour;
import com.svs.farm_app.entities.FingerOne;
import com.svs.farm_app.entities.FingerThree;
import com.svs.farm_app.entities.FingerTwo;
import com.svs.farm_app.entities.Germination;
import com.svs.farm_app.entities.MappedFarm;
import com.svs.farm_app.entities.MolassesTrapCatch;
import com.svs.farm_app.entities.PlantingRains;
import com.svs.farm_app.entities.ProductPurchase;
import com.svs.farm_app.entities.ReRegisteredFarmers;
import com.svs.farm_app.entities.RegisteredFarmer;
import com.svs.farm_app.entities.Scouting;
import com.svs.farm_app.entities.ShowIntent;
import com.svs.farm_app.entities.SignedDoc;
import com.svs.farm_app.entities.TransportHseToMarket;
import com.svs.farm_app.entities.UpdateFarmArea;
import com.svs.farm_app.entities.UserVillage;
import com.svs.farm_app.entities.YieldEstimate;
import com.svs.farm_app.farmersearch.FarmerSearchActivity;
import com.svs.farm_app.main.FarmAssPlotSelectionActitivty;
import com.svs.farm_app.main.SettingsActivity;
import com.svs.farm_app.main.calendar.CalenderActivity;
import com.svs.farm_app.main.farm_inputs.FarmInputsActivity;
import com.svs.farm_app.main.recapture_details.ReRegistrationActivity;
import com.svs.farm_app.main.registration.RegisterFarmerActivity;
import com.svs.farm_app.main.show_intent.ShowIntentActivity;
import com.svs.farm_app.main.sign_document.SignDocActivity;
import com.svs.farm_app.main.training_attendance.TrainingActivity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class DashBoardActivity extends BaseClass {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;

    public static final int REGISTER_FARMER = 0;
    public static final int EDIT_FARMER = 1;
    public static final int SHOW_INTENT = 2;
    public static final int SIGN_DOCUMENT = 3;
    public static final int FARM_MAPPING = 4;
    public static final int UPDATE_FARM_AREA_ESTIMATE = 5;
    public static final int TRAINING_CALENDAR = 6;
    public static final int TRAINING_MATERIALS = 7;
    public static final int TRAINING_ACTIVITY = 8;
    public static final int FARM_INPUT_COLLECTION = 9;
    public static final int FARM_ASSESSMENT = 10;
    public static final int SODA = 11;
    public static final int RE_REGISTRATION = 12;
    public static final int RECOVERY = 13;
    public static final int SURVEY = 14;

    public static int max_upload_counts = 0;
    public static final int MAX_DOWNLOAD_COUNTS = 20;

    private ConnectionDetector cd;
    private static ProgressDialog dialog;
    private String companyID;
    private DatabaseHandler db;
    private SimpleDateFormat dateFormat;
    private Date date;
    @BindView(R.id.gridview)
    GridView gridview;
    private File f2;
    private String userID;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private static final String TAG = DashBoardActivity.class.getSimpleName();
    private DashBoardActivity mContext;

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Config.SD_CARD_PATH + Config.IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Error: ", "Oops! Failed create " + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStampString = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStampString + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        this.mContext = DashBoardActivity.this;

        initView();
        initData();
        initListeners();

    }

    private void initData() {

        cd = new ConnectionDetector(mContext);

        companyID = Preferences.COMPANY_ID;
        userID = Preferences.USER_ID;
        db = new DatabaseHandler(mContext);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        date = new Date();
        dateFormat.format(date);
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Welcome " + Preferences.USERNAME);

        ImageAdapter image_adapter = new ImageAdapter(mContext);
        gridview.setAdapter(image_adapter);
        dialog = new ProgressDialog(mContext);
        dialog.setCancelable(false);
    }

    public void initListeners() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position) {
                    case REGISTER_FARMER:
                        if ((db.getTableCount(db.TABLE_VILLAGES) > 0) && (db.getTableCount(db.TABLE_SUBVILLAGES) > 0)
                                && (db.getTableCount(DatabaseHandler.TABLE_OTHER_CROPS) > 0)) {
                            captureImage();
                            //startActivity(new Intent(mContext, RegisterFarmerActivity.class));
                            //startActivity(new Intent(mContext, FarmerRegistrationActivity.class));
                        } else {
                            showDialogDownloadData(R.string.download_areas_first);
                        }
                        break;
                    case EDIT_FARMER:
                        if ((db.getTableCount(db.TABLE_REGISTERED_FARMERS) > 0)) {
                            Intent mapping = new Intent(mContext, FarmerSearchActivity.class);
                            mapping.putExtra(Config.TO_ACTIVITY, "editFarmer");
                            startActivity(mapping);
                        } else {
                            showDialogDownloadData(R.string.download_farmer_data_first);
                        }
                        break;
                    case SHOW_INTENT:
                        if ((db.getTableCount(db.TABLE_REGISTERED_FARMERS) > 0)) {
                            Intent showIntent = new Intent(mContext, ShowIntentActivity.class);

                            startActivity(showIntent);
                        } else {
                            showDialogDownloadData(R.string.download_farmer_data_first);
                        }
                        break;
                    case SIGN_DOCUMENT:
                        if ((db.getTableCount(db.TABLE_REGISTERED_FARMERS) > 0)) {
                            Intent sign = new Intent(mContext, SignDocActivity.class);

                            startActivity(sign);
                        } else {
                            showDialogDownloadData(R.string.download_farmer_data_first);
                        }
                        break;

                    case FARM_MAPPING:
                        if ((db.getTableCount(db.TABLE_REGISTERED_FARMERS) > 0)) {

                            Intent mapping = new Intent(mContext, FarmerSearchActivity.class);
                            mapping.putExtra(Config.TO_ACTIVITY, "mapping");
                            startActivity(mapping);

                        } else {

                            showDialogDownloadData(R.string.download_farmer_data_first);

                        }
                        break;
                    case UPDATE_FARM_AREA_ESTIMATE:
                        if ((db.getTableCount(db.TABLE_FARMS) > 0) && (db.getTableCount(db.TABLE_REGISTERED_FARMERS) > 0)) {
                            Intent farmerSearch = new Intent(mContext, FarmerSearchActivity.class);
                            farmerSearch.putExtra(Config.TO_ACTIVITY, "farm_update");

                            startActivity(farmerSearch);
                        } else {
                            showDialogDownloadData(R.string.download_farmer_farm_data_first);
                        }
                        break;
                    case TRAINING_CALENDAR:
                        Intent calender = new Intent(mContext, CalenderActivity.class);
                        startActivity(calender);
                        break;
                    case TRAINING_MATERIALS:
                        Intent fileChooser = new Intent(mContext, ChoosePDFActivity.class);
                        startActivity(fileChooser);
                        break;

                    case TRAINING_ACTIVITY:

                        if (db.getTrainingCountToday() > 0) {
                            if ((db.getTableCount(db.TABLE_REGISTERED_FARMERS) > 0)) {
                                Intent trainingActivity = new Intent(mContext, TrainingActivity.class);

                                startActivity(trainingActivity);
                            } else {
                                showDialogDownloadData(R.string.download_farmer_data_first);
                            }
                        } else {
                            MyAlerts.genericDialog(mContext, R.string.no_trainings_available);
                        }

                        break;
                    case FARM_INPUT_COLLECTION:
                        if ((db.getTableCount(db.TABLE_REGISTERED_FARMERS) > 0)) {
                            Intent verify = new Intent(mContext, FarmInputsActivity.class);

                            startActivity(verify);
                        } else {
                            showDialogDownloadData(R.string.download_farmer_data_first);
                        }
                        break;

                    case FARM_ASSESSMENT:
                        List<UserVillage> userVillages = db.getVillageIdByUserId(Preferences.USER_ID);

                        String[] villagesArray = new String[userVillages.size()];

                        int j = 0;
                        for (UserVillage cn : userVillages) {
                            villagesArray[j] = cn.getVillageId();
                            j++;
                        }
                        String villages = TextUtils.join(",", villagesArray);

                        List<Farm> farmsList = db.getAllFarmsForAss(villages);
                        if (farmsList.size() > 0) {
                            Intent veri2 = new Intent(mContext, FarmAssPlotSelectionActitivty.class);
                            MyPrefrences.savePrefrence(mContext, "to_which2", "farm_assesment");

                            startActivity(veri2);
                        } else {
                            MyAlerts.genericDialog(mContext,
                                    "No Assessment Farms available \nDownload data first!");
                        }

                        break;
                    case SODA:
                        try {
                            Intent soda = getPackageManager().getLaunchIntentForPackage("com.techneos.soda.client.micro");
                            startActivity(soda);
                        } catch (Exception ex) {
                            Toast.makeText(mContext, "Install Soda", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case RE_REGISTRATION:
                        Intent reRegistration = new Intent(mContext, ReRegistrationActivity.class);
                        startActivity(reRegistration);
                        break;
                    case RECOVERY:
                        Intent recovery = new Intent(mContext, FarmerSearchActivity.class);
                        recovery.putExtra(Config.TO_ACTIVITY, "recovery");
                        startActivity(recovery);
                        break;
                    case SURVEY:
                        Intent SURVEY = new Intent(mContext, Survey.class);
                        startActivity(SURVEY);
                        break;

                }
            }
        });
        File farmer_details = new File(Config.SD_CARD_PATH + "/farmer_details");
        if (!farmer_details.exists()) {
            farmer_details.mkdir();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();

        int upload_total = getTotalUploads();

        if (upload_total > 0) {
            MenuItem uploadItem = menu.findItem(R.id.menu_upload_data);
            uploadItem.setTitle(upload_total + "\nUpload Data");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private int getTotalUploads() {
        return db.getTableCount(db.TABLE_RE_REGISTERED_FARMERS)
                + db.getTableCount(db.TABLE_ASSIGNED_TRAININGS)
                + db.getTableCount(db.TABLE_COLLECTED_INPUTS) + db.getTableCount(db.TABLE_FARMERS)
                + db.getTableCount(db.TABLE_FARMER_TIMES) + db.getTableCount(db.TABLE_FARM_INCOME)
                + db.getTableCount(db.TABLE_FARM_OTHER_CROPS) + db.getTableCount(db.TABLE_FINGER_FIVE)
                + db.getTableCount(db.TABLE_FINGER_FOUR) + db.getTableCount(db.TABLE_FINGER_TWO)
                + db.getTableCount(db.TABLE_MANUAL_INPUT_FINGERPRINT_RECAPTURE)
                + db.getTableCount(db.TABLE_FINGER_THREE) + db.getTableCount(db.TABLE_FARM_ASS_MAJOR)
                + db.getTableCount(db.TABLE_FARM_ASS_MEDIUM) + db.getTableCount(db.TABLE_GERMINATION)
                + db.getTableCount(db.TABLE_MOLASSES_TRAP_CATCHES) + db.getTableCount(db.TABLE_SCOUTING)
                + db.getTableCount(db.TABLE_SHOW_INTENT) + db.getTableCount(db.TABLE_PLANTING_RAINS)
                + db.getTableCount(db.TABLE_FARM_AREA_UPDATES) + db.getTableCount(db.TABLE_MAPPED_FARMS)
                + db.getTableCount(db.TABLE_SIGNED_DOCS) + db.getTableCount(db.TABLE_YIELD_ESTIMATE)
                + db.getTableCount(db.TABLE_FARM_PRODUCTION) + db.getTableCount(db.TABLE_FINGER_ONE)
                + db.getTableCount(db.TABLE_TRANS_HSE_TO_MARKET) + db.getTableCount(db.TABLE_MAPPED_FARMS)
                +db.getTableCount(db.TABLE_PRODUCT_PURCHASES)
                +db.getTableCount(db.TABLE_UPDATED_FARMERS);
    }

    String mCurrentPhotoPath;
    public static int uploadCounter;

    private void showToast(String message) {
        Toast.makeText(DashBoardActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void captureImage() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private final PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            TedBottomPicker.with(DashBoardActivity.this).show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                @Override
                public void onImageSelected(Uri uri) {
                    startCropImageActivity(uri);
                }
            });
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            showToast("Permission(s) denied ");
        }
    };

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(10, 10)
                .start(this);
    }

    private void saveImageToInternalStorage(Uri imageUri) {
        // Create an image file name
        String timeStampString = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = imageUri.getLastPathSegment();
        File file = writeFileOnInternalStorage(this, imageFileName, Config.IMAGE_DIRECTORY_NAME, imageUri);
        mCurrentPhotoPath = "file:" + file.getAbsolutePath();
        MyPrefrences.savePrefrence(mContext, "farmer_pic", file.getAbsolutePath());
        openFingerPrintActivity();
    }

    private void openFingerPrintActivity() {
        if (Preferences.DEVICE_MODEL.equals(Config.TPS350)) {
            Intent intent = new Intent(this, TPS350FingerprintCaptureActivity.class);
            intent.putExtra("which_thumb", LEFT_THUMB);
            startActivity(intent);
        } else if (Preferences.DEVICE_MODEL.equals(Config.TPS900)) {
            Log.i(TAG, "TPS900");
            Intent intent = new Intent(this, TPS900FingerprintCaptureActivity.class);
            intent.putExtra("which_thumb", LEFT_THUMB);
            startActivity(intent);
        } else if (Preferences.DEVICE_MODEL.equals(Config.COREWISE_V0)) {
            Intent intent = new Intent(this, FingerprintActivityLeft.class);
            intent.putExtra(Config.TO_ACTIVITY, Config.FINGERPRINT_CAPTURE);
            startActivity(intent);
        } else if (Preferences.DEVICE_MODEL.equals(Config.FP05)) {
            Intent intent = new Intent(this, FP05FingerprintCaptureActivity.class);
            intent.putExtra("which_thumb", LEFT_THUMB);
            startActivity(intent);
        } else {
            //MyAlerts.genericDialog(mContext, R.string.device_not_supported);
            startActivity(new Intent(mContext, RegisterFarmerActivity.class));
        }

        Log.i("Farmer Picture: ", MyPrefrences.getPrefrence(mContext, "farmer_pic"));
    }

    public File writeFileOnInternalStorage(Context context, String fileName, String path, Uri uri) {
        File dir = new File(context.getFilesDir(), path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            File file = new File(dir, fileName);
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileUtils.copyInputStreamToFile(inputStream, file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            startCropImageActivity(imageUri);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            switch (resultCode) {
                case RESULT_OK:
                    try {
                        saveImageToInternalStorage(result.getUri());
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("Please try again");
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE:
                    showToast("Image not added: " + result.getError());
                    break;
                default:
                    showToast("Please try again");
                    break;
            }
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    /*
     * @Override protected void onSaveInstanceState(Bundle savedInstanceState2)
	 * {
	 * 
	 * savedInstanceState2.putString("file_uri",
	 * getPrefrence(mContext, "imagePath"));
	 * super.onSaveInstanceState(savedInstanceState2); }
	 * 
	 * @Override protected void onRestoreInstanceState(Bundle
	 * savedInstanceState3) { fileUri =
	 * Uri.parse(getPrefrence(mContext,"imagePath")); f2 = new
	 * File(getPrefrence(mContext, "imagePath"));
	 * super.onRestoreInstanceState(savedInstanceState3); }
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_upload_data:
                int upload_total = getTotalUploads();
                if (upload_total > 0) {

                    uploadAll();
                } else {
                    Toast.makeText(mContext, "No data to sync", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.menu_download_data:
                downloadAll();
                break;

            case R.id.menu_check_status:
                startActivity(new Intent(mContext, SyncedDataDownloadActivity.class));

                break;

            case R.id.menu_check_sync:
                startActivity(new Intent(mContext, SyncedData.class));

                break;
            case R.id.menu_settings:
                startActivity(new Intent(mContext, SettingsActivity.class));

                break;
            default:
                return super.onOptionsItemSelected(item);
            //break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.finish();
    }

    private class DownloadLibrary extends AsyncTask<Void, Void, Void> {

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            if (cd.isConnectingToInternet()) {
                InputStream is = null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    Log.e("", Config.DOWNLOAD_LIBRARY);
                    HttpPost httppost = new HttpPost(Config.DOWNLOAD_LIBRARY);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("company_id", companyID));
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

                String result = null;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result = sb.toString();
                    Log.i("COMMA:Library Names", result);
                } catch (Exception e) {
                    Log.e("Ex Library: ", e.toString());
                }
                try {
                    if (result.length() > 0) {
                        List<String> namesList = Arrays.asList(result.split(","));
                        for (int i = 0; i < namesList.size(); i++) {
                            if (i == namesList.size() - 1) {
                                String last1 = namesList.get(i);
                                String last2 = last1.substring(0, last1.length() - 1);
                                new DownloadLibraryPdf(last2).execute();
                            }

                            new DownloadLibraryPdf(namesList.get(i)).execute();

                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "No Library data on server", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }
                } catch (Exception e) {

                    Log.e("ExceptionPDFLIB ", e.toString());

                }
            } else {

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

	/* get all pdf file names and download each */

    private class DownloadAllTrainMats extends AsyncTask<Void, Void, Void> {
        private final String TAG = DownloadAllTrainMats.class.getSimpleName();
        private DatabaseHandler db;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            if (cd.isConnectingToInternet()) {

                InputStream is = null;
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(Config.DOWNLOAD_LIBRARY + "?company_id=" + companyID).openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    //connection.setDoOutput(true);

                    /*String urlParameters = "company_id=" + companyID;

                    DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                    dos.writeBytes(urlParameters);
                    dos.flush();
                    dos.close();*/

                    Log.i(TAG, "CODE: " + connection.getResponseCode());

                    is = connection.getInputStream();

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                }

                String result = null;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result = sb.toString();
                    Log.i(TAG, "JSON:Pdf names" + result);
                } catch (Exception e) {
                    Log.e(TAG, "Ex Pdf Names: " + e.toString());
                    e.printStackTrace();
                }
                try {
                    if (result.length() > 0) {
                        JSONArray JA = new JSONArray(result);
                        JSONObject json;
                        String[] file_name = new String[JA.length()];

                        for (int i = 0; i < JA.length(); i++) {
                            json = JA.getJSONObject(i);
                            file_name[i] = json.getString("file_url");

                            Log.d(TAG, "PDF" + file_name[i]);
                            File pdfFile = new File(Config.SD_CARD_PATH + "/train_mats/" + file_name[i]);
                            if (ifFileExists(pdfFile) == false) {
                                new DownloadAllPdfs(file_name[i]).execute();
                            }
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "No AssignedTrainings data on server",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {

                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                }
            } else {

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            DashBoardActivity.downloadCount ++;
        }
    }

    private class DownloadAllPdfs extends AsyncTask<String, String, String> {

        private final String file_name;
        private int lenghtOfFile;

        public DownloadAllPdfs(String s) {
            this.file_name = s;
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            if (cd.isConnectingToInternet()) {
                Log.e("Downloading Pdfs.", "..");
                try {
                    Log.e("", Config.TRAIN_MATS);
                    URL url = new URL(Config.TRAIN_MATS + "/" + file_name);
                    // url
                    Log.e("PDF URE: ", url.toString());
                    HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                    conection.setRequestMethod("POST");
                    conection.setDoOutput(true);
                    conection.getOutputStream().write(companyID.getBytes("UTF-8"));
                    conection.connect();
                    // getting file length
                    lenghtOfFile = conection.getContentLength();

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(conection.getInputStream(), 8193);
                    File f = new File(Config.SD_CARD_PATH + "/train_mats/");
                    if (!f.exists()) {
                        f.mkdir();
                    }
                    // FileUtils.cleanDirectory(SD_CARD_PATH);

                    // Output stream to write file
                    OutputStream output = new FileOutputStream(Config.SD_CARD_PATH + "/train_mats/" + file_name);
                    Log.e("PDFiles: ", Config.SD_CARD_PATH + "/train_mats/" + file_name);
                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Old Pdf Error: ", e.getMessage());
                }
            } else {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }
    }

    private class DownloadLibraryPdf extends AsyncTask<String, String, String> {

        private final String file_name;
        private int lenghtOfFile;

        public DownloadLibraryPdf(String s) {
            this.file_name = s;
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            if (cd.isConnectingToInternet()) {
                //Log.e("Downloading Pdf Library.", "..");
                try {
                    Log.e("", Config.TRAIN_MATS);
                    URL url = new URL(Config.TRAIN_MATS + "/" + file_name);
                    Log.e("PDF URE: ", url.toString());
                    URLConnection conection = url.openConnection();
                    conection.connect();
                    // getting file length
                    lenghtOfFile = conection.getContentLength();

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8193);
                    File f = new File(Config.SD_CARD_PATH + "/train_mats/");
                    if (!f.exists()) {
                        f.mkdir();
                    }
                    // FileUtils.cleanDirectory(SD_CARD_PATH);

                    // Output stream to write file
                    OutputStream output = new FileOutputStream(Config.SD_CARD_PATH + "/train_mats/" + file_name);
                    Log.e("PDFiles: ", Config.SD_CARD_PATH + "/train_mats/" + file_name);
                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Library Error: ", e.getMessage());
                }
            } else {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }
    }

    private class DownloadAllImages2 extends AsyncTask<String, String, String> {
        private final String file_name;
        private int lenghtOfFile;

        public DownloadAllImages2(String s) {
            this.file_name = s;
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            if (cd.isConnectingToInternet()) {
                Log.i("in", "download images 2");
                try {
                    URL url = new URL(Config.DOWNLOAD_FARMER_PICS + "/" + file_name + ".jpg");
                    URLConnection conection = url.openConnection();
                    conection.connect();
                    // getting file length
                    lenghtOfFile = conection.getContentLength();

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    File f = new File(Config.SD_CARD_PATH + "/farmer_details/");
                    if (!f.exists())
                        f.mkdir();
                    // FileUtils.cleanDirectory(SD_CARD_PATH);

                    // Output stream to write file
                    OutputStream output = new FileOutputStream(Config.SD_CARD_PATH + "/farmer_details/" + file_name);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Images Error: ", e.getMessage());
                }
            } else {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }
    }

    private class DownloadAllLeftThumbFingerprints extends AsyncTask<String, String, String> {
        private final String file_name;
        private int lenghtOfFile;

        public DownloadAllLeftThumbFingerprints(String s) {
            this.file_name = s;
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            if (cd.isConnectingToInternet()) {
                Log.i("in", "download right");
                try {
                    URL url = new URL(Config.DOWNLOAD_FINGERPRINTS + "/" + file_name + ".bmp");
                    URLConnection conection = url.openConnection();
                    conection.connect();

                    lenghtOfFile = conection.getContentLength();

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    File fingerprintDirectory = new File(Config.FINGERPRINTS_PATH);
                    if (!fingerprintDirectory.exists())
                        fingerprintDirectory.mkdir();
                    // FileUtils.cleanDirectory(SD_CARD_PATH);

                    OutputStream output = new FileOutputStream(Config.FINGERPRINTS_PATH + file_name);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Images Error: ", e.getMessage());
                }
            } else {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }
    }

    private class DownloadAllRightThumbFingerprints extends AsyncTask<String, String, String> {
        private final String file_name;
        private int lenghtOfFile;

        public DownloadAllRightThumbFingerprints(String s) {
            this.file_name = s;
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            if (cd.isConnectingToInternet()) {
                Log.i("in", "download left");
                try {
                    URL url = new URL(Config.DOWNLOAD_FINGERPRINTS + "/" + file_name + ".bmp");
                    URLConnection conection = url.openConnection();
                    conection.connect();
                    // getting file length
                    lenghtOfFile = conection.getContentLength();

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    File f = new File(Config.FINGERPRINTS_PATH);
                    if (!f.exists())
                        f.mkdir();
                    // FileUtils.cleanDirectory(SD_CARD_PATH);

                    // Output stream to write file
                    OutputStream output = new FileOutputStream(
                            Config.FINGERPRINTS_PATH + file_name);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Images Error: ", e.getMessage());
                }
            } else {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }
    }

    // check if file exists
    private boolean ifFileExists(File file) {
        boolean result = false;
        if (file.exists()) {
            result = true;
        }
        return result;
    }


    public static int downloadCount = 0;

    private void downloadAll() {

        if (cd.isConnectingToInternet()) {

            dialog.setMessage("Downloading data from server...");
            dialog.show();

            //new DownloadLibrary().execute();

            new DownloadAllTrainMats().execute();
            DownloadUserVillages usersVillages = new DownloadUserVillages(Request.Method.GET, Config.DOWNLOAD_USER_VILLAGES + "?user_id=" + userID + "&company_id=" + companyID, db, this, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("USER VILLAGES: ", "" + error.getMessage());
                    DashBoardActivity.downloadCount++;
                }
            });
            DownloadSubVillages subVillages = new DownloadSubVillages(Request.Method.GET, Config.DOWNLOAD_SUB_VILLAGES + "?company_id=" + companyID, db, this, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("SUB VILLAGES: ", "" + error.getMessage());
                    DashBoardActivity.downloadCount++;
                }
            });

            DownloadHerbicides herbicides = new DownloadHerbicides(Request.Method.GET, Config.DOWNLOAD_HERBICIDES + "?company_id=" + companyID, db, this, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("HERBICIDES: ", "" + error.getMessage());
                    DashBoardActivity.downloadCount++;
                }
            });

            DownloadFoliarFeed foliarFeed = new DownloadFoliarFeed(Request.Method.GET, Config.DOWNLOAD_FOLIAR_FEED + "?company_id=" + companyID, db, this, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("FOLIAR FEED: ", "" + error.getMessage());
                    DashBoardActivity.downloadCount++;
                }
            });
            DownloadPesticides pesticides = new DownloadPesticides(Request.Method.GET, Config.DOWNLOAD_PESTICIDES + "?company_id=" + companyID, db, this, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("PESTICIDES: ", "" + error.getMessage());
                    DashBoardActivity.downloadCount++;
                }
            });

            DownloadOtherCrops otherCrops = new DownloadOtherCrops(Request.Method.GET, Config.DOWNLOAD_OTHER_CROPS, db, this, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("OTHER CROPS: ", "" + error.getMessage());
                    DashBoardActivity.downloadCount++;
                }
            });

            DownloadVillages villages = new DownloadVillages(Request.Method.GET, Config.DOWNLOAD_VILLAGES + "?company_id=" + companyID, db, this, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VILLAGES: ", "" + error.getMessage());
                    DashBoardActivity.downloadCount++;
                }
            });

            DownloadOfficerTraining myTrainings = new DownloadOfficerTraining(Request.Method.GET, Config.DOWNLOAD_OFFICER_TRAININGS + "?user_id=" + userID + "&company_id=" + companyID, db, this, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("MY TRAININGS: ", "" + error.getMessage());
                    DashBoardActivity.downloadCount++;
                }
            });

        /*DownloadTrainingCategories trainingCategories = new DownloadTrainingCategories(Request.Method.GET, Config.DOWNLOAD_TRAINING_CATEGORIES + "?company_id=" + companyID, db, this, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TRAINING CATEGORIES: ", "" + error.getMessage());
                DashBoardActivity.downloadCount++;
            }
        });*/

            DownloadTrainingTypes trainingTypes = new DownloadTrainingTypes(Request.Method.GET, Config.DOWNLOAD_TRAINING_TYPES + "?company_id=" + companyID, db, this, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TRAINING TYPES: ", "" + error.getMessage());
                    DashBoardActivity.downloadCount++;
                }
            });

            DownloadWackFarmers wackFarmers = new DownloadWackFarmers(Request.Method.GET, Config.WACK_FARMERS_LIST + "?user_id=" + userID + "&company_id=" + companyID, db, this, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("WACK FARMERS: ", "" + error.getMessage());
                    DashBoardActivity.downloadCount++;
                }
            });

            DownloadDataCollected dataCollected = new DownloadDataCollected(Request.Method.GET, Config.DATA_COLLECTED_URL + "?user_id=" + userID + "&company_id=" + companyID, db, this, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("DATA COLLECTED: ", "" + error.getMessage());
                    DashBoardActivity.downloadCount++;
                }
            });

            int socketTimeout = 50000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            dataCollected.setRetryPolicy(policy);
            //TODO: Verify
            DownloadWards wards = new DownloadWards(Request.Method.GET, Config.FARM_DOWNLOAD_WARDS + "?company_id=" + companyID, db, this, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("WARDS: ", "" + error.getMessage());
                    DashBoardActivity.downloadCount++;
                }
            });

            List<UserVillage> usersVillageIds = db.getVillageIdByUserId(Preferences.USER_ID);
            String CSVUserVillageIds = db.userVillageIdsToCsv(usersVillageIds, ",");
            Log.i(TAG, CSVUserVillageIds);


            RequestQueue queue = VolleySingleton.getInstance(this.mContext).getRequestQueue();

            queue.add(usersVillages);
            queue.add(subVillages);
            queue.add(herbicides);
            queue.add(foliarFeed);
            queue.add(pesticides);
            queue.add(otherCrops);
            queue.add(villages);
            queue.add(myTrainings);
            queue.add(trainingTypes);
            queue.add(wackFarmers);
            queue.add(dataCollected);
            queue.add(wards);
            queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {

                @Override
                public void onRequestFinished(Request<String> request) {

                    downloadFinished();
                }
            });

        } else {
            MyAlerts.genericDialog(mContext, R.string.no_connection);
        }
    }

    /**
     * Checks if all downloads are finished;
     */
    private void downloadFinished() {
        Log.i(TAG, "DOWNLOAD COUNT: " + downloadCount + "");
        if (dialog.isShowing() && downloadCount == MAX_DOWNLOAD_COUNTS) {
            downloadCount = 0;
            dialog.dismiss();
            Toast.makeText(mContext, "Check download details", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext, SyncedDataDownloadActivity.class));
        }
    }

    /**
     * Checks if all uploads are finished
     *
     * @param ctx
     */
    public static void isUploadFinished(Context ctx) {
        uploadCounter++;
        Log.i(TAG, "UPLOAD COUNT: " + uploadCounter);
        if (uploadCounter >= max_upload_counts) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                ctx.startActivity(new Intent(ctx, SyncedData.class));
            }
            uploadCounter = 0;
            max_upload_counts = 0;
        }
    }

    private void uploadAll() {

        if (cd.isConnectingToInternet()) {

            dialog.setMessage("Uploading data to server...");
            dialog.show();

            if (db.getTableCount(db.TABLE_FARMERS) > 0) {
                List<Farmers> farmers = db.getAllFarmers();
                for (Farmers farmer : farmers) {
                    new UploadFarmers(mContext, farmer).execute();
                }
            }
            // new UploadFingerprints().execute();
            //new UploadKml(mContext).execute();

            List<AssignedTrainings> assignedTrainingsList = db.getAllAssignedTrainings();
            if (assignedTrainingsList.size() > 0) {
                max_upload_counts++;
                new UploadAssignedTrainings(mContext, assignedTrainingsList).execute();
            }

            List<FarmerTime> farmerTimeList = db.getAllFarmerTimes();
            if (farmerTimeList.size() > 0) {
                max_upload_counts++;
                new UploadFarmerTimes(mContext, farmerTimeList).execute();
            }

            List<SignedDoc> signedDocs = db.getAllSignedDocs();
            if (signedDocs.size() > 0) {
                max_upload_counts++;
                new UploadSignedDocs(mContext, signedDocs).execute();
            }

            List<ShowIntent> showIntents = db.getAllShowIntents();
            if (showIntents.size() > 0) {
                max_upload_counts++;
                new UploadShowsIntent(mContext, showIntents).execute();
            }

            /*Farm Assessment*/
            List<PlantingRains> plantingRains = db.getAllPlantingRains();
            if (plantingRains.size() > 0) {
                max_upload_counts++;
                new UploadPlantingRains(mContext, plantingRains).execute();
            }

            List<FarmOtherCrops> farmOtherCrops = db.getAllFarmOtherCrops();
            if (farmOtherCrops.size() > 0) {
                max_upload_counts++;
                new UploadFarmOtherCrops(mContext, farmOtherCrops).execute();
            }

            List<FarmAssFormsMajor> formsMajor = db.getAllFormsMajor();
            if (formsMajor.size() > 0) {
                max_upload_counts++;
                new UploadFarmAssMajor(mContext, formsMajor).execute();
            }

            List<FarmAssFormsMedium> formsMedium = db.getAllFormsMedium();
            if (formsMedium.size() > 0) {
                max_upload_counts++;
                new UploadFarmAssMedium(mContext, formsMedium).execute();
            }

            List<FarmProduction> farmProductions = db.getAllFarmProductions();
            if (farmProductions.size() > 0) {
                max_upload_counts++;
                new UploadFarmProduction(mContext, farmProductions).execute();
            }

            List<TransportHseToMarket> transPortHseToMarket = db.getAllTransPortHseToMarket();
            if (transPortHseToMarket.size() > 0) {
                max_upload_counts++;
                new UploadTransportHouseToMarket(mContext, transPortHseToMarket).execute();
            }

            List<YieldEstimate> yieldEstimates = db.getAllYieldEstimates();
            if (yieldEstimates.size() > 0) {
                max_upload_counts++;
                new UploadYieldEstimates(mContext, yieldEstimates).execute();
            }

            List<MolassesTrapCatch> trapCatches = db.getAllMolassesTrapCatches();
            if (trapCatches.size() > 0) {
                max_upload_counts++;
                new UploadMolassesTrapCatches(mContext, trapCatches).execute();
            }

            List<Scouting> scoutings = db.getAllScoutings();
            if (scoutings.size() > 0) {
                max_upload_counts++;
                new UploadScouting(mContext, scoutings).execute();
            }

            List<Germination> germinations = db.getAllGerminations();
            if (germinations.size() > 0) {
                max_upload_counts++;
                new UploadGerminations(mContext, germinations).execute();
            }

            List<UpdateFarmArea> farmAreasList = db.getAllUpdateFarmArea();
            if (farmAreasList.size() > 0) {
                max_upload_counts++;
                new UploadFarmAreaUpdate(mContext, farmAreasList).execute();
            }

            // new UploadPestControl().execute();
            List<FarmIncome> farmIncomes = db.getAllFarmIncome();
            if (farmIncomes.size() > 0) {
                max_upload_counts++;
                new UploadFarmIncome(mContext, farmIncomes).execute();
            }

            List<FingerOne> fingerOnes = db.getAllFingerOne();
            if (fingerOnes.size() > 0) {
                max_upload_counts++;
                new UploadFingerOneForm(mContext, fingerOnes).execute();
            }

            List<FingerTwo> fingerTwos = db.getAllFingerTwo();
            if (fingerTwos.size() > 0) {
                max_upload_counts++;
                new UploadFingerTwoForm(mContext, fingerTwos).execute();
            }

            List<FingerThree> fingerThrees = db.getAllFingerThree();
            if (fingerThrees.size() > 0) {
                max_upload_counts++;
                new UploadFingerThreeForm(mContext, fingerThrees).execute();
            }

            List<FingerFour> fingerFours = db.getAllFingerFour();
            if (fingerFours.size() > 0) {
                max_upload_counts++;
                new UploadFingerFourForm(mContext, fingerFours).execute();
            }

            List<FingerFive> fingerFives = db.getAllFingerFive();
            if (fingerFives.size() > 0) {
                max_upload_counts++;
                new UploadFingerFiveForm(mContext, fingerFives).execute();
            }

            List<CollectedInputs> collectedInputs = db.getAllCollectedInputs();
            if (collectedInputs.size() > 0) {
                max_upload_counts++;
                new UploadCollectedInputs(mContext, collectedInputs).execute();
            }

            List<MappedFarm> mappedFarms = db.getAllMappedFarms();
            if (mappedFarms.size() > 0) {
                max_upload_counts++;
                new UploadMappedFarms(mContext, mappedFarms).execute();
            }
            //new UploadReRegisteredFarmers(mContext).execute();

            if (db.getTableCount(db.TABLE_RE_REGISTERED_FARMERS) > 0) {
                max_upload_counts++;
                List<ReRegisteredFarmers> farmers = db.getReRegisteredFarmers();
                for (ReRegisteredFarmers farmer : farmers) {
                    new UploadReRegisteredFarmers(mContext, farmer).execute();
                }

                DashBoardActivity.uploadCounter++;
            }

            if (db.getTableCount(db.TABLE_PRODUCT_PURCHASES) > 0) {
                List<ProductPurchase> productPurchases = db.getAllProductPurchases();
                new UploadProductPurchase(mContext,productPurchases,db).execute();
                DashBoardActivity.uploadCounter++;
            }
            if (db.getTableCount(db.TABLE_UPDATED_FARMERS) > 0) {
                List<RegisteredFarmer> registeredFarmers = db.getAllUpdatedFarmers();
                new UploadUpdatedFarmers(mContext,registeredFarmers,db).execute();
                DashBoardActivity.uploadCounter++;
            }

            new UploadInputCollectionRecapture(mContext).execute();
        } else {
            MyAlerts.genericDialog(mContext, R.string.no_connection);
        }
    }

    /**
     * Alerts user to download data first.
     *
     * @param message
     */
    private void showDialogDownloadData(int message) {

        new MaterialStyledDialog.Builder(mContext)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(message)
                .setCancelable(false)
                .setPositiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        downloadAll();
                    }
                }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("Welcome: " + Preferences.USERNAME);
    }
}
