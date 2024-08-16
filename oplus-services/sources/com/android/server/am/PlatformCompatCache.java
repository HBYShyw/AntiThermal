package com.android.server.am;

import android.content.pm.ApplicationInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.LongSparseArray;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.compat.IPlatformCompat;
import com.android.server.compat.CompatChange;
import com.android.server.compat.PlatformCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PlatformCompatCache {
    static final int CACHED_COMPAT_CHANGE_CAMERA_MICROPHONE_CAPABILITY = 1;
    static final long[] CACHED_COMPAT_CHANGE_IDS_MAPPING = {136274596, 136219221, 183972877};
    static final int CACHED_COMPAT_CHANGE_PROCESS_CAPABILITY = 0;
    static final int CACHED_COMPAT_CHANGE_USE_SHORT_FGS_USAGE_INTERACTION_TIME = 2;
    private static PlatformCompatCache sPlatformCompatCache;
    private final boolean mCacheEnabled;
    private final LongSparseArray<CacheItem> mCaches = new LongSparseArray<>();
    private final IPlatformCompat mIPlatformCompatProxy;
    private final PlatformCompat mPlatformCompat;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface CachedCompatChangeId {
    }

    private PlatformCompatCache(long[] jArr) {
        IBinder service = ServiceManager.getService("platform_compat");
        if (service instanceof PlatformCompat) {
            this.mPlatformCompat = (PlatformCompat) ServiceManager.getService("platform_compat");
            for (long j : jArr) {
                this.mCaches.put(j, new CacheItem(this.mPlatformCompat, j));
            }
            this.mIPlatformCompatProxy = null;
            this.mCacheEnabled = true;
            return;
        }
        this.mIPlatformCompatProxy = IPlatformCompat.Stub.asInterface(service);
        this.mPlatformCompat = null;
        this.mCacheEnabled = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PlatformCompatCache getInstance() {
        if (sPlatformCompatCache == null) {
            sPlatformCompatCache = new PlatformCompatCache(new long[]{136274596, 136219221, 183972877});
        }
        return sPlatformCompatCache;
    }

    private boolean isChangeEnabled(long j, ApplicationInfo applicationInfo, boolean z) {
        try {
            return this.mCacheEnabled ? this.mCaches.get(j).isChangeEnabled(applicationInfo) : this.mIPlatformCompatProxy.isChangeEnabled(j, applicationInfo);
        } catch (RemoteException e) {
            Slog.w(IActivityManagerServiceExt.TAG, "Error reading platform compat change " + j, e);
            return z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isChangeEnabled(int i, ApplicationInfo applicationInfo, boolean z) {
        return getInstance().isChangeEnabled(CACHED_COMPAT_CHANGE_IDS_MAPPING[i], applicationInfo, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invalidate(ApplicationInfo applicationInfo) {
        for (int size = this.mCaches.size() - 1; size >= 0; size--) {
            this.mCaches.valueAt(size).invalidate(applicationInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onApplicationInfoChanged(ApplicationInfo applicationInfo) {
        for (int size = this.mCaches.size() - 1; size >= 0; size--) {
            this.mCaches.valueAt(size).onApplicationInfoChanged(applicationInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class CacheItem implements CompatChange.ChangeListener {
        private final long mChangeId;
        private final PlatformCompat mPlatformCompat;
        private final Object mLock = new Object();
        private final ArrayMap<String, Pair<Boolean, WeakReference<ApplicationInfo>>> mCache = new ArrayMap<>();

        CacheItem(PlatformCompat platformCompat, long j) {
            this.mPlatformCompat = platformCompat;
            this.mChangeId = j;
            platformCompat.registerListener(j, this);
        }

        boolean isChangeEnabled(ApplicationInfo applicationInfo) {
            synchronized (this.mLock) {
                int indexOfKey = this.mCache.indexOfKey(applicationInfo.packageName);
                if (indexOfKey < 0) {
                    return fetchLocked(applicationInfo, indexOfKey);
                }
                Pair<Boolean, WeakReference<ApplicationInfo>> valueAt = this.mCache.valueAt(indexOfKey);
                if (((WeakReference) valueAt.second).get() == applicationInfo) {
                    return ((Boolean) valueAt.first).booleanValue();
                }
                return fetchLocked(applicationInfo, indexOfKey);
            }
        }

        void invalidate(ApplicationInfo applicationInfo) {
            synchronized (this.mLock) {
                this.mCache.remove(applicationInfo.packageName);
            }
        }

        @GuardedBy({"mLock"})
        boolean fetchLocked(ApplicationInfo applicationInfo, int i) {
            Pair<Boolean, WeakReference<ApplicationInfo>> pair = new Pair<>(Boolean.valueOf(this.mPlatformCompat.isChangeEnabledInternalNoLogging(this.mChangeId, applicationInfo)), new WeakReference(applicationInfo));
            if (i >= 0) {
                this.mCache.setValueAt(i, pair);
            } else {
                this.mCache.put(applicationInfo.packageName, pair);
            }
            return ((Boolean) pair.first).booleanValue();
        }

        void onApplicationInfoChanged(ApplicationInfo applicationInfo) {
            synchronized (this.mLock) {
                int indexOfKey = this.mCache.indexOfKey(applicationInfo.packageName);
                if (indexOfKey >= 0) {
                    fetchLocked(applicationInfo, indexOfKey);
                }
            }
        }

        @Override // com.android.server.compat.CompatChange.ChangeListener
        public void onCompatChange(String str) {
            synchronized (this.mLock) {
                int indexOfKey = this.mCache.indexOfKey(str);
                if (indexOfKey >= 0) {
                    ApplicationInfo applicationInfo = (ApplicationInfo) ((WeakReference) this.mCache.valueAt(indexOfKey).second).get();
                    if (applicationInfo != null) {
                        fetchLocked(applicationInfo, indexOfKey);
                    } else {
                        this.mCache.removeAt(indexOfKey);
                    }
                }
            }
        }
    }
}
