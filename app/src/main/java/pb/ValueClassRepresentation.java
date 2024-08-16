package pb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kd.k;
import ma.NoWhenBranchMatchedException;
import oc.Name;
import za.DefaultConstructorMarker;

/* compiled from: ValueClassRepresentation.kt */
/* renamed from: pb.h1, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class ValueClassRepresentation<Type extends kd.k> {
    private ValueClassRepresentation() {
    }

    public /* synthetic */ ValueClassRepresentation(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract List<ma.o<Name, Type>> a();

    public final <Other extends kd.k> ValueClassRepresentation<Other> b(ya.l<? super Type, ? extends Other> lVar) {
        int u7;
        za.k.e(lVar, "transform");
        if (this instanceof InlineClassRepresentation) {
            InlineClassRepresentation inlineClassRepresentation = (InlineClassRepresentation) this;
            return new InlineClassRepresentation(inlineClassRepresentation.c(), lVar.invoke(inlineClassRepresentation.d()));
        }
        if (!(this instanceof MultiFieldValueClassRepresentation)) {
            throw new NoWhenBranchMatchedException();
        }
        List<ma.o<Name, Type>> a10 = a();
        u7 = kotlin.collections.s.u(a10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = a10.iterator();
        while (it.hasNext()) {
            ma.o oVar = (ma.o) it.next();
            arrayList.add(ma.u.a((Name) oVar.a(), lVar.invoke((kd.k) oVar.b())));
        }
        return new MultiFieldValueClassRepresentation(arrayList);
    }
}
