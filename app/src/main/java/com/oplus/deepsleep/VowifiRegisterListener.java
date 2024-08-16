package com.oplus.deepsleep;

import android.content.Context;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.feature.MmTelFeature;
import b6.LocalLog;
import com.android.ims.FeatureConnector;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;

/* loaded from: classes.dex */
public class VowifiRegisterListener {
    private static final String TAG = "VowifiRegisterListener";
    private Context mContext;
    private FeatureConnector<ImsManager> mFeatureConnector;
    private ImsManager mImsManager;
    private int mPhoneId;
    private boolean mVoiceCapable = false;
    private boolean mVideoCapable = false;
    private ImsMmTelManager.CapabilityCallback mCapabilityCallback = new ImsMmTelManager.CapabilityCallback() { // from class: com.oplus.deepsleep.VowifiRegisterListener.1
        @Override // android.telephony.ims.ImsMmTelManager.CapabilityCallback
        public void onCapabilitiesStatusChanged(MmTelFeature.MmTelCapabilities mmTelCapabilities) {
            VowifiRegisterListener.this.log("onCapabilitiesStatusChanged=" + mmTelCapabilities);
            VowifiRegisterListener.this.mVoiceCapable = mmTelCapabilities.isCapable(1);
            VowifiRegisterListener.this.mVideoCapable = mmTelCapabilities.isCapable(2);
            VowifiRegisterListener.this.log("mCapabilityCallbackOne: isVoiceCapable =" + VowifiRegisterListener.this.mVoiceCapable + " isVideoCapable =" + VowifiRegisterListener.this.mVideoCapable);
        }
    };
    private ImsMmTelManager.RegistrationCallback mImsRegistrationCallback = new ImsMmTelManager.RegistrationCallback() { // from class: com.oplus.deepsleep.VowifiRegisterListener.2
        public void onRegistered(int i10) {
            VowifiRegisterListener.this.log("onRegistered imsTransportType=" + i10);
        }

        public void onRegistering(int i10) {
            VowifiRegisterListener.this.log("onRegistering imsTransportType=" + i10);
        }

        public void onUnregistered(ImsReasonInfo imsReasonInfo) {
            VowifiRegisterListener.this.log("onDeregistered imsReasonInfo=" + imsReasonInfo);
        }
    };

    public VowifiRegisterListener(Context context, int i10) {
        this.mContext = context;
        this.mPhoneId = i10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        LocalLog.a(TAG + this.mPhoneId, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeListener() {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            log("removeListener mImsManager is null!");
            return;
        }
        ImsMmTelManager.CapabilityCallback capabilityCallback = this.mCapabilityCallback;
        if (capabilityCallback != null && this.mImsRegistrationCallback != null) {
            imsManager.removeCapabilitiesCallback(capabilityCallback);
            this.mImsManager.removeRegistrationListener(this.mImsRegistrationCallback);
            log("removeCapabilitiesCallback " + this.mCapabilityCallback + " from " + this.mImsManager);
            log("removeRegistrationCallback " + this.mImsRegistrationCallback + " from " + this.mImsManager);
            return;
        }
        log("remove listener callback is null!");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setListener() {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            log("setListener mImsManager is null!");
            return;
        }
        ImsMmTelManager.CapabilityCallback capabilityCallback = this.mCapabilityCallback;
        if (capabilityCallback != null && this.mImsRegistrationCallback != null) {
            try {
                imsManager.addCapabilitiesCallback(capabilityCallback, this.mContext.getMainExecutor());
                this.mImsManager.addRegistrationCallback(this.mImsRegistrationCallback, this.mContext.getMainExecutor());
                log("addCapabilitiesCallback " + this.mCapabilityCallback + " into " + this.mImsManager);
                log("addRegistrationCallback " + this.mImsRegistrationCallback + " into " + this.mImsManager);
                return;
            } catch (ImsException unused) {
                log("unable to addCapabilitiesCallback callback.");
                return;
            }
        }
        log("set listener callback is null!");
    }

    public void startListen() {
        if (this.mContext == null) {
            log("context is null!");
            return;
        }
        log("mContext = " + this.mContext);
        FeatureConnector<ImsManager> connector = ImsManager.getConnector(this.mContext, this.mPhoneId, TAG, new FeatureConnector.Listener<ImsManager>() { // from class: com.oplus.deepsleep.VowifiRegisterListener.3
            public void connectionUnavailable(int i10) {
                VowifiRegisterListener.this.removeListener();
            }

            public void connectionReady(ImsManager imsManager, int i10) {
                VowifiRegisterListener.this.mImsManager = imsManager;
                VowifiRegisterListener.this.setListener();
            }
        }, this.mContext.getMainExecutor());
        this.mFeatureConnector = connector;
        connector.connect();
    }

    public void unRegisterListener() {
        if (this.mFeatureConnector != null) {
            log("unRegister connector listener");
            this.mFeatureConnector.disconnect();
        }
    }

    public boolean vowifiRegistered() {
        ImsManager imsManager = this.mImsManager;
        boolean z10 = false;
        if (imsManager == null) {
            log("IsVowifiRegistered mImsManager is null!");
            return false;
        }
        if ((this.mVoiceCapable || this.mVideoCapable) && imsManager.getRegistrationTech() == 1) {
            z10 = true;
        }
        log("vowifiRegistered : mImsManager.getRegistrationTech() = " + this.mImsManager.getRegistrationTech());
        return z10;
    }
}
