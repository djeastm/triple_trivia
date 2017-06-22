package com.davidjeastman.hardcoretrivia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class ProfileFragment extends Fragment {

    private static final String ARG_PROFILE_ID = "profile_id";

    private Profile mProfile;
    private TextView mNameField;
    private TextView mLocationField;
    private TextView mLevelField;
    private TextView mPointsField;
    private TextView mRankField;

    private Button mPlayButton;

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

        mNameField = v.findViewById(R.id.profile_name_text_view);
        mNameField.setText(mProfile.getName());
        mLocationField = v.findViewById(R.id.profile_location_text_view);
        mLocationField.setText(mProfile.getLocation());
        mLevelField = v.findViewById(R.id.profile_level_text_view);
        mLevelField.setText(String.valueOf(mProfile.getLevel()));
        mPointsField = v.findViewById(R.id.profile_points_text_view);
        mPointsField.setText(String.valueOf(mProfile.getPoints()));
        mRankField = v.findViewById(R.id.profile_rank_text_view);
        mRankField.setText(String.valueOf(mProfile.getRank()));

        mPlayButton = v.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stage = mProfile.getStage();
                Intent intent = new Intent(GameActivity.newIntent(getActivity(), stage));
                startActivity(intent);
            }
        });


        return v;
    }
}
