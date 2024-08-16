package i5;

import kotlin.Metadata;

/* compiled from: BaseCardCommand.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\t\b&\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0013\u0010\u0014R\"\u0010\u0003\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\"\u0010\t\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\t\u0010\u0004\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR$\u0010\r\u001a\u0004\u0018\u00010\f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012¨\u0006\u0015"}, d2 = {"Li5/a;", "", "", "genTime", "J", "getGenTime", "()J", "b", "(J)V", "consumeTime", "getConsumeTime", "a", "", "source", "Ljava/lang/String;", "getSource", "()Ljava/lang/String;", "c", "(Ljava/lang/String;)V", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: i5.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class BaseCardCommand {

    /* renamed from: a, reason: collision with root package name */
    private long f12644a;

    /* renamed from: b, reason: collision with root package name */
    private long f12645b;

    /* renamed from: c, reason: collision with root package name */
    private String f12646c;

    public final void a(long j10) {
        this.f12645b = j10;
    }

    public final void b(long j10) {
        this.f12644a = j10;
    }

    public final void c(String str) {
        this.f12646c = str;
    }
}
