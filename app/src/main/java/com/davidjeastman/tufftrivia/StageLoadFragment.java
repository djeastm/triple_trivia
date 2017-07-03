package com.davidjeastman.tufftrivia;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class StageLoadFragment extends Fragment {

    private static final String TAG = "StageLoadFragment";
    private static final int mSeconds = 3;

    private TextView mStageLabelTextView;

    public static StageLoadFragment newInstance() {
        Bundle args = new Bundle();
        StageLoadFragment fragment = new StageLoadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stage_load, container, false);

        int mStage = ProfileManager.get(getActivity()).getProfile().getStage();
        mStageLabelTextView = v.findViewById(R.id.stage_load_stage_label_text_view);
        mStageLabelTextView.setText(getResources().getString(R.string.stage) + " " + String.valueOf(mStage));

        final TextView countdownTimerButton = v.findViewById(R.id.stage_load_countdown_text_view);
        countdownTimerButton.setText(String.valueOf(mSeconds));

        final CircleProgressBar countdownTimerProgressBar = v.findViewById(R.id.countdown_circle_progressbar);
        //countdownTimerProgressBar.setProgress(0);

        new CountDownTimer((mSeconds + 1) * 1000, 10) {
            @Override
            public void onTick(long l) {
                if (l > 1000) {
                    countdownTimerButton.setText(String.valueOf(l / 1000));
                    countdownTimerProgressBar.setProgress(1000 - ((int) l % 1000));
                } else {
                    countdownTimerButton.setText(getResources().getString(R.string.go));
                    countdownTimerProgressBar.setProgress(1000);
                }
            }

            @Override
            public void onFinish() {
                StageRunFragment nextFrag = StageRunFragment.newInstance();
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.stage_container, nextFrag, TAG)
                        .commit();
            }

        }.start();
        return v;
    }

}
