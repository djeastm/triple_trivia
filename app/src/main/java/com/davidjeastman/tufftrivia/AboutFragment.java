package com.davidjeastman.tufftrivia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by David Eastman on 6/22/2017.
 */

public class AboutFragment extends Fragment {
    private static final String ARG_ABOUT_ID = "about_id";

    private TextView mAboutField;

    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ABOUT_ID, 1);
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        mAboutField = (TextView) v.findViewById(R.id.about_label_text_view);
        mAboutField.setText("About");
        return v;
    }
}
