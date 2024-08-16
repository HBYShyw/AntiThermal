package com.android.server.location.geofence;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.location.GeofenceHardwareService;
import android.hardware.location.IGeofenceHardware;
import android.location.IGeofenceProvider;
import android.location.IGpsGeofenceHardware;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.android.server.servicewatcher.CurrentUserServiceSupplier;
import com.android.server.servicewatcher.ServiceWatcher;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class GeofenceProxy implements ServiceWatcher.ServiceListener<CurrentUserServiceSupplier.BoundServiceInfo> {
    private static final String SERVICE_ACTION = "com.android.location.service.GeofenceProvider";
    private static final String TAG = "GeofenceProxy";
    volatile IGeofenceHardware mGeofenceHardware;
    final IGpsGeofenceHardware mGpsGeofenceHardware;
    final ServiceWatcher mServiceWatcher;

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onUnbind() {
    }

    public static GeofenceProxy createAndBind(Context context, IGpsGeofenceHardware iGpsGeofenceHardware) {
        GeofenceProxy geofenceProxy = new GeofenceProxy(context, iGpsGeofenceHardware);
        if (geofenceProxy.register(context)) {
            return geofenceProxy;
        }
        return null;
    }

    private GeofenceProxy(Context context, IGpsGeofenceHardware iGpsGeofenceHardware) {
        Objects.requireNonNull(iGpsGeofenceHardware);
        this.mGpsGeofenceHardware = iGpsGeofenceHardware;
        this.mServiceWatcher = ServiceWatcher.create(context, TAG, CurrentUserServiceSupplier.createFromConfig(context, SERVICE_ACTION, 17891662, R.string.console_running_notification_title), this);
        this.mGeofenceHardware = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateGeofenceHardware(IBinder iBinder) throws RemoteException {
        IGeofenceProvider.Stub.asInterface(iBinder).setGeofenceHardware(this.mGeofenceHardware);
    }

    private boolean register(Context context) {
        boolean checkServiceResolves = this.mServiceWatcher.checkServiceResolves();
        if (checkServiceResolves) {
            this.mServiceWatcher.register();
            context.bindServiceAsUser(new Intent(context, (Class<?>) GeofenceHardwareService.class), new GeofenceProxyServiceConnection(), 1, UserHandle.SYSTEM);
        }
        return checkServiceResolves;
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onBind(IBinder iBinder, CurrentUserServiceSupplier.BoundServiceInfo boundServiceInfo) throws RemoteException {
        updateGeofenceHardware(iBinder);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class GeofenceProxyServiceConnection implements ServiceConnection {
        GeofenceProxyServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IGeofenceHardware asInterface = IGeofenceHardware.Stub.asInterface(iBinder);
            try {
                asInterface.setGpsGeofenceHardware(GeofenceProxy.this.mGpsGeofenceHardware);
                GeofenceProxy.this.mGeofenceHardware = asInterface;
                GeofenceProxy geofenceProxy = GeofenceProxy.this;
                geofenceProxy.mServiceWatcher.runOnBinder(new GeofenceProxy$GeofenceProxyServiceConnection$$ExternalSyntheticLambda0(geofenceProxy));
            } catch (RemoteException e) {
                Log.w(GeofenceProxy.TAG, "unable to initialize geofence hardware", e);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            GeofenceProxy.this.mGeofenceHardware = null;
            GeofenceProxy geofenceProxy = GeofenceProxy.this;
            geofenceProxy.mServiceWatcher.runOnBinder(new GeofenceProxy$GeofenceProxyServiceConnection$$ExternalSyntheticLambda0(geofenceProxy));
        }
    }
}
