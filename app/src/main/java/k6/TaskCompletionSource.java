package k6;

import kotlin.Metadata;
import l6.TaskImpl;
import ma.h;
import ma.j;
import za.Lambda;
import za.k;

/* compiled from: TaskCompletionSource.kt */
@Metadata(bv = {}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0007¢\u0006\u0004\b\u0017\u0010\u0018J\u0015\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00028\u0000¢\u0006\u0004\b\u0005\u0010\u0006J\u000e\u0010\t\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u0007J\u0018\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\n2\b\u0010\r\u001a\u0004\u0018\u00010\fJ\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u000fR!\u0010\u0016\u001a\b\u0012\u0004\u0012\u00028\u00000\u00118BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015¨\u0006\u0019"}, d2 = {"Lk6/e;", "TResult", "", "result", "Lma/f0;", "e", "(Ljava/lang/Object;)V", "Ljava/lang/Exception;", "exception", "d", "", "errorCode", "", "errorMessage", "c", "Lk6/d;", "a", "Ll6/e;", "task$delegate", "Lma/h;", "b", "()Ll6/e;", "task", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* renamed from: k6.e, reason: use source file name */
/* loaded from: classes.dex */
public final class TaskCompletionSource<TResult> {

    /* renamed from: a, reason: collision with root package name */
    private final h f14054a;

    /* compiled from: TaskCompletionSource.kt */
    @Metadata(bv = {}, d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0001\"\u0004\b\u0000\u0010\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"TResult", "Ll6/e;", "a", "()Ll6/e;"}, k = 3, mv = {1, 6, 0})
    /* renamed from: k6.e$a */
    /* loaded from: classes.dex */
    static final class a extends Lambda implements ya.a<TaskImpl<TResult>> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f14055e = new a();

        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final TaskImpl<TResult> invoke() {
            return new TaskImpl<>();
        }
    }

    public TaskCompletionSource() {
        h b10;
        b10 = j.b(a.f14055e);
        this.f14054a = b10;
    }

    private final TaskImpl<TResult> b() {
        return (TaskImpl) this.f14054a.getValue();
    }

    public final d<TResult> a() {
        return b();
    }

    public final void c(int i10, String str) {
        b().d(i10, str);
    }

    public final void d(Exception exc) {
        k.e(exc, "exception");
        b().j(exc);
    }

    public final void e(TResult result) {
        b().e(result);
    }
}
