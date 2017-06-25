package com.davidjeastman.hardcoretrivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
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

        int skill = ProfileManager.get(getActivity()).getProfile().getSkill();
        mQuestions = QuestionManager.get(getActivity()).getNextTripleSet(skill);

//        for(int i = 0; i < mQuestions.size(); i++) {
//            Log.i(TAG, mQuestions.get(i).getQuestion());
//        }

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

        final String correctAnswer = thisQuestion.getCorrectAnswer();

        class Answer {
            private boolean isCorrect;
            private String questionString;

            private Answer(boolean c, String q) {
                isCorrect = c;
                questionString = q;
            }

        }

        ArrayList<Answer> answerBasket = new ArrayList<>();
        answerBasket.add(new Answer(true, thisQuestion.getCorrectAnswer()));
        answerBasket.add(new Answer(false, thisQuestion.getAnswer2()));
        answerBasket.add(new Answer(false, thisQuestion.getAnswer3()));
        answerBasket.add(new Answer(false, thisQuestion.getAnswer4()));

        Collections.shuffle(answerBasket);

        View.OnClickListener answerButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button thisButton = (Button) v;
                if (thisButton.getText().equals(correctAnswer))
                    Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getActivity(), "Wrong!", Toast.LENGTH_SHORT).show();
            }
        };

        mAnswerButton1 = v.findViewById(R.id.answer_button_1);
        mAnswerButton1.setText(answerBasket.get(0).questionString);
        mAnswerButton1.setOnClickListener(answerButtonClick);

        mAnswerButton2 = v.findViewById(R.id.answer_button_2);
        mAnswerButton2.setText(answerBasket.get(1).questionString);
        mAnswerButton2.setOnClickListener(answerButtonClick);

        mAnswerButton3 = v.findViewById(R.id.answer_button_3);
        mAnswerButton3.setText(answerBasket.get(2).questionString);
        mAnswerButton3.setOnClickListener(answerButtonClick);

        mAnswerButton4 = v.findViewById(R.id.answer_button_4);
        mAnswerButton4.setText(answerBasket.get(3).questionString);
        mAnswerButton4.setOnClickListener(answerButtonClick);


        return v;
    }
}
