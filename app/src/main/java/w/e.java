package w;

import androidx.lifecycle.ViewModel;
import kotlin.Metadata;
import ya.l;
import za.k;

/* compiled from: InitializerViewModelFactory.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u00012\u00020\u0003B)\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00028\u00000\t¢\u0006\u0004\b\u000f\u0010\u0010R \u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00048\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\bR&\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00028\u00000\t8\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000e¨\u0006\u0011"}, d2 = {"Lw/e;", "Landroidx/lifecycle/g0;", "T", "", "Ljava/lang/Class;", "clazz", "Ljava/lang/Class;", "a", "()Ljava/lang/Class;", "Lkotlin/Function1;", "Lw/a;", "initializer", "Lya/l;", "b", "()Lya/l;", "<init>", "(Ljava/lang/Class;Lya/l;)V", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class e<T extends ViewModel> {

    /* renamed from: a, reason: collision with root package name */
    private final Class<T> f19308a;

    /* renamed from: b, reason: collision with root package name */
    private final l<a, T> f19309b;

    /* JADX WARN: Multi-variable type inference failed */
    public e(Class<T> cls, l<? super a, ? extends T> lVar) {
        k.e(cls, "clazz");
        k.e(lVar, "initializer");
        this.f19308a = cls;
        this.f19309b = lVar;
    }

    public final Class<T> a() {
        return this.f19308a;
    }

    public final l<a, T> b() {
        return this.f19309b;
    }
}
