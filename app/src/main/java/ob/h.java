package ob;

import pb.ClassDescriptor;
import qd.DFS;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class h implements DFS.c {

    /* renamed from: a, reason: collision with root package name */
    private final i f16388a;

    public h(i iVar) {
        this.f16388a = iVar;
    }

    @Override // qd.DFS.c
    public Iterable a(Object obj) {
        Iterable s7;
        s7 = i.s(this.f16388a, (ClassDescriptor) obj);
        return s7;
    }
}
