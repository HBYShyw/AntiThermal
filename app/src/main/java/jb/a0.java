package jb;

import java.lang.reflect.Member;
import za.Lambda;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: KProperty2Impl.kt */
/* loaded from: classes2.dex */
public final class a0 extends Lambda implements ya.a<Member> {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ y<Object, Object, Object> f13134e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a0(y<Object, Object, Object> yVar) {
        super(0);
        this.f13134e = yVar;
    }

    @Override // ya.a
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Member invoke() {
        return this.f13134e.L();
    }
}
