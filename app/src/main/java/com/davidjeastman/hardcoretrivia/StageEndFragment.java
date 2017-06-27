package com.davidjeastman.hardcoretrivia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private List<Question> mQuestions;

    private int mProfilePoints;

    private int mNumCorrect;
    private int mStagePoints;
    private int mTimeBonusPoints;

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

        mProfilePoints = ProfileManager.get(getActivity()).getProfile().getPoints();
        mStage = getArguments().getInt(ARG_STAGE_ID, -1);

        //for (Question q : mQuestions) Log.i(TAG,q.getQuestion());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stage_end, container, false);

        TextView mStageAppNameTextView = v.findViewById(R.id.stage_app_name_textview);
        TextView mStageEndMessageTextView = v.findViewById(R.id.stage_end_message_textview);
        TextView mStageEndSubtitleTextView = v.findViewById(R.id.stage_end_subtitle_textview);
        TextView mStageEndCorrectAnswersTextView = v.findViewById(R.id.stage_end_correct_answers_textview);
        TextView mStageEndPointsFractionTextView = v.findViewById(R.id.stage_end_points_fraction_textview);
        TextView mStageEndPointsAbbrevTextView = v.findViewById(R.id.stage_end_points_points_abbrev_textview);
        TextView mStageEndTimeBonusPtsAbbrevTextView = v.findViewById(R.id.stage_end_time_bonus_points_abbrev_textview);
        Button mStageEndContinueTryAgainButton = v.findViewById(R.id.stage_end_continue_try_again_button);

        int skill = ProfileManager.get(getActivity()).getProfile().getSkill();
        mQuestions = (ArrayList) getArguments().getSerializable(ARG_QUESTION_LIST_ID);
        calculateScore();

        double threshold = mQuestions.size() * .7;
        boolean isPassed = mNumCorrect > threshold;

        if (isPassed) {
            v.getRootView()
                    .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.passColorPrimary));
            mStageAppNameTextView
                    .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.passColorPrimary));
            mStageEndMessageTextView
                    .setText(getString(R.string.stage) + " " + String.valueOf(mStage)
                            + " " + getString(R.string.completed));
            mStageEndContinueTryAgainButton
                    .setText(R.string.continue_button);
        } else {
            v.getRootView()
                    .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.failColorPrimary));
            mStageAppNameTextView
                    .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.failColorPrimary));
            mStageEndMessageTextView
                    .setText(R.string.game_over);
            mStageEndSubtitleTextView
                    .setText(R.string.better_luck);
            mStageEndPointsFractionTextView
                    .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.failColorPrimaryDark));
            mStageEndContinueTryAgainButton
                    .setText(R.string.try_again_button);

            mStagePoints = 0;
            mTimeBonusPoints = 0;
        }

        mStageEndCorrectAnswersTextView
                .setText(String.valueOf(mNumCorrect)+ " / "
                        + String.valueOf(mQuestions.size()) + " " + getString(R.string.correct_answers));
        mStageEndPointsFractionTextView
                .setText(String.valueOf(mProfilePoints + mStagePoints) + " / " + 4000);
        mStageEndPointsAbbrevTextView
                .setText(String.valueOf(mStagePoints));
        mStageEndTimeBonusPtsAbbrevTextView
                .setText(String.valueOf(mTimeBonusPoints));


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
