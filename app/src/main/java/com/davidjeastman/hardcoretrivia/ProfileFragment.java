package com.davidjeastman.hardcoretrivia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class ProfileFragment extends Fragment {

    private static final String ARG_PROFILE_ID = "profile_id";

    private Profile mProfile;
    private TextView mNameField;

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROFILE_ID, 1);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfile = new Profile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mNameField = (TextView) v.findViewById(R.id.profile_name_text_view);
        mNameField.setText(mProfile.getName());
        return v;
    }
}
