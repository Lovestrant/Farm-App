package com.svs.farm_app.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.svs.farm_app.R;

/**
 * Created by Benson on 3/10/2015.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        }

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

        String language = SP.getString("language","NULL");
        
    }

    /**
     * This fragment shows the preferences for the second header.
     */
    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
