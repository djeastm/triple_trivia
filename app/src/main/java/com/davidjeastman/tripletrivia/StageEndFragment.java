package com.davidjeastman.tripletrivia;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.duration;
import static com.davidjeastman.tripletrivia.R.id.stage_end_points_fraction_text_view;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class StageEndFragment extends Fragment {

    private static final String TAG = "StageEndFragment";
    private static final String ARG_QUESTION_LIST_ID = "question_list_id";
    private static final String ARG_TIME_ID = "time_id";
    private static final String KEY_UPDATED = "updated";
    private static final int POINTS_MULTIPLIER = 50;
    private static final int TIME_BONUS_MULTIPLIER = 25;
    private static final int NUM_REQUIRED_CORRECT = 6;
    private static final int PROGRESS_ANIMATION_DURATION = 1750;
    private static final int SPIN_ANIMATION_DURATION = 1000;

    TextView mStageEndMessageTextView;
    TextView mStageEndSubtitleTextView;
    TextView mStageEndCorrectAnswersTextView;
    TextView mStageEndNextLevelTextView;
    ProgressBar1000 mStageEndPointsFractionProgressBar;
    TextView mStageEndPointsFractionTextView;
    TextView mStageEndPointsAbbrevTextView;
    TextView mStageEndTimeBonusPtsAbbrevTextView;
    Button mStageEndContinueTryAgainButton;
    CountDownTimer mLevelChangeTimer;

    private Profile mProfile;
    private ArrayList<Question> mQuestions;
    private QuestionAdapter mAdapter;
    private int mNumCorrect;
    private int mStagePoints;
    private int mTime;
    private int mTimeBonusPoints;
    private boolean mIsUpdated = false;

    private View.OnClickListener continueTryAgainButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            StageLoadFragment nextFrag = StageLoadFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(R.id.stage_container, nextFrag, TAG)
                    .commit();
        }
    };

    public static StageEndFragment newInstance(ArrayList<Question> questions, long time) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION_LIST_ID, questions);
        args.putLong(ARG_TIME_ID, time);
        StageEndFragment fragment = new StageEndFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mProfile = ProfileManager.get(getActivity()).getProfile();
        if (savedInstanceState != null) {
            mIsUpdated = savedInstanceState.getBoolean(KEY_UPDATED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stage_end, container, false);

        mStageEndMessageTextView = v.findViewById(R.id.stage_end_message_textview);
        mStageEndSubtitleTextView = v.findViewById(R.id.stage_end_subtitle_textview);
        mStageEndCorrectAnswersTextView = v.findViewById(R.id.stage_end_correct_answers_textview);
        mStageEndNextLevelTextView = v.findViewById(R.id.stage_end_next_level_score_textview);
        mStageEndPointsFractionProgressBar = v.findViewById(R.id.stage_end_points_fraction_progress_bar);
        mStageEndPointsFractionTextView = v.findViewById(stage_end_points_fraction_text_view);
        mStageEndPointsAbbrevTextView = v.findViewById(R.id.stage_end_points_points_abbrev_textview);
        mStageEndTimeBonusPtsAbbrevTextView = v.findViewById(R.id.stage_end_time_bonus_points_abbrev_textview);
        mStageEndContinueTryAgainButton = v.findViewById(R.id.stage_end_continue_try_again_button);

        mQuestions = (ArrayList<Question>) getArguments().getSerializable(ARG_QUESTION_LIST_ID);
        ArrayList<Question> toRemove = new ArrayList<>();
        for (Question q: mQuestions) {
            if (!q.isQuestionSeen()) toRemove.add(q);
        }
        mQuestions.removeAll(toRemove);

        mTime = (int) getArguments().getLong(ARG_TIME_ID);
        RecyclerView questionRecyclerView = v.findViewById(R.id.question_recycler_view);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mAdapter == null) {
            mAdapter = new QuestionAdapter(mQuestions);
            questionRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setQuestions(mQuestions);
            mAdapter.notifyDataSetChanged();
        }

        boolean isStagePassed = calculateScore();
        int thisStageNum = mProfile.getStage();

        if (!mIsUpdated) {
            updateProfile(isStagePassed);
            mProfile = ProfileManager.get(getActivity()).getProfile();
        }

        if (isStagePassed) {
            mStageEndMessageTextView
                    .setText(getString(R.string.stage_completed, thisStageNum));
            mStageEndMessageTextView
                    .setTextColor(ContextCompat.getColor(getContext(), R.color.mediumGreen));
            mStageEndContinueTryAgainButton
                    .setText(R.string.continue_button);

        } else {
            mStageEndMessageTextView
                    .setText(R.string.game_over);
            mStageEndMessageTextView
                    .setTextColor(ContextCompat.getColor(getContext(), R.color.mediumRed));
            mStageEndSubtitleTextView
                    .setText(R.string.better_luck);
            mStageEndContinueTryAgainButton
                    .setText(R.string.try_again_button);
        }

        mStageEndCorrectAnswersTextView
                .setText(getString(R.string.correct_answers, mNumCorrect, mQuestions.size()));
//        String progress_to_next_level =
//                getString(R.string.progress_to_level) + " " + (mProfile.getLevel() + 1);
        mStageEndNextLevelTextView
                .setText(getString(R.string.profile_level_text,mProfile.getLevel()));
        mStageEndPointsAbbrevTextView
                .setText(String.valueOf(mStagePoints));
        mStageEndTimeBonusPtsAbbrevTextView
                .setText(String.valueOf(mTimeBonusPoints));

        mStageEndContinueTryAgainButton.setOnClickListener(continueTryAgainButtonClick);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(KEY_UPDATED, mIsUpdated);
    }

    private boolean calculateScore() {
        mNumCorrect = 0;
        mStagePoints = 0;

        for (Question q : mQuestions) {
            if (q.isPlayerCorrect()) {
                mNumCorrect++;
                mStagePoints = mStagePoints + q.getDifficulty() * POINTS_MULTIPLIER;
            }
        }

        mTimeBonusPoints = (mTime/1000) * TIME_BONUS_MULTIPLIER;
        //double threshold = mQuestions.size() * .7;
        double threshold = NUM_REQUIRED_CORRECT;

        return mNumCorrect > threshold;
    }

    private void updateProfile(boolean isStagePassed) {
        mIsUpdated = true;
        int pointsBeforeAdding = mProfile.getPoints();
        final int next_level_point_threshold = Profile.getNextLevelThreshold(mProfile.getLevel());
        int initialFraction = (int) (((double) pointsBeforeAdding
                / next_level_point_threshold) * ProgressBar1000.MAX);
        mStageEndPointsFractionProgressBar.setProgress(initialFraction);
        mStageEndPointsFractionTextView.setText(getString(
                R.string.points_fraction,String.valueOf(pointsBeforeAdding),next_level_point_threshold));

        if (isStagePassed) {
            mProfile.increaseSkill();
            mProfile.increaseStage();

            int levelPoints = mStagePoints + mTimeBonusPoints;
            int newTotalPoints = mProfile.getPoints() + levelPoints;
            mProfile.setPoints(newTotalPoints);

            if (newTotalPoints > next_level_point_threshold) {
                // Split animations
                // Part 1 - Complete current level
                int updatedFraction = ProgressBar1000.MAX;
                ObjectAnimator progressAnimator1 = ObjectAnimator
                        .ofInt(mStageEndPointsFractionProgressBar, "progress",
                                initialFraction, updatedFraction);
                progressAnimator1.setDuration(PROGRESS_ANIMATION_DURATION);
                progressAnimator1.setInterpolator(new AccelerateInterpolator());

                ValueAnimator pointsAnimator1 = ValueAnimator
                        .ofInt(pointsBeforeAdding, next_level_point_threshold);
                pointsAnimator1.setDuration(PROGRESS_ANIMATION_DURATION);
                pointsAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        if (isAdded())
                        if (mStageEndPointsFractionTextView != null)
                        mStageEndPointsFractionTextView
                                .setText(getString(R.string.points_fraction,
                                        valueAnimator.getAnimatedValue().toString(), next_level_point_threshold));
                    }
                });


                final int newLevel = mProfile.getLevel() + 1;
                mProfile.setLevel(newLevel);

                // Change level
                long levelChangeDuration = SPIN_ANIMATION_DURATION + PROGRESS_ANIMATION_DURATION;
                mLevelChangeTimer = new CountDownTimer(
                        levelChangeDuration, levelChangeDuration) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if (mStageEndPointsFractionTextView != null)
//                        String progress_to_next_level =
//                                getString(R.string.progress_to_level) + " " + (newLevel + 1);
                        mStageEndNextLevelTextView
                                .setText(getString(R.string.profile_level_text,newLevel));

                    }
                }.start();

                final int next_next_level_point_threshold = Profile.getNextLevelThreshold(newLevel);
                ObjectAnimator levelChangeAnimatorBar = ObjectAnimator
                        .ofFloat(mStageEndPointsFractionProgressBar, "rotation",
                                0f, 360f);
                levelChangeAnimatorBar.setDuration(SPIN_ANIMATION_DURATION);
                levelChangeAnimatorBar.setInterpolator(new AccelerateInterpolator());
                ObjectAnimator levelChangeAnimatorPts = ObjectAnimator
                        .ofFloat(mStageEndPointsFractionTextView, "rotation",
                                0f, 360f);
                levelChangeAnimatorPts.setDuration(SPIN_ANIMATION_DURATION);
                levelChangeAnimatorPts.setInterpolator(new AccelerateInterpolator());

                // Part 2

                initialFraction = (int) (((double) pointsBeforeAdding
                        / next_next_level_point_threshold) * ProgressBar1000.MAX);;
                updatedFraction = (int) (((double) newTotalPoints
                        / next_next_level_point_threshold) * ProgressBar1000.MAX);
                ObjectAnimator progressAnimator2 = ObjectAnimator
                        .ofInt(mStageEndPointsFractionProgressBar, "progress",
                                initialFraction, updatedFraction);
                progressAnimator2.setDuration(PROGRESS_ANIMATION_DURATION);
                progressAnimator2.setInterpolator(new AccelerateInterpolator());

                ValueAnimator pointsAnimator2 = ValueAnimator
                        .ofInt(next_level_point_threshold, mProfile.getPoints());
                pointsAnimator2.setDuration(PROGRESS_ANIMATION_DURATION);
                pointsAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        if (isAdded())
                        if (mStageEndPointsFractionTextView != null)
                        mStageEndPointsFractionTextView
                                .setText(getString(R.string.points_fraction,
                                        valueAnimator.getAnimatedValue().toString(), next_next_level_point_threshold));
                    }
                });

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(progressAnimator1).with(pointsAnimator1).before(levelChangeAnimatorBar);
                animatorSet.play(levelChangeAnimatorBar).with(levelChangeAnimatorPts).before(progressAnimator2);
                animatorSet.play(progressAnimator2).with(pointsAnimator2);
                animatorSet.start();
            }
            else {
                int updatedFraction = (int) (((double) mProfile.getPoints()
                        / next_level_point_threshold) * ProgressBar1000.MAX);
                ObjectAnimator progressAnimator = ObjectAnimator
                        .ofInt(mStageEndPointsFractionProgressBar, "progress",
                                initialFraction, updatedFraction);
                progressAnimator.setDuration(duration);
                progressAnimator.setInterpolator(new AccelerateInterpolator());

                ValueAnimator pointsAnimator = ValueAnimator
                        .ofInt(pointsBeforeAdding, mProfile.getPoints());
                pointsAnimator.setDuration(duration);
                pointsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        if (isAdded())
                        if (mStageEndPointsFractionTextView != null)
                        mStageEndPointsFractionTextView
                                .setText(getString(R.string.points_fraction,
                                        valueAnimator.getAnimatedValue().toString(), next_level_point_threshold));
                    }
                });

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(progressAnimator).with(pointsAnimator);
                animatorSet.start();


            }
            ProfileManager.get(getActivity()).updateProfile(mProfile);
        } else {
            mStagePoints = 0;
            mTimeBonusPoints = 0;

            mProfile.reduceSkill();
        }
    }

    private class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Question mQuestion;

        private TextView mQuestionTextView;
        private TextView mCorrectAnswerTextView;
        private ImageView mImageView;


        public QuestionHolder(View v) {
            super(v);

            mQuestionTextView = v.findViewById(R.id.list_item_question_textview);
            mCorrectAnswerTextView = v.findViewById(R.id.list_item_correct_answer_textview);
            mImageView = v.findViewById(R.id.question_correct_imageview);
        }

        public void bindEntry(Question question) {
            mQuestion = question;
            if (mQuestion.isPlayerCorrect())
                mImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_accept,null));
            else mImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_remove,null));
            mQuestionTextView.setText(mQuestion.getQuestion());
            mCorrectAnswerTextView.setText(mQuestion.getCorrectAnswer());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), "Clicked!", Toast.LENGTH_SHORT);
        }
    }

    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {


        public QuestionAdapter(ArrayList<Question> questions) {
            mQuestions = questions;
        }

        @Override
        public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_question, parent, false);
            return new QuestionHolder(view);
        }

        @Override
        public void onBindViewHolder(QuestionHolder holder, int position) {
            Question question = mQuestions.get(position);
            holder.bindEntry(question);
        }

        @Override
        public int getItemCount() {
            return mQuestions.size();
        }

        public void setQuestions(ArrayList<Question> questions) {
            mQuestions = questions;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLevelChangeTimer != null) mLevelChangeTimer.cancel();
    }
}
