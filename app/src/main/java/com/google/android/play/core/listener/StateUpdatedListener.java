package com.google.android.play.core.listener;

import com.oplus.oms.split.core.listener.OplusStateUpdatedListener;

/* loaded from: classes.dex */
public interface StateUpdatedListener<State> extends OplusStateUpdatedListener<State> {
    void onStateUpdate(State state);
}
