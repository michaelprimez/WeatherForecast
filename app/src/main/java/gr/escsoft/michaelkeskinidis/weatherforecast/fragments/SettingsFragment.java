package gr.escsoft.michaelkeskinidis.weatherforecast.fragments;

import gr.escsoft.michaelkeskinidis.weatherforecast.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Michael on 8/5/2015.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }
}