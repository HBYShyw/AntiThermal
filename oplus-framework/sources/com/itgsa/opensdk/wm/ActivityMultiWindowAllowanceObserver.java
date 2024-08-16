package com.itgsa.opensdk.wm;

import com.oplus.app.IActivityMultiWindowAllowanceObserver;

/* loaded from: classes.dex */
public class ActivityMultiWindowAllowanceObserver {
    private final IActivityMultiWindowAllowanceObserver mStub = new IActivityMultiWindowAllowanceObserver.Stub() { // from class: com.itgsa.opensdk.wm.ActivityMultiWindowAllowanceObserver.1
        public void onMultiWindowAllowanceChanged(com.oplus.app.ActivityMultiWindowAllowance allowance) {
            try {
                ActivityMultiWindowAllowance allowanceWrapper = new ActivityMultiWindowAllowance(allowance.allowSelfSplitToSplitScreen, allowance.allowSwitchToSplitScreen, allowance.allowSwitchToFullScreen);
                ActivityMultiWindowAllowanceObserver.this.onMultiWindowAllowanceChanged(allowanceWrapper);
            } catch (Exception e) {
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public IActivityMultiWindowAllowanceObserver asObserver() {
        return this.mStub;
    }

    public void onMultiWindowAllowanceChanged(ActivityMultiWindowAllowance allowance) {
    }
}
