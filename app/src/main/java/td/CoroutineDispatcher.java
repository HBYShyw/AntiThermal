package td;

import kotlin.Metadata;
import qa.ContinuationInterceptor;
import qa.g;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: CoroutineDispatcher.kt */
@Metadata(bv = {}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\b&\u0018\u00002\u00020\u00012\u00020\u0002:\u0001\u0018B\u0007¢\u0006\u0004\b\u0016\u0010\u0017J\u0010\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0003H\u0016J\u0010\u0010\t\u001a\u00020\u00002\u0006\u0010\b\u001a\u00020\u0007H\u0017J\u001c\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\u00032\n\u0010\f\u001a\u00060\nj\u0002`\u000bH&J \u0010\u0012\u001a\b\u0012\u0004\u0012\u00028\u00000\u0010\"\u0004\b\u0000\u0010\u000f2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00028\u00000\u0010J\u0012\u0010\u0013\u001a\u00020\r2\n\u0010\u0011\u001a\u0006\u0012\u0002\b\u00030\u0010J\b\u0010\u0015\u001a\u00020\u0014H\u0016¨\u0006\u0019"}, d2 = {"Ltd/c0;", "Lqa/a;", "Lqa/e;", "Lqa/g;", "context", "", "u0", "", "parallelism", "v0", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "block", "Lma/f0;", "t0", "T", "Lqa/d;", "continuation", "L", "u", "", "toString", "<init>", "()V", "a", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: td.c0, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class CoroutineDispatcher extends qa.a implements ContinuationInterceptor {

    /* renamed from: f, reason: collision with root package name */
    public static final a f18724f = new a(null);

    /* compiled from: CoroutineDispatcher.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0087\u0003\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001B\t\b\u0002¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Ltd/c0$a;", "Lqa/b;", "Lqa/e;", "Ltd/c0;", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* renamed from: td.c0$a */
    /* loaded from: classes2.dex */
    public static final class a extends qa.b<ContinuationInterceptor, CoroutineDispatcher> {

        /* compiled from: CoroutineDispatcher.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"Lqa/g$b;", "it", "Ltd/c0;", "a", "(Lqa/g$b;)Ltd/c0;"}, k = 3, mv = {1, 6, 0})
        /* renamed from: td.c0$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0106a extends Lambda implements ya.l<g.b, CoroutineDispatcher> {

            /* renamed from: e, reason: collision with root package name */
            public static final C0106a f18725e = new C0106a();

            C0106a() {
                super(1);
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final CoroutineDispatcher invoke(g.b bVar) {
                if (bVar instanceof CoroutineDispatcher) {
                    return (CoroutineDispatcher) bVar;
                }
                return null;
            }
        }

        private a() {
            super(ContinuationInterceptor.f17170a, C0106a.f18725e);
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public CoroutineDispatcher() {
        super(ContinuationInterceptor.f17170a);
    }

    @Override // qa.ContinuationInterceptor
    public final <T> qa.d<T> L(qa.d<? super T> continuation) {
        return new kotlinx.coroutines.internal.e(this, continuation);
    }

    @Override // qa.a, qa.g.b, qa.g
    public <E extends g.b> E c(g.c<E> cVar) {
        return (E) ContinuationInterceptor.a.a(this, cVar);
    }

    @Override // qa.a, qa.g
    public qa.g j0(g.c<?> cVar) {
        return ContinuationInterceptor.a.b(this, cVar);
    }

    public abstract void t0(qa.g gVar, Runnable runnable);

    public String toString() {
        return DebugStrings.a(this) + '@' + DebugStrings.b(this);
    }

    @Override // qa.ContinuationInterceptor
    public final void u(qa.d<?> dVar) {
        ((kotlinx.coroutines.internal.e) dVar).r();
    }

    public boolean u0(qa.g context) {
        return true;
    }

    public CoroutineDispatcher v0(int parallelism) {
        kotlinx.coroutines.internal.k.a(parallelism);
        return new kotlinx.coroutines.internal.j(this, parallelism);
    }
}
