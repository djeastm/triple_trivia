package com.davidjeastman.tufftrivia;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import static java.lang.Boolean.TRUE;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class SettingsFragment extends Fragment {
    private static final String TAG = "SettingsFragment";
    private static final String ARG_SETTINGS_ID = "settings_id";

    private SwitchCompat mSoundOnOffSwitch;
    SharedPreferences mSharedPreferences;

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SETTINGS_ID, 1);
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        Context context = getActivity();
        mSharedPreferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        boolean isSoundOn = mSharedPreferences.getBoolean(getString(R.string.sound_on_off_key), true);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(getString(R.string.sound_on_off_key), isSoundOn);
        editor.apply();

        mSoundOnOffSwitch = v.findViewById(R.id.sound_on_off_switch);
        mSoundOnOffSwitch.setChecked(isSoundOn);
        mSoundOnOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean(getString(R.string.sound_on_off_key), isChecked);
                editor.apply();
            }
        });

        return v;
    }
}
