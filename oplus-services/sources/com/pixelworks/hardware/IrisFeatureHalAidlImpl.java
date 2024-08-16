package com.pixelworks.hardware;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.util.Log;
import java.util.NoSuchElementException;
import vendor.pixelworks.hardware.feature.IIrisFeature;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class IrisFeatureHalAidlImpl {
    private static final String INSTANCE_NAME = IIrisFeature.DESCRIPTOR + "/default";
    private static final String TAG = "IrisFeatureHal";
    private IIrisFeature mIrisFeature;
    private boolean mIsFeatureParsed = false;
    private int mFeature = 0;
    private int mChipType = 0;
    private int mChipCapability = 0;
    private int mSoftIrisCapability = 0;

    public IrisFeatureHalAidlImpl() {
        if (isDeclared()) {
            try {
                IIrisFeature asInterface = IIrisFeature.Stub.asInterface(ServiceManager.waitForDeclaredService(INSTANCE_NAME));
                this.mIrisFeature = asInterface;
                if (asInterface != null) {
                    Log.d(TAG, "Use IIrisFeature AIDL");
                    return;
                }
                return;
            } catch (NoSuchElementException e) {
                Log.e(TAG, "Access IIrisFeature failed", e);
                return;
            }
        }
        Log.d(TAG, "IIrisFeature AIDL is not declared");
    }

    public boolean ready() {
        return this.mIrisFeature != null;
    }

    public static boolean isDeclared() {
        return ServiceManager.isDeclared(INSTANCE_NAME);
    }

    public int getFeature() {
        getFeatures();
        this.mIsFeatureParsed = true;
        return this.mFeature;
    }

    public int[] getFeatures() {
        int[] iArr;
        IIrisFeature iIrisFeature = this.mIrisFeature;
        if (iIrisFeature != null) {
            try {
                this.mFeature = iIrisFeature.getFeature();
            } catch (RemoteException | RuntimeException e) {
                Log.e(TAG, "Get Iris feature failed", e);
                this.mFeature = 0;
            } catch (ServiceSpecificException e2) {
                Log.e(TAG, "Get Iris feature failed", e2);
                this.mFeature = 0;
            }
            try {
                iArr = this.mIrisFeature.getFeatures();
                if (iArr.length > 2) {
                    Log.i(TAG, String.format("Get Iris features %d %d %d from aidl", Integer.valueOf(iArr[0]), Integer.valueOf(iArr[1]), Integer.valueOf(iArr[2])));
                }
            } catch (RemoteException | RuntimeException e3) {
                Log.e(TAG, "Get Iris feature failed", e3);
                iArr = new int[0];
            } catch (ServiceSpecificException e4) {
                Log.e(TAG, "Get Iris feature failed", e4);
                iArr = new int[0];
            }
        } else {
            Log.e(TAG, "Access IIrisFeature failed");
            this.mFeature = 0;
            iArr = new int[0];
        }
        if (iArr.length > 2) {
            this.mChipType = iArr[0];
            this.mChipCapability = iArr[1];
            this.mSoftIrisCapability = iArr[2];
        } else {
            this.mChipType = 0;
            this.mChipCapability = 0;
            this.mSoftIrisCapability = 0;
        }
        this.mIsFeatureParsed = true;
        return iArr;
    }

    public int getChipType() {
        if (!this.mIsFeatureParsed) {
            getFeatures();
        }
        return this.mChipType;
    }

    public int getChipCapability() {
        if (!this.mIsFeatureParsed) {
            getFeatures();
        }
        return this.mChipCapability;
    }

    public int getSoftIrisCapability() {
        if (!this.mIsFeatureParsed) {
            getFeatures();
        }
        return this.mSoftIrisCapability;
    }
}
