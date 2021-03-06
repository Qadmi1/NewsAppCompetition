package com.alghazal.mohammed.hussein.project6newapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by appty on 29/01/18.
 */


public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        Fragment fragment1 = new FirstFragment();
        Fragment fragment2 = new SecondFragment();
        Fragment fragment3 = new ThirdFragment();
        Fragment fragment4 = new FourthFragment();
        Fragment fragment5 = new FifthFragment();



        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(fragment1);
        addSlide(fragment2);
        addSlide(fragment3);
        addSlide(fragment4);
        addSlide(fragment5);


        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(false);
        showDoneButton(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
    }

    @Override
    public void onDonePressed() {
        super.onDonePressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    @Override
    public void onSlideChanged() {
    }
}
