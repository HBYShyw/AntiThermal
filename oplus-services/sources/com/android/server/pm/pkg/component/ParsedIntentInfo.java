package com.android.server.pm.pkg.component;

import android.content.IntentFilter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ParsedIntentInfo {
    int getIcon();

    IntentFilter getIntentFilter();

    int getLabelRes();

    CharSequence getNonLocalizedLabel();

    boolean isHasDefault();
}
