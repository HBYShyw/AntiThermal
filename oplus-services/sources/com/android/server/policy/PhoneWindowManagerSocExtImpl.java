package com.android.server.policy;

import com.android.server.wm.DisplayPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PhoneWindowManagerSocExtImpl implements IPhoneWindowManagerSocExt {
    PhoneWindowManager mPhoneWindowManager;

    @Override // com.android.server.policy.IPhoneWindowManagerSocExt
    public void hookInitializeHdmiStateInternal() {
    }

    @Override // com.android.server.policy.IPhoneWindowManagerSocExt
    public void hookSetDefaultDisplay(DisplayPolicy displayPolicy) {
    }

    public PhoneWindowManagerSocExtImpl(Object obj) {
        this.mPhoneWindowManager = (PhoneWindowManager) obj;
    }
}
