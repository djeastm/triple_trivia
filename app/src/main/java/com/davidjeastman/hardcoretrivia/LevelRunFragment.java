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

public class LevelRunFragment extends Fragment{

    private static final String TAG = "LevelRunFragment";
    private static final String ARG_LEVEL_ID = "level_id";


    private TextView mTestTextView;
    private int level;
    private Question mQuestion;

    public static LevelRunFragment newInstance(int levelId) {
        Bundle args = new Bundle();
        args.putInt(ARG_LEVEL_ID, levelId);
        LevelRunFragment fragment = new LevelRunFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

//        UUID questionId = (UUID) getArguments().getSerializable(ARG_TRIPLE_ID);
//        mQuestion = QuestionBank.get(getActivity()).getQuestion(questionId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_run, container, false);
        mTestTextView = (TextView) v.findViewById(R.id.game_status_textview);

        level = getArguments().getInt(ARG_LEVEL_ID,-1);
        mTestTextView.setText(String.valueOf(level)+" gameRun");


        return v;
    }
}
