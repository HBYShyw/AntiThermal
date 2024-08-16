package com.android.server.pm;

import android.content.IntentFilter;
import com.android.server.utils.SnapshotCache;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PersistentPreferredIntentResolver extends WatchedIntentResolver<PersistentPreferredActivity, PersistentPreferredActivity> {
    final SnapshotCache<PersistentPreferredIntentResolver> mSnapshot;

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: newArray, reason: merged with bridge method [inline-methods] */
    public PersistentPreferredActivity[] m2139newArray(int i) {
        return new PersistentPreferredActivity[i];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IntentFilter getIntentFilter(PersistentPreferredActivity persistentPreferredActivity) {
        return persistentPreferredActivity.getIntentFilter();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isPackageForFilter(String str, PersistentPreferredActivity persistentPreferredActivity) {
        return str.equals(persistentPreferredActivity.mComponent.getPackageName());
    }

    public PersistentPreferredIntentResolver() {
        this.mSnapshot = makeCache();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PersistentPreferredActivity snapshot(PersistentPreferredActivity persistentPreferredActivity) {
        if (persistentPreferredActivity == null) {
            return null;
        }
        return persistentPreferredActivity.snapshot();
    }

    private PersistentPreferredIntentResolver(PersistentPreferredIntentResolver persistentPreferredIntentResolver) {
        copyFrom(persistentPreferredIntentResolver);
        this.mSnapshot = new SnapshotCache.Sealed();
    }

    private SnapshotCache makeCache() {
        return new SnapshotCache<PersistentPreferredIntentResolver>(this, this) { // from class: com.android.server.pm.PersistentPreferredIntentResolver.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public PersistentPreferredIntentResolver createSnapshot() {
                return new PersistentPreferredIntentResolver();
            }
        };
    }

    @Override // com.android.server.utils.Snappable
    public PersistentPreferredIntentResolver snapshot() {
        return this.mSnapshot.snapshot();
    }
}
