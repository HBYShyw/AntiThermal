package com.oplus.internal.telephony.imsphone;

import com.android.ims.ImsCall;
import com.android.ims.ImsConnectionStateListener;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.OplusTelephonyFactory;
import com.android.internal.telephony.imsphone.IOplusImsPhone;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;

/* loaded from: classes.dex */
public class ImsPhoneExt {
    public static void holdActiveCallOnly(ImsPhone imsPhone) throws CallStateException {
        imsPhone.holdActiveCallOnly();
    }

    public static void unholdHeldCall(ImsPhone imsPhone, ImsPhoneConnection connection) throws CallStateException {
        imsPhone.unholdHeldCall(connection);
    }

    /* loaded from: classes.dex */
    public static class ImsDialArgsExt {

        /* loaded from: classes.dex */
        public enum DeferDial {
            INVALID,
            ENABLE,
            DISABLE
        }

        public static void setDeferDial(ImsPhone.ImsDialArgs.Builder builder, DeferDial deferDialExt) {
            ImsPhone.ImsDialArgs.DeferDial deferDial = ImsPhone.ImsDialArgs.DeferDial.INVALID;
            switch (AnonymousClass1.$SwitchMap$com$oplus$internal$telephony$imsphone$ImsPhoneExt$ImsDialArgsExt$DeferDial[deferDialExt.ordinal()]) {
                case 2:
                    deferDial = ImsPhone.ImsDialArgs.DeferDial.ENABLE;
                    break;
                case 3:
                    deferDial = ImsPhone.ImsDialArgs.DeferDial.DISABLE;
                    break;
            }
            builder.setDeferDial(deferDial);
        }
    }

    /* renamed from: com.oplus.internal.telephony.imsphone.ImsPhoneExt$1, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$oplus$internal$telephony$imsphone$ImsPhoneExt$ImsDialArgsExt$DeferDial;

        static {
            int[] iArr = new int[ImsDialArgsExt.DeferDial.values().length];
            $SwitchMap$com$oplus$internal$telephony$imsphone$ImsPhoneExt$ImsDialArgsExt$DeferDial = iArr;
            try {
                iArr[ImsDialArgsExt.DeferDial.INVALID.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$oplus$internal$telephony$imsphone$ImsPhoneExt$ImsDialArgsExt$DeferDial[ImsDialArgsExt.DeferDial.ENABLE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$oplus$internal$telephony$imsphone$ImsPhoneExt$ImsDialArgsExt$DeferDial[ImsDialArgsExt.DeferDial.DISABLE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public void addImsConnectionStateListener(ImsConnectionStateListener callback, int phoneId) {
        IOplusImsPhone interFcaeImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusImsPhone.DEFAULT);
        interFcaeImpl.addImsConnectionStateListener(callback);
    }

    public void removeImsConnectionStateListener(ImsConnectionStateListener callback, int phoneId) {
        IOplusImsPhone interFcaeImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusImsPhone.DEFAULT);
        interFcaeImpl.removeImsConnectionStateListener(callback);
    }

    public boolean isSupportMims(int phoneId) {
        IOplusImsPhone interFcaeImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusImsPhone.DEFAULT);
        return interFcaeImpl.isSupportMims();
    }

    public int getWfcRegErrorCode(int phoneId) {
        IOplusImsPhone interFcaeImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusImsPhone.DEFAULT);
        return interFcaeImpl.getWfcRegErrorCode();
    }

    public void approveEccRedial(boolean isAprroved, ImsCall call, int phoneId) {
        IOplusImsPhone interFcaeImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusImsPhone.DEFAULT);
        interFcaeImpl.approveEccRedial(isAprroved, call);
    }
}
