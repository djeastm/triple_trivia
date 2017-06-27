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

public class StageLoadFragment extends Fragment {

    private static final String TAG = "StageLoadFragment";
    private static final String ARG_STAGE_ID = "stage_id";

    private TextView mTestTextView;
    private int mStage;

    public static StageLoadFragment newInstance(int stageId) {
        Bundle args = new Bundle();
        args.putInt(ARG_STAGE_ID, stageId);
        StageLoadFragment fragment = new StageLoadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stage_load, container, false);
        mTestTextView = v.findViewById(R.id.game_test_text_view);

        mStage = getArguments().getInt(ARG_STAGE_ID,-1);
        mTestTextView.setText(String.valueOf(mStage));

        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                StageRunFragment nextFrag= StageRunFragment.newInstance(mStage);
                getFragmentManager().beginTransaction()
                        .replace(R.id.stage_container, nextFrag,TAG)
                        .commit();
            }

        }.start();
        return v;
    }

}
