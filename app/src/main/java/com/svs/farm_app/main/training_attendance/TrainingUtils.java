package com.svs.farm_app.main.training_attendance;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 15-Sep-17.
 */

public class TrainingUtils {
    private static final String TAG = TrainingUtils.class.getSimpleName();
    public static List<String> trackFarmers = new ArrayList<>();

    public static void removeFarmer(String farmerId){
        for(int i = 0;i<trackFarmers.size();i++){
            if(trackFarmers.get(i).equals(farmerId)){
                trackFarmers.remove(i);
                break;
            }
        }
    }

    public static boolean addFarmer(String farmerId){
        return trackFarmers.add(farmerId);
    }

    public static boolean isInTraining(String farmerId){
        for(int i = 0;i<trackFarmers.size();i++){
            if(trackFarmers.get(i).equals(farmerId)){
                return true;
            }
        }
        return false;
    }

}
