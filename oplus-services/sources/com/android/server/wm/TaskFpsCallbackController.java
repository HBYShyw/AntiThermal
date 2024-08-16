package com.android.server.wm;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.window.ITaskFpsCallback;
import java.util.HashMap;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class TaskFpsCallbackController {
    private final Context mContext;
    private final HashMap<IBinder, Long> mTaskFpsCallbacks = new HashMap<>();
    private final HashMap<IBinder, IBinder.DeathRecipient> mDeathRecipients = new HashMap<>();

    private static native long nativeRegister(ITaskFpsCallback iTaskFpsCallback, int i);

    private static native void nativeUnregister(long j);

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskFpsCallbackController(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerListener(int i, final ITaskFpsCallback iTaskFpsCallback) {
        if (iTaskFpsCallback == null) {
            return;
        }
        IBinder asBinder = iTaskFpsCallback.asBinder();
        if (this.mTaskFpsCallbacks.containsKey(asBinder)) {
            return;
        }
        this.mTaskFpsCallbacks.put(asBinder, Long.valueOf(nativeRegister(iTaskFpsCallback, i)));
        IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() { // from class: com.android.server.wm.TaskFpsCallbackController$$ExternalSyntheticLambda0
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                TaskFpsCallbackController.this.lambda$registerListener$0(iTaskFpsCallback);
            }
        };
        try {
            asBinder.linkToDeath(deathRecipient, 0);
            this.mDeathRecipients.put(asBinder, deathRecipient);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: unregisterListener, reason: merged with bridge method [inline-methods] */
    public void lambda$registerListener$0(ITaskFpsCallback iTaskFpsCallback) {
        if (iTaskFpsCallback == null) {
            return;
        }
        IBinder asBinder = iTaskFpsCallback.asBinder();
        if (this.mTaskFpsCallbacks.containsKey(asBinder)) {
            asBinder.unlinkToDeath(this.mDeathRecipients.get(asBinder), 0);
            this.mDeathRecipients.remove(asBinder);
            nativeUnregister(this.mTaskFpsCallbacks.get(asBinder).longValue());
            this.mTaskFpsCallbacks.remove(asBinder);
        }
    }
}
