package com.oplus.wrapper.os;

import android.os.UEventObserver;

/* loaded from: classes.dex */
public abstract class UEventObserver {
    private final UEventObserverImpl mUEventObserverImpl = new UEventObserverImpl();

    public abstract void onUEvent(UEvent uEvent);

    public final void startObserving(String match) {
        this.mUEventObserverImpl.startObserving(match);
    }

    public final void stopObserving() {
        this.mUEventObserverImpl.stopObserving();
    }

    /* loaded from: classes.dex */
    private class UEventObserverImpl extends android.os.UEventObserver {
        private UEventObserverImpl() {
        }

        public void onUEvent(UEventObserver.UEvent uEvent) {
            UEventObserver.this.onUEvent(new UEvent(uEvent));
        }
    }

    /* loaded from: classes.dex */
    public static final class UEvent {
        private final UEventObserver.UEvent mUEvent;

        public UEvent(String message) {
            this.mUEvent = new UEventObserver.UEvent(message);
        }

        public UEvent(UEventObserver.UEvent uEvent) {
            this.mUEvent = uEvent;
        }

        public String get(String key) {
            return this.mUEvent.get(key);
        }
    }
}
