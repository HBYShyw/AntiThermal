package com.android.server.pm;

import android.content.IntentFilter;
import com.android.server.utils.SnapshotCache;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class CrossProfileIntentResolver extends WatchedIntentResolver<CrossProfileIntentFilter, CrossProfileIntentFilter> {
    final SnapshotCache<CrossProfileIntentResolver> mSnapshot;

    @Override // com.android.server.pm.WatchedIntentResolver
    protected void sortResults(List<CrossProfileIntentFilter> list) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: newArray, reason: merged with bridge method [inline-methods] */
    public CrossProfileIntentFilter[] m1918newArray(int i) {
        return new CrossProfileIntentFilter[i];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isPackageForFilter(String str, CrossProfileIntentFilter crossProfileIntentFilter) {
        return (crossProfileIntentFilter.mFlags & 8) != 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IntentFilter getIntentFilter(CrossProfileIntentFilter crossProfileIntentFilter) {
        return crossProfileIntentFilter.getIntentFilter();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CrossProfileIntentResolver() {
        this.mSnapshot = makeCache();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CrossProfileIntentFilter snapshot(CrossProfileIntentFilter crossProfileIntentFilter) {
        if (crossProfileIntentFilter == null) {
            return null;
        }
        return crossProfileIntentFilter.snapshot();
    }

    private CrossProfileIntentResolver(CrossProfileIntentResolver crossProfileIntentResolver) {
        copyFrom(crossProfileIntentResolver);
        this.mSnapshot = new SnapshotCache.Sealed();
    }

    private SnapshotCache makeCache() {
        return new SnapshotCache<CrossProfileIntentResolver>(this, this) { // from class: com.android.server.pm.CrossProfileIntentResolver.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public CrossProfileIntentResolver createSnapshot() {
                return new CrossProfileIntentResolver();
            }
        };
    }

    @Override // com.android.server.utils.Snappable
    public CrossProfileIntentResolver snapshot() {
        return this.mSnapshot.snapshot();
    }
}
