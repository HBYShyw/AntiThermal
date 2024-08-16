package com.android.server.pm.pkg.component;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ParsedMainComponent extends ParsedComponent {
    String[] getAttributionTags();

    String getClassName();

    int getOrder();

    String getProcessName();

    String getSplitName();

    boolean isDirectBootAware();

    boolean isEnabled();

    boolean isExported();
}
