package com.android.server.pm;

import android.content.pm.SharedLibraryInfo;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.SharedLibraryWrapper;
import com.android.server.utils.WatchedLongSparseArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SharedLibraryUtils {
    SharedLibraryUtils() {
    }

    public static boolean addSharedLibraryToPackageVersionMap(Map<String, WatchedLongSparseArray<SharedLibraryInfo>> map, SharedLibraryInfo sharedLibraryInfo) {
        String name = sharedLibraryInfo.getName();
        if (map.containsKey(name)) {
            if (sharedLibraryInfo.getType() != 2 || map.get(name).indexOfKey(sharedLibraryInfo.getLongVersion()) >= 0) {
                return false;
            }
        } else {
            map.put(name, new WatchedLongSparseArray<>());
        }
        map.get(name).put(sharedLibraryInfo.getLongVersion(), sharedLibraryInfo);
        return true;
    }

    public static SharedLibraryInfo getSharedLibraryInfo(String str, long j, Map<String, WatchedLongSparseArray<SharedLibraryInfo>> map, Map<String, WatchedLongSparseArray<SharedLibraryInfo>> map2) {
        if (map2 != null) {
            WatchedLongSparseArray<SharedLibraryInfo> watchedLongSparseArray = map2.get(str);
            SharedLibraryInfo sharedLibraryInfo = watchedLongSparseArray != null ? watchedLongSparseArray.get(j) : null;
            if (sharedLibraryInfo != null) {
                return sharedLibraryInfo;
            }
        }
        WatchedLongSparseArray<SharedLibraryInfo> watchedLongSparseArray2 = map.get(str);
        if (watchedLongSparseArray2 == null) {
            return null;
        }
        return watchedLongSparseArray2.get(j);
    }

    public static List<SharedLibraryInfo> findSharedLibraries(PackageStateInternal packageStateInternal) {
        if (!packageStateInternal.getTransientState().getUsesLibraryInfos().isEmpty()) {
            ArrayList arrayList = new ArrayList();
            HashSet hashSet = new HashSet();
            Iterator<SharedLibraryWrapper> it = packageStateInternal.getTransientState().getUsesLibraryInfos().iterator();
            while (it.hasNext()) {
                findSharedLibrariesRecursive(it.next().getInfo(), arrayList, hashSet);
            }
            return arrayList;
        }
        return Collections.emptyList();
    }

    private static void findSharedLibrariesRecursive(SharedLibraryInfo sharedLibraryInfo, ArrayList<SharedLibraryInfo> arrayList, Set<String> set) {
        if (set.contains(sharedLibraryInfo.getName())) {
            return;
        }
        set.add(sharedLibraryInfo.getName());
        arrayList.add(sharedLibraryInfo);
        if (sharedLibraryInfo.getDependencies() != null) {
            Iterator it = sharedLibraryInfo.getDependencies().iterator();
            while (it.hasNext()) {
                findSharedLibrariesRecursive((SharedLibraryInfo) it.next(), arrayList, set);
            }
        }
    }
}
