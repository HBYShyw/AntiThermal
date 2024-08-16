package androidx.window.sidecar;

import java.lang.reflect.Method;
import kotlin.Metadata;
import ya.a;
import za.Lambda;
import za.k;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: SafeWindowLayoutComponentProvider.kt */
@Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"", "a", "()Ljava/lang/Boolean;"}, k = 3, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class SafeWindowLayoutComponentProvider$isWindowLayoutProviderValid$1 extends Lambda implements a<Boolean> {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ SafeWindowLayoutComponentProvider f4450e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SafeWindowLayoutComponentProvider$isWindowLayoutProviderValid$1(SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider) {
        super(0);
        this.f4450e = safeWindowLayoutComponentProvider;
    }

    @Override // ya.a
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Boolean invoke() {
        Class n10;
        Class m10;
        boolean k10;
        boolean r10;
        n10 = this.f4450e.n();
        boolean z10 = false;
        Method declaredMethod = n10.getDeclaredMethod("getWindowExtensions", new Class[0]);
        m10 = this.f4450e.m();
        SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider = this.f4450e;
        k.d(declaredMethod, "getWindowExtensionsMethod");
        k10 = safeWindowLayoutComponentProvider.k(declaredMethod, m10);
        if (k10) {
            r10 = this.f4450e.r(declaredMethod);
            if (r10) {
                z10 = true;
            }
        }
        return Boolean.valueOf(z10);
    }
}
