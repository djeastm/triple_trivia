package com.davidjeastman.tufftrivia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class SettingsFragment extends Fragment {
    private static final String ARG_SETTINGS_ID = "settings_id";

    private TextView mSettingsField;

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

        mSettingsField = (TextView) v.findViewById(R.id.settings_label_text_view);
        mSettingsField.setText("Settings");
        return v;
    }
}
