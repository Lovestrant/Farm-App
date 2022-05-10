package com.authentication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.svs.farm_app.R;
import com.svs.farm_app.entities.CollectedInputs;
import com.svs.farm_app.main.farm_inputs.FarmInputsActivity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.telpo.usb.finger.Finger;
import com.telpo.usb.finger.util.DataProcessUtil;
import com.telpo.usb.finger.util.RawToBitMap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TPS900FingerprintVerificationActivity extends BaseClass {

    private static final String TAG = TPS900FingerprintVerificationActivity.class.getSimpleName();

    private ProgressDialog progressDialog;
    private Context mContext;
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
    private int rets;
    private byte[] ISOTmpl = new byte[810];
    private byte[] FPTAddr;
    private byte[] img_data = new byte[250 * 360];
    private int[] TMPL_No = new int[1];
    private int[] pnDuplNo = new int[1];
    private String toBeCompared;
    private int ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tps_main);
        ButterKnife.bind(this);

        mContext = TPS900FingerprintVerificationActivity.this;

        /*power on fingerprint reader*/
        rets = Finger.initialize();
        if (rets == 0) {
            Toast.makeText(this, "Initialized fingerprint successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to initialize fingerprint", Toast.LENGTH_SHORT).show();
        }

        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();

        initListeners();

    }

    private void captureFingerprint() {
        int ret;
        int en_Step;
        ret = Finger.get_empty_id(TMPL_No);
        if (ret == 0) {
            en_Step = 0;
            ret = Finger.enroll(en_Step, TMPL_No[0], pnDuplNo);
            if (ret == 0) {
                ret = Finger.get_image(img_data);
                if (ret == 0) {
                    en_Step = 1;
                    ret = Finger.enroll(en_Step, TMPL_No[0], pnDuplNo);
                    if (ret == 0) {
                        Toast.makeText(this, "Captring Successful", Toast.LENGTH_SHORT).show();
                        getImage();
                        readRAM();
                        return;
                    }
                }
            }
        }
        Toast.makeText(this, "Capturing fingerprint failed", Toast.LENGTH_SHORT).show();
    }

    private void getImage() {
        int ret;
        ret = Finger.get_image(img_data);
        if (ret == 0) {
            Bitmap bmp = RawToBitMap.convert8bit(img_data, 242, 266);

            if (bmp != null) {
                ivFingerprint.setImageBitmap(bmp);
            }
            return;
        }
        Toast.makeText(this, "Failed to show image", Toast.LENGTH_SHORT).show();
    }

    private void readRAM() {
        int ret;
        byte[] p_nTmplAddr = new byte[512];
        int RamAddrs = 0;
        ret = Finger.read_ram(RamAddrs, p_nTmplAddr);
        if (ret == 0) {
            FPTAddr = p_nTmplAddr;
            Log.e("read_ram", DataProcessUtil.bytesToHexString(p_nTmplAddr));
            Toast.makeText(this, "Read RAM Successfully", Toast.LENGTH_SHORT).show();
            convertFPTToISO();
            return;
        }
        Toast.makeText(this, String.format("0x%02x", ret)+"fail" ,Toast.LENGTH_SHORT).show();
    }

    private void convertFPTToISO() {
        int ret;
        byte[] ISOAddr = new byte[810];
        try{

            int[] length = new int[1];
            ret = Finger.convert_FPT_to_ISO(FPTAddr , ISOAddr ,length);
            if (ret == 0) {
                ISOTmpl = ISOAddr;
                saveFile(ISOTmpl);
                Log.e("convert_FPT_to_ISO", DataProcessUtil.bytesToHexString(ISOAddr));
                Toast.makeText(this, "Convert FPT to ISO successful", Toast.LENGTH_SHORT).show();
                compareFingerprints(fingerprintPath,toBeCompared);
                return;
            }
            Toast.makeText(this, "Failed to convert FPT to ISO" ,Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }
        return;
    }

    public void compareFingerprints(String stored,String toBeCompared){
        try{

            byte[] ISO1 = readFile(stored);
            byte[] ISO2 = readFile(toBeCompared);

            Log.e("verify_ISO_tmpl", "ISO1: "+DataProcessUtil.bytesToHexString(ISO1)+"\nISO2: "+DataProcessUtil.bytesToHexString(ISOTmpl));

            ret = Finger.verify_ISO_tmpl(ISO1, ISO2); //ISOTmpl

            if (ret == 0) {
                progressDialog.dismiss();

                Date date = new Date();

                String collectDate = dateFormat.format(date);
                db.addCollectedInputs(new CollectedInputs(orderID, collectDate,
                        userID,Config.FINGERPRINT));

                db.deleteAssignedInput(orderID);

                Intent intent = new Intent(mContext, FarmInputsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                MyAlerts.toActivityDialog(mContext,"Fingerprint successfully matched \n Inputs collected",intent);

                return;
            }
            progressDialog.dismiss();
            MyAlerts.genericDialog(mContext, "Fingerprint failed to match");
        }catch (Exception e){
            Log.e("verify_ISO_tmpl", "ISO2: "+DataProcessUtil.bytesToHexString(ISOTmpl));
        }
    }

    private byte[] readFile(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            if(!(file.exists())){
                return null;
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            MyAlerts.genericDialog(mContext, "Fingerprint not found!"+filePath);
            return null;
        }
        return buffer;
    }

    private int saveFile(byte[] out){
        try {

            File tempDir = new File(Config.SD_CARD_PATH+"/temp-fingerprint");

            if(!tempDir.exists()){
                tempDir.mkdir();
            }

            String tmplName = String.valueOf(System.currentTimeMillis()) + ".tps900";

            toBeCompared = Config.SD_CARD_PATH+"/temp-fingerprint" + File.separator + tmplName;
            File file = new File(toBeCompared);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(out);
            bufferedOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    private void initListeners() {
        bVerify.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(new File(fingerprintPath).exists()) {
                    showProgressDialog(R.string.place_finger);
                    captureFingerprint();
                }else{
                    MyAlerts.genericDialog(mContext,"Fingerprint does not exist!");
                }

            }
        });

        rgSelectThumb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup rGroup, int checkedId)
            {
                if(rbLeftThumb.isChecked()){
                    String leftFingerprint = genId+"L";
                    fingerprintPath = Config.FINGERPRINTS_PATH+ leftFingerprint.trim()+".tps900";

                    File file = new File(fingerprintPath);
                    Log.i(TAG, " File exists: "+file.exists()+" File name: "+file.getName());

                }else if(rbRightThumb.isChecked()){
                    String rightFingerprint = genId+"R";
                    fingerprintPath = Config.FINGERPRINTS_PATH + rightFingerprint.trim()+".tps900";

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

        Intent intent = getIntent();

        orderID = intent.getStringExtra("order_id");
        userID = intent.getStringExtra(DatabaseHandler.KEY_USER_ID);
        collectDate = intent.getStringExtra(DatabaseHandler.KEY_COLLECT_DATE);
        genId = intent.getStringExtra(DatabaseHandler.KEY_GEN_ID);

        fingerprintPath = Config.FINGERPRINTS_PATH+genId+"R.tps900";
        Log.i(TAG,"Right finger template"+ fingerprintPath +" Exists: "+new File(fingerprintPath).exists());
    }

    private void initView() {
        bCreate.setVisibility(View.GONE);
        rgSelectThumb.setVisibility(View.VISIBLE);
        bManualCollection.setVisibility(View.VISIBLE);
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
                    progressDialog.dismiss();
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
