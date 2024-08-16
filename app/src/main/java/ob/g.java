package ob;

import pb.CallableMemberDescriptor;
import qd.DFS;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class g implements DFS.c {

    /* renamed from: a, reason: collision with root package name */
    public static final g f16387a = new g();

    @Override // qd.DFS.c
    public Iterable a(Object obj) {
        Iterable w10;
        w10 = i.w((CallableMemberDescriptor) obj);
        return w10;
    }
}
