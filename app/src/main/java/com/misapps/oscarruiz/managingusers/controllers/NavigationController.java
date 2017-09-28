package com.misapps.oscarruiz.managingusers.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.misapps.oscarruiz.managingusers.R;
import com.misapps.oscarruiz.managingusers.activities.HomeActivity;
import com.misapps.oscarruiz.managingusers.fragments.HomeFragment;
import com.misapps.oscarruiz.managingusers.utils.Constants;

/**
 * Created by Oscar Ruiz on 25/09/2017.
 */

public class NavigationController {

    /**
     * Fragment Manager
     */
    private FragmentManager fragmentManager;

    /**
     * App state controller
     */
    private AppStateController controller;

    public NavigationController() {
        controller = AppStateController.getInstance();
    }

    /**
     * Method to change activity
     */
    public void changeActivity(Activity activity, Bundle params) {
        //Create Intent
        Intent intent;

        switch (controller.getState()) {
            case Constants.APLICATION_STATES.SPLASH_STATE:
                //close activity
                activity.finish();

                //set intent
                intent = new Intent(activity, HomeActivity.class);

                //check parms
                if(params!=null) {
                    //add paramas
                    intent.putExtras(params);
                }

                //start activity
                activity.startActivity(intent);

                //change controller state
                controller.setState(Constants.APLICATION_STATES.HOME_STATE);
                break;
            case Constants.APLICATION_STATES.HOME_STATE:
                break;
        }
    }

    /**
     * Method to change fragment
     */
    public void changeFragment(Activity activity, Fragment fragment, Bundle params, int homeState) {
        //check params
        if (params != null) {
            fragment.setArguments(params);
        }
        //change fragment
        fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();

        if (controller.getState() != homeState) {
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out).replace(R.id.fragment_container, fragment).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
        //set home state
        controller.setState(homeState);
    }

    /**
     * Method to control onBackPressed
     */
    public void backPressed(Activity activity) {
        switch (controller.getState()) {
            case Constants.APLICATION_STATES.HOME_STATE:
                //Ends app
                activity.finishAffinity();
                break;
            default:
                //Go to Home Fragment
                HomeFragment fragment = new HomeFragment();
                changeFragment(activity, fragment, null, Constants.APLICATION_STATES.HOME_STATE);
                break;
        }
    }
}
