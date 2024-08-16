package de;

import kotlin.Metadata;
import ma.Unit;

/* compiled from: TaskQueue.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0004"}, d2 = {"Lde/c;", "Lde/a;", "", "f", "okhttp"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class c extends a {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ String f10934e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ boolean f10935f;

    /* renamed from: g, reason: collision with root package name */
    final /* synthetic */ ya.a<Unit> f10936g;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public c(String str, boolean z10, ya.a<Unit> aVar) {
        super(str, z10);
        this.f10934e = str;
        this.f10935f = z10;
        this.f10936g = aVar;
    }

    @Override // de.a
    public long f() {
        this.f10936g.invoke();
        return -1L;
    }
}
