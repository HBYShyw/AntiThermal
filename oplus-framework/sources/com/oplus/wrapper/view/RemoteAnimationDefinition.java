package com.oplus.wrapper.view;

/* loaded from: classes.dex */
public class RemoteAnimationDefinition {
    android.view.RemoteAnimationDefinition mRemoteAnimationDefinition;

    public RemoteAnimationDefinition(android.view.RemoteAnimationDefinition remoteAnimationDefinition) {
        this.mRemoteAnimationDefinition = remoteAnimationDefinition;
    }

    public void addRemoteAnimation(int transition, int activityTypeFilter, RemoteAnimationAdapter adapter) {
        this.mRemoteAnimationDefinition.addRemoteAnimation(transition, activityTypeFilter, adapter.getRemoteAnimationAdapter());
    }
}
