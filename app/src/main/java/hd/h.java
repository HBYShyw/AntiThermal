package hd;

import gd.g0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import pb.ModuleCapability;

/* compiled from: KotlinTypeRefiner.kt */
/* loaded from: classes2.dex */
public final class h {

    /* renamed from: a, reason: collision with root package name */
    private static final ModuleCapability<p<x>> f12216a = new ModuleCapability<>("KotlinTypeRefiner");

    public static final ModuleCapability<p<x>> a() {
        return f12216a;
    }

    public static final List<g0> b(g gVar, Iterable<? extends g0> iterable) {
        int u7;
        za.k.e(gVar, "<this>");
        za.k.e(iterable, "types");
        u7 = kotlin.collections.s.u(iterable, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<? extends g0> it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add(gVar.a(it.next()));
        }
        return arrayList;
    }
}
