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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fgtit.fpcore.FPMatch;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.CollectedInputs;
import com.svs.farm_app.main.farm_inputs.FarmInputsActivity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DataUtils;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.Preferences;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android_serialport_api.AsyncFingerprint;
import android_serialport_api.SerialPortManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FP05FingerprintVerificationActivity extends BaseClass {
    private static final String TAG = FP05FingerprintVerificationActivity.class.getSimpleName();
    private int MATCH_SCORE = 65;
    private ProgressDialog progressDialog;
    private Context mContext;
    private String orderId;
    private String genId;
    private String fingerprintPath;
    private DatabaseHandler db;
    private SimpleDateFormat dateFormat;
    private String userId;
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
    private AsyncFingerprint vFingerprint;
    private boolean bfpWork;
    private boolean bIsCancel;
    private String fingerprintExtension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tps_main);
        ButterKnife.bind(this);

        mContext = FP05FingerprintVerificationActivity.this;

         /*power on fingerprint reader*/
        if(FPMatch.getInstance().InitMatch()==0){
            Toast.makeText(mContext, "Init Matcher Fail!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, "Init Matcher OK!", Toast.LENGTH_SHORT).show();
        }

        //Fingerprint
        vFingerprint = SerialPortManager.getInstance().getNewAsyncFingerprint();

        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();

        FPInit();

        initListeners();

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
                    Toast.makeText(mContext, "Cancel OK", Toast.LENGTH_SHORT).show();
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

                Log.i(TAG,"setOnUpCharListener");

                Log.i(TAG,"fingerprint exists: "+new File(fingerprintPath).exists());
                Log.i(TAG,"path: "+fingerprintPath);

                String fingerprintLocation = fingerprintPath;

                byte[] fingerprintBytes =  DataUtils.fileToByteArray(new File(fingerprintLocation));

                Log.i(TAG," model.length: "+model.length);

                byte[] tmp=new byte[fingerprintBytes.length];

                System.arraycopy(fingerprintBytes, 0, tmp, 0, fingerprintBytes.length);

                int score = FPMatch.getInstance().MatchTemplate(model, tmp);

                Log.i(TAG,"score: "+score);

                if (score > MATCH_SCORE) {
                    Date date = new Date();

                    String collectDate = dateFormat.format(date);
                    db.addCollectedInputs(new CollectedInputs(orderId, collectDate,
                            userId,Config.FINGERPRINT));

                    db.deleteAssignedInput(orderId);

                    progressDialog.dismiss();

                    Intent intent = new Intent(mContext, FarmInputsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    MyAlerts.toActivityDialog(mContext,"Fingerprint successfully matched \n Inputs collected",intent);

                } else{
                    progressDialog.dismiss();
                    MyAlerts.genericDialog(mContext, "Fingerprint failed to match");

                }

                bfpWork=false;
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

    private void initListeners() {

        bVerify.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(new File(fingerprintPath).exists()) {
                    showProgressDialog(R.string.place_finger);
                    FPProcess();
                }else{
                    MyAlerts.genericDialog(mContext,R.string.fingerprint_not_on_tab);
                }
            }
        });

        rgSelectThumb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup rGroup, int checkedId)
            {
                if(rbLeftThumb.isChecked()){
                    String leftFingerprint = genId+"L";


                    fingerprintPath = Config.FINGERPRINTS_PATH+ leftFingerprint.trim()+"."+fingerprintExtension;

                    File file = new File(fingerprintPath);
                    Log.i(TAG, " File exists: "+file.exists()+" File name: "+file.getName());

                }else if(rbRightThumb.isChecked()){
                    String rightFingerprint = genId+"R";
                    fingerprintPath = Config.FINGERPRINTS_PATH + rightFingerprint.trim()+"."+fingerprintExtension;

                    File file = new File(fingerprintPath);
                    Log.i(TAG, " File exists: "+file.exists()+" File name: "+file.getName());

                }
            }
        });

        bManualCollection.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Date date = new Date();

                String collectDate = dateFormat.format(date);

                db.addCollectedInputs(new CollectedInputs(orderId, collectDate,
                        userId,Config.MANUAL));
                db.deleteAssignedInput(orderId);

                Intent intent = new Intent(mContext,
                        FP05FingerprintCaptureActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(Config.WHICH_THUMB,Config.NEW_LEFT_THUMB).putExtra(Config.TO_ACTIVITY, Config.MANUAL_INPUT_COLLECTION);

                MyAlerts.toActivityDialog(mContext,R.string.saved_offline,intent);
            }
        });
    }

    private void initData() {
        db = new DatabaseHandler(mContext);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Intent intent = getIntent();

        orderId = intent.getStringExtra("order_id");
        userId = intent.getStringExtra("user_id");
        //collectDate = intent.getStringExtra("collect_date");
        genId = intent.getStringExtra(DatabaseHandler.KEY_GEN_ID);


        fingerprintExtension = getFingerprintExtension(Preferences.DEVICE_MODEL);

        fingerprintPath = Config.FINGERPRINTS_PATH+genId+"R."+fingerprintExtension;
        Log.i(TAG,"Right finger template"+ fingerprintPath +" Exists: "+new File(fingerprintPath).exists());
    }

    private String getFingerprintExtension(String deviceModel) {
        String extension = null;
        switch(deviceModel){
            case Config.COREWISE_V0:
                extension = "bmp";
                break;
            case Config.TPS350:
                extension = "ansi";
                break;
            case Config.FP05:
                extension = "fpo5";
                break;

        }
        return extension;
    }

    private void initView() {
        bCreate.setVisibility(View.GONE);
        rgSelectThumb.setVisibility(View.VISIBLE);
        bManualCollection.setVisibility(View.VISIBLE);
    }

    private void workExit(){
        if(SerialPortManager.getInstance().isOpen()){
            bIsCancel=true;
            SerialPortManager.getInstance().closeSerialPort();
            this.finish();
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

}
