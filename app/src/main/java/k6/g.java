package k6;

import java.util.function.Supplier;
import kotlin.Metadata;
import l6.CompletableFutureTasks;
import za.k;

/* compiled from: Tasks.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0007\u0010\bJ\"\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005\"\u0004\b\u0000\u0010\u00022\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0007¨\u0006\t"}, d2 = {"Lk6/g;", "", "TResult", "Ljava/util/function/Supplier;", "supplier", "Lk6/d;", "a", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class g {

    /* renamed from: a, reason: collision with root package name */
    public static final g f14071a = new g();

    private g() {
    }

    public static final <TResult> d<TResult> a(Supplier<TResult> supplier) {
        k.e(supplier, "supplier");
        return CompletableFutureTasks.f14636a.b(TaskExecutors.f14056f.a().d(), supplier);
    }
}
