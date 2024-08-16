package jb;

import java.util.Comparator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class n implements Comparator {

    /* renamed from: e, reason: collision with root package name */
    private final ya.p f13291e;

    public n(ya.p pVar) {
        this.f13291e = pVar;
    }

    @Override // java.util.Comparator
    public int compare(Object obj, Object obj2) {
        int H;
        H = KDeclarationContainerImpl.H(this.f13291e, obj, obj2);
        return H;
    }
}
