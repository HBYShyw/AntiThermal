package com.oplus.animation;

import android.os.IBinder;
import android.view.SurfaceControl;
import com.oplus.wrapper.window.IRemoteTransitionFinishedCallback;
import com.oplus.wrapper.window.TransitionInfo;

/* loaded from: classes.dex */
public interface RemoteTransitionCallbackWrapper {
    void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback);

    void startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback);
}
