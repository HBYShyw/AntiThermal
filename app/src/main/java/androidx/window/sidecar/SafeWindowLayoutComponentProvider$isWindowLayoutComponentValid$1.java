package androidx.window.sidecar;

import android.app.Activity;
import androidx.window.core.ConsumerAdapter;
import java.lang.reflect.Method;
import kotlin.Metadata;
import ya.a;
import za.Lambda;
import za.k;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: SafeWindowLayoutComponentProvider.kt */
@Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"", "a", "()Ljava/lang/Boolean;"}, k = 3, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class SafeWindowLayoutComponentProvider$isWindowLayoutComponentValid$1 extends Lambda implements a<Boolean> {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ SafeWindowLayoutComponentProvider f4449e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SafeWindowLayoutComponentProvider$isWindowLayoutComponentValid$1(SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider) {
        super(0);
        this.f4449e = safeWindowLayoutComponentProvider;
    }

    @Override // ya.a
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Boolean invoke() {
        ConsumerAdapter consumerAdapter;
        Class p10;
        boolean r10;
        boolean r11;
        consumerAdapter = this.f4449e.consumerAdapter;
        Class<?> c10 = consumerAdapter.c();
        if (c10 == null) {
            return Boolean.FALSE;
        }
        p10 = this.f4449e.p();
        boolean z10 = false;
        Method method = p10.getMethod("addWindowLayoutInfoListener", Activity.class, c10);
        Method method2 = p10.getMethod("removeWindowLayoutInfoListener", c10);
        SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider = this.f4449e;
        k.d(method, "addListenerMethod");
        r10 = safeWindowLayoutComponentProvider.r(method);
        if (r10) {
            SafeWindowLayoutComponentProvider safeWindowLayoutComponentProvider2 = this.f4449e;
            k.d(method2, "removeListenerMethod");
            r11 = safeWindowLayoutComponentProvider2.r(method2);
            if (r11) {
                z10 = true;
            }
        }
        return Boolean.valueOf(z10);
    }
}
