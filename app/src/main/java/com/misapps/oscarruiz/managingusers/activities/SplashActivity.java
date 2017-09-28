package com.misapps.oscarruiz.managingusers.activities;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.misapps.oscarruiz.managingusers.R;
import com.misapps.oscarruiz.managingusers.animations.AnimationManager;
import com.misapps.oscarruiz.managingusers.controllers.NavigationController;

import org.w3c.dom.Text;

public class SplashActivity extends AppCompatActivity {

    /**
     * Title Users
     */
    private TextView usersTV;

    /**
     * Title Managing
     */
    private TextView managingTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getViews();
        startAnim();
        initCount();
    }

    /**
     * Method to get views
     */
    private void getViews() {
        usersTV = (TextView)findViewById(R.id.users);
        managingTV = (TextView)findViewById(R.id.managing);
    }

    /**
     * Method to start animation
     */
    private void startAnim() {
        AnimationManager animationManager = new AnimationManager();
        animationManager.splashAnimation(SplashActivity.this, managingTV, usersTV);
    }

    /**
     * Method to init countdown
     */
    private void initCount() {
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                //Change activity
                NavigationController navigationController = new NavigationController();
                navigationController.changeActivity(SplashActivity.this, null);
            }
        }.start();
    }
}
