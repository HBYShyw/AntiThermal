package xd;

import kotlin.Metadata;
import kotlinx.coroutines.internal.y;
import qa.g;
import td.i1;
import ya.p;
import za.Lambda;

/* compiled from: SafeCollector.common.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0018\u0010\u0004\u001a\u00020\u0003*\u0006\u0012\u0002\b\u00030\u00002\u0006\u0010\u0002\u001a\u00020\u0001H\u0001\u001a\u001b\u0010\u0007\u001a\u0004\u0018\u00010\u0005*\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0080\u0010¨\u0006\b"}, d2 = {"Lxd/c;", "Lqa/g;", "currentContext", "Lma/f0;", "a", "Ltd/i1;", "collectJob", "b", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class e {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SafeCollector.common.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0004\u001a\u00020\u00002\u0006\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0002H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"", "count", "Lqa/g$b;", "element", "a", "(ILqa/g$b;)Ljava/lang/Integer;"}, k = 3, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements p<Integer, g.b, Integer> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ c<?> f19732e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(c<?> cVar) {
            super(2);
            this.f19732e = cVar;
        }

        public final Integer a(int i10, g.b bVar) {
            g.c<?> key = bVar.getKey();
            g.b c10 = this.f19732e.f19725i.c(key);
            if (key != i1.f18746d) {
                return Integer.valueOf(bVar != c10 ? Integer.MIN_VALUE : i10 + 1);
            }
            i1 i1Var = (i1) c10;
            i1 b10 = e.b((i1) bVar, i1Var);
            if (b10 == i1Var) {
                if (i1Var != null) {
                    i10++;
                }
                return Integer.valueOf(i10);
            }
            throw new IllegalStateException(("Flow invariant is violated:\n\t\tEmission from another coroutine is detected.\n\t\tChild of " + b10 + ", expected child of " + i1Var + ".\n\t\tFlowCollector is not thread-safe and concurrent emissions are prohibited.\n\t\tTo mitigate this restriction please use 'channelFlow' builder instead of 'flow'").toString());
        }

        @Override // ya.p
        public /* bridge */ /* synthetic */ Integer invoke(Integer num, g.b bVar) {
            return a(num.intValue(), bVar);
        }
    }

    public static final void a(c<?> cVar, g gVar) {
        if (((Number) gVar.i0(0, new a(cVar))).intValue() == cVar.f19726j) {
            return;
        }
        throw new IllegalStateException(("Flow invariant is violated:\n\t\tFlow was collected in " + cVar.f19725i + ",\n\t\tbut emission happened in " + gVar + ".\n\t\tPlease refer to 'flow' documentation or use 'flowOn' instead").toString());
    }

    public static final i1 b(i1 i1Var, i1 i1Var2) {
        while (i1Var != null) {
            if (i1Var == i1Var2 || !(i1Var instanceof y)) {
                return i1Var;
            }
            i1Var = ((y) i1Var).D0();
        }
        return null;
    }
}
