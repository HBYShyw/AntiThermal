package vb;

import java.util.ArrayList;
import java.util.List;
import oc.Name;
import vb.f;

/* compiled from: ReflectJavaAnnotationArguments.kt */
/* loaded from: classes2.dex */
public final class j extends f implements fc.e {

    /* renamed from: c, reason: collision with root package name */
    private final Object[] f19235c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public j(Name name, Object[] objArr) {
        super(name, null);
        za.k.e(objArr, "values");
        this.f19235c = objArr;
    }

    @Override // fc.e
    public List<f> e() {
        Object[] objArr = this.f19235c;
        ArrayList arrayList = new ArrayList(objArr.length);
        for (Object obj : objArr) {
            f.a aVar = f.f19232b;
            za.k.b(obj);
            arrayList.add(aVar.a(obj, null));
        }
        return arrayList;
    }
}
