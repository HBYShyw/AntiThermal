package w;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.h0;
import gb.KClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kotlin.Metadata;
import xa.JvmClassMapping;
import ya.l;
import za.k;

/* compiled from: InitializerViewModelFactory.kt */
@Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\r\u0010\u000eJ2\u0010\n\u001a\u00020\t\"\b\b\u0000\u0010\u0003*\u00020\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00042\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00028\u00000\u0006J\u0006\u0010\f\u001a\u00020\u000b¨\u0006\u000f"}, d2 = {"Lw/c;", "", "Landroidx/lifecycle/g0;", "T", "Lgb/d;", "clazz", "Lkotlin/Function1;", "Lw/a;", "initializer", "Lma/f0;", "a", "Landroidx/lifecycle/h0$b;", "b", "<init>", "()V", "lifecycle-viewmodel_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class c {

    /* renamed from: a, reason: collision with root package name */
    private final List<e<?>> f19307a = new ArrayList();

    public final <T extends ViewModel> void a(KClass<T> kClass, l<? super a, ? extends T> lVar) {
        k.e(kClass, "clazz");
        k.e(lVar, "initializer");
        this.f19307a.add(new e<>(JvmClassMapping.b(kClass), lVar));
    }

    public final h0.b b() {
        Object[] array = this.f19307a.toArray(new e[0]);
        Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        e[] eVarArr = (e[]) array;
        return new b((e[]) Arrays.copyOf(eVarArr, eVarArr.length));
    }
}
