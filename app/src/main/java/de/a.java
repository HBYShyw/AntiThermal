package de;

import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: Task.kt */
@Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0011\b&\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u000b\u001a\u00020\t\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u000f¢\u0006\u0004\b\u001e\u0010\u001fJ\b\u0010\u0003\u001a\u00020\u0002H&J\u0017\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0004H\u0000¢\u0006\u0004\b\u0007\u0010\bJ\b\u0010\n\u001a\u00020\tH\u0016R\u0017\u0010\u000b\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u0010\u001a\u00020\u000f8\u0006¢\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013R$\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0005\u0010\u0014\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\bR\"\u0010\u0018\u001a\u00020\u00028\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0018\u0010\u0019\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001d¨\u0006 "}, d2 = {"Lde/a;", "", "", "f", "Lde/d;", "queue", "Lma/f0;", "e", "(Lde/d;)V", "", "toString", "name", "Ljava/lang/String;", "b", "()Ljava/lang/String;", "", "cancelable", "Z", "a", "()Z", "Lde/d;", "d", "()Lde/d;", "setQueue$okhttp", "nextExecuteNanoTime", "J", "c", "()J", "g", "(J)V", "<init>", "(Ljava/lang/String;Z)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public abstract class a {

    /* renamed from: a, reason: collision with root package name */
    private final String f10930a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f10931b;

    /* renamed from: c, reason: collision with root package name */
    private d f10932c;

    /* renamed from: d, reason: collision with root package name */
    private long f10933d;

    public a(String str, boolean z10) {
        k.e(str, "name");
        this.f10930a = str;
        this.f10931b = z10;
        this.f10933d = -1L;
    }

    /* renamed from: a, reason: from getter */
    public final boolean getF10931b() {
        return this.f10931b;
    }

    /* renamed from: b, reason: from getter */
    public final String getF10930a() {
        return this.f10930a;
    }

    /* renamed from: c, reason: from getter */
    public final long getF10933d() {
        return this.f10933d;
    }

    /* renamed from: d, reason: from getter */
    public final d getF10932c() {
        return this.f10932c;
    }

    public final void e(d queue) {
        k.e(queue, "queue");
        d dVar = this.f10932c;
        if (dVar == queue) {
            return;
        }
        if (dVar == null) {
            this.f10932c = queue;
            return;
        }
        throw new IllegalStateException("task is in multiple queues".toString());
    }

    public abstract long f();

    public final void g(long j10) {
        this.f10933d = j10;
    }

    public String toString() {
        return this.f10930a;
    }

    public /* synthetic */ a(String str, boolean z10, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i10 & 2) != 0 ? true : z10);
    }
}
