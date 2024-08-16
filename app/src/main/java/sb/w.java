package sb;

import java.util.List;
import java.util.Set;

/* compiled from: ModuleDescriptorImpl.kt */
/* loaded from: classes2.dex */
public final class w implements v {

    /* renamed from: a, reason: collision with root package name */
    private final List<x> f18391a;

    /* renamed from: b, reason: collision with root package name */
    private final Set<x> f18392b;

    /* renamed from: c, reason: collision with root package name */
    private final List<x> f18393c;

    /* renamed from: d, reason: collision with root package name */
    private final Set<x> f18394d;

    public w(List<x> list, Set<x> set, List<x> list2, Set<x> set2) {
        za.k.e(list, "allDependencies");
        za.k.e(set, "modulesWhoseInternalsAreVisible");
        za.k.e(list2, "directExpectedByDependencies");
        za.k.e(set2, "allExpectedByDependencies");
        this.f18391a = list;
        this.f18392b = set;
        this.f18393c = list2;
        this.f18394d = set2;
    }

    @Override // sb.v
    public List<x> a() {
        return this.f18391a;
    }

    @Override // sb.v
    public Set<x> b() {
        return this.f18392b;
    }

    @Override // sb.v
    public List<x> c() {
        return this.f18393c;
    }
}
