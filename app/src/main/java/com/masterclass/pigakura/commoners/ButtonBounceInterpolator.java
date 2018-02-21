package com.masterclass.pigakura.commoners;

/**
 * Created by tirgei on 6/19/17.
 */

public class ButtonBounceInterpolator implements android.view.animation.Interpolator {
    double ampltude = 1;
    double frequency = 10;

    public ButtonBounceInterpolator(double ampltude, double frequency){
        this.ampltude = ampltude;
        this.frequency = frequency;
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (-1 * Math.pow(Math.E, -input/ ampltude) * Math.cos(frequency * input) + 1);
    }
}
