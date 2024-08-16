package com.oplus.media;

import com.oplus.media.IOplusMediaEventObserver;

/* loaded from: classes.dex */
public abstract class OplusMediaEventObserver extends IOplusMediaEventObserver.Stub {
    @Override // com.oplus.media.IOplusMediaEventObserver
    public abstract void onEvent(OplusMediaEvent oplusMediaEvent);
}
