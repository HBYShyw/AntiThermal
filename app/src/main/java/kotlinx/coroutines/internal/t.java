package kotlinx.coroutines.internal;

import kotlin.Metadata;
import ma.ExceptionsH;
import td.Delay;
import td.MainCoroutineDispatcher;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MainDispatchers.kt */
@Metadata(bv = {}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\f\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u001d\u0012\b\u0010\u0012\u001a\u0004\u0018\u00010\u000f\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\r¢\u0006\u0004\b\u0019\u0010\u001aJ\b\u0010\u0004\u001a\u00020\u0003H\u0002J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0005H\u0016J\u001c\u0010\f\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00052\n\u0010\u000b\u001a\u00060\tj\u0002`\nH\u0016J\b\u0010\u000e\u001a\u00020\rH\u0016R\u0016\u0010\u0012\u001a\u0004\u0018\u00010\u000f8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0015\u001a\u0004\u0018\u00010\r8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0018\u001a\u00020\u00018VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017¨\u0006\u001b"}, d2 = {"Lkotlinx/coroutines/internal/t;", "Ltd/s1;", "Ltd/o0;", "", "z0", "Lqa/g;", "context", "", "u0", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "block", "y0", "", "toString", "", "g", "Ljava/lang/Throwable;", "cause", "h", "Ljava/lang/String;", "errorHint", "w0", "()Ltd/s1;", "immediate", "<init>", "(Ljava/lang/Throwable;Ljava/lang/String;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class t extends MainCoroutineDispatcher implements Delay {

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private final Throwable cause;

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private final String errorHint;

    public t(Throwable th, String str) {
        this.cause = th;
        this.errorHint = str;
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0023, code lost:
    
        if (r1 == null) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final Void z0() {
        String str;
        if (this.cause != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Module with the Main dispatcher had failed to initialize");
            String str2 = this.errorHint;
            if (str2 != null) {
                str = ". " + str2;
            }
            str = "";
            sb2.append(str);
            throw new IllegalStateException(sb2.toString(), this.cause);
        }
        s.d();
        throw new ExceptionsH();
    }

    @Override // td.MainCoroutineDispatcher, td.CoroutineDispatcher
    public String toString() {
        String str;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Dispatchers.Main[missing");
        if (this.cause != null) {
            str = ", cause=" + this.cause;
        } else {
            str = "";
        }
        sb2.append(str);
        sb2.append(']');
        return sb2.toString();
    }

    @Override // td.CoroutineDispatcher
    public boolean u0(qa.g context) {
        z0();
        throw new ExceptionsH();
    }

    @Override // td.MainCoroutineDispatcher
    public MainCoroutineDispatcher w0() {
        return this;
    }

    @Override // td.CoroutineDispatcher
    /* renamed from: y0, reason: merged with bridge method [inline-methods] */
    public Void t0(qa.g context, Runnable block) {
        z0();
        throw new ExceptionsH();
    }
}
