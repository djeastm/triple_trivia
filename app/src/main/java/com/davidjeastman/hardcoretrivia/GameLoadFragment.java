package com.davidjeastman.hardcoretrivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class GameLoadFragment extends Fragment {

    private static final String ARG_STAGE_ID = "stage_id";

    private TextView mTestTextView;

    public static GameLoadFragment newInstance(int stageId) {
        Bundle args = new Bundle();
        args.putInt(ARG_STAGE_ID, stageId);
        GameLoadFragment fragment = new GameLoadFragment();
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

        int stage = getArguments().getInt(ARG_STAGE_ID,-1);
        mTestTextView.setText(String.valueOf(stage));

        return v;
    }

}
