package kotlinx.coroutines.internal;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import td.b2;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ThreadContext.kt */
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\b\u0002\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\u0011\u001a\u00020\r¢\u0006\u0004\b\u0012\u0010\u0013J\u001c\u0010\u0006\u001a\u00020\u00052\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u00022\b\u0010\u0004\u001a\u0004\u0018\u00010\u0001J\u000e\u0010\t\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0007R\u001c\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010\n8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\t\u0010\u000bR\u0016\u0010\u0010\u001a\u00020\r8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u000e\u0010\u000f¨\u0006\u0014"}, d2 = {"Lkotlinx/coroutines/internal/h0;", "", "Ltd/b2;", "element", ThermalBaseConfig.Item.ATTR_VALUE, "Lma/f0;", "a", "Lqa/g;", "context", "b", "", "[Ljava/lang/Object;", "values", "", "d", "I", "i", "n", "<init>", "(Lqa/g;I)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class h0 {

    /* renamed from: a, reason: collision with root package name */
    public final qa.g f14367a;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final Object[] values;

    /* renamed from: c, reason: collision with root package name */
    private final b2<Object>[] f14369c;

    /* renamed from: d, reason: collision with root package name and from kotlin metadata */
    private int i;

    public h0(qa.g gVar, int i10) {
        this.f14367a = gVar;
        this.values = new Object[i10];
        this.f14369c = new b2[i10];
    }

    public final void a(b2<?> b2Var, Object obj) {
        Object[] objArr = this.values;
        int i10 = this.i;
        objArr[i10] = obj;
        b2<Object>[] b2VarArr = this.f14369c;
        this.i = i10 + 1;
        b2VarArr[i10] = b2Var;
    }

    public final void b(qa.g gVar) {
        int length = this.f14369c.length - 1;
        if (length < 0) {
            return;
        }
        while (true) {
            int i10 = length - 1;
            b2<Object> b2Var = this.f14369c[length];
            za.k.b(b2Var);
            b2Var.S(gVar, this.values[length]);
            if (i10 < 0) {
                return;
            } else {
                length = i10;
            }
        }
    }
}
