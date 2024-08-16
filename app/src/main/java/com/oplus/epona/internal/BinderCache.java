package com.oplus.epona.internal;

import android.os.IBinder;
import android.os.RemoteException;
import com.oplus.epona.utils.Logger;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class BinderCache {
    private static final String TAG = "BinderCache";
    private static volatile BinderCache sInstance;
    private final Map<String, IBinder> mCache = new HashMap();

    public static BinderCache getInstance() {
        if (sInstance == null) {
            synchronized (BinderCache.class) {
                if (sInstance == null) {
                    sInstance = new BinderCache();
                }
            }
        }
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$put$0(String str) {
        this.mCache.remove(str);
    }

    public IBinder get(String str) {
        return this.mCache.get(str);
    }

    public void put(final String str, IBinder iBinder) {
        this.mCache.put(str, iBinder);
        try {
            iBinder.linkToDeath(new IBinder.DeathRecipient() { // from class: com.oplus.epona.internal.a
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    BinderCache.this.lambda$put$0(str);
                }
            }, 0);
        } catch (RemoteException e10) {
            Logger.w(TAG, e10.toString(), new Object[0]);
        }
    }
}
