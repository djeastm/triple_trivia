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

public class LevelLoadFragment extends Fragment {

    private static final String TAG = "LevelLoadFragment";
    private static final String ARG_LEVEL_ID = "level_id";

    private TextView mTestTextView;
    private int level;

    public static LevelLoadFragment newInstance(int levelId) {
        Bundle args = new Bundle();
        args.putInt(ARG_LEVEL_ID, levelId);
        LevelLoadFragment fragment = new LevelLoadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_load, container, false);
        mTestTextView = (TextView) v.findViewById(R.id.game_test_text_view);

        level = getArguments().getInt(ARG_LEVEL_ID,-1);
        mTestTextView.setText(String.valueOf(level));

        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                LevelRunFragment nextFrag= LevelRunFragment.newInstance(level);
                getFragmentManager().beginTransaction()
                        .replace(R.id.game_container, nextFrag,TAG)
                        .commit();
            }

        }.start();
        return v;
    }

}
