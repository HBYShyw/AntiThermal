package com.android.server.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.DataLoaderParamsParcel;
import android.content.pm.IDataLoader;
import android.content.pm.IDataLoaderManager;
import android.content.pm.IDataLoaderStatusListener;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Slog;
import android.util.SparseArray;
import com.android.server.SystemService;
import com.android.server.pm.DataLoaderManagerService;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DataLoaderManagerService extends SystemService {
    private static final String TAG = "DataLoaderManager";
    private final DataLoaderManagerBinderService mBinderService;
    private final Context mContext;
    private final Handler mHandler;
    private SparseArray<DataLoaderServiceConnection> mServiceConnections;
    private final HandlerThread mThread;

    public DataLoaderManagerService(Context context) {
        super(context);
        this.mServiceConnections = new SparseArray<>();
        this.mContext = context;
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mThread = handlerThread;
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
        this.mBinderService = new DataLoaderManagerBinderService();
    }

    public void onStart() {
        publishBinderService("dataloader_manager", this.mBinderService);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class DataLoaderManagerBinderService extends IDataLoaderManager.Stub {
        DataLoaderManagerBinderService() {
        }

        public boolean bindToDataLoader(final int i, DataLoaderParamsParcel dataLoaderParamsParcel, long j, IDataLoaderStatusListener iDataLoaderStatusListener) {
            synchronized (DataLoaderManagerService.this.mServiceConnections) {
                if (DataLoaderManagerService.this.mServiceConnections.get(i) != null) {
                    return true;
                }
                ComponentName componentName = new ComponentName(dataLoaderParamsParcel.packageName, dataLoaderParamsParcel.className);
                final ComponentName resolveDataLoaderComponentName = resolveDataLoaderComponentName(componentName);
                if (resolveDataLoaderComponentName == null) {
                    Slog.e(DataLoaderManagerService.TAG, "Invalid component: " + componentName + " for ID=" + i);
                    return false;
                }
                final DataLoaderServiceConnection dataLoaderServiceConnection = new DataLoaderServiceConnection(i, iDataLoaderStatusListener);
                final Intent intent = new Intent();
                intent.setComponent(resolveDataLoaderComponentName);
                return DataLoaderManagerService.this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.pm.DataLoaderManagerService$DataLoaderManagerBinderService$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DataLoaderManagerService.DataLoaderManagerBinderService.this.lambda$bindToDataLoader$0(intent, dataLoaderServiceConnection, resolveDataLoaderComponentName, i);
                    }
                }, j);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$bindToDataLoader$0(Intent intent, DataLoaderServiceConnection dataLoaderServiceConnection, ComponentName componentName, int i) {
            if (DataLoaderManagerService.this.mContext.bindServiceAsUser(intent, dataLoaderServiceConnection, 1, DataLoaderManagerService.this.mHandler, UserHandle.of(UserHandle.getCallingUserId()))) {
                return;
            }
            Slog.e(DataLoaderManagerService.TAG, "Failed to bind to: " + componentName + " for ID=" + i);
            DataLoaderManagerService.this.mContext.unbindService(dataLoaderServiceConnection);
        }

        private ComponentName resolveDataLoaderComponentName(ComponentName componentName) {
            PackageManager packageManager = DataLoaderManagerService.this.mContext.getPackageManager();
            if (packageManager == null) {
                Slog.e(DataLoaderManagerService.TAG, "PackageManager is not available.");
                return null;
            }
            Intent intent = new Intent("android.intent.action.LOAD_DATA");
            intent.setComponent(componentName);
            List queryIntentServicesAsUser = packageManager.queryIntentServicesAsUser(intent, 0, UserHandle.getCallingUserId());
            if (queryIntentServicesAsUser == null || queryIntentServicesAsUser.isEmpty()) {
                Slog.e(DataLoaderManagerService.TAG, "Failed to find data loader service provider in " + componentName);
                return null;
            }
            if (queryIntentServicesAsUser.size() > 0) {
                ServiceInfo serviceInfo = ((ResolveInfo) queryIntentServicesAsUser.get(0)).serviceInfo;
                return new ComponentName(serviceInfo.packageName, serviceInfo.name);
            }
            Slog.e(DataLoaderManagerService.TAG, "Didn't find any matching data loader service provider.");
            return null;
        }

        public IDataLoader getDataLoader(int i) {
            synchronized (DataLoaderManagerService.this.mServiceConnections) {
                DataLoaderServiceConnection dataLoaderServiceConnection = (DataLoaderServiceConnection) DataLoaderManagerService.this.mServiceConnections.get(i, null);
                if (dataLoaderServiceConnection == null) {
                    return null;
                }
                return dataLoaderServiceConnection.getDataLoader();
            }
        }

        public void unbindFromDataLoader(int i) {
            synchronized (DataLoaderManagerService.this.mServiceConnections) {
                DataLoaderServiceConnection dataLoaderServiceConnection = (DataLoaderServiceConnection) DataLoaderManagerService.this.mServiceConnections.get(i, null);
                if (dataLoaderServiceConnection == null) {
                    return;
                }
                dataLoaderServiceConnection.destroy();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class DataLoaderServiceConnection implements ServiceConnection, IBinder.DeathRecipient {
        IDataLoader mDataLoader = null;
        final int mId;
        final IDataLoaderStatusListener mListener;

        DataLoaderServiceConnection(int i, IDataLoaderStatusListener iDataLoaderStatusListener) {
            this.mId = i;
            this.mListener = iDataLoaderStatusListener;
            callListener(1);
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IDataLoader asInterface = IDataLoader.Stub.asInterface(iBinder);
            this.mDataLoader = asInterface;
            if (asInterface == null) {
                onNullBinding(componentName);
                return;
            }
            if (!append()) {
                DataLoaderManagerService.this.mContext.unbindService(this);
                return;
            }
            try {
                iBinder.linkToDeath(this, 0);
                callListener(2);
            } catch (RemoteException e) {
                Slog.e(DataLoaderManagerService.TAG, "Failed to link to DataLoader's death: " + this.mId, e);
                onBindingDied(componentName);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Slog.i(DataLoaderManagerService.TAG, "DataLoader " + this.mId + " disconnected, but will try to recover");
            unbindAndReportDestroyed();
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName componentName) {
            Slog.i(DataLoaderManagerService.TAG, "DataLoader " + this.mId + " died");
            unbindAndReportDestroyed();
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(ComponentName componentName) {
            Slog.i(DataLoaderManagerService.TAG, "DataLoader " + this.mId + " failed to start");
            unbindAndReportDestroyed();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Slog.i(DataLoaderManagerService.TAG, "DataLoader " + this.mId + " died");
            unbindAndReportDestroyed();
        }

        IDataLoader getDataLoader() {
            return this.mDataLoader;
        }

        private void unbindAndReportDestroyed() {
            if (unbind()) {
                callListener(0);
            }
        }

        void destroy() {
            IDataLoader iDataLoader = this.mDataLoader;
            if (iDataLoader != null) {
                try {
                    iDataLoader.destroy(this.mId);
                } catch (RemoteException unused) {
                }
                this.mDataLoader = null;
            }
            unbind();
        }

        boolean unbind() {
            try {
                DataLoaderManagerService.this.mContext.unbindService(this);
            } catch (Exception unused) {
            }
            return remove();
        }

        private boolean append() {
            synchronized (DataLoaderManagerService.this.mServiceConnections) {
                DataLoaderServiceConnection dataLoaderServiceConnection = (DataLoaderServiceConnection) DataLoaderManagerService.this.mServiceConnections.get(this.mId);
                if (dataLoaderServiceConnection == this) {
                    return true;
                }
                if (dataLoaderServiceConnection != null) {
                    return false;
                }
                DataLoaderManagerService.this.mServiceConnections.append(this.mId, this);
                return true;
            }
        }

        private boolean remove() {
            synchronized (DataLoaderManagerService.this.mServiceConnections) {
                if (DataLoaderManagerService.this.mServiceConnections.get(this.mId) != this) {
                    return false;
                }
                DataLoaderManagerService.this.mServiceConnections.remove(this.mId);
                return true;
            }
        }

        private void callListener(int i) {
            IDataLoaderStatusListener iDataLoaderStatusListener = this.mListener;
            if (iDataLoaderStatusListener != null) {
                try {
                    iDataLoaderStatusListener.onStatusChanged(this.mId, i);
                } catch (RemoteException unused) {
                }
            }
        }
    }
}
