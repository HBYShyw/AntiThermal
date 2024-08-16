package k5;

import kotlin.Metadata;

/* compiled from: CardEvent.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\t\n\u0002\b\f\n\u0002\u0010\u000e\n\u0002\b\t\b&\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0016\u0010\u0017R\"\u0010\u0003\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\"\u0010\t\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\t\u0010\u0004\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\"\u0010\f\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\f\u0010\u0004\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR$\u0010\u0010\u001a\u0004\u0018\u00010\u000f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015¨\u0006\u0018"}, d2 = {"Lk5/a;", "", "", "genTime", "J", "getGenTime", "()J", "b", "(J)V", "saveTime", "getSaveTime", "c", "consumeTime", "getConsumeTime", "a", "", "source", "Ljava/lang/String;", "getSource", "()Ljava/lang/String;", "d", "(Ljava/lang/String;)V", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: k5.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class CardEvent {

    /* renamed from: a, reason: collision with root package name */
    private long f14045a;

    /* renamed from: b, reason: collision with root package name */
    private long f14046b;

    /* renamed from: c, reason: collision with root package name */
    private long f14047c;

    /* renamed from: d, reason: collision with root package name */
    private String f14048d;

    public final void a(long j10) {
        this.f14047c = j10;
    }

    public final void b(long j10) {
        this.f14045a = j10;
    }

    public final void c(long j10) {
        this.f14046b = j10;
    }

    public final void d(String str) {
        this.f14048d = str;
    }
}
