package com.android.server.display;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusDisplayManagerShellCommandExt {
    default boolean customMatchedOnCommand(DisplayManagerService displayManagerService, String str) {
        return false;
    }

    default int customRunOnCommand(DisplayManagerShellCommand displayManagerShellCommand, String str) {
        return -1;
    }
}
