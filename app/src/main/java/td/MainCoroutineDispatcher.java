package td;

import kotlin.Metadata;

/* compiled from: MainCoroutineDispatcher.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\b\b&\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\b\u0010\tJ\b\u0010\u0003\u001a\u00020\u0002H\u0016J\n\u0010\u0004\u001a\u0004\u0018\u00010\u0002H\u0005R\u0014\u0010\u0007\u001a\u00020\u00008&X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\n"}, d2 = {"Ltd/s1;", "Ltd/c0;", "", "toString", "x0", "w0", "()Ltd/s1;", "immediate", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: td.s1, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class MainCoroutineDispatcher extends CoroutineDispatcher {
    @Override // td.CoroutineDispatcher
    public String toString() {
        String x02 = x0();
        if (x02 != null) {
            return x02;
        }
        return DebugStrings.a(this) + '@' + DebugStrings.b(this);
    }

    public abstract MainCoroutineDispatcher w0();

    /* JADX INFO: Access modifiers changed from: protected */
    public final String x0() {
        MainCoroutineDispatcher mainCoroutineDispatcher;
        MainCoroutineDispatcher c10 = Dispatchers.c();
        if (this == c10) {
            return "Dispatchers.Main";
        }
        try {
            mainCoroutineDispatcher = c10.w0();
        } catch (UnsupportedOperationException unused) {
            mainCoroutineDispatcher = null;
        }
        if (this == mainCoroutineDispatcher) {
            return "Dispatchers.Main.immediate";
        }
        return null;
    }
}
