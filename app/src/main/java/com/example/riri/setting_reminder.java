package com.example.riri;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.DropDownPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import java.util.Objects;

public class setting_reminder extends PreferenceFragmentCompat {

    SwitchPreference user_theme;
    DropDownPreference time_style,date_style,date_format;
    SharedPreferences sharedPreferences;


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.user_preferences, rootKey);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());

    }

}