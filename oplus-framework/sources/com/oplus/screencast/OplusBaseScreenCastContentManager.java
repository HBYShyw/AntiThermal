package com.oplus.screencast;

import android.os.IBinder;
import android.os.ServiceManager;

/* loaded from: classes.dex */
public abstract class OplusBaseScreenCastContentManager implements IOplusScreenCastContentManager {
    protected IBinder mRemote;

    public OplusBaseScreenCastContentManager(String name) {
        this(ServiceManager.getService(name));
    }

    private OplusBaseScreenCastContentManager(IBinder remote) {
        this.mRemote = remote;
    }
}
