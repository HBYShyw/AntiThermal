package com.coui.component.responsiveui.status;

import com.coui.component.responsiveui.window.LayoutGridWindowSize;
import com.coui.component.responsiveui.window.WindowSizeClass;
import kotlin.Metadata;

/* compiled from: IWindowStatus.kt */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&¨\u0006\b"}, d2 = {"Lcom/coui/component/responsiveui/status/IWindowStatus;", "", "layoutGridWindowSize", "Lcom/coui/component/responsiveui/window/LayoutGridWindowSize;", "windowOrientation", "", "windowSizeClass", "Lcom/coui/component/responsiveui/window/WindowSizeClass;", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
/* loaded from: classes.dex */
public interface IWindowStatus {
    LayoutGridWindowSize layoutGridWindowSize();

    int windowOrientation();

    WindowSizeClass windowSizeClass();
}
