package com.android.server.wm;

import android.os.IBinder;
import java.util.Collection;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface BackgroundActivityStartCallback {
    boolean canCloseSystemDialogs(Collection<IBinder> collection, int i);

    boolean isActivityStartAllowed(Collection<IBinder> collection, int i, String str);
}
