package com.android.server.pm;

import android.util.ArrayMap;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PendingPackageBroadcasts {
    private final Object mLock = new PackageManagerTracedLock();

    @GuardedBy({"mLock"})
    final SparseArray<ArrayMap<String, ArrayList<String>>> mUidMap = new SparseArray<>(2);

    public boolean hasPackage(int i, String str) {
        boolean z;
        synchronized (this.mLock) {
            ArrayMap<String, ArrayList<String>> arrayMap = this.mUidMap.get(i);
            z = arrayMap != null && arrayMap.containsKey(str);
        }
        return z;
    }

    public void put(int i, String str, ArrayList<String> arrayList) {
        synchronized (this.mLock) {
            getOrAllocate(i).put(str, arrayList);
        }
    }

    public void addComponent(int i, String str, String str2) {
        synchronized (this.mLock) {
            ArrayList<String> orAllocate = getOrAllocate(i, str);
            if (!orAllocate.contains(str2)) {
                orAllocate.add(str2);
            }
        }
    }

    public void addComponents(int i, String str, List<String> list) {
        synchronized (this.mLock) {
            ArrayList<String> orAllocate = getOrAllocate(i, str);
            for (int i2 = 0; i2 < list.size(); i2++) {
                String str2 = list.get(i2);
                if (!orAllocate.contains(str2)) {
                    orAllocate.add(str2);
                }
            }
        }
    }

    public void remove(int i, String str) {
        synchronized (this.mLock) {
            ArrayMap<String, ArrayList<String>> arrayMap = this.mUidMap.get(i);
            if (arrayMap != null) {
                arrayMap.remove(str);
            }
        }
    }

    public void remove(int i) {
        synchronized (this.mLock) {
            this.mUidMap.remove(i);
        }
    }

    public SparseArray<ArrayMap<String, ArrayList<String>>> copiedMap() {
        SparseArray<ArrayMap<String, ArrayList<String>>> sparseArray;
        synchronized (this.mLock) {
            sparseArray = new SparseArray<>();
            for (int i = 0; i < this.mUidMap.size(); i++) {
                ArrayMap<String, ArrayList<String>> valueAt = this.mUidMap.valueAt(i);
                ArrayMap<String, ArrayList<String>> arrayMap = new ArrayMap<>();
                for (int i2 = 0; i2 < valueAt.size(); i2++) {
                    arrayMap.put(valueAt.keyAt(i2), new ArrayList<>(valueAt.valueAt(i2)));
                }
                sparseArray.put(this.mUidMap.keyAt(i), arrayMap);
            }
        }
        return sparseArray;
    }

    public void clear() {
        synchronized (this.mLock) {
            this.mUidMap.clear();
        }
    }

    private ArrayMap<String, ArrayList<String>> getOrAllocate(int i) {
        ArrayMap<String, ArrayList<String>> arrayMap;
        synchronized (this.mLock) {
            arrayMap = this.mUidMap.get(i);
            if (arrayMap == null) {
                arrayMap = new ArrayMap<>();
                this.mUidMap.put(i, arrayMap);
            }
        }
        return arrayMap;
    }

    private ArrayList<String> getOrAllocate(int i, String str) {
        ArrayList<String> computeIfAbsent;
        synchronized (this.mLock) {
            ArrayMap<String, ArrayList<String>> arrayMap = this.mUidMap.get(i);
            if (arrayMap == null) {
                arrayMap = new ArrayMap<>();
                this.mUidMap.put(i, arrayMap);
            }
            computeIfAbsent = arrayMap.computeIfAbsent(str, new Function() { // from class: com.android.server.pm.PendingPackageBroadcasts$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    ArrayList lambda$getOrAllocate$0;
                    lambda$getOrAllocate$0 = PendingPackageBroadcasts.lambda$getOrAllocate$0((String) obj);
                    return lambda$getOrAllocate$0;
                }
            });
        }
        return computeIfAbsent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ArrayList lambda$getOrAllocate$0(String str) {
        return new ArrayList();
    }
}
