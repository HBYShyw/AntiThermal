package com.android.server.display.util;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.android.internal.util.FrameworkStatsLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import vendor.oplus.hardware.displaypanelfeature.V1_0.IDisplayPanelFeature;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class HidlDisplayPanelFeature {
    public static final int FINGER_LAYER_HIDE = 0;
    public static final int FINGER_LAYER_SHOW = 1;
    private static final int GET_DISPLAY_PANEL_FEATURE_VALUE_PASS = 0;
    public static final int MTK_FINGER_LAYER_HIDE = 1;
    public static final int MTK_FINGER_LAYER_SHOW = 0;
    private static final int NOTIFY_DISP_FINGER_LAYER = 20004;
    public static final int OMMDP_FPPRESS = 28;
    private static final String TAG = "HidlDisplayPanelFeature";
    private static IDisplayPanelFeature mDisplayPanelFeature;

    public static ArrayList<Integer> getDisplayPanelFeatureValue(int i) {
        Slog.d(TAG, "getDisplayPanelFeatureValue +++, featureID:" + i);
        ArrayList<Integer> arrayList = null;
        final PendingResult pendingResult = new PendingResult(null);
        try {
            if (mDisplayPanelFeature == null) {
                getService();
            }
            IDisplayPanelFeature iDisplayPanelFeature = mDisplayPanelFeature;
            if (iDisplayPanelFeature != null) {
                iDisplayPanelFeature.getDisplayPanelFeatureValue(i, new IDisplayPanelFeature.getDisplayPanelFeatureValueCallback() { // from class: com.android.server.display.util.HidlDisplayPanelFeature$$ExternalSyntheticLambda0
                    public final void onValues(int i2, ArrayList arrayList2) {
                        HidlDisplayPanelFeature.lambda$getDisplayPanelFeatureValue$0(PendingResult.this, i2, arrayList2);
                    }
                });
                ArrayList<Integer> arrayList2 = (ArrayList) pendingResult.await(10L, TimeUnit.MILLISECONDS);
                if (arrayList2 != null) {
                    try {
                        if (arrayList2.isEmpty()) {
                            Slog.e(TAG, "PendingResultTimeout");
                        }
                    } catch (RemoteException | NoSuchElementException unused) {
                        arrayList = arrayList2;
                        Slog.e(TAG, "getDisplayPanelFeatureValue error");
                        Slog.d(TAG, "getDisplayPanelFeatureValue ---, result = " + arrayList);
                        return arrayList;
                    }
                }
                arrayList = arrayList2;
            } else {
                Slog.e(TAG, "achieve displayPanelFeature failed");
            }
        } catch (RemoteException | NoSuchElementException unused2) {
        }
        Slog.d(TAG, "getDisplayPanelFeatureValue ---, result = " + arrayList);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getDisplayPanelFeatureValue$0(PendingResult pendingResult, int i, ArrayList arrayList) {
        Slog.d(TAG, "getDisplayPanelFeatureValue, ret = " + i);
        if (i == 0 && arrayList != null && !arrayList.isEmpty()) {
            Slog.i(TAG, Arrays.toString(arrayList.toArray()));
        }
        pendingResult.setResult(arrayList);
    }

    public static boolean isAvailable() {
        if (mDisplayPanelFeature == null) {
            getService();
        }
        return mDisplayPanelFeature != null;
    }

    private static void getService() {
        try {
            mDisplayPanelFeature = IDisplayPanelFeature.getService();
        } catch (RemoteException | NoSuchElementException unused) {
            Slog.e(TAG, "getHidlDisplayPanelFeatureService error");
        }
    }

    public static String getDisplayPanelFeatureValueAsString(int i) {
        ArrayList<Integer> displayPanelFeatureValue = getDisplayPanelFeatureValue(i);
        if (displayPanelFeatureValue == null || displayPanelFeatureValue.isEmpty()) {
            return null;
        }
        Iterator<Integer> it = displayPanelFeatureValue.iterator();
        String str = "";
        while (it.hasNext()) {
            str = str + it.next().intValue() + " ";
        }
        return str.trim();
    }

    public static int[] getDisplayPanelFeatureValueAsIntArray(int i) {
        ArrayList<Integer> displayPanelFeatureValue = getDisplayPanelFeatureValue(i);
        if (displayPanelFeatureValue == null || displayPanelFeatureValue.isEmpty()) {
            return null;
        }
        int[] iArr = new int[displayPanelFeatureValue.size()];
        for (int i2 = 0; i2 < displayPanelFeatureValue.size(); i2++) {
            iArr[i2] = displayPanelFeatureValue.get(i2).intValue();
        }
        return iArr;
    }

    public static int getDisplayPanelFeatureValueAsInt(int i) {
        ArrayList<Integer> displayPanelFeatureValue = getDisplayPanelFeatureValue(i);
        if (displayPanelFeatureValue == null || displayPanelFeatureValue.isEmpty()) {
            return -1;
        }
        return displayPanelFeatureValue.get(0).intValue();
    }

    public static boolean isFODHwDimlayer() {
        int displayPanelFeatureValueAsInt = getDisplayPanelFeatureValueAsInt(FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__ROLE_HOLDER_UPDATER_UPDATE_RETRY);
        return (displayPanelFeatureValueAsInt == -1 || (displayPanelFeatureValueAsInt & 4) == 0) ? false : true;
    }

    public static void setDisplayPanelFeatureValue(int i, int i2) {
        Slog.d(TAG, "setDisplayPanelFeatureValue +++, ID:" + i + ", value:" + i2);
        if (i == 28) {
            try {
                if (!isFODHwDimlayer()) {
                    setDisplayPanelFeatureValueForMtk(i, i2);
                    return;
                }
            } catch (RemoteException | NoSuchElementException unused) {
                Slog.e(TAG, "setDisplayPanelFeatureValue error");
                return;
            }
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(i2));
        if (mDisplayPanelFeature == null) {
            getService();
        }
        IDisplayPanelFeature iDisplayPanelFeature = mDisplayPanelFeature;
        if (iDisplayPanelFeature != null) {
            iDisplayPanelFeature.setDisplayPanelFeatureValue(i, arrayList);
        }
    }

    public static void setDisplayPanelFeatureValueArray(int i, int[] iArr) {
        if (iArr == null) {
            return;
        }
        try {
            ArrayList arrayList = new ArrayList();
            for (int i2 : iArr) {
                arrayList.add(Integer.valueOf(i2));
            }
            if (mDisplayPanelFeature == null) {
                getService();
            }
            IDisplayPanelFeature iDisplayPanelFeature = mDisplayPanelFeature;
            if (iDisplayPanelFeature != null) {
                iDisplayPanelFeature.setDisplayPanelFeatureValue(i, arrayList);
            }
        } catch (RemoteException | NoSuchElementException unused) {
            Slog.e(TAG, "setDisplayPanelFeatureValue error");
        }
    }

    private static int notifyDispFingerLayer(int i) {
        Slog.d(TAG, "notifyDispFingerLayer " + i);
        int i2 = -1;
        try {
            IBinder service = ServiceManager.getService("SurfaceFlinger");
            if (service != null) {
                synchronized (service) {
                    Parcel obtain = Parcel.obtain();
                    Parcel obtain2 = Parcel.obtain();
                    obtain.writeInterfaceToken("android.ui.ISurfaceComposer");
                    obtain.writeInt(i);
                    service.transact(NOTIFY_DISP_FINGER_LAYER, obtain, obtain2, 0);
                    i2 = obtain2.readInt();
                    obtain.recycle();
                    obtain2.recycle();
                }
                return i2;
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "notifyDispFingerLayer exception " + e);
        }
        return i2;
    }

    public static int setDisplayPanelFeatureValueForMtk(int i, int i2) {
        if (i != 28) {
            return -1;
        }
        return notifyDispFingerLayer(i2 == 1 ? 0 : 1);
    }

    public static ArrayList<String> getDisplayPanelInfo(int i) {
        Slog.d(TAG, "getDisplayPanelInfo +++, featureID:" + i);
        ArrayList<String> arrayList = null;
        final PendingResult pendingResult = new PendingResult(null);
        try {
            if (mDisplayPanelFeature == null) {
                getService();
            }
            IDisplayPanelFeature iDisplayPanelFeature = mDisplayPanelFeature;
            if (iDisplayPanelFeature != null) {
                iDisplayPanelFeature.getDisplayPanelInfo(i, new IDisplayPanelFeature.getDisplayPanelInfoCallback() { // from class: com.android.server.display.util.HidlDisplayPanelFeature$$ExternalSyntheticLambda1
                    public final void onValues(int i2, ArrayList arrayList2) {
                        HidlDisplayPanelFeature.lambda$getDisplayPanelInfo$1(PendingResult.this, i2, arrayList2);
                    }
                });
                arrayList = (ArrayList) pendingResult.await(10L, TimeUnit.MILLISECONDS);
            } else {
                Slog.e(TAG, "achieve getDisplayPanelInfo failed");
            }
        } catch (RemoteException | NoSuchElementException unused) {
            Slog.e(TAG, "getDisplayPanelInfo Error");
        }
        Slog.d(TAG, "getDisplayPanelInfo ---, result = " + arrayList);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getDisplayPanelInfo$1(PendingResult pendingResult, int i, ArrayList arrayList) {
        Slog.d(TAG, "getDisplayPanelInfo, ret = " + i);
        if (i < 0 || arrayList == null || arrayList.isEmpty()) {
            return;
        }
        Slog.i(TAG, Arrays.toString(arrayList.toArray()));
        pendingResult.setResult(arrayList);
    }

    public static String getDisplayPanelInfoAsString(int i) {
        ArrayList<String> displayPanelInfo = getDisplayPanelInfo(i);
        if (displayPanelInfo == null || displayPanelInfo.isEmpty()) {
            return null;
        }
        Iterator<String> it = displayPanelInfo.iterator();
        String str = "";
        while (it.hasNext()) {
            str = str + it.next() + " ";
        }
        return str.trim();
    }
}
