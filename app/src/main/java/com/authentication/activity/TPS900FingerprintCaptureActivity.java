package com.authentication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.util.Log;
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
import com.telpo.usb.finger.Finger;
import com.telpo.usb.finger.util.DataProcessUtil;
import com.telpo.usb.finger.util.RawToBitMap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.svs.farm_app.utils.Config.LEFT_THUMB;
import static com.svs.farm_app.utils.Config.NEW_FARMER_PICTURE;
import static com.svs.farm_app.utils.Config.NEW_LEFT_THUMB;
import static com.svs.farm_app.utils.Config.NEW_RIGHT_THUMB;
import static com.svs.farm_app.utils.Config.RECAPTURE_FARMER_DETAILS;
import static com.svs.farm_app.utils.Config.RIGHT_THUMB;
import static com.svs.farm_app.utils.Config.WHICH_THUMB;

public class TPS900FingerprintCaptureActivity extends BaseClass {

    //Pending operations
    private static final String TAG = TPS900FingerprintCaptureActivity.class.getSimpleName();

    private static Bitmap mBitmapFP = null;

    public static String mFingerprintDir;

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
    private int rets;
    private byte[] ISOTmpl = new byte[810];
    private byte[] FPTAddr;
    private byte[] img_data = new byte[250 * 360];
    private int[] TMPL_No = new int[1];
    private int[] pnDuplNo = new int[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tps_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mContext = TPS900FingerprintCaptureActivity.this;

        Intent intent = getIntent();
        whichThumb = intent.getStringExtra(WHICH_THUMB);
        String toActivityString = intent.getStringExtra(Config.TO_ACTIVITY);
        toActivity = (toActivityString == null) ? "" : toActivityString;

        /*power on fingerprint reader*/
        rets = Finger.initialize();
        if (rets == 0) {
            Toast.makeText(this, "Initialized fingerprint successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to initialize fingerprint", Toast.LENGTH_SHORT).show();
        }

        initView();
        initData();
        setToolbarTitle();

        bCreate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                captureFingerprint();
            }
        });

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
            //saveImage(bmp);
            if (bmp != null) {
                ivFingerprint.setImageBitmap(bmp);
            }
            return;
        }
        Toast.makeText(this, String.format("0x%02x", ret), Toast.LENGTH_SHORT).show();
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
                return;
            }
            Toast.makeText(this, String.format("0x%02x", ret)+"fail" ,Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }
        return;
    }

    private int saveFile(byte[] out){
        try {
            String tmplName = String.valueOf(System.currentTimeMillis()) + ".tps900";

            finalFingerprintPath = mFingerprintDir + File.separator + tmplName;
            File file = new File(finalFingerprintPath);
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

    private void initData() {
        db = new DatabaseHandler(mContext);

        mFingerprintDir = TPSFingerprintUtils.GetFingerprintDirectory();
    }

    private void initView() {
        bVerify.setVisibility(View.GONE);
        rgSelectThumb.setVisibility(View.GONE);
    }

    private void setToolbarTitle() {
        if (whichThumb.equals(LEFT_THUMB)) {
            getSupportActionBar().setTitle("Left Thumb");
        } else if (whichThumb.equals(RIGHT_THUMB)) {
            getSupportActionBar().setTitle("Right Thumb");
        } else if (whichThumb.equals(NEW_LEFT_THUMB)) {
            getSupportActionBar().setTitle("Recapture Left Thumb");
        } else if (whichThumb.equals(NEW_RIGHT_THUMB)) {
            getSupportActionBar().setTitle("Recapture Right Thumb");
        }
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
                if (finalFingerprintPath != null) {
                    Intent intent = null;
                    if (whichThumb.equals(LEFT_THUMB)) {
                        MyPrefrences.savePrefrence(mContext, Config.LEFT_THUMB, finalFingerprintPath);

                        intent = new Intent(mContext, TPS900FingerprintCaptureActivity.class).putExtra(Config.WHICH_THUMB, RIGHT_THUMB);

                    } else if (whichThumb.equals(RIGHT_THUMB)) {

                        MyPrefrences.savePrefrence(mContext, Config.RIGHT_THUMB, finalFingerprintPath);
                        intent = new Intent(mContext, FarmerRegistrationActivity.class);

                    } else if (whichThumb.equals(NEW_LEFT_THUMB)) {

                        MyPrefrences.savePrefrence(mContext, NEW_LEFT_THUMB, finalFingerprintPath);
                        intent = new Intent(mContext, TPS900FingerprintCaptureActivity.class).putExtra(Config.WHICH_THUMB, NEW_RIGHT_THUMB);

                        if (toActivity.equals(RECAPTURE_FARMER_DETAILS)) {
                            intent.putExtra(Config.TO_ACTIVITY, Config.RECAPTURE_FARMER_DETAILS);
                        }

                    } else if (whichThumb.equals(NEW_RIGHT_THUMB)) {

                        MyPrefrences.savePrefrence(mContext, NEW_RIGHT_THUMB, finalFingerprintPath);
                        Log.i(TAG, "toActivity|" + toActivity);
                        if (toActivity.equals(RECAPTURE_FARMER_DETAILS)) {

                            db.addReRegisteredFarmer(
                                    new ReRegisteredFarmers(Preferences.COMPANY_ID, Preferences.USER_ID, MyPrefrences.getPrefrence(mContext, "wack_fid"),
                                            MyPrefrences.getPrefrence(mContext, "wack_gen_id"),
                                            MyPrefrences.getPrefrence(mContext, NEW_FARMER_PICTURE),
                                            MyPrefrences.getPrefrence(mContext, Config.NEW_LEFT_THUMB),
                                            MyPrefrences.getPrefrence(mContext, Config.NEW_RIGHT_THUMB)));

                            db.removeWackFarmer(MyPrefrences.getPrefrence(mContext, "wack_fid").toString());

                            intent = new Intent(mContext, ReRegistrationActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        } else {

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
                } else {
                    MyAlerts.genericDialog(mContext, "Capture fingerprint first!");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
