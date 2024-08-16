package nd;

import java.util.Iterator;
import za.DefaultConstructorMarker;

/* compiled from: ArrayMap.kt */
/* loaded from: classes2.dex */
public abstract class c<T> implements Iterable<T>, ab.a {
    private c() {
    }

    public /* synthetic */ c(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract int d();

    public abstract void e(int i10, T t7);

    public abstract T get(int i10);

    @Override // java.lang.Iterable
    public abstract Iterator<T> iterator();
}
