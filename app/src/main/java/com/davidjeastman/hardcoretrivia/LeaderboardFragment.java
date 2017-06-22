package com.davidjeastman.hardcoretrivia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class LeaderboardFragment extends Fragment {
    private static final String ARG_LEADERBOARD_ID = "leaderboard_id";

    //private Leaderboard mLeaderboard;
    private TextView mLeaderField;

    public static LeaderboardFragment newInstance() {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LEADERBOARD_ID, 1);
        LeaderboardFragment fragment = new LeaderboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mLeaderboard = new Profile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        mLeaderField = (TextView) v.findViewById(R.id.leaderboard_label_text_view);
        mLeaderField.setText("Leader");
        return v;
    }
}
