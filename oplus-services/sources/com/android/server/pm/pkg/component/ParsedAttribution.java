package com.android.server.pm.pkg.component;

import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ParsedAttribution {
    public static final int MAX_ATTRIBUTION_TAG_LEN = 50;

    List<String> getInheritFrom();

    int getLabel();

    String getTag();
}
