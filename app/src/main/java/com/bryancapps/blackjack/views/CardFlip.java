package com.bryancapps.blackjack.views;

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

class CardFlip extends Transition {
    private static final String drawableKey = "image drawable";
    private static final String imageTagKey = "image tag";

    private void capture(TransitionValues transitionValues) {
        if (transitionValues.view instanceof ImageView) {
            ImageView imageView = (ImageView) transitionValues.view;
            transitionValues.values.put(drawableKey, imageView.getDrawable());
            transitionValues.values.put(imageTagKey, imageView.getTag());
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
        String startImageTag = startValues != null ? (String) startValues.values.get(imageTagKey) : null;
        String endImageTag = endValues != null ? (String) endValues.values.get(imageTagKey) : null;
        if (startImageTag == null || endImageTag == null
                || !startImageTag.contains("blank") || endImageTag.contains("blank")) {
            return null;
        }

        final ImageView view = (ImageView) endValues.view;
        final List<Animator> animators = new ArrayList<>();

        view.setImageDrawable((Drawable) startValues.values.get(drawableKey));

        ValueAnimator rotateOut = ValueAnimator.ofObject(new IntEvaluator(), 0, 90);
        rotateOut.setDuration(300);
        rotateOut.addUpdateListener(valueAnimator -> {
            Object value = valueAnimator.getAnimatedValue();
            if (value != null) {
                view.setRotationY((Integer) value);
            }
        });
        rotateOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setImageDrawable((Drawable) endValues.values.get(drawableKey));
            }
        });
        animators.add(rotateOut);

        ValueAnimator rotateIn = ValueAnimator.ofObject(new IntEvaluator(), -90, 0);
        rotateIn.setDuration(300);
        rotateIn.addUpdateListener(valueAnimator -> {
            Object value = valueAnimator.getAnimatedValue();
            if (value != null) {
                view.setRotationY((Integer) value);
            }
        });
        animators.add(rotateIn);

        AnimatorSet anim = new AnimatorSet();
        anim.playSequentially(animators);
        return anim;
    }
}
