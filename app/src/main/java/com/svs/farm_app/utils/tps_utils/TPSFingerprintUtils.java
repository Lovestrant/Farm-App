package com.svs.farm_app.utils.tps_utils;

import android.os.Handler;
import android.util.Log;

import com.authentication.activity.TPS350FingerprintCaptureActivity;
import com.common.pos.api.util.posutil.PosUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ADMIN on 21-Jun-17.
 */



public class TPSFingerprintUtils {
    private static final String TAG = TPSFingerprintUtils.class.getSimpleName();

    public static boolean powerOn(){
        try {
            PosUtil.setFingerPrintPower(PosUtil.FINGERPRINT_POWER_ON);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String GetFingerprintDirectory() {
        String szDbDir;
        File Dir = new File(com.svs.farm_app.utils.Config.NEW_FINGERPRINTS_PATH);
        if (Dir.exists()) {
            if (!Dir.isDirectory()) {

            }
        } else {
            try {
                Dir.mkdirs();
            } catch (SecurityException e) {

            }
        }
        szDbDir = Dir.getAbsolutePath();
        return szDbDir;
    }

    public static void saveFingerprintTemplate(Handler mHandler,String dir,String name, byte[] template, int size) {
        FileOutputStream fs = null;
        File f = null;

        try {
            File fingerprintDir = new File(dir);

            if (!fingerprintDir.exists()) {
                if (!f.isDirectory()) {
                    f.mkdirs();
                }
            }

            Log.i(TAG,"TPS350 "+File.separator+"fingerprints path"+name);

            f = new File(dir+File.separator+name);
            fs = new FileOutputStream(f);

            byte[] writeTemplate = new byte[size];
            System.arraycopy(template, 0, writeTemplate, 0, size);
            fs.write(writeTemplate);
            fs.close();
        } catch (Exception e) {
            String error = String.format("Failed to save template to file %s. Error: %s.", name, e.toString());
            mHandler.obtainMessage(TPS350FingerprintCaptureActivity.MESSAGE_SHOW_ERROR_MSG, -1, -1, error).sendToTarget();
        }
    }
}
