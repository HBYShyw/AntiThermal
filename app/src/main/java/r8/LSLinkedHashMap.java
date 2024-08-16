package r8;

import java.util.LinkedHashMap;
import java.util.Map;

/* compiled from: LSLinkedHashMap.java */
/* renamed from: r8.c, reason: use source file name */
/* loaded from: classes2.dex */
public class LSLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

    /* renamed from: e, reason: collision with root package name */
    private static int f17642e = 49;

    @Override // java.util.LinkedHashMap
    protected boolean removeEldestEntry(Map.Entry entry) {
        return size() > f17642e;
    }
}
