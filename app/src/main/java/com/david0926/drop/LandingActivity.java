package com.david0926.drop;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.david0926.drop.fragment.LandingFragment;
import com.david0926.drop.util.SharedPreferenceUtil;
import com.github.paolorotolo.appintro.AppIntro;

public class LandingActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        addSlide(LandingFragment.newInstance(R.layout.activity_landing1));
        addSlide(LandingFragment.newInstance(R.layout.activity_landing2));
        addSlide(LandingFragment.newInstance(R.layout.activity_landing3));

        showSkipButton(false);
        setProgressButtonEnabled(true);
        showSeparator(false);
        setIndicatorColor(getColor(R.color.colorPrimary), getColor(R.color.materialGray5));
        setImageNextButton(getDrawable(R.drawable.ic_navigate_next_primary_24dp));
        setColorTransitionsEnabled(true);
        setSkipText("건너뛰기");
        setColorSkipButton(getColor(R.color.colorPrimary));
        setSkipTextTypeface(R.font.apple_sd_gothic_neo_h);
        setDoneText("시작하기");
        setColorDoneText(getColor(R.color.colorPrimary));
        setDoneTextTypeface(R.font.apple_sd_gothic_neo_h);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        finishLanding();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        finishLanding();
    }


    private void finishLanding() {
        SharedPreferenceUtil.putBoolean(this, "landing_shown", true);
        startActivity(new Intent(LandingActivity.this, LoginActivity.class));
        finish();
    }
}
