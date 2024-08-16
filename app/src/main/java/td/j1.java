package td;

import java.util.concurrent.CancellationException;
import kotlin.Metadata;

/* compiled from: Exceptions.kt */
@Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u00002\u00060\u0001j\u0002`\u00022\b\u0012\u0004\u0012\u00020\u00000\u0003B!\u0012\u0006\u0010\r\u001a\u00020\u0006\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\u0004\u0012\u0006\u0010\u0010\u001a\u00020\u000f¢\u0006\u0004\b\u0011\u0010\u0012J\b\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\u0007\u001a\u00020\u0006H\u0016J\u0013\u0010\n\u001a\u00020\t2\b\u0010\b\u001a\u0004\u0018\u00010\u0003H\u0096\u0002J\b\u0010\f\u001a\u00020\u000bH\u0016¨\u0006\u0013"}, d2 = {"Ltd/j1;", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "", "", "fillInStackTrace", "", "toString", "other", "", "equals", "", "hashCode", "message", "cause", "Ltd/i1;", "job", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;Ltd/i1;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class j1 extends CancellationException {

    /* renamed from: e, reason: collision with root package name */
    public final transient i1 f18754e;

    public j1(String str, Throwable th, i1 i1Var) {
        super(str);
        this.f18754e = i1Var;
        if (th != null) {
            initCause(th);
        }
    }

    public boolean equals(Object other) {
        if (other != this) {
            if (other instanceof j1) {
                j1 j1Var = (j1) other;
                if (!za.k.a(j1Var.getMessage(), getMessage()) || !za.k.a(j1Var.f18754e, this.f18754e) || !za.k.a(j1Var.getCause(), getCause())) {
                }
            }
            return false;
        }
        return true;
    }

    @Override // java.lang.Throwable
    public Throwable fillInStackTrace() {
        setStackTrace(new StackTraceElement[0]);
        return this;
    }

    public int hashCode() {
        String message = getMessage();
        za.k.b(message);
        int hashCode = ((message.hashCode() * 31) + this.f18754e.hashCode()) * 31;
        Throwable cause = getCause();
        return hashCode + (cause != null ? cause.hashCode() : 0);
    }

    @Override // java.lang.Throwable
    public String toString() {
        return super.toString() + "; job=" + this.f18754e;
    }
}
