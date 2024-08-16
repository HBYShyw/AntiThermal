package com.pixelworks.hardware;

import android.util.Log;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class IrisFeatureHal {
    private static final String TAG = "IrisFeatureHal";
    private IrisFeatureHalAidlImpl mAidlImpl;
    private IrisFeatureHalHidlImpl mHidlImpl;
    private boolean mUseAidl = false;
    private boolean mHasIris = false;
    private final String PROP_IRIS_SUPPORT = "sys.pxlw.iris.support";

    public IrisFeatureHal() {
        createHalImpl();
    }

    public int getFeature() {
        if (!this.mHasIris) {
            return 0;
        }
        if (this.mUseAidl) {
            return this.mAidlImpl.getFeature();
        }
        return this.mHidlImpl.getFeature();
    }

    public int[] getFeatures() {
        if (!this.mHasIris) {
            return new int[0];
        }
        if (this.mUseAidl) {
            return this.mAidlImpl.getFeatures();
        }
        return this.mHidlImpl.getFeatures();
    }

    public int getChipType() {
        if (!this.mHasIris) {
            return 0;
        }
        if (this.mUseAidl) {
            return this.mAidlImpl.getChipType();
        }
        return this.mHidlImpl.getChipType();
    }

    public int getChipCapability() {
        if (!this.mHasIris) {
            return 0;
        }
        if (this.mUseAidl) {
            return this.mAidlImpl.getChipCapability();
        }
        return this.mHidlImpl.getChipCapability();
    }

    public int getSoftIrisCapability() {
        if (!this.mHasIris) {
            return 0;
        }
        if (this.mUseAidl) {
            return this.mAidlImpl.getSoftIrisCapability();
        }
        return this.mHidlImpl.getSoftIrisCapability();
    }

    private void createHalImpl() {
        try {
            if (IrisFeatureHalAidlImpl.isDeclared()) {
                IrisFeatureHalAidlImpl irisFeatureHalAidlImpl = new IrisFeatureHalAidlImpl();
                this.mAidlImpl = irisFeatureHalAidlImpl;
                if (irisFeatureHalAidlImpl.ready()) {
                    this.mUseAidl = true;
                    this.mHasIris = true;
                    return;
                }
            }
        } catch (NullPointerException unused) {
        } catch (SecurityException unused2) {
            Log.w(TAG, "SELinux denied IIrisFeature AIDL");
        }
        try {
            if (IrisFeatureHalHidlImpl.isDeclared()) {
                IrisFeatureHalHidlImpl irisFeatureHalHidlImpl = new IrisFeatureHalHidlImpl();
                this.mHidlImpl = irisFeatureHalHidlImpl;
                if (irisFeatureHalHidlImpl.ready()) {
                    this.mHasIris = true;
                    return;
                }
            }
        } catch (NullPointerException unused3) {
        } catch (SecurityException unused4) {
            Log.w(TAG, "SELinux denied IIrisFeature HIDL");
        }
        Log.i(TAG, "No found IIrisFeature service");
    }
}
