package com.android.server.wm;

import android.app.ActivityOptions;
import android.os.Handler;
import android.os.IBinder;
import android.util.ArrayMap;
import android.view.RemoteAnimationAdapter;
import com.android.server.wm.PendingRemoteAnimationRegistry;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class PendingRemoteAnimationRegistry {
    private static final long TIMEOUT_MS = 3000;
    private final ArrayMap<String, Entry> mEntries = new ArrayMap<>();
    private final Handler mHandler;
    private final WindowManagerGlobalLock mLock;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PendingRemoteAnimationRegistry(WindowManagerGlobalLock windowManagerGlobalLock, Handler handler) {
        this.mLock = windowManagerGlobalLock;
        this.mHandler = handler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addPendingAnimation(String str, RemoteAnimationAdapter remoteAnimationAdapter, IBinder iBinder) {
        this.mEntries.put(str, new Entry(str, remoteAnimationAdapter, iBinder));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityOptions overrideOptionsIfNeeded(String str, ActivityOptions activityOptions) {
        Entry entry = this.mEntries.get(str);
        if (entry == null) {
            return activityOptions;
        }
        if (activityOptions == null) {
            activityOptions = ActivityOptions.makeRemoteAnimation(entry.adapter);
        } else {
            activityOptions.setRemoteAnimationAdapter(entry.adapter);
        }
        IBinder iBinder = entry.launchCookie;
        if (iBinder != null) {
            activityOptions.setLaunchCookie(iBinder);
        }
        this.mEntries.remove(str);
        return activityOptions;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class Entry {
        final RemoteAnimationAdapter adapter;
        final IBinder launchCookie;
        final String packageName;

        Entry(final String str, RemoteAnimationAdapter remoteAnimationAdapter, IBinder iBinder) {
            this.packageName = str;
            this.adapter = remoteAnimationAdapter;
            this.launchCookie = iBinder;
            PendingRemoteAnimationRegistry.this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.wm.PendingRemoteAnimationRegistry$Entry$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PendingRemoteAnimationRegistry.Entry.this.lambda$new$0(str);
                }
            }, PendingRemoteAnimationRegistry.TIMEOUT_MS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(String str) {
            WindowManagerGlobalLock windowManagerGlobalLock = PendingRemoteAnimationRegistry.this.mLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (((Entry) PendingRemoteAnimationRegistry.this.mEntries.get(str)) == this) {
                        PendingRemoteAnimationRegistry.this.mEntries.remove(str);
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }
    }
}
