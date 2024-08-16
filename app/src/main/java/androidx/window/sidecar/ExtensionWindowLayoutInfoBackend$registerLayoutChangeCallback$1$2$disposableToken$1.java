package androidx.window.sidecar;

import androidx.window.extensions.layout.WindowLayoutInfo;
import androidx.window.sidecar.ExtensionWindowLayoutInfoBackend;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import ma.Unit;
import ya.l;
import za.Lambda;
import za.k;

/* compiled from: ExtensionWindowLayoutInfoBackend.kt */
@Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"Landroidx/window/extensions/layout/WindowLayoutInfo;", ThermalBaseConfig.Item.ATTR_VALUE, "Lma/f0;", "a", "(Landroidx/window/extensions/layout/WindowLayoutInfo;)V"}, k = 3, mv = {1, 6, 0})
/* loaded from: classes.dex */
final class ExtensionWindowLayoutInfoBackend$registerLayoutChangeCallback$1$2$disposableToken$1 extends Lambda implements l<WindowLayoutInfo, Unit> {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ ExtensionWindowLayoutInfoBackend.MulticastConsumer f4423e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ExtensionWindowLayoutInfoBackend$registerLayoutChangeCallback$1$2$disposableToken$1(ExtensionWindowLayoutInfoBackend.MulticastConsumer multicastConsumer) {
        super(1);
        this.f4423e = multicastConsumer;
    }

    public final void a(WindowLayoutInfo windowLayoutInfo) {
        k.e(windowLayoutInfo, ThermalBaseConfig.Item.ATTR_VALUE);
        this.f4423e.accept(windowLayoutInfo);
    }

    @Override // ya.l
    public /* bridge */ /* synthetic */ Unit invoke(WindowLayoutInfo windowLayoutInfo) {
        a(windowLayoutInfo);
        return Unit.f15173a;
    }
}
