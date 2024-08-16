package com.android.server.credentials.metrics.shared;

import com.android.server.audio.AudioService$$ExternalSyntheticLambda3;
import com.android.server.credentials.metrics.EntryEnum;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.ToIntFunction;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ResponseCollective {
    private static final String TAG = "ResponseCollective";
    private final Map<EntryEnum, Integer> mEntryCounts;
    private final Map<String, Integer> mResponseCounts;

    public ResponseCollective(Map<String, Integer> map, Map<EntryEnum, Integer> map2) {
        LinkedHashMap linkedHashMap;
        LinkedHashMap linkedHashMap2;
        if (map == null) {
            linkedHashMap = new LinkedHashMap();
        } else {
            linkedHashMap = new LinkedHashMap(map);
        }
        this.mResponseCounts = linkedHashMap;
        if (map2 == null) {
            linkedHashMap2 = new LinkedHashMap();
        } else {
            linkedHashMap2 = new LinkedHashMap(map2);
        }
        this.mEntryCounts = linkedHashMap2;
    }

    public String[] getUniqueResponseStrings() {
        String[] strArr = new String[this.mResponseCounts.keySet().size()];
        this.mResponseCounts.keySet().toArray(strArr);
        return strArr;
    }

    public Map<EntryEnum, Integer> getEntryCountsMap() {
        return Collections.unmodifiableMap(this.mEntryCounts);
    }

    public Map<String, Integer> getResponseCountsMap() {
        return Collections.unmodifiableMap(this.mResponseCounts);
    }

    public int[] getUniqueResponseCounts() {
        return this.mResponseCounts.values().stream().mapToInt(new AudioService$$ExternalSyntheticLambda3()).toArray();
    }

    public int[] getUniqueEntries() {
        return this.mEntryCounts.keySet().stream().mapToInt(new ToIntFunction() { // from class: com.android.server.credentials.metrics.shared.ResponseCollective$$ExternalSyntheticLambda0
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return ((EntryEnum) obj).ordinal();
            }
        }).toArray();
    }

    public int[] getUniqueEntryCounts() {
        return this.mEntryCounts.values().stream().mapToInt(new AudioService$$ExternalSyntheticLambda3()).toArray();
    }

    public int getCountForEntry(EntryEnum entryEnum) {
        return this.mEntryCounts.getOrDefault(entryEnum, 0).intValue();
    }

    public int getNumEntriesTotal() {
        return this.mEntryCounts.values().stream().mapToInt(new AudioService$$ExternalSyntheticLambda3()).sum();
    }

    public ResponseCollective combineCollectives(ResponseCollective responseCollective) {
        if (this == responseCollective) {
            return this;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap(responseCollective.mResponseCounts);
        for (String str : this.mResponseCounts.keySet()) {
            linkedHashMap.merge(str, this.mResponseCounts.get(str), new BiFunction() { // from class: com.android.server.credentials.metrics.shared.ResponseCollective$$ExternalSyntheticLambda1
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return Integer.valueOf(Integer.sum(((Integer) obj).intValue(), ((Integer) obj2).intValue()));
                }
            });
        }
        LinkedHashMap linkedHashMap2 = new LinkedHashMap(responseCollective.mEntryCounts);
        for (EntryEnum entryEnum : this.mEntryCounts.keySet()) {
            linkedHashMap2.merge(entryEnum, this.mEntryCounts.get(entryEnum), new BiFunction() { // from class: com.android.server.credentials.metrics.shared.ResponseCollective$$ExternalSyntheticLambda1
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    return Integer.valueOf(Integer.sum(((Integer) obj).intValue(), ((Integer) obj2).intValue()));
                }
            });
        }
        return new ResponseCollective(linkedHashMap, linkedHashMap2);
    }

    public static <T> Map<T, Integer> combineTypeCountMaps(Map<T, Integer> map, Map<T, Integer> map2) {
        for (T t : map2.keySet()) {
            map.put(t, Integer.valueOf(map.getOrDefault(t, 0).intValue() + map2.get(t).intValue()));
        }
        return map;
    }
}
