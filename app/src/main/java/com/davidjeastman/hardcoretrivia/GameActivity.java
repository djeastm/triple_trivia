package com.davidjeastman.hardcoretrivia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private static final String EXTRA_STAGE_ID = "com.davidjeastman.hardcoretrivia.stage_id";

    public static Intent newIntent(Context packageContext, int stage) {
        Intent intent = new Intent(packageContext, GameActivity.class);
        intent.putExtra(EXTRA_STAGE_ID, stage);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.game_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.game_container, fragment).commit();
        }
    }

    protected Fragment createFragment() {
        int stageId = (int) getIntent().getSerializableExtra(EXTRA_STAGE_ID);
        return GameLoadFragment.newInstance(stageId);
    }

}
