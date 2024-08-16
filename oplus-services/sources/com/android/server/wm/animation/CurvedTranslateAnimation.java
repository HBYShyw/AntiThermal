package com.android.server.wm.animation;

import android.animation.KeyframeSet;
import android.animation.PathKeyframes;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class CurvedTranslateAnimation extends Animation {
    private final PathKeyframes mKeyframes;

    public CurvedTranslateAnimation(Path path) {
        this.mKeyframes = KeyframeSet.ofPath(path);
    }

    @Override // android.view.animation.Animation
    protected void applyTransformation(float f, Transformation transformation) {
        PointF pointF = (PointF) this.mKeyframes.getValue(f);
        transformation.getMatrix().setTranslate(pointF.x, pointF.y);
    }
}
