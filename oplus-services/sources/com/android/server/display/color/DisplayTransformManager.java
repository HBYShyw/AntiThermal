package com.android.server.display.color;

import android.app.ActivityTaskManager;
import android.content.res.Configuration;
import android.opengl.Matrix;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.reflect.Array;
import java.util.Arrays;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DisplayTransformManager {
    private static final float COLOR_SATURATION_BOOSTED = 1.1f;
    private static final float COLOR_SATURATION_NATURAL = 1.0f;
    private static final int DISPLAY_COLOR_ENHANCED = 2;
    private static final int DISPLAY_COLOR_MANAGED = 0;
    private static final int DISPLAY_COLOR_UNMANAGED = 1;
    public static final int LEVEL_COLOR_MATRIX_DISPLAY_WHITE_BALANCE = 125;
    public static final int LEVEL_COLOR_MATRIX_GRAYSCALE = 200;
    public static final int LEVEL_COLOR_MATRIX_INVERT_COLOR = 300;
    public static final int LEVEL_COLOR_MATRIX_NIGHT_DISPLAY = 100;
    public static final int LEVEL_COLOR_MATRIX_REDUCE_BRIGHT_COLORS = 250;
    public static final int LEVEL_COLOR_MATRIX_SATURATION = 150;

    @VisibleForTesting
    static final String PERSISTENT_PROPERTY_COMPOSITION_COLOR_MODE = "persist.sys.sf.color_mode";

    @VisibleForTesting
    static final String PERSISTENT_PROPERTY_DISPLAY_COLOR = "persist.sys.sf.native_mode";

    @VisibleForTesting
    static final String PERSISTENT_PROPERTY_SATURATION = "persist.sys.sf.color_saturation";
    private static final int SURFACE_FLINGER_TRANSACTION_COLOR_MATRIX = 1015;
    private static final int SURFACE_FLINGER_TRANSACTION_DALTONIZER = 1014;
    private static final int SURFACE_FLINGER_TRANSACTION_DISPLAY_COLOR = 1023;
    private static final int SURFACE_FLINGER_TRANSACTION_QUERY_COLOR_MANAGED = 1030;
    private static final int SURFACE_FLINGER_TRANSACTION_SATURATION = 1022;
    private static final String TAG = "DisplayTransformManager";
    private static final String SURFACE_FLINGER = "SurfaceFlinger";
    private static final IBinder sFlinger = ServiceManager.getService(SURFACE_FLINGER);

    @GuardedBy({"mColorMatrix"})
    private final SparseArray<float[]> mColorMatrix = new SparseArray<>(6);

    @GuardedBy({"mColorMatrix"})
    private final float[][] mTempColorMatrix = (float[][]) Array.newInstance((Class<?>) Float.TYPE, 2, 16);
    private final Object mDaltonizerModeLock = new Object();

    @GuardedBy({"mDaltonizerModeLock"})
    private int mDaltonizerMode = -1;
    public IDisplayTransformManagerWrapper Wrapper = new DisplayTransformManagerWrapper();

    public boolean needsLinearColorMatrix(int i) {
        return i != 2;
    }

    public float[] getColorMatrix(int i) {
        float[] copyOf;
        synchronized (this.mColorMatrix) {
            float[] fArr = this.mColorMatrix.get(i);
            copyOf = fArr == null ? null : Arrays.copyOf(fArr, fArr.length);
        }
        return copyOf;
    }

    public void setColorMatrix(int i, float[] fArr) {
        if (fArr != null && fArr.length != 16) {
            throw new IllegalArgumentException("Expected length: 16 (4x4 matrix), actual length: " + fArr.length);
        }
        synchronized (this.mColorMatrix) {
            float[] fArr2 = this.mColorMatrix.get(i);
            if (!Arrays.equals(fArr2, fArr)) {
                if (fArr == null) {
                    this.mColorMatrix.remove(i);
                } else if (fArr2 == null) {
                    this.mColorMatrix.put(i, Arrays.copyOf(fArr, fArr.length));
                } else {
                    System.arraycopy(fArr, 0, fArr2, 0, fArr.length);
                }
                applyColorMatrix(computeColorMatrixLocked());
            }
        }
    }

    public void setDaltonizerMode(int i) {
        synchronized (this.mDaltonizerModeLock) {
            if (this.mDaltonizerMode != i) {
                this.mDaltonizerMode = i;
                applyDaltonizerMode(i);
            }
        }
    }

    @GuardedBy({"mColorMatrix"})
    private float[] computeColorMatrixLocked() {
        int size = this.mColorMatrix.size();
        if (size == 0) {
            return null;
        }
        float[][] fArr = this.mTempColorMatrix;
        Matrix.setIdentityM(fArr[0], 0);
        for (int i = 0; i < size; i++) {
            float[] valueAt = this.mColorMatrix.valueAt(i);
            if (i == size - 1 && this.mColorMatrix.keyAt(i) > 300) {
                Matrix.multiplyMM(fArr[(i + 1) % 2], 0, valueAt, 0, fArr[i % 2], 0);
            } else {
                Matrix.multiplyMM(fArr[(i + 1) % 2], 0, fArr[i % 2], 0, valueAt, 0);
            }
        }
        return fArr[size % 2];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyColorMatrix(float[] fArr) {
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken("android.ui.ISurfaceComposer");
        if (fArr != null) {
            obtain.writeInt(1);
            for (int i = 0; i < 16; i++) {
                obtain.writeFloat(fArr[i]);
            }
        } else {
            obtain.writeInt(0);
        }
        try {
            try {
                sFlinger.transact(1015, obtain, null, 0);
            } catch (RemoteException e) {
                Slog.e(TAG, "Failed to set color transform", e);
            }
        } finally {
            obtain.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyColorMatrix32(float[] fArr) {
        int i;
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken("android.ui.ISurfaceComposer");
        if (fArr != null) {
            obtain.writeInt(1);
            int i2 = 0;
            while (true) {
                if (i2 >= 16) {
                    break;
                }
                obtain.writeFloat(fArr[i2]);
                i2++;
            }
            obtain.writeInt(1);
            for (i = 16; i < 32; i++) {
                obtain.writeFloat(fArr[i]);
            }
        } else {
            obtain.writeInt(0);
        }
        try {
            try {
                Slog.d(TAG, "Success to set color transform ");
                sFlinger.transact(1015, obtain, null, 0);
            } catch (RemoteException e) {
                Slog.e(TAG, "Failed to set color transform", e);
            }
        } finally {
            obtain.recycle();
        }
    }

    private static void applyDaltonizerMode(int i) {
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken("android.ui.ISurfaceComposer");
        obtain.writeInt(i);
        try {
            try {
                sFlinger.transact(1014, obtain, null, 0);
            } catch (RemoteException e) {
                Slog.e(TAG, "Failed to set Daltonizer mode", e);
            }
        } finally {
            obtain.recycle();
        }
    }

    public boolean needsLinearColorMatrix() {
        return SystemProperties.getInt(PERSISTENT_PROPERTY_DISPLAY_COLOR, 1) != 1;
    }

    public boolean setColorMode(int i, float[] fArr, int i2) {
        if (i == 0) {
            applySaturation(1.0f);
            setDisplayColor(0, i2);
        } else if (i == 1) {
            applySaturation(COLOR_SATURATION_BOOSTED);
            setDisplayColor(0, i2);
        } else if (i == 2) {
            applySaturation(1.0f);
            setDisplayColor(1, i2);
        } else if (i == 3) {
            applySaturation(1.0f);
            setDisplayColor(2, i2);
        } else if (i >= 256 && i <= 511) {
            applySaturation(1.0f);
            setDisplayColor(i, i2);
        }
        setColorMatrix(100, fArr);
        updateConfiguration();
        return true;
    }

    public boolean isDeviceColorManaged() {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        obtain.writeInterfaceToken("android.ui.ISurfaceComposer");
        try {
            sFlinger.transact(1030, obtain, obtain2, 0);
            return obtain2.readBoolean();
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to query wide color support", e);
            return false;
        } finally {
            obtain.recycle();
            obtain2.recycle();
        }
    }

    private void applySaturation(float f) {
        SystemProperties.set(PERSISTENT_PROPERTY_SATURATION, Float.toString(f));
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken("android.ui.ISurfaceComposer");
        obtain.writeFloat(f);
        try {
            try {
                sFlinger.transact(SURFACE_FLINGER_TRANSACTION_SATURATION, obtain, null, 0);
            } catch (RemoteException e) {
                Slog.e(TAG, "Failed to set saturation", e);
            }
        } finally {
            obtain.recycle();
        }
    }

    private void setDisplayColor(int i, int i2) {
        SystemProperties.set(PERSISTENT_PROPERTY_DISPLAY_COLOR, Integer.toString(i));
        if (i2 != -1) {
            SystemProperties.set(PERSISTENT_PROPERTY_COMPOSITION_COLOR_MODE, Integer.toString(i2));
        }
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken("android.ui.ISurfaceComposer");
        obtain.writeInt(i);
        if (i2 != -1) {
            obtain.writeInt(i2);
        }
        try {
            try {
                sFlinger.transact(SURFACE_FLINGER_TRANSACTION_DISPLAY_COLOR, obtain, null, 0);
            } catch (RemoteException e) {
                Slog.e(TAG, "Failed to set display color", e);
            }
        } finally {
            obtain.recycle();
        }
    }

    private void updateConfiguration() {
        try {
            ActivityTaskManager.getService().updateConfiguration((Configuration) null);
        } catch (RemoteException e) {
            Slog.e(TAG, "Could not update configuration", e);
        }
    }

    public IDisplayTransformManagerWrapper getWrapper() {
        return this.Wrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class DisplayTransformManagerWrapper implements IDisplayTransformManagerWrapper {
        private DisplayTransformManagerWrapper() {
        }

        @Override // com.android.server.display.color.IDisplayTransformManagerWrapper
        public SparseArray<float[]> getColorMatrixs() {
            return DisplayTransformManager.this.mColorMatrix;
        }

        @Override // com.android.server.display.color.IDisplayTransformManagerWrapper
        public void applyColorMatrix(float[] fArr) {
            DisplayTransformManager.applyColorMatrix(fArr);
        }

        @Override // com.android.server.display.color.IDisplayTransformManagerWrapper
        public void applyColorMatrix32(float[] fArr) {
            DisplayTransformManager.applyColorMatrix32(fArr);
        }
    }
}
