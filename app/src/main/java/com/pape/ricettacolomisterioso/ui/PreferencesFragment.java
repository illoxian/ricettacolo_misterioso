package com.pape.ricettacolomisterioso.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.pape.ricettacolomisterioso.R;

public class PreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        //set version summary
        Preference versionPreference = findPreference("version");
        versionPreference.setSummary(requireContext().getResources().getString(R.string.app_name) + " 0.1"); //version hardcoded, change this when implementing app versions

        //set listener to switch instantly to dark mode and vice versa
        Preference darkModePreference = findPreference("dark_mode");

        darkModePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean selectedValue = (boolean) newValue;
                if(selectedValue)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                requireActivity().recreate();
                return true;
            }
        });

        Preference launchDinnerNotificationsPreference = findPreference("notifications_launch_dinner");

        launchDinnerNotificationsPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean selectedValue = (boolean) newValue;
                if(selectedValue)
                    MainActivity.SetAlarmManager(getContext());
                else
                    MainActivity.ClearAlarmManager();
                return true;
            }
        });
    }

}