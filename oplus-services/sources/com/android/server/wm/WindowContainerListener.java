package com.android.server.wm;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface WindowContainerListener extends ConfigurationContainerListener {
    default void onDisplayChanged(DisplayContent displayContent) {
    }

    default void onRemoved() {
    }

    default void onVisibleRequestedChanged(boolean z) {
    }
}
