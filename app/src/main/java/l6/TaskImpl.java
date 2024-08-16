package l6;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import k6.ITask;
import k6.PendingResult;
import k6.TaskExecutors;
import kotlin.Metadata;
import za.k;

/* compiled from: TaskImpl.kt */
@Metadata(bv = {}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000\u0002B\u0007¢\u0006\u0004\b\u001d\u0010\u001eJ\u001c\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u00052\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0002J\b\u0010\b\u001a\u00020\u0007H\u0002J\b\u0010\n\u001a\u00020\tH\u0016J\b\u0010\u000b\u001a\u00020\tH\u0016J\b\u0010\f\u001a\u00020\tH\u0016J\u0011\u0010\r\u001a\u0004\u0018\u00018\u0000H\u0016¢\u0006\u0004\b\r\u0010\u000eJ\u0017\u0010\u0010\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00028\u0000H\u0016¢\u0006\u0004\b\u0010\u0010\u0011J\u0010\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J\u001a\u0010\u0019\u001a\u00020\u00072\u0006\u0010\u0016\u001a\u00020\u00152\b\u0010\u0018\u001a\u0004\u0018\u00010\u0017H\u0016J\u001c\u0010\u001c\u001a\b\u0012\u0004\u0012\u00028\u00000\u00052\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00028\u00000\u001aH\u0016¨\u0006\u001f"}, d2 = {"Ll6/e;", "TResult", "Lk6/d;", "Lk6/c;", "pendingResult", "Lk6/a;", "f", "Lma/f0;", "g", "", "c", "i", "h", "b", "()Ljava/lang/Object;", "result", "e", "(Ljava/lang/Object;)V", "Ljava/lang/Exception;", "exception", "j", "", "errorCode", "", "errorMessage", "d", "Lk6/b;", "listener", "a", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* renamed from: l6.e, reason: use source file name */
/* loaded from: classes.dex */
public final class TaskImpl<TResult> extends k6.d<TResult> {

    /* renamed from: b, reason: collision with root package name */
    private volatile boolean f14642b;

    /* renamed from: c, reason: collision with root package name */
    private volatile boolean f14643c;

    /* renamed from: d, reason: collision with root package name */
    private volatile TResult f14644d;

    /* renamed from: e, reason: collision with root package name */
    private volatile Exception f14645e;

    /* renamed from: g, reason: collision with root package name */
    private volatile String f14647g;

    /* renamed from: a, reason: collision with root package name */
    private final ReentrantLock f14641a = new ReentrantLock();

    /* renamed from: f, reason: collision with root package name */
    private volatile int f14646f = -1;

    /* renamed from: h, reason: collision with root package name */
    private final List<PendingResult<TResult>> f14648h = new ArrayList();

    private final ITask<TResult> f(PendingResult<TResult> pendingResult) {
        boolean z10;
        this.f14641a.lock();
        try {
            if (this.f14642b) {
                z10 = true;
            } else {
                this.f14648h.add(pendingResult);
                z10 = false;
            }
            if (z10) {
                pendingResult.a(this);
            }
            return this;
        } finally {
            this.f14641a.unlock();
        }
    }

    private final void g() {
        this.f14641a.lock();
        try {
            Iterator<T> it = this.f14648h.iterator();
            while (it.hasNext()) {
                ((PendingResult) it.next()).a(this);
            }
        } finally {
            this.f14641a.unlock();
        }
    }

    @Override // k6.d
    public ITask<TResult> a(k6.b<TResult> listener) {
        k.e(listener, "listener");
        return f(new OnSuccessPendingResult(TaskExecutors.f14056f.a().c(), listener));
    }

    @Override // k6.d
    public TResult b() {
        this.f14641a.lock();
        try {
            return this.f14644d;
        } finally {
            this.f14641a.unlock();
        }
    }

    @Override // k6.d
    public boolean c() {
        boolean z10;
        this.f14641a.lock();
        try {
            if (this.f14642b && !i() && !h()) {
                if (!this.f14643c) {
                    z10 = true;
                    return z10;
                }
            }
            z10 = false;
            return z10;
        } finally {
            this.f14641a.unlock();
        }
    }

    @Override // k6.d
    public void d(int i10, String str) {
        this.f14641a.lock();
        try {
            this.f14642b = true;
            this.f14646f = i10;
            this.f14647g = str;
            g();
        } finally {
            this.f14641a.unlock();
        }
    }

    @Override // k6.d
    public void e(TResult result) {
        this.f14641a.lock();
        try {
            this.f14642b = true;
            this.f14644d = result;
            g();
        } finally {
            this.f14641a.unlock();
        }
    }

    public boolean h() {
        boolean z10;
        this.f14641a.lock();
        try {
            if (this.f14642b && this.f14645e != null) {
                if (!this.f14643c) {
                    z10 = true;
                    return z10;
                }
            }
            z10 = false;
            return z10;
        } finally {
            this.f14641a.unlock();
        }
    }

    public boolean i() {
        boolean z10;
        this.f14641a.lock();
        try {
            if (this.f14642b && (this.f14646f != -1 || this.f14647g != null)) {
                if (!this.f14643c) {
                    z10 = true;
                    return z10;
                }
            }
            z10 = false;
            return z10;
        } finally {
            this.f14641a.unlock();
        }
    }

    public void j(Exception exc) {
        k.e(exc, "exception");
        this.f14641a.lock();
        try {
            this.f14642b = true;
            this.f14645e = exc;
            g();
        } finally {
            this.f14641a.unlock();
        }
    }
}
