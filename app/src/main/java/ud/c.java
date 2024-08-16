package ud;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import qa.g;
import td.Dispatchers;
import td.m1;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: HandlerDispatcher.kt */
@Metadata(bv = {}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0000\u0018\u00002\u00020\u00012\u00020\u0002B#\b\u0002\u0012\u0006\u0010\u0019\u001a\u00020\u0018\u0012\b\u0010\u001a\u001a\u0004\u0018\u00010\r\u0012\u0006\u0010\u001b\u001a\u00020\n¢\u0006\u0004\b\u001c\u0010\u001dB\u001d\b\u0016\u0012\u0006\u0010\u0019\u001a\u00020\u0018\u0012\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\r¢\u0006\u0004\b\u001c\u0010\u001eJ\u001c\u0010\t\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00032\n\u0010\u0007\u001a\u00060\u0005j\u0002`\u0006H\u0002J\u0010\u0010\u000b\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\u0003H\u0016J\u001c\u0010\f\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00032\n\u0010\u0007\u001a\u00060\u0005j\u0002`\u0006H\u0016J\b\u0010\u000e\u001a\u00020\rH\u0016J\u0013\u0010\u0011\u001a\u00020\n2\b\u0010\u0010\u001a\u0004\u0018\u00010\u000fH\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0012H\u0016R\u001a\u0010\u0014\u001a\u00020\u00008\u0016X\u0096\u0004¢\u0006\f\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0016\u0010\u0017¨\u0006\u001f"}, d2 = {"Lud/c;", "Lud/d;", "Ltd/o0;", "Lqa/g;", "context", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "block", "Lma/f0;", "y0", "", "u0", "t0", "", "toString", "", "other", "equals", "", "hashCode", "immediate", "Lud/c;", "z0", "()Lud/c;", "Landroid/os/Handler;", "handler", "name", "invokeImmediately", "<init>", "(Landroid/os/Handler;Ljava/lang/String;Z)V", "(Landroid/os/Handler;Ljava/lang/String;)V", "kotlinx-coroutines-android"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class c extends d {
    private volatile c _immediate;

    /* renamed from: g, reason: collision with root package name */
    private final Handler f19016g;

    /* renamed from: h, reason: collision with root package name */
    private final String f19017h;

    /* renamed from: i, reason: collision with root package name */
    private final boolean f19018i;

    /* renamed from: j, reason: collision with root package name */
    private final c f19019j;

    private c(Handler handler, String str, boolean z10) {
        super(null);
        this.f19016g = handler;
        this.f19017h = str;
        this.f19018i = z10;
        this._immediate = z10 ? this : null;
        c cVar = this._immediate;
        if (cVar == null) {
            cVar = new c(handler, str, true);
            this._immediate = cVar;
        }
        this.f19019j = cVar;
    }

    private final void y0(g gVar, Runnable runnable) {
        m1.c(gVar, new CancellationException("The task was rejected, the handler underlying the dispatcher '" + this + "' was closed"));
        Dispatchers.b().t0(gVar, runnable);
    }

    public boolean equals(Object other) {
        return (other instanceof c) && ((c) other).f19016g == this.f19016g;
    }

    public int hashCode() {
        return System.identityHashCode(this.f19016g);
    }

    @Override // td.CoroutineDispatcher
    public void t0(g gVar, Runnable runnable) {
        if (this.f19016g.post(runnable)) {
            return;
        }
        y0(gVar, runnable);
    }

    @Override // td.MainCoroutineDispatcher, td.CoroutineDispatcher
    public String toString() {
        String x02 = x0();
        if (x02 != null) {
            return x02;
        }
        String str = this.f19017h;
        if (str == null) {
            str = this.f19016g.toString();
        }
        if (!this.f19018i) {
            return str;
        }
        return str + ".immediate";
    }

    @Override // td.CoroutineDispatcher
    public boolean u0(g context) {
        return (this.f19018i && k.a(Looper.myLooper(), this.f19016g.getLooper())) ? false : true;
    }

    @Override // td.MainCoroutineDispatcher
    /* renamed from: z0, reason: from getter and merged with bridge method [inline-methods] */
    public c w0() {
        return this.f19019j;
    }

    public /* synthetic */ c(Handler handler, String str, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(handler, (i10 & 2) != 0 ? null : str);
    }

    public c(Handler handler, String str) {
        this(handler, str, false);
    }
}
