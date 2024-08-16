package com.oplus.internal.telephony;

import com.android.internal.telephony.IOplusServiceStateTracker;
import com.android.internal.telephony.OplusTelephonyFactory;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.ServiceStateTracker;

/* loaded from: classes.dex */
public class ServiceStateTrackerExt {
    private IOplusServiceStateTracker mImpl;
    private Phone mPhone;
    private ServiceStateTracker mServiceStateTracker;

    public ServiceStateTrackerExt(Phone phone) {
        this.mPhone = phone;
        if (phone != null) {
            this.mImpl = OplusTelephonyFactory.getFeatureFromCache(phone.getPhoneId(), IOplusServiceStateTracker.DEFAULT);
            this.mServiceStateTracker = this.mPhone.getServiceStateTracker();
        }
    }

    public boolean hasPendingRadioPowerOff() {
        ServiceStateTracker serviceStateTracker = this.mServiceStateTracker;
        if (serviceStateTracker != null) {
            return serviceStateTracker.getWrapper().hasPendingRadioPowerOff();
        }
        return false;
    }

    public String getLocatedPlmn() {
        IOplusServiceStateTracker iOplusServiceStateTracker = this.mImpl;
        if (iOplusServiceStateTracker != null) {
            return iOplusServiceStateTracker.getLocatedPlmn();
        }
        return null;
    }
}
