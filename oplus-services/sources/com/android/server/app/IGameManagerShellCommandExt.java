package com.android.server.app;

import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IGameManagerShellCommandExt {
    default int onCommandExt(PrintWriter printWriter) {
        return -1;
    }

    default void onHelp(PrintWriter printWriter) {
    }
}
