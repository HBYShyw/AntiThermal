package wc;

import pb.CallableMemberDescriptor;
import qd.DFS;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class b implements DFS.c {

    /* renamed from: a, reason: collision with root package name */
    private final boolean f19435a;

    public b(boolean z10) {
        this.f19435a = z10;
    }

    @Override // qd.DFS.c
    public Iterable a(Object obj) {
        Iterable g6;
        g6 = c.g(this.f19435a, (CallableMemberDescriptor) obj);
        return g6;
    }
}
