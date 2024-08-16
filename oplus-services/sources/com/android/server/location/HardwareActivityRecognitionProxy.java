package com.android.server.location;

import android.R;
import android.content.Context;
import android.hardware.location.ActivityRecognitionHardware;
import android.hardware.location.IActivityRecognitionHardwareClient;
import android.hardware.location.IActivityRecognitionHardwareWatcher;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.server.servicewatcher.CurrentUserServiceSupplier;
import com.android.server.servicewatcher.ServiceWatcher;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class HardwareActivityRecognitionProxy implements ServiceWatcher.ServiceListener<CurrentUserServiceSupplier.BoundServiceInfo> {
    private static final String SERVICE_ACTION = "com.android.location.service.ActivityRecognitionProvider";
    private static final String TAG = "ARProxy";
    private final ActivityRecognitionHardware mInstance;
    private final boolean mIsSupported;
    private final ServiceWatcher mServiceWatcher;

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onUnbind() {
    }

    public static HardwareActivityRecognitionProxy createAndRegister(Context context) {
        HardwareActivityRecognitionProxy hardwareActivityRecognitionProxy = new HardwareActivityRecognitionProxy(context);
        if (hardwareActivityRecognitionProxy.register()) {
            return hardwareActivityRecognitionProxy;
        }
        return null;
    }

    private HardwareActivityRecognitionProxy(Context context) {
        boolean isSupported = ActivityRecognitionHardware.isSupported();
        this.mIsSupported = isSupported;
        if (isSupported) {
            this.mInstance = ActivityRecognitionHardware.getInstance(context);
        } else {
            this.mInstance = null;
        }
        this.mServiceWatcher = ServiceWatcher.create(context, "HardwareActivityRecognitionProxy", CurrentUserServiceSupplier.createFromConfig(context, SERVICE_ACTION, 17891652, R.string.config_defaultNearbySharingComponent), this);
    }

    private boolean register() {
        boolean checkServiceResolves = this.mServiceWatcher.checkServiceResolves();
        if (checkServiceResolves) {
            this.mServiceWatcher.register();
        }
        return checkServiceResolves;
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onBind(IBinder iBinder, CurrentUserServiceSupplier.BoundServiceInfo boundServiceInfo) throws RemoteException {
        String interfaceDescriptor = iBinder.getInterfaceDescriptor();
        if (IActivityRecognitionHardwareWatcher.class.getCanonicalName().equals(interfaceDescriptor)) {
            IActivityRecognitionHardwareWatcher asInterface = IActivityRecognitionHardwareWatcher.Stub.asInterface(iBinder);
            ActivityRecognitionHardware activityRecognitionHardware = this.mInstance;
            if (activityRecognitionHardware != null) {
                asInterface.onInstanceChanged(activityRecognitionHardware);
                return;
            }
            return;
        }
        if (IActivityRecognitionHardwareClient.class.getCanonicalName().equals(interfaceDescriptor)) {
            IActivityRecognitionHardwareClient.Stub.asInterface(iBinder).onAvailabilityChanged(this.mIsSupported, this.mInstance);
            return;
        }
        Log.e(TAG, "Unknown descriptor: " + interfaceDescriptor);
    }
}
