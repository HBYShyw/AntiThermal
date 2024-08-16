package com.oplus.wrapper.os;

import android.os.Handler;

/* loaded from: classes.dex */
public class Registrant {
    private final android.os.Registrant mRegistrant;

    public Registrant(Handler h, int what, Object obj) {
        this.mRegistrant = new android.os.Registrant(h, what, obj);
    }

    public void notifyRegistrant() {
        this.mRegistrant.notifyRegistrant();
    }
}
