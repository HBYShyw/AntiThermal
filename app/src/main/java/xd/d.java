package xd;

import kotlin.Metadata;
import ma.Unit;
import wd.FlowCollector;
import ya.q;
import za.FunctionReferenceImpl;
import za.TypeIntrinsics;

/* compiled from: SafeCollector.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0002\n\u0000¨\u0006\u0000"}, d2 = {"kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class d {

    /* renamed from: a, reason: collision with root package name */
    private static final q<FlowCollector<Object>, Object, qa.d<? super Unit>, Object> f19730a = (q) TypeIntrinsics.d(a.f19731n, 3);

    /* compiled from: SafeCollector.kt */
    @Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes2.dex */
    /* synthetic */ class a extends FunctionReferenceImpl implements q<FlowCollector<? super Object>, Object, qa.d<? super Unit>, Object> {

        /* renamed from: n, reason: collision with root package name */
        public static final a f19731n = new a();

        a() {
            super(3, FlowCollector.class, "emit", "emit(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", 0);
        }

        @Override // ya.q
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final Object g(FlowCollector<Object> flowCollector, Object obj, qa.d<? super Unit> dVar) {
            return flowCollector.emit(obj, dVar);
        }
    }

    public static final /* synthetic */ q a() {
        return f19730a;
    }
}
