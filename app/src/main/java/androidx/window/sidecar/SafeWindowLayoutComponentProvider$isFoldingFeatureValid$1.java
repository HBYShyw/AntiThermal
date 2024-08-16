package androidx.window.sidecar;

import android.graphics.Rect;
import java.lang.reflect.Method;
import kotlin.Metadata;
import ya.a;
import za.Lambda;
import za.Reflection;
import za.k;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: SafeWindowLayoutComponentProvider.kt */
@Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"", "a", "()Ljava/lang/Boolean;"}, k = 3, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class SafeWindowLayoutComponentProvider$isFoldingFeatureValid$1 extends Lambda implements a<Boolean> {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ SafeWindowLayoutComponentProvider f4447e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SafeWindowLayoutComponentProvider$isFoldingFeatureValid$1(SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider) {
        super(0);
        this.f4447e = safeWindowLayoutComponentProvider;
    }

    @Override // ya.a
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Boolean invoke() {
        Class l10;
        boolean j10;
        boolean r10;
        boolean j11;
        boolean r11;
        boolean j12;
        boolean r12;
        l10 = this.f4447e.l();
        boolean z10 = false;
        Method method = l10.getMethod("getBounds", new Class[0]);
        Method method2 = l10.getMethod("getType", new Class[0]);
        Method method3 = l10.getMethod("getState", new Class[0]);
        SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider = this.f4447e;
        k.d(method, "getBoundsMethod");
        j10 = safeWindowLayoutComponentProvider.j(method, Reflection.b(Rect.class));
        if (j10) {
            r10 = this.f4447e.r(method);
            if (r10) {
                SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider2 = this.f4447e;
                k.d(method2, "getTypeMethod");
                Class cls = Integer.TYPE;
                j11 = safeWindowLayoutComponentProvider2.j(method2, Reflection.b(cls));
                if (j11) {
                    r11 = this.f4447e.r(method2);
                    if (r11) {
                        SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider3 = this.f4447e;
                        k.d(method3, "getStateMethod");
                        j12 = safeWindowLayoutComponentProvider3.j(method3, Reflection.b(cls));
                        if (j12) {
                            r12 = this.f4447e.r(method3);
                            if (r12) {
                                z10 = true;
                            }
                        }
                    }
                }
            }
        }
        return Boolean.valueOf(z10);
    }
}
