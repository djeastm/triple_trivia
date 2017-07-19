package com.davidjeastman.tufftrivia;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class StageRunFragment extends Fragment {

    private static final String TAG = "StageRunFragment";
    private static final int TIMER_SECONDS = 10;

    private static final String SOUNDS_FOLDER = "stage_sounds";
    private static final String CORRECT_SOUND = "Bing";
    private static final String INCORRECT_SOUND = "Whiff";
    private static final int MAX_SOUNDS = 2;

    Button[] allButtons = new Button[4];
    private TextView mAppNameTextView;
    private TextView mQuestionTextView;
    private Button mFiftyFiftyButton;
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
    private long mStageTimeLeft;
    CountDownTimer mCountDownTimer;
    CountDownTimer mStartQuestionTimer;
    CountDownTimer mEndQuestionTimer;

    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

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
                    play(CORRECT_SOUND);
                } else {
                    mQuestions.get(mCurrentQuestionNumber).setPlayerCorrect(false);
                    thisButton.setBackground(getResources().getDrawable(R.drawable.button_answer_incorrect));

                    for (Button b : allButtons) {
                        if (b.getText().equals(mCurrentQuestion.getCorrectAnswer()))
                            b.setBackground(getResources().getDrawable(R.drawable.button_answer_correct));
                    }
                    play(INCORRECT_SOUND);
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

        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = getActivity();
        mPlayConstraintSet.clone(context, R.layout.fragment_stage_run_play_constraints);

        View v = inflater.inflate(R.layout.fragment_stage_run_prepost, container, false);
        mConstraintLayout = (ConstraintLayout) v;
        mPrepostConstraintSet.clone(mConstraintLayout);

        final Button timerButton = v.findViewById(R.id.timer_box_button);
        timerButton.setText(String.valueOf(TIMER_SECONDS));

        mFiftyFiftyButton = v.findViewById(R.id.fifty_fifty_button);

        mCountDownTimer = new CountDownTimer((TIMER_SECONDS+1)*1000, 1000) {
            @Override
            public void onTick(long l) {
                timerButton.setText(String.valueOf(l/1000));
                mStageTimeLeft = l;
            }

            @Override
            public void onFinish() {
                loadEndStage();
            }
        };

        mFiftyFiftyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeTwoAnswerButtons();
            }
        });

        // Debug cheat to end stage fast
        timerButton.setOnClickListener(new View.OnClickListener() {
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

        mAssets = context.getAssets();
        loadSounds();
        mCountDownTimer.start();
        return v;
    }

    void removeTwoAnswerButtons() {
        // Shuffle the buttons
        List<Button> buttons = Arrays.asList(allButtons);
        Collections.shuffle(buttons);

        // Remove two (checking that they're not the correct answer)
        int count = 0;
        for (Button b : buttons) {
            if (!b.getText().equals(mCurrentQuestion.getCorrectAnswer())) {
                b.setVisibility(View.INVISIBLE);
                count++;
            }
            if (count == 2) break;
        }

        mFiftyFiftyButton.setEnabled(false);
        mFiftyFiftyButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray));
    }

    private void startQuestion() {
        if (!isRoundOver) {
            mStartQuestionTimer = new CountDownTimer(1000, 1000) {

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

        mEndQuestionTimer = new CountDownTimer(1500, 1500) {
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

        if (getFragmentManager() != null) {
        StageEndFragment nextFrag = StageEndFragment
                .newInstance((ArrayList<Question>) mQuestions, mStageTimeLeft);
                getFragmentManager().beginTransaction()
                .replace(R.id.stage_container, nextFrag, TAG)
                .commit();
        }
    }

    void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            //Log.i(TAG, "Found " + soundNames.length + " stage_sounds");
            //for (String s : soundNames) Log.i(TAG, s);
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            }catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + filename, ioe);
            }
        }
    }

    public void play(String soundFilename) {
        int soundId;
        switch (soundFilename) {
            case CORRECT_SOUND:
                soundId = mSounds.get(0).getSoundId();
                break;
            case INCORRECT_SOUND:
                soundId = mSounds.get(1).getSoundId();
                break;
            default:
                soundId = mSounds.get(0).getSoundId();
        }

        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSoundPool.release();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
        if (mStartQuestionTimer != null)
            mStartQuestionTimer.cancel();
        if (mEndQuestionTimer != null)
            mEndQuestionTimer.cancel();

    }
}
