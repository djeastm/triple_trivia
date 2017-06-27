package com.davidjeastman.hardcoretrivia;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class StageEndFragment extends Fragment {

    private static final String TAG = "StageEndFragment";
    private static final String ARG_STAGE_ID = "stage_id";
    private static final String ARG_QUESTION_LIST_ID = "question_list_id";
    int mStage;
    private TextView mStageStatusTextView;
    private List<Question> mQuestions;

    private int mNumCorrect;
    private int mStagePoints;

    public static StageEndFragment newInstance(int stageId, ArrayList<Question> questions) {
        Bundle args = new Bundle();
        args.putInt(ARG_STAGE_ID, stageId);
        args.putSerializable(ARG_QUESTION_LIST_ID, questions);
        StageEndFragment fragment = new StageEndFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mStage = getArguments().getInt(ARG_STAGE_ID, -1);

        int skill = ProfileManager.get(getActivity()).getProfile().getSkill();
        mQuestions = (ArrayList) getArguments().getSerializable(ARG_QUESTION_LIST_ID);

        calculateScore();

        //for (Question q : mQuestions) Log.i(TAG,q.getQuestion());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stage_end, container, false);

        mStageStatusTextView = v.findViewById(R.id.stage_status_textview);

        double threshold = mQuestions.size()*.7;
        Log.i(TAG, String.valueOf(threshold) + " " + mNumCorrect);
        if (mNumCorrect <= threshold) {
            v.getRootView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
            mStageStatusTextView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
        }else
            v.getRootView().setBackgroundColor(ContextCompat.getColor(getContext(),R.color.green));


        return v;
    }

    private void calculateScore() {

        mNumCorrect = 0;
        mStagePoints = 0;
        for (Question q : mQuestions) {
            if (q.isPlayerCorrect()) {
                Log.i(TAG, String.valueOf(mNumCorrect));
                mNumCorrect++;
                mStagePoints = mStagePoints + q.getDifficulty() * 100;
            }
        }


    }
}
