package com.bryancapps.blackjack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryancapps on 7/17/16.
 */
public class CardFlip extends Transition {

    private void capture(TransitionValues transitionValues) {
        if (transitionValues.view instanceof ImageView) {
            ImageView imageView = (ImageView) transitionValues.view;
            transitionValues.values.put("image drawable", imageView.getDrawable());
            transitionValues.values.put("image tag", imageView.getTag());
        }
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        capture(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        capture(transitionValues);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, final TransitionValues endValues) {
        if (startValues == null || endValues == null
                || startValues.values.get("image tag") == null
                || !((String) startValues.values.get("image tag")).contains("blank")
                || endValues.values.get("image tag") == null
                || ((String) endValues.values.get("image tag")).contains("blank")) {
            return null;
        }

        final ImageView view = (ImageView) endValues.view;
        final List<Animator> animators = new ArrayList<>();

        view.setImageDrawable((Drawable) startValues.values.get("image drawable"));

        ValueAnimator rotateOut = ValueAnimator.ofObject(new IntEvaluator(), 0, 90);
        rotateOut.setDuration(300);
        rotateOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Object value = valueAnimator.getAnimatedValue();
                if (value != null) {
                    view.setRotationY((Integer) value);
                }
            }
        });
        rotateOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setImageDrawable((Drawable) endValues.values.get("image drawable"));
            }
        });
        animators.add(rotateOut);

        ValueAnimator rotateIn = ValueAnimator.ofObject(new IntEvaluator(), -90, 0);
        rotateIn.setDuration(300);
        rotateIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Object value = valueAnimator.getAnimatedValue();
                if (value != null) {
                    view.setRotationY((Integer) value);
                }
            }
        });
        animators.add(rotateIn);

        AnimatorSet anim = new AnimatorSet();
        anim.playSequentially(animators);
        return anim;
    }
}
