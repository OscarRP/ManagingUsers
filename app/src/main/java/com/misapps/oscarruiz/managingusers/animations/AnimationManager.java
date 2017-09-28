package com.misapps.oscarruiz.managingusers.animations;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.misapps.oscarruiz.managingusers.R;

/**
 * Created by Oscar Ruiz on 26/09/2017.
 */

public class AnimationManager {

    /**
     * Animation
     */
    private Animation animation;

    /**
     * Splash animation
     */
    public void splashAnimation(final Context context, View managingView, View usersView) {
        animation = AnimationUtils.loadAnimation(context, R.anim.left_to_right);
        animation.setDuration(1500);
        managingView.setAnimation(animation);
        animation = AnimationUtils.loadAnimation(context, R.anim.right_to_left);
        animation.setDuration(1500);
        usersView.setAnimation(animation);
    }



}
