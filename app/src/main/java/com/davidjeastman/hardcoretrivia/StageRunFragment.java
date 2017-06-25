package com.davidjeastman.hardcoretrivia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class StageRunFragment extends Fragment{

    private static final String TAG = "StageRunFragment";
    private static final String ARG_STAGE_ID = "stage_id";

    private TextView mStageStatusTextView;
    private TextView mQuestionTextView;
    private ImageView mCorrectBox;

    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    private int stage;
    private List<Question> mQuestions; // Every stage has three triples, making 9 questions

    public static StageRunFragment newInstance(int stageId) {
        Bundle args = new Bundle();
        args.putInt(ARG_STAGE_ID, stageId);
        StageRunFragment fragment = new StageRunFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

//        int tripleId = (int) getArguments().getSerializable(ARG_TRIPLE_ID);
        mQuestions = new ArrayList<>(9);
        for (int tripleId = 1; tripleId <= 3; tripleId++) {
            mQuestions.addAll(QuestionManager.get(getActivity()).getQuestions(tripleId));
        }
        
        for(int i = 0; i < mQuestions.size(); i++) {
            Log.i(TAG, mQuestions.get(i).getQuestion());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stage_run, container, false);
        mStageStatusTextView = v.findViewById(R.id.stage_status_textview);

        stage = getArguments().getInt(ARG_STAGE_ID,-1);
        mStageStatusTextView.setText(String.valueOf(stage)+" gameRun");

        Question thisQuestion = mQuestions.get(0);
        mQuestionTextView = v.findViewById(R.id.question_textview);
        mQuestionTextView.setText(thisQuestion.getQuestion());
        mAnswerButton1 = v.findViewById(R.id.answer_button_1);
        mAnswerButton1.setText(thisQuestion.getCorrectAnswer());
        mAnswerButton2 = v.findViewById(R.id.answer_button_2);
        mAnswerButton2.setText(thisQuestion.getAnswer2());
        mAnswerButton3 = v.findViewById(R.id.answer_button_3);
        mAnswerButton3.setText(thisQuestion.getAnswer3());
        mAnswerButton4 = v.findViewById(R.id.answer_button_4);
        mAnswerButton4.setText(thisQuestion.getAnswer4());


        return v;
    }
}
