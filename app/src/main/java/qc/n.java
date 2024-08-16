package qc;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/* compiled from: LazyStringArrayList.java */
/* loaded from: classes2.dex */
public class n extends AbstractList<String> implements RandomAccess, o {

    /* renamed from: f, reason: collision with root package name */
    public static final o f17325f = new n().getUnmodifiableView();

    /* renamed from: e, reason: collision with root package name */
    private final List<Object> f17326e;

    public n() {
        this.f17326e = new ArrayList();
    }

    private static d d(Object obj) {
        if (obj instanceof d) {
            return (d) obj;
        }
        if (obj instanceof String) {
            return d.h((String) obj);
        }
        return d.f((byte[]) obj);
    }

    private static String e(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof d) {
            return ((d) obj).w();
        }
        return j.b((byte[]) obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection<? extends String> collection) {
        return addAll(size(), collection);
    }

    @Override // qc.o
    public void b(d dVar) {
        this.f17326e.add(dVar);
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public void add(int i10, String str) {
        this.f17326e.add(i10, str);
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.f17326e.clear();
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public String get(int i10) {
        Object obj = this.f17326e.get(i10);
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof d) {
            d dVar = (d) obj;
            String w10 = dVar.w();
            if (dVar.n()) {
                this.f17326e.set(i10, w10);
            }
            return w10;
        }
        byte[] bArr = (byte[]) obj;
        String b10 = j.b(bArr);
        if (j.a(bArr)) {
            this.f17326e.set(i10, b10);
        }
        return b10;
    }

    @Override // java.util.AbstractList, java.util.List
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public String remove(int i10) {
        Object remove = this.f17326e.remove(i10);
        ((AbstractList) this).modCount++;
        return e(remove);
    }

    @Override // qc.o
    public d getByteString(int i10) {
        Object obj = this.f17326e.get(i10);
        d d10 = d(obj);
        if (d10 != obj) {
            this.f17326e.set(i10, d10);
        }
        return d10;
    }

    @Override // qc.o
    public List<?> getUnderlyingElements() {
        return Collections.unmodifiableList(this.f17326e);
    }

    @Override // qc.o
    public o getUnmodifiableView() {
        return new x(this);
    }

    @Override // java.util.AbstractList, java.util.List
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public String set(int i10, String str) {
        return e(this.f17326e.set(i10, str));
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.f17326e.size();
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int i10, Collection<? extends String> collection) {
        if (collection instanceof o) {
            collection = ((o) collection).getUnderlyingElements();
        }
        boolean addAll = this.f17326e.addAll(i10, collection);
        ((AbstractList) this).modCount++;
        return addAll;
    }

    public n(o oVar) {
        this.f17326e = new ArrayList(oVar.size());
        addAll(oVar);
    }
}
