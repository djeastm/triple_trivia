package com.davidjeastman.hardcoretrivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Eastman on 6/25/2017.
 */

public class ProfileSetupFragment extends Fragment {
    private static final String TAG = "ProfileSetupFragment";

    private Profile mProfile;

    public static ProfileSetupFragment newInstance() {
        Bundle args = new Bundle();
        ProfileSetupFragment fragment = new ProfileSetupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mProfile = new Profile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_setup, container, false);

        EditText mNameEditText = v.findViewById(R.id.profile_setup_name_edittext);
        mNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mProfile.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        EditText mLocationEditText =  v.findViewById(R.id.profile_setup_location_edittext);
        mLocationEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mProfile.setLocation(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        Button mSubmitButton = v.findViewById(R.id.profile_setup_submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView notificationTextView = view.findViewById(R.id.profile_setup_notification_textview);
                if (mProfile.getName().isEmpty()) {
                    notificationTextView.setText(R.string.profile_setup_notification_name);
                    return;
                }
                if (mProfile.getLocation().isEmpty()) {
                    notificationTextView.setText(R.string.profile_setup_notification_location);
                    return;
                }

                ProfileManager.get(getActivity()).addProfile(mProfile);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return v;
    }
}
