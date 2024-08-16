package com.android.server.pm.pkg.component;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ParsedInstrumentation extends ParsedComponent {
    String getTargetPackage();

    String getTargetProcesses();

    boolean isFunctionalTest();

    boolean isHandleProfiling();
}
