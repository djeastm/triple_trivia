package com.davidjeastman.tripletrivia;

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
    private ProgressBar1000 mNextLevelProgressBar;
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

        mNextLevelProgressBar = v.findViewById(R.id.profile_points_fraction_progress_bar);

        mPointsField = v.findViewById(R.id.profile_points_fraction_text_view);

        // Hide rank until networking is set up
        mRankField = v.findViewById(R.id.profile_rank_text_view);
        TextView mRankLabel = v.findViewById(R.id.profile_rank_label_text_view);
        mRankField.setVisibility(View.GONE);
        mRankLabel.setVisibility(View.GONE);

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
        getResources().getString(R.string.profile_level_text, mProfile.getLevel());
        mLevelField.setText(getResources()
                .getString(R.string.profile_level_text, mProfile.getLevel()));

        int next_level_point_threshold = Profile.getNextLevelThreshold(mProfile.getLevel());

        mNextLevelProgressBar
                .setProgress((int) (((double) mProfile.getPoints()
                        / next_level_point_threshold) * ProgressBar1000.MAX));
        mPointsField
                .setText(getString(R.string.points_fraction,
                        String.valueOf(mProfile.getPoints()), next_level_point_threshold));
        mRankField.setText(String.valueOf(mProfile.getRank()));
    }
}
