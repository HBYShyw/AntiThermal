package com.android.server.pm;

import android.content.IntentFilter;
import com.android.server.utils.SnapshotCache;
import java.io.PrintWriter;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PreferredIntentResolver extends WatchedIntentResolver<PreferredActivity, PreferredActivity> {
    final SnapshotCache<PreferredIntentResolver> mSnapshot;

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: newArray, reason: merged with bridge method [inline-methods] */
    public PreferredActivity[] m2147newArray(int i) {
        return new PreferredActivity[i];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isPackageForFilter(String str, PreferredActivity preferredActivity) {
        return str.equals(preferredActivity.mPref.mComponent.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dumpFilter(PrintWriter printWriter, String str, PreferredActivity preferredActivity) {
        preferredActivity.mPref.dump(printWriter, str, preferredActivity);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IntentFilter getIntentFilter(PreferredActivity preferredActivity) {
        return preferredActivity.getIntentFilter();
    }

    public boolean shouldAddPreferredActivity(PreferredActivity preferredActivity) {
        ArrayList<PreferredActivity> findFilters = findFilters(preferredActivity);
        if (findFilters != null && !findFilters.isEmpty()) {
            if (!preferredActivity.mPref.mAlways) {
                return false;
            }
            int size = findFilters.size();
            for (int i = 0; i < size; i++) {
                PreferredComponent preferredComponent = findFilters.get(i).mPref;
                if (preferredComponent.mAlways) {
                    int i2 = preferredComponent.mMatch;
                    PreferredComponent preferredComponent2 = preferredActivity.mPref;
                    if (i2 == (preferredComponent2.mMatch & 268369920) && preferredComponent.sameSet(preferredComponent2)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public PreferredIntentResolver() {
        this.mSnapshot = makeCache();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PreferredActivity snapshot(PreferredActivity preferredActivity) {
        if (preferredActivity == null) {
            return null;
        }
        return preferredActivity.snapshot();
    }

    private PreferredIntentResolver(PreferredIntentResolver preferredIntentResolver) {
        copyFrom(preferredIntentResolver);
        this.mSnapshot = new SnapshotCache.Sealed();
    }

    private SnapshotCache makeCache() {
        return new SnapshotCache<PreferredIntentResolver>(this, this) { // from class: com.android.server.pm.PreferredIntentResolver.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public PreferredIntentResolver createSnapshot() {
                return new PreferredIntentResolver();
            }
        };
    }

    @Override // com.android.server.utils.Snappable
    public PreferredIntentResolver snapshot() {
        return this.mSnapshot.snapshot();
    }
}
