package com.davidjeastman.hardcoretrivia;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class GameRunFragment extends Fragment{

    private static final String TAG_FRAGMENT = "game_run_fragment";
    private static final String ARG_STAGE_ID = "stage_id";

    private TextView mTestTextView;
    private int stage;

    public static GameRunFragment newInstance(int stageId) {
        Bundle args = new Bundle();
        args.putInt(ARG_STAGE_ID, stageId);
        GameRunFragment fragment = new GameRunFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_run, container, false);
        mTestTextView = (TextView) v.findViewById(R.id.game_status_textview);

        stage = getArguments().getInt(ARG_STAGE_ID,-1);
        mTestTextView.setText(String.valueOf(stage)+" gameRun");


        return v;
    }
}
