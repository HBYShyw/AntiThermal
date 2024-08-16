package com.android.server.usage;

import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PackagesTokenData {
    private static final int PACKAGE_NAME_INDEX = 0;
    public static final int UNASSIGNED_TOKEN = -1;
    public int counter = 1;
    public final SparseArray<ArrayList<String>> tokensToPackagesMap = new SparseArray<>();
    public final ArrayMap<String, ArrayMap<String, Integer>> packagesToTokensMap = new ArrayMap<>();
    public final ArrayMap<String, Long> removedPackagesMap = new ArrayMap<>();
    public final ArraySet<Integer> removedPackageTokens = new ArraySet<>();

    public int getPackageTokenOrAdd(String str, long j) {
        Long l = this.removedPackagesMap.get(str);
        if (l != null && l.longValue() > j) {
            return -1;
        }
        ArrayMap<String, Integer> arrayMap = this.packagesToTokensMap.get(str);
        if (arrayMap == null) {
            arrayMap = new ArrayMap<>();
            this.packagesToTokensMap.put(str, arrayMap);
        }
        int intValue = arrayMap.getOrDefault(str, -1).intValue();
        if (intValue != -1) {
            return intValue;
        }
        int i = this.counter;
        this.counter = i + 1;
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(str);
        arrayMap.put(str, Integer.valueOf(i));
        this.tokensToPackagesMap.put(i, arrayList);
        return i;
    }

    public int getTokenOrAdd(int i, String str, String str2) {
        if (str.equals(str2)) {
            return 0;
        }
        int intValue = this.packagesToTokensMap.get(str).getOrDefault(str2, -1).intValue();
        if (intValue != -1) {
            return intValue;
        }
        int size = this.tokensToPackagesMap.get(i).size();
        this.packagesToTokensMap.get(str).put(str2, Integer.valueOf(size));
        this.tokensToPackagesMap.get(i).add(str2);
        return size;
    }

    public String getPackageString(int i) {
        ArrayList<String> arrayList = this.tokensToPackagesMap.get(i);
        if (arrayList == null) {
            return null;
        }
        return arrayList.get(0);
    }

    public String getString(int i, int i2) {
        try {
            return this.tokensToPackagesMap.get(i).get(i2);
        } catch (IndexOutOfBoundsException unused) {
            return null;
        } catch (NullPointerException e) {
            Slog.e("PackagesTokenData", "Unable to find tokenized strings for package " + i, e);
            return null;
        }
    }

    public int removePackage(String str, long j) {
        this.removedPackagesMap.put(str, Long.valueOf(j));
        if (!this.packagesToTokensMap.containsKey(str)) {
            return -1;
        }
        int intValue = this.packagesToTokensMap.get(str).get(str).intValue();
        this.packagesToTokensMap.remove(str);
        this.tokensToPackagesMap.delete(intValue);
        this.removedPackageTokens.add(Integer.valueOf(intValue));
        return intValue;
    }
}
