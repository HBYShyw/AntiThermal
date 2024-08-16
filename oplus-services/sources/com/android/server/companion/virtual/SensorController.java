package com.android.server.companion.virtual;

import android.companion.virtual.sensor.IVirtualSensorCallback;
import android.companion.virtual.sensor.VirtualSensor;
import android.companion.virtual.sensor.VirtualSensorConfig;
import android.companion.virtual.sensor.VirtualSensorEvent;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SharedMemory;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import com.android.server.companion.virtual.SensorController;
import com.android.server.sensors.SensorManagerInternal;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SensorController {
    private static final int BAD_VALUE = -22;
    private static final int OK = 0;
    private static final String TAG = "SensorController";
    private static final int UNKNOWN_ERROR = Integer.MIN_VALUE;
    private static AtomicInteger sNextDirectChannelHandle = new AtomicInteger(1);
    private final SensorManagerInternal.RuntimeSensorCallback mRuntimeSensorCallback;
    private final int mVirtualDeviceId;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final ArrayMap<IBinder, SensorDescriptor> mSensorDescriptors = new ArrayMap<>();
    private final SensorManagerInternal mSensorManagerInternal = (SensorManagerInternal) LocalServices.getService(SensorManagerInternal.class);
    private final VirtualDeviceManagerInternal mVdmInternal = (VirtualDeviceManagerInternal) LocalServices.getService(VirtualDeviceManagerInternal.class);

    public SensorController(int i, IVirtualSensorCallback iVirtualSensorCallback) {
        this.mVirtualDeviceId = i;
        this.mRuntimeSensorCallback = new RuntimeSensorCallbackWrapper(iVirtualSensorCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        synchronized (this.mLock) {
            this.mSensorDescriptors.values().forEach(new Consumer() { // from class: com.android.server.companion.virtual.SensorController$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    SensorController.this.lambda$close$0((SensorController.SensorDescriptor) obj);
                }
            });
            this.mSensorDescriptors.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$close$0(SensorDescriptor sensorDescriptor) {
        this.mSensorManagerInternal.removeRuntimeSensor(sensorDescriptor.getHandle());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int createSensor(IBinder iBinder, VirtualSensorConfig virtualSensorConfig) {
        Objects.requireNonNull(iBinder);
        Objects.requireNonNull(virtualSensorConfig);
        try {
            return createSensorInternal(iBinder, virtualSensorConfig);
        } catch (SensorCreationException e) {
            throw new RuntimeException("Failed to create virtual sensor '" + virtualSensorConfig.getName() + "'.", e);
        }
    }

    private int createSensorInternal(IBinder iBinder, VirtualSensorConfig virtualSensorConfig) throws SensorCreationException {
        if (virtualSensorConfig.getType() <= 0) {
            throw new SensorCreationException("Received an invalid virtual sensor type.");
        }
        int createRuntimeSensor = this.mSensorManagerInternal.createRuntimeSensor(this.mVirtualDeviceId, virtualSensorConfig.getType(), virtualSensorConfig.getName(), virtualSensorConfig.getVendor() == null ? "" : virtualSensorConfig.getVendor(), virtualSensorConfig.getMaximumRange(), virtualSensorConfig.getResolution(), virtualSensorConfig.getPower(), virtualSensorConfig.getMinDelay(), virtualSensorConfig.getMaxDelay(), virtualSensorConfig.getFlags(), this.mRuntimeSensorCallback);
        if (createRuntimeSensor <= 0) {
            throw new SensorCreationException("Received an invalid virtual sensor handle.");
        }
        synchronized (this.mLock) {
            this.mSensorDescriptors.put(iBinder, new SensorDescriptor(createRuntimeSensor, virtualSensorConfig.getType(), virtualSensorConfig.getName()));
        }
        return createRuntimeSensor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean sendSensorEvent(IBinder iBinder, VirtualSensorEvent virtualSensorEvent) {
        boolean sendSensorEvent;
        Objects.requireNonNull(iBinder);
        Objects.requireNonNull(virtualSensorEvent);
        synchronized (this.mLock) {
            SensorDescriptor sensorDescriptor = this.mSensorDescriptors.get(iBinder);
            if (sensorDescriptor == null) {
                throw new IllegalArgumentException("Could not send sensor event for given token");
            }
            sendSensorEvent = this.mSensorManagerInternal.sendSensorEvent(sensorDescriptor.getHandle(), sensorDescriptor.getType(), virtualSensorEvent.getTimestampNanos(), virtualSensorEvent.getValues());
        }
        return sendSensorEvent;
    }

    void unregisterSensor(IBinder iBinder) {
        Objects.requireNonNull(iBinder);
        synchronized (this.mLock) {
            SensorDescriptor remove = this.mSensorDescriptors.remove(iBinder);
            if (remove == null) {
                throw new IllegalArgumentException("Could not unregister sensor for given token");
            }
            this.mSensorManagerInternal.removeRuntimeSensor(remove.getHandle());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.println("    SensorController: ");
        synchronized (this.mLock) {
            printWriter.println("      Active descriptors: ");
            for (SensorDescriptor sensorDescriptor : this.mSensorDescriptors.values()) {
                printWriter.println("        handle: " + sensorDescriptor.getHandle());
                printWriter.println("          type: " + sensorDescriptor.getType());
                printWriter.println("          name: " + sensorDescriptor.getName());
            }
        }
    }

    @VisibleForTesting
    void addSensorForTesting(IBinder iBinder, int i, int i2, String str) {
        synchronized (this.mLock) {
            this.mSensorDescriptors.put(iBinder, new SensorDescriptor(i, i2, str));
        }
    }

    @VisibleForTesting
    Map<IBinder, SensorDescriptor> getSensorDescriptors() {
        ArrayMap arrayMap;
        synchronized (this.mLock) {
            arrayMap = new ArrayMap(this.mSensorDescriptors);
        }
        return arrayMap;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class RuntimeSensorCallbackWrapper implements SensorManagerInternal.RuntimeSensorCallback {
        private IVirtualSensorCallback mCallback;

        RuntimeSensorCallbackWrapper(IVirtualSensorCallback iVirtualSensorCallback) {
            this.mCallback = iVirtualSensorCallback;
        }

        public int onConfigurationChanged(int i, boolean z, int i2, int i3) {
            if (this.mCallback == null) {
                Slog.e(SensorController.TAG, "No sensor callback configured for sensor handle " + i);
                return -22;
            }
            VirtualSensor virtualSensor = SensorController.this.mVdmInternal.getVirtualSensor(SensorController.this.mVirtualDeviceId, i);
            if (virtualSensor == null) {
                Slog.e(SensorController.TAG, "No sensor found for deviceId=" + SensorController.this.mVirtualDeviceId + " and sensor handle=" + i);
                return -22;
            }
            try {
                this.mCallback.onConfigurationChanged(virtualSensor, z, i2, i3);
                return 0;
            } catch (RemoteException e) {
                Slog.e(SensorController.TAG, "Failed to call sensor callback: " + e);
                return Integer.MIN_VALUE;
            }
        }

        public int onDirectChannelCreated(ParcelFileDescriptor parcelFileDescriptor) {
            if (this.mCallback == null) {
                Slog.e(SensorController.TAG, "No sensor callback for virtual deviceId " + SensorController.this.mVirtualDeviceId);
                return -22;
            }
            if (parcelFileDescriptor == null) {
                Slog.e(SensorController.TAG, "Received invalid ParcelFileDescriptor");
                return -22;
            }
            int andIncrement = SensorController.sNextDirectChannelHandle.getAndIncrement();
            try {
                this.mCallback.onDirectChannelCreated(andIncrement, SharedMemory.fromFileDescriptor(parcelFileDescriptor));
                return andIncrement;
            } catch (RemoteException e) {
                Slog.e(SensorController.TAG, "Failed to call sensor callback: " + e);
                return Integer.MIN_VALUE;
            }
        }

        public void onDirectChannelDestroyed(int i) {
            IVirtualSensorCallback iVirtualSensorCallback = this.mCallback;
            if (iVirtualSensorCallback == null) {
                Slog.e(SensorController.TAG, "No sensor callback for virtual deviceId " + SensorController.this.mVirtualDeviceId);
                return;
            }
            try {
                iVirtualSensorCallback.onDirectChannelDestroyed(i);
            } catch (RemoteException e) {
                Slog.e(SensorController.TAG, "Failed to call sensor callback: " + e);
            }
        }

        public int onDirectChannelConfigured(int i, int i2, int i3) {
            if (this.mCallback == null) {
                Slog.e(SensorController.TAG, "No runtime sensor callback configured.");
                return -22;
            }
            VirtualSensor virtualSensor = SensorController.this.mVdmInternal.getVirtualSensor(SensorController.this.mVirtualDeviceId, i2);
            if (virtualSensor == null) {
                Slog.e(SensorController.TAG, "No sensor found for deviceId=" + SensorController.this.mVirtualDeviceId + " and sensor handle=" + i2);
                return -22;
            }
            try {
                this.mCallback.onDirectChannelConfigured(i, virtualSensor, i3, i2);
                if (i3 == 0) {
                    return 0;
                }
                return i2;
            } catch (RemoteException e) {
                Slog.e(SensorController.TAG, "Failed to call sensor callback: " + e);
                return Integer.MIN_VALUE;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class SensorDescriptor {
        private final int mHandle;
        private final String mName;
        private final int mType;

        SensorDescriptor(int i, int i2, String str) {
            this.mHandle = i;
            this.mType = i2;
            this.mName = str;
        }

        public int getHandle() {
            return this.mHandle;
        }

        public int getType() {
            return this.mType;
        }

        public String getName() {
            return this.mName;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class SensorCreationException extends Exception {
        SensorCreationException(String str) {
            super(str);
        }

        SensorCreationException(String str, Exception exc) {
            super(str, exc);
        }
    }
}
