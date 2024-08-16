package com.android.server.biometrics.sensors;

import android.hardware.fingerprint.ISidefpsController;
import android.hardware.fingerprint.IUdfpsOverlay;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.hardware.fingerprint.IUdfpsOverlayControllerCallback;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.biometrics.sensors.fingerprint.IUdfpsHelperExt;
import java.util.Optional;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SensorOverlays {
    private static final String TAG = "SensorOverlays";
    private static IUdfpsHelperExt mUdfpsHelperExt = (IUdfpsHelperExt) ExtLoader.type(IUdfpsHelperExt.class).create();
    private final Optional<ISidefpsController> mSidefpsController;
    private final Optional<IUdfpsOverlay> mUdfpsOverlay;
    private final Optional<IUdfpsOverlayController> mUdfpsOverlayController;

    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface OverlayControllerConsumer<T> {
        void accept(T t) throws RemoteException;
    }

    public SensorOverlays(IUdfpsOverlayController iUdfpsOverlayController, ISidefpsController iSidefpsController, IUdfpsOverlay iUdfpsOverlay) {
        this.mUdfpsOverlayController = Optional.ofNullable(iUdfpsOverlayController);
        this.mSidefpsController = Optional.ofNullable(iSidefpsController);
        this.mUdfpsOverlay = Optional.ofNullable(iUdfpsOverlay);
    }

    public void show(int i, int i2, final AcquisitionClient<?> acquisitionClient) {
        if (this.mSidefpsController.isPresent()) {
            try {
                this.mSidefpsController.get().show(i, i2);
            } catch (RemoteException e) {
                Slog.e(TAG, "Remote exception when showing the side-fps overlay", e);
            }
        }
        if (this.mUdfpsOverlayController.isPresent()) {
            IUdfpsOverlayControllerCallback.Stub stub = new IUdfpsOverlayControllerCallback.Stub() { // from class: com.android.server.biometrics.sensors.SensorOverlays.1
                public void onUserCanceled() {
                    acquisitionClient.onUserCanceled();
                }
            };
            try {
                mUdfpsHelperExt.preShowUdfpsOverlay(i, i2);
                this.mUdfpsOverlayController.get().showUdfpsOverlay(acquisitionClient.getRequestId(), i, i2, stub);
            } catch (RemoteException e2) {
                Slog.e(TAG, "Remote exception when showing the UDFPS overlay", e2);
            }
        }
        if (this.mUdfpsOverlay.isPresent()) {
            try {
                this.mUdfpsOverlay.get().show(acquisitionClient.getRequestId(), i, i2);
            } catch (RemoteException e3) {
                Slog.e(TAG, "Remote exception when showing the new UDFPS overlay", e3);
            }
        }
    }

    public void hide(int i) {
        if (this.mSidefpsController.isPresent()) {
            try {
                this.mSidefpsController.get().hide(i);
            } catch (RemoteException e) {
                Slog.e(TAG, "Remote exception when hiding the side-fps overlay", e);
            }
        }
        if (this.mUdfpsOverlayController.isPresent()) {
            try {
                mUdfpsHelperExt.preHideUdfpsOverlay(i);
                this.mUdfpsOverlayController.get().hideUdfpsOverlay(i);
            } catch (RemoteException e2) {
                Slog.e(TAG, "Remote exception when hiding the UDFPS overlay", e2);
            }
        }
        if (this.mUdfpsOverlay.isPresent()) {
            try {
                this.mUdfpsOverlay.get().hide(i);
            } catch (RemoteException e3) {
                Slog.e(TAG, "Remote exception when hiding the new udfps overlay", e3);
            }
        }
    }

    public void ifUdfps(OverlayControllerConsumer<IUdfpsOverlayController> overlayControllerConsumer) {
        if (this.mUdfpsOverlayController.isPresent()) {
            try {
                overlayControllerConsumer.accept(this.mUdfpsOverlayController.get());
            } catch (RemoteException e) {
                Slog.e(TAG, "Remote exception using overlay controller", e);
            }
        }
    }
}
