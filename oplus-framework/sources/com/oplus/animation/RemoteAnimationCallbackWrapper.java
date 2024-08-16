package com.oplus.animation;

import android.animation.AnimatorSet;

/* loaded from: classes.dex */
public interface RemoteAnimationCallbackWrapper {
    void onAnimationCancelled();

    AnimatorSet onCreateAnimation(int i, RemoteAnimationTargetWrapper[] remoteAnimationTargetWrapperArr, RemoteAnimationTargetWrapper[] remoteAnimationTargetWrapperArr2, RemoteAnimationTargetWrapper[] remoteAnimationTargetWrapperArr3);
}
