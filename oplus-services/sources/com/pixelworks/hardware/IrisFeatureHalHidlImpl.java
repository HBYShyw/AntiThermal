package com.pixelworks.hardware;

import android.hidl.manager.V1_0.IServiceManager;
import android.os.RemoteException;
import android.util.Log;
import java.util.NoSuchElementException;
import vendor.pixelworks.hardware.feature.V1_0.IIrisFeature;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class IrisFeatureHalHidlImpl {
    private static final String INSTANCE_NAME = "default";
    private static final String TAG = "IrisFeatureHal";
    private IIrisFeature mIrisFeature;
    private boolean mIsFeatureParsed = false;
    private int mFeature = 0;
    private int mChipType = 0;
    private int mChipCapability = 0;
    private int mSoftIrisCapability = 0;

    public int[] getFeatures() {
        return new int[0];
    }

    public IrisFeatureHalHidlImpl() {
        if (isDeclared()) {
            try {
                IIrisFeature service = IIrisFeature.getService(true);
                this.mIrisFeature = service;
                if (service != null) {
                    Log.d(TAG, "Use IIrisFeature HIDL");
                    return;
                }
                return;
            } catch (RemoteException | NoSuchElementException e) {
                Log.e(TAG, "Access IIrisFeature failed", e);
                return;
            }
        }
        Log.d(TAG, "IIrisFeature HIDL is not declared");
    }

    public boolean ready() {
        return this.mIrisFeature != null;
    }

    public static boolean isDeclared() {
        try {
            return IServiceManager.getService().getTransport(IIrisFeature.kInterfaceName, INSTANCE_NAME) != 0;
        } catch (RemoteException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.pixelworks.hardware.IrisFeatureHalHidlImpl$1ret, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class C1ret {
        int v = -1;
        int rc = -1;

        C1ret() {
        }
    }

    public int getFeature() {
        if (this.mIrisFeature != null) {
            final C1ret c1ret = new C1ret();
            try {
                this.mIrisFeature.getFeature(new IIrisFeature.getFeatureCallback() { // from class: com.pixelworks.hardware.IrisFeatureHalHidlImpl.1
                    @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature.getFeatureCallback
                    public void onValues(int i, int i2) {
                        C1ret c1ret2 = c1ret;
                        c1ret2.rc = i;
                        c1ret2.v = i2;
                    }
                });
            } catch (RemoteException e) {
                Log.e(TAG, "Access IIrisFeature failed", e);
            }
            Log.i(TAG, String.format("Get Iris feature 0x%x from hidl result %d", Integer.valueOf(c1ret.v), Integer.valueOf(c1ret.rc)));
            if (c1ret.rc == 0) {
                this.mFeature = c1ret.v;
            } else {
                this.mFeature = 0;
            }
        } else {
            Log.e(TAG, "Access IIrisFeature failed");
            this.mFeature = 0;
        }
        int i = this.mFeature;
        this.mChipType = 0;
        this.mChipCapability = 0;
        this.mSoftIrisCapability = 0;
        if ((i & 128) > 0) {
            int i2 = (i >> 8) & 255;
            this.mChipType = i2;
            if ((65536 & i) > 0) {
                this.mChipCapability = 0 | 128;
            }
            if ((262144 & i) > 0) {
                this.mChipCapability = this.mChipCapability | 2 | 8;
            }
            if ((524288 & i) > 0) {
                this.mChipCapability = this.mChipCapability | 4 | 16 | 1;
            }
            if ((1048576 & i) > 0) {
                int i3 = this.mChipCapability | 32;
                this.mChipCapability = i3;
                if (i2 == 7) {
                    this.mChipCapability = i3 | 1;
                }
            }
            if ((2097152 & i) > 0) {
                this.mChipCapability |= 64;
            }
            if ((8388608 & i) > 0) {
                this.mChipCapability |= 256;
            }
            if ((16777216 & i) > 0) {
                this.mChipCapability |= 512;
            }
            if ((33554432 & i) > 0) {
                this.mChipCapability |= 1024;
            }
            if ((4194304 & i) > 0) {
                this.mSoftIrisCapability = 1;
            }
            if (i2 > 0) {
                int i4 = 3 & (i >> 30);
                if (i4 == 0) {
                    this.mChipType = i2 | 256;
                } else if (i4 > 1) {
                    this.mChipType = i2 | 512;
                }
            }
        } else {
            if ((i & 32) > 0) {
                this.mSoftIrisCapability = 1;
            }
            if ((i & 4) > 0) {
                this.mChipType = 3;
            }
            if ((i & 8) > 0) {
                this.mChipType = 5;
            }
            if ((i & 64) > 0) {
                this.mChipType = 5;
                this.mChipCapability = 0 | 1;
            }
            if ((i & 16) > 0) {
                this.mChipType = 6;
            }
            int i5 = this.mChipType;
            if (i5 > 0 && (1073741824 & i) > 0) {
                this.mChipType = i5 | 256;
            }
        }
        this.mIsFeatureParsed = true;
        return i;
    }

    public int getChipType() {
        if (!this.mIsFeatureParsed) {
            getFeature();
        }
        return this.mChipType;
    }

    public int getChipCapability() {
        if (!this.mIsFeatureParsed) {
            getFeature();
        }
        return this.mChipCapability;
    }

    public int getSoftIrisCapability() {
        if (!this.mIsFeatureParsed) {
            getFeature();
        }
        return this.mSoftIrisCapability;
    }
}
