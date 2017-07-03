package com.davidjeastman.tufftrivia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class StageEndFragment extends Fragment {

    private static final String TAG = "StageEndFragment";
    private static final String ARG_QUESTION_LIST_ID = "question_list_id";
    private static final String KEY_UPDATED = "updated";

    private List<Question> mQuestions;

    private RecyclerView mQuestionRecyclerView;
    private QuestionAdapter mAdapter;

    private int mStage;
    private int mProfilePoints;
    private int mNumCorrect;
    private int mStagePoints;
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

    public static StageEndFragment newInstance(ArrayList<Question> questions) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION_LIST_ID, questions);
        StageEndFragment fragment = new StageEndFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Profile profile = ProfileManager.get(getActivity()).getProfile();
        if (savedInstanceState != null) {
            mIsUpdated = savedInstanceState.getBoolean(KEY_UPDATED);
        }

        mProfilePoints = profile.getPoints();
        mStage = profile.getStage();

        //for (Question q : mQuestions) Log.i(TAG,q.getQuestion());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stage_end, container, false);

        DrawerLayout mDrawerLayout = v.findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        TextView mStageAppNameTextView = v.findViewById(R.id.app_name_textview);
        TextView mStageEndMessageTextView = v.findViewById(R.id.stage_end_message_textview);
        TextView mStageEndSubtitleTextView = v.findViewById(R.id.stage_end_subtitle_textview);
        TextView mStageEndCorrectAnswersTextView = v.findViewById(R.id.stage_end_correct_answers_textview);
        TextView mStageEndPointsFractionTextView = v.findViewById(R.id.stage_end_points_fraction_textview);
        TextView mStageEndPointsAbbrevTextView = v.findViewById(R.id.stage_end_points_points_abbrev_textview);
        TextView mStageEndTimeBonusPtsAbbrevTextView = v.findViewById(R.id.stage_end_time_bonus_points_abbrev_textview);
        Button mStageEndContinueTryAgainButton = v.findViewById(R.id.stage_end_continue_try_again_button);

        mQuestions = (ArrayList) getArguments().getSerializable(ARG_QUESTION_LIST_ID);

        mQuestionRecyclerView = v.findViewById(R.id.question_recycler_view);
        mQuestionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mAdapter == null) {
            mAdapter = new QuestionAdapter(mQuestions);
            mQuestionRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setQuestions(mQuestions);
            mAdapter.notifyDataSetChanged();
        }

        boolean isStagePassed = calculateScore();
        if (!mIsUpdated) updateProfile(isStagePassed);

        if (isStagePassed) {
            v.getRootView()
                    .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.mediumGreen));
            mStageAppNameTextView
                    .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.mediumGreen));
            mStageEndMessageTextView
                    .setText(getString(R.string.stage) + " " + String.valueOf(mStage)
                            + " " + getString(R.string.completed));
            mStageEndContinueTryAgainButton
                    .setText(R.string.continue_button);

        } else {
            v.getRootView()
                    .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.mediumRed));
            mStageAppNameTextView
                    .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.mediumRed));
            mStageEndMessageTextView
                    .setText(R.string.game_over);
            mStageEndSubtitleTextView
                    .setText(R.string.better_luck);
            mStageEndPointsFractionTextView
                    .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.darkRed));
            mStageEndContinueTryAgainButton
                    .setText(R.string.try_again_button);
        }

        mStageEndCorrectAnswersTextView
                .setText(String.valueOf(mNumCorrect) + " / "
                        + String.valueOf(mQuestions.size()) + " " + getString(R.string.correct_answers));
        mStageEndPointsFractionTextView
                .setText(String.valueOf(mProfilePoints) + " / " + 4000);
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
                mStagePoints = mStagePoints + q.getDifficulty() * 100;
            }
        }

        double threshold = mQuestions.size() * .7;
        boolean isStagePassed = mNumCorrect > threshold;

        return isStagePassed;
    }

    private void updateProfile(boolean isStagePassed) {
        mIsUpdated = true;
        ProfileManager pm = ProfileManager.get(getActivity());
        Profile profile = pm.getProfile();
        if (isStagePassed) {
            profile.increaseSkill();
            profile.increaseStage();

            mProfilePoints += mStagePoints;
            profile.setPoints(mProfilePoints);

            pm.updateProfile(profile);
        } else {
            mStagePoints = 0;
            mTimeBonusPoints = 0;

            profile.reduceSkill();
        }
    }

    private class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Question mQuestion;

        private TextView mQuestionTextView;
        private TextView mCorrectAnswerTextView;


        public QuestionHolder(View v) {
            super(v);

            mQuestionTextView = v.findViewById(R.id.list_item_question_textview);
            mCorrectAnswerTextView = v.findViewById(R.id.list_item_correct_answer_textview);
        }

        public void bindEntry(Question question) {
            Log.i(TAG, "bindEntry: "+question.getQuestion());
            mQuestion = question;
            mQuestionTextView.setText(mQuestion.getQuestion());
            mCorrectAnswerTextView.setText(mQuestion.getCorrectAnswer());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), "Clicked!", Toast.LENGTH_SHORT);
        }
    }
    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {


        public QuestionAdapter(List<Question> questions) {
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

        public void setQuestions(List<Question> questions) {
            mQuestions = questions;
        }
    }
}
