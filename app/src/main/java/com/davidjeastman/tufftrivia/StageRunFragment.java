package com.davidjeastman.tufftrivia;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
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

public class StageRunFragment extends Fragment {

    private static final String TAG = "StageRunFragment";
    Button[] allButtons = new Button[4];
    private TextView mAppNameTextView;
    private TextView mQuestionTextView;
    private ImageView mCorrectBox;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;
    private ConstraintSet mPrepostConstraintSet = new ConstraintSet();
    private ConstraintSet mPlayConstraintSet = new ConstraintSet();
    private ConstraintLayout mConstraintLayout;

    private List<Question> mQuestions; // Every mStage has three triples, making 9 questions
    private int mCurrentQuestionNumber;
    private Question mCurrentQuestion;
    private boolean isRoundOver;
    private boolean isQuestionOver;

    private View.OnClickListener answerButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isQuestionOver) {
                Button thisButton = (Button) v;

                mQuestions.get(mCurrentQuestionNumber)
                        .setPlayerAnswer(thisButton.getText().toString());

                if (thisButton.getText().equals(mCurrentQuestion.getCorrectAnswer())) {
                    mQuestions.get(mCurrentQuestionNumber).setPlayerCorrect(true);
                    thisButton.setBackground(getResources().getDrawable(R.drawable.button_answer_correct));

                } else {
                    mQuestions.get(mCurrentQuestionNumber).setPlayerCorrect(false);
                    thisButton.setBackground(getResources().getDrawable(R.drawable.button_answer_incorrect));

                    for (Button b : allButtons) {
                        if (b.getText().equals(mCurrentQuestion.getCorrectAnswer()))
                            b.setBackground(getResources().getDrawable(R.drawable.button_answer_correct));
                    }
                }
                isQuestionOver = true;
                endQuestion();
            }
        }
    };

    public static StageRunFragment newInstance() {
        Bundle args = new Bundle();
        StageRunFragment fragment = new StageRunFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Profile profile = ProfileManager.get(getActivity()).getProfile();

        int skill = profile.getSkill();
        mQuestions = QuestionManager.get(getActivity()).getNextTripleSet(skill);
        mCurrentQuestionNumber = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = getActivity();
        mPlayConstraintSet.clone(context, R.layout.fragment_stage_run_play_constraints);

        View v = inflater.inflate(R.layout.fragment_stage_run_prepost, container, false);
        mConstraintLayout = (ConstraintLayout) v;
        mPrepostConstraintSet.clone(mConstraintLayout);

        ImageView correctBox = v.findViewById(R.id.correct_box_imageview);
        // Debug cheat to end stage fast
        correctBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Question q : mQuestions) {

                    q.setQuestionSeen(false);
                    q.setPlayerAnswer(q.getCorrectAnswer());
                    q.setPlayerCorrect(true);
                    QuestionManager.get(getActivity()).updateQuestion(q);
                }
                loadEndStage();
            }
        });

        mAppNameTextView = v.findViewById(R.id.app_name_textview);
        mAppNameTextView.setText(R.string.app_name);
        if (mQuestions.size() == 0)
            Toast.makeText(getContext(), "Not enough questions!", Toast.LENGTH_LONG).show();
        else {
            mCurrentQuestion = mQuestions.get(mCurrentQuestionNumber);
            mQuestionTextView = v.findViewById(R.id.question_textview);

            mAnswerButton1 = v.findViewById(R.id.answer_button_1);
            mAnswerButton2 = v.findViewById(R.id.answer_button_2);
            mAnswerButton3 = v.findViewById(R.id.answer_button_3);
            mAnswerButton4 = v.findViewById(R.id.answer_button_4);

            allButtons[0] = mAnswerButton1;
            allButtons[1] = mAnswerButton2;
            allButtons[2] = mAnswerButton3;
            allButtons[3] = mAnswerButton4;

            startQuestion();
        }

        return v;
    }

    private void startQuestion() {
        if (!isRoundOver) {
            new CountDownTimer(1000, 1000) {

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    TransitionManager.beginDelayedTransition(mConstraintLayout);
                    mPlayConstraintSet.applyTo(mConstraintLayout);

                    isQuestionOver = false;
                    updateUI();
                    updateModel();
                }
            }.start();
        }
    }

    private void endQuestion() {

        new CountDownTimer(1500, 1500) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                getNextQuestion();
                startQuestion();
                TransitionManager.beginDelayedTransition(mConstraintLayout);
                mPrepostConstraintSet.applyTo(mConstraintLayout);
            }
        }.start();

    }

    private void updateUI() {
        for (Button b : allButtons) {
            b.setBackground(getResources().getDrawable(R.drawable.button_answer));
        }

        mQuestionTextView.setText(mCurrentQuestion.getQuestion());

        ArrayList<String> answerBasket = new ArrayList<>();
        answerBasket.add(mCurrentQuestion.getCorrectAnswer());
        answerBasket.add(mCurrentQuestion.getAnswer2());
        answerBasket.add(mCurrentQuestion.getAnswer3());
        answerBasket.add(mCurrentQuestion.getAnswer4());

        Collections.shuffle(answerBasket);
        mAnswerButton1.setText(answerBasket.get(0));
        mAnswerButton1.setOnClickListener(answerButtonClick);
        mAnswerButton2.setText(answerBasket.get(1));
        mAnswerButton2.setOnClickListener(answerButtonClick);
        mAnswerButton3.setText(answerBasket.get(2));
        mAnswerButton3.setOnClickListener(answerButtonClick);
        mAnswerButton4.setText(answerBasket.get(3));
        mAnswerButton4.setOnClickListener(answerButtonClick);
    }

    private void updateModel() {
        mCurrentQuestion.setQuestionSeen(true);
        QuestionManager.get(getActivity()).updateQuestion(mCurrentQuestion);
    }

    private void getNextQuestion() {
        if (mCurrentQuestionNumber < mQuestions.size() - 1)
            mCurrentQuestion = mQuestions.get(++mCurrentQuestionNumber);
        else {
            Log.e(TAG, "No more questions. End stage.");
            isRoundOver = true;
            loadEndStage();
        }
    }

    private void loadEndStage() {
        StageEndFragment nextFrag = StageEndFragment.newInstance((ArrayList<Question>) mQuestions);
        getFragmentManager().beginTransaction()
                .replace(R.id.stage_container, nextFrag, TAG)
                .commit();
    }


}
