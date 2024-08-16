package kd;

import java.util.ArrayList;

/* compiled from: TypeSystemContext.kt */
/* loaded from: classes2.dex */
public final class a extends ArrayList<m> implements l {
    public a(int i10) {
        super(i10);
    }

    public /* bridge */ boolean c(m mVar) {
        return super.contains(mVar);
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ boolean contains(Object obj) {
        if (obj == null ? true : obj instanceof m) {
            return c((m) obj);
        }
        return false;
    }

    public /* bridge */ int d() {
        return super.size();
    }

    public /* bridge */ int e(m mVar) {
        return super.indexOf(mVar);
    }

    public /* bridge */ int f(m mVar) {
        return super.lastIndexOf(mVar);
    }

    public /* bridge */ boolean g(m mVar) {
        return super.remove(mVar);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public final /* bridge */ int indexOf(Object obj) {
        if (obj == null ? true : obj instanceof m) {
            return e((m) obj);
        }
        return -1;
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public final /* bridge */ int lastIndexOf(Object obj) {
        if (obj == null ? true : obj instanceof m) {
            return f((m) obj);
        }
        return -1;
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ boolean remove(Object obj) {
        if (obj == null ? true : obj instanceof m) {
            return g((m) obj);
        }
        return false;
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ int size() {
        return d();
    }
}
