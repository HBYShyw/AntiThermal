package jb;

import jb.y;
import za.Lambda;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: KProperty2Impl.kt */
/* loaded from: classes2.dex */
public final class z extends Lambda implements ya.a<y.a<Object, Object, Object>> {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ y<Object, Object, Object> f13369e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public z(y<Object, Object, Object> yVar) {
        super(0);
        this.f13369e = yVar;
    }

    @Override // ya.a
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final y.a<Object, Object, Object> invoke() {
        return new y.a<>(this.f13369e);
    }
}
