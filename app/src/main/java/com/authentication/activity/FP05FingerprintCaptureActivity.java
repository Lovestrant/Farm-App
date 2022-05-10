package com.authentication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import com.fgtit.fpcore.FPMatch;
import com.futronictech.UsbDeviceDataExchangeImpl;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.ManualInputFingerprintRecapture;
import com.svs.farm_app.entities.ReRegisteredFarmers;
import com.svs.farm_app.main.farm_inputs.FarmInputsActivity;
import com.svs.farm_app.main.recapture_details.ReRegistrationActivity;
import com.svs.farm_app.main.registration.FarmerRegistrationActivity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DataUtils;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;
import com.svs.farm_app.utils.tps_utils.TPSFingerprintUtils;

import java.io.File;

import android_serialport_api.AsyncFingerprint;
import android_serialport_api.SerialPortManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.svs.farm_app.utils.Config.LEFT_THUMB;
import static com.svs.farm_app.utils.Config.NEW_FARMER_PICTURE;
import static com.svs.farm_app.utils.Config.NEW_LEFT_THUMB;
import static com.svs.farm_app.utils.Config.NEW_RIGHT_THUMB;
import static com.svs.farm_app.utils.Config.RECAPTURE_FARMER_DETAILS;
import static com.svs.farm_app.utils.Config.RIGHT_THUMB;
import static com.svs.farm_app.utils.Config.WHICH_THUMB;

public class FP05FingerprintCaptureActivity extends BaseClass {

    public static final int MESSAGE_SHOW_MSG = 1;
    public static final int MESSAGE_SHOW_IMAGE = 2;
    public static final int MESSAGE_SHOW_ERROR_MSG = 3;
    public static final int MESSAGE_END_OPERATION = 4;

    //Pending operations
    private static final int OPERATION_CREATE = 2;
    private static final String TAG = FP05FingerprintCaptureActivity.class.getSimpleName();

    private static Bitmap mBitmapFP = null;

    private static final int FINGER_TYPE = 0;

    private static final boolean HOST_MODE = true;

    private int mPendingOperation = 0;

    private OperationThread mOperationThread = null;

    public static String mFingerprintDir;

    private UsbDeviceDataExchangeImpl usb_host_ctx = null;
    private final int MATCH_SCORE = 65;
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
    private AsyncFingerprint vFingerprint;
    private boolean bIsCancel = false;
    private boolean bfpWork = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tps_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mContext = FP05FingerprintCaptureActivity.this;

        Intent intent = getIntent();
        whichThumb = intent.getStringExtra(WHICH_THUMB);
        String toActivityString = intent.getStringExtra(Config.TO_ACTIVITY);
        toActivity = (toActivityString == null)?"":toActivityString;

        /*power on fingerprint reader*/
        if(FPMatch.getInstance().InitMatch()==0){
            Toast.makeText(mContext, "Init Matcher Fail!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, "Init Matcher OK!", Toast.LENGTH_SHORT).show();
        }

        //Fingerprint
        vFingerprint = SerialPortManager.getInstance().getNewAsyncFingerprint();

        initView();
        initData();
        setToolbarTitle();

        FPInit();

        bCreate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                StartCreateTemplate();

                FPProcess();
            }
        });

    }

    private void workExit(){
        if(SerialPortManager.getInstance().isOpen()){
            bIsCancel=true;
            SerialPortManager.getInstance().closeSerialPort();
            this.finish();
        }
    }

    private void FPInit(){
        final boolean IsUpImage =true;
        vFingerprint.setOnGetImageListener(new AsyncFingerprint.OnGetImageListener() {
            @Override
            public void onGetImageSuccess() {
                if(IsUpImage){
                    vFingerprint.FP_UpImage();
                    Log.i(TAG,"GET IMAGE SUCCESS");
                }else{
                    progressDialog.setMessage(getResources().getString(R.string.processing));
                    vFingerprint.FP_GenChar(1);
                }
            }

            @Override
            public void onGetImageFail() {
                if(!bIsCancel){
                    vFingerprint.FP_GetImage();
                    Log.i(TAG,"GET IMAGE FAIL");
                }else{
                    Toast.makeText(FP05FingerprintCaptureActivity.this, "Cancel OK", Toast.LENGTH_SHORT).show();
                }
            }
        });

        vFingerprint.setOnUpImageListener(new AsyncFingerprint.OnUpImageListener() {
            @Override
            public void onUpImageSuccess(byte[] data) {
                Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
                Log.i(TAG," onUpImageSuccess.data.length: "+data.length);

                //ExtApi.saveDataToFile(sDir+"/data/fingerprint.jpg", data);

                ivFingerprint.setImageBitmap(image);
                //tvFpStatus.setText(getString(R.string.txt_fpprocess));
                vFingerprint.FP_GenChar(1);
                Log.i(TAG,"UP IMAGE SUCCESS");
            }

            @Override
            public void onUpImageFail() {
                Log.i(TAG,"UP IMAGE FAIL");
                //bfpWork=false;
                //TimerStart();
            }
        });

        vFingerprint.setOnGenCharListener(new AsyncFingerprint.OnGenCharListener() {
            @Override
            public void onGenCharSuccess(int bufferId) {
                Log.i(TAG,"GEN CHAR SUCCESS");
                vFingerprint.FP_UpChar();
            }

            @Override
            public void onGenCharFail() {
                //tvFpStatus.setText(getString(R.string.txt_fpfail));
                Log.i(TAG,"GEN CHAR FAIL");
            }
        });

        vFingerprint.setOnUpCharListener(new AsyncFingerprint.OnUpCharListener() {

            @Override
            public void onUpCharSuccess(byte[] model) {

                DataUtils.writeBytesToFile(finalFingerprintPath,model);

                progressDialog.dismiss();

                Log.i(TAG,"setOnUpCharListener");

                Log.i(TAG,"fingerprint exists: "+new File(finalFingerprintPath).exists());
                Log.i(TAG,"path: "+finalFingerprintPath);

                //String fingerprintLocation = Config.SD_CARD_PATH+"/farmer/data/fingerprint.jpg";

                /*byte[] fingerprintBytes =  DataUtils.fileToByteArray(new File(finalFingerprintPath));

                Log.i(TAG," model.length: "+model.length);

                //byte[] tmp=new byte[fingerprintBytes.length];
                byte[] tmp=new byte[fingerprintBytes.length];

                //System.arraycopy(fingerprintBytes, 0, tmp, 0, fingerprintBytes.length);
                //System.arraycopy(fingerprintBytes, 0, tmp, 0, 256);
                System.arraycopy(fingerprintBytes, 0, tmp, 0, fingerprintBytes.length);

                int score = FPMatch.getInstance().MatchTemplate(model, tmp);

                Log.i(TAG,"score: "+score);

                if(score > MATCH_SCORE){
                    //AddPersonItem(GlobalData.getInstance().userList.get(i));
                   // tvFpStatus.setText(getString(R.string.txt_fpmatchok));
                }else{
                    //tvFpStatus.setText(getString(R.string.txt_fpmatchfail));
                }*/

                bfpWork=false;
                //TimerStart();
            }

            @Override
            public void onUpCharFail() {
                Log.i(TAG,"UP CHAR FAIL");
               bfpWork=false;
                MyAlerts.genericDialog(mContext,R.string.fingerprint_capture_failed);
            }
        });

    }

    private void FPProcess(){
        if(!bfpWork){
            try {
                Thread.currentThread();
                Thread.sleep(500);
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            vFingerprint.FP_GetImage();
            bfpWork=true;
        }
    }

    private void initData() {
        db = new DatabaseHandler(mContext);

        mFingerprintDir = TPSFingerprintUtils.GetFingerprintDirectory();
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

    private void StartCreateTemplate() {

        String tmplName = String.valueOf(System.currentTimeMillis())+".fpo5";

        finalFingerprintPath = mFingerprintDir+File.separator+tmplName;

        showProgressDialog(R.string.place_finger);
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

    private void showProgressDialog(int resId) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(resId));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == keyCode) {
                    workExit();
                }
                return false;
            }
        });
        progressDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

                        intent  = new Intent(mContext,FP05FingerprintCaptureActivity.class).putExtra(Config.WHICH_THUMB, RIGHT_THUMB);

                    } else if(whichThumb.equals(RIGHT_THUMB)){

                        MyPrefrences.savePrefrence(mContext, Config.RIGHT_THUMB, finalFingerprintPath);
                        intent = new Intent(mContext, FarmerRegistrationActivity.class);

                    }else if(whichThumb.equals(NEW_LEFT_THUMB)){

                        MyPrefrences.savePrefrence(mContext, NEW_LEFT_THUMB, finalFingerprintPath);
                        intent  = new Intent(mContext,FP05FingerprintCaptureActivity.class).putExtra(Config.WHICH_THUMB, NEW_RIGHT_THUMB);

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

                            MyAlerts.toActivityDialog(mContext, R.string.saved_offline, intent);
                            return true;

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
