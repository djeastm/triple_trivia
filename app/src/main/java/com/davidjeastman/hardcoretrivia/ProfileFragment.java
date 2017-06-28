package com.davidjeastman.hardcoretrivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
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
        mProfile = ProfileManager.get(getActivity()).getProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mNameField = v.findViewById(R.id.profile_name_text_view);

        mLocationField = v.findViewById(R.id.profile_location_text_view);

        mLevelField = v.findViewById(R.id.profile_level_text_view);

        mPointsField = v.findViewById(R.id.profile_points_text_view);

        mRankField = v.findViewById(R.id.profile_rank_text_view);

        mPlayButton = v.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stage = mProfile.getStage();
                Intent intent = new Intent(StageActivity.newIntent(getActivity(), stage));
                startActivity(intent);
            }
        });

        updateUI();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        mProfile = ProfileManager.get(getActivity()).getProfile();
        mNameField.setText(mProfile.getName());
        mLocationField.setText(mProfile.getLocation());
        mLevelField.setText(String.valueOf(mProfile.getLevel()));
        mPointsField.setText(String.valueOf(mProfile.getPoints())
                + " " + getResources().getString(R.string.profile_points_label));
        mRankField.setText(String.valueOf(mProfile.getRank()));
    }
}
