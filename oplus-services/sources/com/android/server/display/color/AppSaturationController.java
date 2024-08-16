package com.android.server.display.color;

import android.util.ArrayMap;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.display.color.ColorDisplayService;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AppSaturationController {

    @VisibleForTesting
    static final float[] TRANSLATION_VECTOR = {0.0f, 0.0f, 0.0f};
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final Map<String, SparseArray<SaturationController>> mAppsMap = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean addColorTransformController(String str, int i, WeakReference<ColorDisplayService.ColorTransformController> weakReference) {
        boolean addColorTransformController;
        synchronized (this.mLock) {
            addColorTransformController = getSaturationControllerLocked(str, i).addColorTransformController(weakReference);
        }
        return addColorTransformController;
    }

    public boolean setSaturationLevel(String str, String str2, int i, int i2) {
        boolean saturationLevel;
        synchronized (this.mLock) {
            saturationLevel = getSaturationControllerLocked(str2, i).setSaturationLevel(str, i2);
        }
        return saturationLevel;
    }

    public void dump(PrintWriter printWriter) {
        synchronized (this.mLock) {
            printWriter.println("App Saturation: ");
            if (this.mAppsMap.size() == 0) {
                printWriter.println("    No packages");
                return;
            }
            ArrayList<String> arrayList = new ArrayList(this.mAppsMap.keySet());
            Collections.sort(arrayList);
            for (String str : arrayList) {
                printWriter.println("    " + str + ":");
                SparseArray<SaturationController> sparseArray = this.mAppsMap.get(str);
                for (int i = 0; i < sparseArray.size(); i++) {
                    printWriter.println("        " + sparseArray.keyAt(i) + ":");
                    sparseArray.valueAt(i).dump(printWriter);
                }
            }
        }
    }

    private SaturationController getSaturationControllerLocked(String str, int i) {
        return getOrCreateSaturationControllerLocked(getOrCreateUserIdMapLocked(str), i);
    }

    private SparseArray<SaturationController> getOrCreateUserIdMapLocked(String str) {
        if (this.mAppsMap.get(str) != null) {
            return this.mAppsMap.get(str);
        }
        SparseArray<SaturationController> sparseArray = new SparseArray<>();
        this.mAppsMap.put(str, sparseArray);
        return sparseArray;
    }

    private SaturationController getOrCreateSaturationControllerLocked(SparseArray<SaturationController> sparseArray, int i) {
        if (sparseArray.get(i) != null) {
            return sparseArray.get(i);
        }
        SaturationController saturationController = new SaturationController();
        sparseArray.put(i, saturationController);
        return saturationController;
    }

    @VisibleForTesting
    static void computeGrayscaleTransformMatrix(float f, float[] fArr) {
        float f2 = 1.0f - f;
        float f3 = 0.231f * f2;
        float[] fArr2 = {f3, 0.715f * f2, f2 * 0.072f};
        fArr[0] = f3 + f;
        float f4 = fArr2[0];
        fArr[1] = f4;
        fArr[2] = f4;
        float f5 = fArr2[1];
        fArr[3] = f5;
        fArr[4] = f5 + f;
        fArr[5] = f5;
        float f6 = fArr2[2];
        fArr[6] = f6;
        fArr[7] = f6;
        fArr[8] = f6 + f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class SaturationController {
        private static final int FULL_SATURATION = 100;
        private final List<WeakReference<ColorDisplayService.ColorTransformController>> mControllerRefs;
        private final ArrayMap<String, Integer> mSaturationLevels;
        private float[] mTransformMatrix;

        private SaturationController() {
            this.mControllerRefs = new ArrayList();
            this.mSaturationLevels = new ArrayMap<>();
            this.mTransformMatrix = new float[9];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setSaturationLevel(String str, int i) {
            if (i == 100) {
                this.mSaturationLevels.remove(str);
            } else {
                this.mSaturationLevels.put(str, Integer.valueOf(i));
            }
            if (this.mControllerRefs.isEmpty()) {
                return false;
            }
            return updateState();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean addColorTransformController(WeakReference<ColorDisplayService.ColorTransformController> weakReference) {
            clearExpiredReferences();
            this.mControllerRefs.add(weakReference);
            if (this.mSaturationLevels.isEmpty()) {
                return false;
            }
            return updateState();
        }

        private int calculateSaturationLevel() {
            int i = 100;
            for (int i2 = 0; i2 < this.mSaturationLevels.size(); i2++) {
                int intValue = this.mSaturationLevels.valueAt(i2).intValue();
                if (intValue < i) {
                    i = intValue;
                }
            }
            return i;
        }

        private boolean updateState() {
            AppSaturationController.computeGrayscaleTransformMatrix(calculateSaturationLevel() / 100.0f, this.mTransformMatrix);
            Iterator<WeakReference<ColorDisplayService.ColorTransformController>> it = this.mControllerRefs.iterator();
            boolean z = false;
            while (it.hasNext()) {
                ColorDisplayService.ColorTransformController colorTransformController = it.next().get();
                if (colorTransformController != null) {
                    colorTransformController.applyAppSaturation(this.mTransformMatrix, AppSaturationController.TRANSLATION_VECTOR);
                    z = true;
                } else {
                    it.remove();
                }
            }
            return z;
        }

        private void clearExpiredReferences() {
            Iterator<WeakReference<ColorDisplayService.ColorTransformController>> it = this.mControllerRefs.iterator();
            while (it.hasNext()) {
                if (it.next().get() == null) {
                    it.remove();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(PrintWriter printWriter) {
            printWriter.println("            mSaturationLevels: " + this.mSaturationLevels);
            printWriter.println("            mControllerRefs count: " + this.mControllerRefs.size());
        }
    }
}
