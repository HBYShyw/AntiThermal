package com.android.server.soundtrigger_middleware;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
interface ICaptureStateNotifier {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Listener {
        void onCaptureStateChange(boolean z);
    }

    boolean registerListener(Listener listener);

    void unregisterListener(Listener listener);
}
