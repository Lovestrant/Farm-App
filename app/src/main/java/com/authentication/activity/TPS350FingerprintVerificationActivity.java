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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.authentication.utils.ImageUtils;
import com.authentication.utils.ToastUtil;
import com.common.pos.api.util.posutil.PosUtil;
import com.futronictech.AnsiSDKLib;
import com.futronictech.UsbDeviceDataExchangeImpl;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.CollectedInputs;
import com.svs.farm_app.main.farm_inputs.FarmInputsActivity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.tps_utils.TPSFingerprintUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.authentication.utils.ImageUtils.CreateBitmap;

public class TPS350FingerprintVerificationActivity extends BaseClass {

    public static final int MESSAGE_SHOW_MSG = 1;
    public static final int MESSAGE_SHOW_IMAGE = 2;
    public static final int MESSAGE_SHOW_ERROR_MSG = 3;
    public static final int MESSAGE_END_OPERATION = 4;

    //Pending operations
    private static final int OPERATION_CREATE = 2;
    private static final int OPERATION_VERIFY = 3;

    // Intent request codes
    private static final int REQUEST_INPUT_TMPL_NAME = 1;
    private static final String TAG = TPS350FingerprintVerificationActivity.class.getSimpleName();

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
    private String orderID;
    private String collectDate;
    private String genId;
    private String fingerprintPath;
    private boolean isAMatch = false;
    private DatabaseHandler db;
    private SimpleDateFormat dateFormat;
    private String userID;
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
    @BindView(R.id.bManualCollection)
    Button bManualCollection;
    @BindView(R.id.ivFingerprint)
    public ImageView ivFingerprint;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tps_main);
        ButterKnife.bind(this);

        mContext = TPS350FingerprintVerificationActivity.this;

        /*power on fingerprint reader*/
        new OpenTask().execute();

        /*mMatchScoreValue[0] = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_LOW;
        mMatchScoreValue[1] = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_LOW_MEDIUM;
        mMatchScoreValue[2] = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_MEDIUM;
        mMatchScoreValue[3] = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_HIGH_MEDIUM;
        mMatchScoreValue[4] = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_HIGH;
        mMatchScoreValue[5] = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_VERY_HIGH;*/

        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();

        initListeners();

    }

    private void initListeners() {
        bVerify.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (HOST_MODE) {
                    if (usb_host_ctx.OpenDevice(0, true)) {
                        ToastUtil.showToast(mContext,"1");
                        startVerifyTemplate(fingerprintPath);
                    } else {
                        if (usb_host_ctx.IsPendingOpen()) {
                            mPendingOperation = OPERATION_VERIFY;
                        } else {
                            ToastUtil.showToast(mContext,"2");
                            MyAlerts.genericDialog(mContext, "Can not start verify template operation.\nCan't open scanner device");
                        }
                    }
                } else {
                    startVerifyTemplate(fingerprintPath);
                }
            }
        });

        rgSelectThumb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup rGroup, int checkedId)
            {
                if(rbLeftThumb.isChecked()){
                    String leftFingerprint = genId+"L";
                    fingerprintPath = Config.FINGERPRINTS_PATH+ leftFingerprint.trim()+".ansi";

                    File file = new File(fingerprintPath);
                    Log.i(TAG, " File exists: "+file.exists()+" File name: "+file.getName());

                }else if(rbRightThumb.isChecked()){
                    String rightFingerprint = genId+"R";
                    fingerprintPath = Config.FINGERPRINTS_PATH + rightFingerprint.trim()+".ansi";

                    File file = new File(fingerprintPath);
                    Log.i(TAG, " File exists: "+file.exists()+" File name: "+file.getName());

                }
            }
        });

        bManualCollection.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Date date = new Date();

                String collectDate = dateFormat.format(date);

                db.addCollectedInputs(new CollectedInputs(orderID, collectDate,
                        userID,Config.MANUAL));
                db.deleteAssignedInput(orderID);

                Intent intent = new Intent(mContext,
                        TPS350FingerprintCaptureActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(Config.WHICH_THUMB,Config.NEW_LEFT_THUMB).putExtra(Config.TO_ACTIVITY, Config.MANUAL_INPUT_COLLECTION);

                MyAlerts.toActivityDialog(mContext,R.string.saved_offline,intent);
            }
        });
    }

    private void initData() {
        db = new DatabaseHandler(mContext);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        MATCH_SCORE = AnsiSDKLib.FTR_ANSISDK_MATCH_SCORE_HIGH_MEDIUM;

        usb_host_ctx = new UsbDeviceDataExchangeImpl(this, mHandler);

        Intent intent = getIntent();

        orderID = intent.getStringExtra("order_id");
        userID = intent.getStringExtra(DatabaseHandler.KEY_USER_ID);
        collectDate = intent.getStringExtra(DatabaseHandler.KEY_COLLECT_DATE);
        genId = intent.getStringExtra(DatabaseHandler.KEY_GEN_ID);

        fingerprintPath = com.svs.farm_app.utils.Config.FINGERPRINTS_PATH+genId+"R.ansi";
        Log.i(TAG,"Right finger template"+ fingerprintPath +" Exists: "+new File(fingerprintPath).exists());
    }

    private void initView() {
        bCreate.setVisibility(View.GONE);
        rgSelectThumb.setVisibility(View.VISIBLE);
        bManualCollection.setVisibility(View.VISIBLE);
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

                    if (isAMatch) {
                        Date date = new Date();

                        String collectDate = dateFormat.format(date);
                        db.addCollectedInputs(new CollectedInputs(orderID, collectDate,
                                userID,Config.FINGERPRINT));

                        db.deleteAssignedInput(orderID);

                        Intent intent = new Intent(mContext, FarmInputsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        MyAlerts.toActivityDialog(mContext,"Fingerprint successfully matched \n Inputs collected",intent);

                    } else{
                        MyAlerts.genericDialog(mContext, "Fingerprint failed to match");

                    }

                    break;

                case UsbDeviceDataExchangeImpl.MESSAGE_ALLOW_DEVICE: {
                    if (usb_host_ctx.ValidateContext()) {
                        switch (mPendingOperation) {

                            case OPERATION_CREATE:
                                StartCreateTemplate();
                                break;

                            case OPERATION_VERIFY:
                                startVerifyTemplate(fingerprintPath);
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

    private void startVerifyTemplate(String tmplName) {
        byte[] templateContent = null;
        FileInputStream fs = null;
        File f = null;

        try {
            f = new File(tmplName);
            if (!f.exists() || !f.canRead())
                throw new FileNotFoundException();

            long nFileSize = f.length();
            fs = new FileInputStream(f);

            byte[] fileContent = new byte[(int) nFileSize];
            fs.read(fileContent);
            fs.close();

            templateContent = fileContent;
            Log.i(TAG,"Byte size: "+templateContent.length);
        } catch (Exception e) {
            String error = "Failed to load template";
            e.printStackTrace();

            MyAlerts.genericDialog(mContext, error);
        }

        if (templateContent != null) {
            showProgressDialog(R.string.place_finger);
            mOperationThread = new VerifyThread(HOST_MODE, FINGER_TYPE, templateContent, MATCH_SCORE);
            mOperationThread.start();
        }
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

                        mBitmapFP = CreateBitmap(
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

                        mBitmapFP = CreateBitmap(
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

    private class VerifyThread extends OperationThread {
        private AnsiSDKLib ansi_lib = null;
        private boolean mUseUsbHost = true;
        private byte[] mTmpl = null;
        private float mMatchScore = 0;

        public VerifyThread(boolean useUsbHost, int finger, byte[] template, float matchScore) {
            ansi_lib = new AnsiSDKLib();
            mUseUsbHost = useUsbHost;
            mTmpl = template;
            mMatchScore = matchScore;
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

                    float[] matchResult = new float[1];
                    long lT1 = SystemClock.uptimeMillis();
                    if (ansi_lib.VerifyTemplate(FINGER_TYPE, mTmpl, img_buffer, matchResult)) {
                        long op_time = SystemClock.uptimeMillis() - lT1;

                        String op_info = String.format("Verify done. Result: %s(%f). Time is %d(ms)", matchResult[0] > mMatchScore ? "OK" : "FAILED", matchResult[0], op_time);
                        isAMatch = matchResult[0] > mMatchScore;
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
                            String error = String.format("Verify failed. Error: %s.", ansi_lib.GetErrorMessage());
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

    private class IdentifyThread extends OperationThread {
        private AnsiSDKLib ansi_lib = null;
        private boolean mUseUsbHost = true;
        private float mMatchScore = 0;
        private String mTemplateStore = "";


        public IdentifyThread(boolean useUsbHost, int finger, float matchScore, String templateStore) {
            ansi_lib = new AnsiSDKLib();
            mUseUsbHost = useUsbHost;
            mMatchScore = matchScore;
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
                    byte[] templateBase = new byte[tmplSize];
                    int[] realSize = new int[1];

                    long lT1 = SystemClock.uptimeMillis();
                    if (ansi_lib.CreateTemplate(FINGER_TYPE, img_buffer, templateBase, realSize)) {
                        long op_time = SystemClock.uptimeMillis() - lT1;

                        String op_info = String.format("Create done. Time is %d(ms)", op_time);
                        mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, op_info).sendToTarget();

                        mBitmapFP = ImageUtils.CreateBitmap(
                                ansi_lib.GetImageWidth(),
                                ansi_lib.GetImageHeight(),
                                img_buffer);
                        mHandler.obtainMessage(MESSAGE_SHOW_IMAGE).sendToTarget();

                        findTemplate(templateBase);

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

        private void findTemplate(byte[] baseTemplate) {
            long lT1 = SystemClock.uptimeMillis();

            File fingerprintDir;
            File[] files;

            // Read all records to identify
            fingerprintDir = new File(mFingerprintDir);
            files = fingerprintDir.listFiles();

            float[] matchResult = new float[1];

            boolean found = false;
            for (int iFiles = 0; iFiles < files.length; iFiles++) {
                File curFile = files[iFiles];
                if (curFile.isFile()) {
                    byte[] template = ReadTemplate(curFile);

                    if (template != null) {
                        if (ansi_lib.MatchTemplates(baseTemplate, template, matchResult) &&
                                matchResult[0] > mMatchScore) {
                            long op_time = SystemClock.uptimeMillis() - lT1;
                            String message = String.format("Template found.\nName: %s(%d:%d).\nTime: %d(ms)", curFile.getName(), iFiles + 1, files.length, op_time);
                            mHandler.obtainMessage(MESSAGE_SHOW_MSG, -1, -1, message).sendToTarget();

                            found = true;

                            break;
                        }
                    }
                }
            }

            if (!found) {
                mHandler.obtainMessage(MESSAGE_SHOW_ERROR_MSG, -1, -1, "Template not found").sendToTarget();
            }
        }

        private byte[] ReadTemplate(File templateFile) {
            byte[] templateContent = null;
            FileInputStream fs = null;

            try {
                long nFileSize = templateFile.length();
                fs = new FileInputStream(templateFile);

                byte[] fileContent = new byte[(int) nFileSize];
                fs.read(fileContent);
                fs.close();

                templateContent = fileContent;
            } catch (Exception e) {

            }

            return templateContent;

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
                Toast.makeText(TPS350FingerprintVerificationActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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


}
