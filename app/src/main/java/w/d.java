package w;

import kotlin.Metadata;
import w.a;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: CreationExtras.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0011\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0001¢\u0006\u0004\b\f\u0010\rJ,\u0010\u0007\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u00022\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u00032\u0006\u0010\u0005\u001a\u00028\u0000H\u0086\u0002¢\u0006\u0004\b\u0007\u0010\bJ&\u0010\t\u001a\u0004\u0018\u00018\u0000\"\u0004\b\u0000\u0010\u00022\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096\u0002¢\u0006\u0004\b\t\u0010\n¨\u0006\u000e"}, d2 = {"Lw/d;", "Lw/a;", "T", "Lw/a$b;", "key", "t", "Lma/f0;", "c", "(Lw/a$b;Ljava/lang/Object;)V", "a", "(Lw/a$b;)Ljava/lang/Object;", "initialExtras", "<init>", "(Lw/a;)V", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class d extends a {
    public d() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public d(a aVar) {
        k.e(aVar, "initialExtras");
        b().putAll(aVar.b());
    }

    @Override // w.a
    public <T> T a(a.b<T> key) {
        k.e(key, "key");
        return (T) b().get(key);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <T> void c(a.b<T> key, T t7) {
        k.e(key, "key");
        b().put(key, t7);
    }

    public /* synthetic */ d(a aVar, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? a.C0113a.f19305b : aVar);
    }
}
