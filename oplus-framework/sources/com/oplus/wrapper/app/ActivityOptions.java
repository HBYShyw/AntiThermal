package com.oplus.wrapper.app;

import com.oplus.wrapper.view.RemoteAnimationAdapter;
import com.oplus.wrapper.window.RemoteTransition;

/* loaded from: classes.dex */
public class ActivityOptions {
    private final android.app.ActivityOptions mActivityOptions;

    public ActivityOptions(android.app.ActivityOptions activityOptions) {
        this.mActivityOptions = activityOptions;
    }

    public RemoteAnimationAdapter getRemoteAnimationAdapter() {
        return new RemoteAnimationAdapter(this.mActivityOptions.getRemoteAnimationAdapter());
    }

    public static android.app.ActivityOptions makeRemoteAnimation(RemoteAnimationAdapter remoteAnimationAdapter) {
        return android.app.ActivityOptions.makeRemoteAnimation(remoteAnimationAdapter.getRemoteAnimationAdapter());
    }

    public static android.app.ActivityOptions makeRemoteAnimation(RemoteAnimationAdapter remoteAnimationAdapter, RemoteTransition remoteTransition) {
        return android.app.ActivityOptions.makeRemoteAnimation(remoteAnimationAdapter.getRemoteAnimationAdapter(), remoteTransition.getRemoteTransition());
    }

    public static android.app.ActivityOptions makeRemoteTransition(RemoteTransition remoteTransition) {
        return android.app.ActivityOptions.makeRemoteTransition(remoteTransition.getRemoteTransition());
    }
}
