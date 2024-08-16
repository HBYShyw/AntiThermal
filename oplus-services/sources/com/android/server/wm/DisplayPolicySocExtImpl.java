package com.android.server.wm;

import android.util.BoostFramework;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DisplayPolicySocExtImpl implements IDisplayPolicySocExt {
    DisplayPolicy mService;

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public String getAppPackageName() {
        return null;
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public void hookOnDown() {
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public void hookOnHorizontalFling(int i) {
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public void hookOnScroll(boolean z) {
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public void hookOnVerticalFling(int i) {
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public boolean isTopAppGame(String str, BoostFramework boostFramework) {
        return false;
    }

    @Override // com.android.server.wm.IDisplayPolicySocExt
    public void loadConfig() {
    }

    public DisplayPolicySocExtImpl(Object obj) {
        this.mService = (DisplayPolicy) obj;
    }
}
