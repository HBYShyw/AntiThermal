package za;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/* compiled from: SpreadBuilder.java */
/* renamed from: za.c0, reason: use source file name */
/* loaded from: classes2.dex */
public class SpreadBuilder {

    /* renamed from: a, reason: collision with root package name */
    private final ArrayList<Object> f20357a;

    public SpreadBuilder(int i10) {
        this.f20357a = new ArrayList<>(i10);
    }

    public void a(Object obj) {
        this.f20357a.add(obj);
    }

    public void b(Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Object[]) {
            Object[] objArr = (Object[]) obj;
            if (objArr.length > 0) {
                ArrayList<Object> arrayList = this.f20357a;
                arrayList.ensureCapacity(arrayList.size() + objArr.length);
                Collections.addAll(this.f20357a, objArr);
                return;
            }
            return;
        }
        if (obj instanceof Collection) {
            this.f20357a.addAll((Collection) obj);
            return;
        }
        if (obj instanceof Iterable) {
            Iterator it = ((Iterable) obj).iterator();
            while (it.hasNext()) {
                this.f20357a.add(it.next());
            }
            return;
        }
        if (obj instanceof Iterator) {
            Iterator it2 = (Iterator) obj;
            while (it2.hasNext()) {
                this.f20357a.add(it2.next());
            }
        } else {
            throw new UnsupportedOperationException("Don't know how to spread " + obj.getClass());
        }
    }

    public int c() {
        return this.f20357a.size();
    }

    public Object[] d(Object[] objArr) {
        return this.f20357a.toArray(objArr);
    }
}
