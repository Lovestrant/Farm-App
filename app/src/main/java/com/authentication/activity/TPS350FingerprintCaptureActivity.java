package com.authentication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.authentication.utils.ImageUtils;
import com.common.pos.api.util.posutil.PosUtil;
import com.futronictech.AnsiSDKLib;
import com.futronictech.UsbDeviceDataExchangeImpl;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.ManualInputFingerprintRecapture;
import com.svs.farm_app.entities.ReRegisteredFarmers;
import com.svs.farm_app.main.farm_inputs.FarmInputsActivity;
import com.svs.farm_app.main.recapture_details.ReRegistrationActivity;
import com.svs.farm_app.main.registration.FarmerRegistrationActivity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;
import com.svs.farm_app.utils.tps_utils.TPSFingerprintUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.svs.farm_app.utils.Config.LEFT_THUMB;
import static com.svs.farm_app.utils.Config.NEW_FARMER_PICTURE;
import static com.svs.farm_app.utils.Config.NEW_LEFT_THUMB;
import static com.svs.farm_app.utils.Config.NEW_RIGHT_THUMB;
import static com.svs.farm_app.utils.Config.RECAPTURE_FARMER_DETAILS;
import static com.svs.farm_app.utils.Config.RIGHT_THUMB;
import static com.svs.farm_app.utils.Config.WHICH_THUMB;

public class TPS350FingerprintCaptureActivity extends BaseClass {

    public static final int MESSAGE_SHOW_MSG = 1;
    public static final int MESSAGE_SHOW_IMAGE = 2;
    public static final int MESSAGE_SHOW_ERROR_MSG = 3;
    public static final int MESSAGE_END_OPERATION = 4;

    //Pending operations
    private static final int OPERATION_CREATE = 2;
    private static final String TAG = TPS350FingerprintCaptureActivity.class.getSimpleName();

    private static Bitmap mBitmapFP = null;

    private static final int FINGER_TYPE = 0;

    private static final boolean HOST_MODE = true;

    private int mPendingOperation = 0;

    private OperationThread mOperationThread = null;

    public static String mFingerprintDir;

    private UsbDeviceDataExchangeImpl usb_host_ctx = null;
    private float MATCH_SCORE;
    private ProgressDialog progressDialog;
    private Context mContext;
    private String finalFingerprintPath;
    private String whichThumb;
    @BindView(R.id.rgSelectThumb)
    public RadioGroup rgSelectThumb;
    @BindView(R.id.rbLeftThumb)
    public RadioButton rbLeftThumb;
    @BindView(R.id.rbRightThumb)
    public RadioButton rbRightThumb;
    @BindView(R.id.bCreate)
    public Button bCreate;
    @BindView(R.id.bVerify)
    public Button bVerify;
    @BindView(R.id.ivFingerprint)
    public ImageView ivFingerprint;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    private DatabaseHandler db;
    private String toActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tps_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mContext = TPS350FingerprintCaptureActivity.this;

        Intent intent = getIntent();
        whichThumb = intent.getStringExtra(WHICH_THUMB);
        String toActivityString = intent.getStringExtra(Config.TO_ACTIVITY);
        toActivity = (toActivityString == null)?"":toActivityString;

        /*power on fingerprint reader*/
        new OpenTask().execute();

        /*mMatchScoreValue[0] = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_LOW;
        mMatchScoreValue[1] = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_LOW_MEDIUM;
        mMatchScoreValue[2] = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_MEDIUM;
        mMatchScoreValue[3] = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_HIGH_MEDIUM;
        mMatchScoreValue[4] = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_HIGH;
        mMatchScoreValue[5] = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_VERY_HIGH;*/

        initView();
        initData();
        setToolbarTitle();

        bCreate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if (HOST_MODE) {
                    if (usb_host_ctx.OpenDevice(0, true)) {
                        StartCreateTemplate();

                    } else {
                        if (usb_host_ctx.IsPendingOpen()) {
                            mPendingOperation = OPERATION_CREATE;
                        } else {
                            MyAlerts.genericDialog(mContext, "Can not start create template operation.\nCan't open scanner device");
                        }
                    }
                } else {
                    StartCreateTemplate();
                }
            }
        });

    }

    private void initData() {
        db = new DatabaseHandler(mContext);
        MATCH_SCORE = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_HIGH_MEDIUM;

        mFingerprintDir = TPSFingerprintUtils.GetFingerprintDirectory();

        usb_host_ctx = new UsbDeviceDataExchangeImpl(this, mHandler);
    }

    private void initView() {
        bVerify.setVisibility(View.GONE);
        rgSelectThumb.setVisibility(View.GONE);
    }

    private void setToolbarTitle() {
        if(whichThumb.equals(LEFT_THUMB)){
            getSupportActionBar().setTitle("Left Thumb");
        }else if(whichThumb.equals(RIGHT_THUMB)){
            getSupportActionBar().setTitle("Right Thumb");
        }else if(whichThumb.equals(NEW_LEFT_THUMB)){
            getSupportActionBar().setTitle("Recapture Left Thumb");
        }
        else if(whichThumb.equals(NEW_RIGHT_THUMB)){
            getSupportActionBar().setTitle("Recapture Right Thumb");
        }
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SHOW_MSG:
                    String showMsg = (String) msg.obj;

                    break;

                case MESSAGE_SHOW_ERROR_MSG:
                    String showErr = (String) msg.obj;
                    MyAlerts.genericDialog(mContext, showErr);
                    break;

                case MESSAGE_SHOW_IMAGE:
                    ivFingerprint.setImageBitmap(mBitmapFP);


                    progressDialog.dismiss();

                    break;

                case UsbDeviceDataExchangeImpl.MESSAGE_ALLOW_DEVICE: {
                    if (usb_host_ctx.ValidateContext()) {
                        switch (mPendingOperation) {

                            case OPERATION_CREATE:
                                StartCreateTemplate();
                                break;
                        }
                    } else {
                        MyAlerts.genericDialog(mContext, "Can't open scanner device");
                    }

                    break;
                }

                case UsbDeviceDataExchangeImpl.MESSAGE_DENY_DEVICE: {
                    MyAlerts.genericDialog(mContext, "User deny scanner device");
                    break;
                }

            }
        }
    };

    private void StartCreateTemplate() {

        String tmplName = String.valueOf(System.currentTimeMillis())+".ansi";

        finalFingerprintPath = mFingerprintDir+File.separator+tmplName;

        showProgressDialog(R.string.place_finger);
        mOperationThread = new CreateThread(
                HOST_MODE,
                tmplName);
        mOperationThread.start();
    }

    private class OperationThread extends Thread {
        private boolean mCanceled = false;

        public OperationThread() {

        }

        public boolean IsCanceled() {
            return mCanceled;
        }

        public void Cancel() {
            mCanceled = true;

            try {
                this.join();    //5sec timeout
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class CaptureThread extends OperationThread {
        private AnsiSDKLib ansi_lib = null;
        private boolean mUseUsbHost = true;

        public CaptureThread(boolean useUsbHost) {
            ansi_lib = new AnsiSDKLib();
            mUseUsbHost = useUsbHost;
        }

        public void run() {
            boolean dev_open = false;

            try {
                if (mUseUsbHost) {
                    if (!ansi_lib.OpenDeviceCtx(usb_host_ctx)) {
                        mHandler.obtainMessage(MESSAGE_SHOW_ERROR_MSG, -1, -1, ansi_lib.GetErrorMessage()).sendToTarget();
                        mHandler.obtainMessage(MESSAGE_END_OPERATION).sendToTarget();
                        return;
                    }
                } else {
                    if (!ansi_lib.OpenDevice(0)) {
                        mHandler.obtainMessage(MESSAGE_SHOW_ERROR_MSG, -1, -1, ansi_lib.GetErrorMessage()).sendToTarget();
                        mHandler.obtainMessage(MESSAGE_END_OPERATION).sendToTarget();
                        return;
                    }
                }

                dev_open = true;

                if (!ansi_lib.FillImageSize()) {
                    mHandler.obtainMessage(MESSAGE_SHOW_ERROR_MSG, -1, -1, ansi_lib.GetErrorMessage()).sendToTarget();
                    mHandler.obtainMessage(MESSAGE_END_OPERATION).sendToTarget();
                    return;
                }

                byte[] img_buffer = new byte[ansi_lib.GetImageSize()];

                for (; ; ) {
                    if (IsCanceled()) {
                        break;
                    }

                    if (ansi_lib.CaptureImage(img_buffer)) {

                        String op_info = "Fingerprint captured";
                        mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, op_info).sendToTarget();

                        mBitmapFP = ImageUtils.CreateBitmap(
                                ansi_lib.GetImageWidth(),
                                ansi_lib.GetImageHeight(),
                                img_buffer);
                        mHandler.obtainMessage(MESSAGE_SHOW_IMAGE).sendToTarget();
                        break;
                    } else {
                        int lastError = ansi_lib.GetErrorCode();

                        if (lastError == AnsiSDKLib.FTR_ERROR_EMPTY_FRAME ||
                                lastError == AnsiSDKLib.FTR_ERROR_NO_FRAME ||
                                lastError == AnsiSDKLib.FTR_ERROR_MOVABLE_FINGER) {
                            Thread.sleep(100);
                            continue;
                        } else {
                            String error = String.format("Capture failed. Error: %s.", ansi_lib.GetErrorMessage());
                            mHandler.obtainMessage(MESSAGE_SHOW_ERROR_MSG, -1, -1, error).sendToTarget();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                mHandler.obtainMessage(MESSAGE_SHOW_ERROR_MSG, -1, -1, e.getMessage()).sendToTarget();
            }

            if (dev_open) {
                ansi_lib.CloseDevice();
            }

            mHandler.obtainMessage(MESSAGE_END_OPERATION).sendToTarget();
        }
    }

    private class CreateThread extends OperationThread {
        private AnsiSDKLib ansi_lib = null;
        private boolean mUseUsbHost = true;
        private String mTmplName = "";

        public CreateThread(boolean useUsbHost, String tmplName) {
            ansi_lib = new AnsiSDKLib();
            mUseUsbHost = useUsbHost;
            mTmplName = tmplName;
        }

        public void run() {
            boolean dev_open = false;

            try {
                if (mUseUsbHost) {
                    if (!ansi_lib.OpenDeviceCtx(usb_host_ctx)) {
                        mHandler.obtainMessage(MESSAGE_SHOW_ERROR_MSG, -1, -1, ansi_lib.GetErrorMessage()).sendToTarget();
                        mHandler.obtainMessage(MESSAGE_END_OPERATION).sendToTarget();
                        return;
                    }
                } else {
                    if (!ansi_lib.OpenDevice(0)) {
                        mHandler.obtainMessage(MESSAGE_SHOW_ERROR_MSG, -1, -1, ansi_lib.GetErrorMessage()).sendToTarget();
                        mHandler.obtainMessage(MESSAGE_END_OPERATION).sendToTarget();
                        return;
                    }
                }

                dev_open = true;

                if (!ansi_lib.FillImageSize()) {
                    mHandler.obtainMessage(MESSAGE_SHOW_ERROR_MSG, -1, -1, ansi_lib.GetErrorMessage()).sendToTarget();
                    mHandler.obtainMessage(MESSAGE_END_OPERATION).sendToTarget();
                    return;
                }

                byte[] img_buffer = new byte[ansi_lib.GetImageSize()];

                for (; ; ) {
                    if (IsCanceled()) {
                        break;
                    }

                    int tmplSize = ansi_lib.GetMaxTemplateSize();
                    byte[] template = new byte[tmplSize];
                    byte[] templateIso = new byte[tmplSize];
                    int[] realSize = new int[1];
                    int[] realIsoSize = new int[1];

                    long lT1 = SystemClock.uptimeMillis();
                    if (ansi_lib.CreateTemplate(FINGER_TYPE, img_buffer, template, realSize)) {
                        long op_time = SystemClock.uptimeMillis() - lT1;

                        String op_info = String.format("Create done. Time is %d(ms)", op_time);
                        mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, op_info).sendToTarget();

                        mBitmapFP = ImageUtils.CreateBitmap(
                                ansi_lib.GetImageWidth(),
                                ansi_lib.GetImageHeight(),
                                img_buffer);
                        mHandler.obtainMessage(MESSAGE_SHOW_IMAGE).sendToTarget();

                        TPSFingerprintUtils.saveFingerprintTemplate(mHandler, mFingerprintDir, mTmplName, template, realSize[0]);

                        break;
                    } else {
                        int lastError = ansi_lib.GetErrorCode();

                        if (lastError == AnsiSDKLib.FTR_ERROR_EMPTY_FRAME ||
                                lastError == AnsiSDKLib.FTR_ERROR_NO_FRAME ||
                                lastError == AnsiSDKLib.FTR_ERROR_MOVABLE_FINGER) {
                            Thread.sleep(100);
                            continue;
                        } else {
                            String error = String.format("Create failed. Error: %s.", ansi_lib.GetErrorMessage());
                            mHandler.obtainMessage(MESSAGE_SHOW_ERROR_MSG, -1, -1, error).sendToTarget();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                mHandler.obtainMessage(MESSAGE_SHOW_ERROR_MSG, -1, -1, e.getMessage()).sendToTarget();
            }

            if (dev_open) {
                ansi_lib.CloseDevice();
            }

            mHandler.obtainMessage(MESSAGE_END_OPERATION).sendToTarget();
        }

    }

    private void showProgressDialog(int resId) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(resId));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == keyCode) {
                    shutdownFingerprint();
                }
                return false;
            }
        });
        progressDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        shutdownFingerprint();
        // System.exit(0);
    }

    private void shutdownFingerprint() {
        if (mOperationThread != null) {
            mOperationThread.Cancel();
        }

        if (usb_host_ctx != null) {
            usb_host_ctx.CloseDevice();
            usb_host_ctx.Destroy();
            usb_host_ctx = null;
        }
        //power off
        new CloseTask().execute();
    }

    private class OpenTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                PosUtil.setFingerPrintPower(PosUtil.FINGERPRINT_POWER_ON);
            } catch (Exception e) {
                Toast.makeText(TPS350FingerprintCaptureActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return null;
        }


    }

    private class CloseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            PosUtil.setFingerPrintPower(PosUtil.FINGERPRINT_POWER_OFF);
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_fingerprint_capture, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if(finalFingerprintPath != null){
                    Intent intent = null;
                    if (whichThumb.equals(LEFT_THUMB)) {
                        MyPrefrences.savePrefrence(mContext, Config.LEFT_THUMB, finalFingerprintPath);

                        intent  = new Intent(mContext,TPS350FingerprintCaptureActivity.class).putExtra(Config.WHICH_THUMB, RIGHT_THUMB);

                    } else if(whichThumb.equals(RIGHT_THUMB)){

                        MyPrefrences.savePrefrence(mContext, Config.RIGHT_THUMB, finalFingerprintPath);
                        intent = new Intent(mContext, FarmerRegistrationActivity.class);

                    }else if(whichThumb.equals(NEW_LEFT_THUMB)){

                        MyPrefrences.savePrefrence(mContext, NEW_LEFT_THUMB, finalFingerprintPath);
                        intent  = new Intent(mContext,TPS350FingerprintCaptureActivity.class).putExtra(Config.WHICH_THUMB, NEW_RIGHT_THUMB);

                        if(toActivity.equals(RECAPTURE_FARMER_DETAILS)){
                            intent.putExtra(Config.TO_ACTIVITY,Config.RECAPTURE_FARMER_DETAILS);
                        }

                    }
                    else if(whichThumb.equals(NEW_RIGHT_THUMB)){

                        MyPrefrences.savePrefrence(mContext, NEW_RIGHT_THUMB, finalFingerprintPath);
                        Log.i(TAG,"toActivity|"+toActivity);
                        if(toActivity.equals(RECAPTURE_FARMER_DETAILS)){

                            db.addReRegisteredFarmer(
                                    new ReRegisteredFarmers(Preferences.COMPANY_ID,Preferences.USER_ID,MyPrefrences.getPrefrence(mContext, "wack_fid"),
                                            MyPrefrences.getPrefrence(mContext, "wack_gen_id"),
                                            MyPrefrences.getPrefrence(mContext, NEW_FARMER_PICTURE),
                                            MyPrefrences.getPrefrence(mContext, Config.NEW_LEFT_THUMB),
                                            MyPrefrences.getPrefrence(mContext, Config.NEW_RIGHT_THUMB)));

                            db.removeWackFarmer(MyPrefrences.getPrefrence(mContext, "wack_fid").toString());

                            intent = new Intent(mContext, ReRegistrationActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        }else {

                            db.addManualInputFingerprintRecapture(
                                    new ManualInputFingerprintRecapture(MyPrefrences.getPrefrence(mContext, "wack_fid"),
                                            MyPrefrences.getPrefrence(mContext, "wack_gen_id"),
                                            MyPrefrences.getPrefrence(mContext, Config.NEW_LEFT_THUMB),
                                            MyPrefrences.getPrefrence(mContext, Config.NEW_RIGHT_THUMB)));

                            intent = new Intent(mContext, FarmInputsActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            MyAlerts.toActivityDialog(mContext, R.string.saved_offline, intent);
                        }

                    }
                    mContext.startActivity(intent);
                }else{
                    MyAlerts.genericDialog(mContext,"Capture fingerprint first!");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
