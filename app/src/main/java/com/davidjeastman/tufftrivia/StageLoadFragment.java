package com.davidjeastman.tufftrivia;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
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

    private TextView mTestTextView;

    public static StageLoadFragment newInstance() {
        Bundle args = new Bundle();
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

        int mStage = ProfileManager.get(getActivity()).getProfile().getStage();
        mTestTextView.setText(String.valueOf(mStage));

        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                StageRunFragment nextFrag= StageRunFragment.newInstance();
                getFragmentManager().beginTransaction()
                        .replace(R.id.stage_container, nextFrag,TAG)
                        .commit();
            }

        }.start();
        return v;
    }

}