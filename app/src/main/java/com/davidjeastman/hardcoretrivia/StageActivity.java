package com.davidjeastman.hardcoretrivia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class StageActivity extends AppCompatActivity {

    private static final String EXTRA_STAGE_ID = "com.davidjeastman.hardcoretrivia.stage_id";

    public static Intent newIntent(Context packageContext, int stage) {
        Intent intent = new Intent(packageContext, StageActivity.class);
        intent.putExtra(EXTRA_STAGE_ID, stage);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.stage_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.stage_container, fragment).commit();
        }
    }

    protected Fragment createFragment() {
        return StageLoadFragment.newInstance();
    }

}
