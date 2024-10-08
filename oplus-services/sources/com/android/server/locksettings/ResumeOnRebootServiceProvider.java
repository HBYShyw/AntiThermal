package com.android.server.locksettings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelableException;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.service.resumeonreboot.IResumeOnRebootService;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.BackgroundThread;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ResumeOnRebootServiceProvider {
    static final String PROP_ROR_PROVIDER_PACKAGE = "persist.sys.resume_on_reboot_provider_package";
    private static final String PROVIDER_PACKAGE = DeviceConfig.getString("ota", "resume_on_reboot_service_package", "");
    private static final String PROVIDER_REQUIRED_PERMISSION = "android.permission.BIND_RESUME_ON_REBOOT_SERVICE";
    private static final String TAG = "ResumeOnRebootServiceProvider";
    private final Context mContext;
    private final PackageManager mPackageManager;

    public ResumeOnRebootServiceProvider(Context context) {
        this(context, context.getPackageManager());
    }

    @VisibleForTesting
    public ResumeOnRebootServiceProvider(Context context, PackageManager packageManager) {
        this.mContext = context;
        this.mPackageManager = packageManager;
    }

    private ServiceInfo resolveService() {
        int i;
        Intent intent = new Intent();
        intent.setAction("android.service.resumeonreboot.ResumeOnRebootService");
        String str = SystemProperties.get(PROP_ROR_PROVIDER_PACKAGE, "");
        if (!str.isEmpty()) {
            Slog.i(TAG, "Using test app: " + str);
            intent.setPackage(str);
            i = 4;
        } else {
            String str2 = PROVIDER_PACKAGE;
            if (str2 != null && !str2.equals("")) {
                intent.setPackage(str2);
            }
            i = 1048580;
        }
        for (ResolveInfo resolveInfo : this.mPackageManager.queryIntentServices(intent, i)) {
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (serviceInfo != null && PROVIDER_REQUIRED_PERMISSION.equals(serviceInfo.permission)) {
                return resolveInfo.serviceInfo;
            }
        }
        return null;
    }

    public ResumeOnRebootServiceConnection getServiceConnection() {
        ServiceInfo resolveService = resolveService();
        if (resolveService == null) {
            return null;
        }
        return new ResumeOnRebootServiceConnection(this.mContext, resolveService.getComponentName());
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ResumeOnRebootServiceConnection {
        private static final String TAG = "ResumeOnRebootServiceConnection";
        private IResumeOnRebootService mBinder;
        private final ComponentName mComponentName;
        private final Context mContext;
        ServiceConnection mServiceConnection;

        private ResumeOnRebootServiceConnection(Context context, ComponentName componentName) {
            this.mContext = context;
            this.mComponentName = componentName;
        }

        public void unbindService() {
            ServiceConnection serviceConnection = this.mServiceConnection;
            if (serviceConnection != null) {
                this.mContext.unbindService(serviceConnection);
            }
            this.mBinder = null;
        }

        public void bindToService(long j) throws RemoteException, TimeoutException {
            IResumeOnRebootService iResumeOnRebootService = this.mBinder;
            if (iResumeOnRebootService == null || !iResumeOnRebootService.asBinder().isBinderAlive()) {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                Intent intent = new Intent();
                intent.setComponent(this.mComponentName);
                ServiceConnection serviceConnection = new ServiceConnection() { // from class: com.android.server.locksettings.ResumeOnRebootServiceProvider.ResumeOnRebootServiceConnection.1
                    @Override // android.content.ServiceConnection
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        ResumeOnRebootServiceConnection.this.mBinder = IResumeOnRebootService.Stub.asInterface(iBinder);
                        countDownLatch.countDown();
                    }

                    @Override // android.content.ServiceConnection
                    public void onServiceDisconnected(ComponentName componentName) {
                        ResumeOnRebootServiceConnection.this.mBinder = null;
                    }
                };
                this.mServiceConnection = serviceConnection;
                if (!this.mContext.bindServiceAsUser(intent, serviceConnection, 67108865, BackgroundThread.getHandler(), UserHandle.SYSTEM)) {
                    Slog.e(TAG, "Binding: " + this.mComponentName + " u" + UserHandle.SYSTEM + " failed.");
                    return;
                }
                waitForLatch(countDownLatch, "serviceConnection", j);
            }
        }

        public byte[] wrapBlob(byte[] bArr, long j, long j2) throws RemoteException, TimeoutException, IOException {
            IResumeOnRebootService iResumeOnRebootService = this.mBinder;
            if (iResumeOnRebootService == null || !iResumeOnRebootService.asBinder().isBinderAlive()) {
                throw new RemoteException("Service not bound");
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            ResumeOnRebootServiceCallback resumeOnRebootServiceCallback = new ResumeOnRebootServiceCallback(countDownLatch);
            this.mBinder.wrapSecret(bArr, j, new RemoteCallback(resumeOnRebootServiceCallback));
            waitForLatch(countDownLatch, "wrapSecret", j2);
            if (resumeOnRebootServiceCallback.getResult().containsKey("exception_key")) {
                throwTypedException((ParcelableException) resumeOnRebootServiceCallback.getResult().getParcelable("exception_key", ParcelableException.class));
            }
            return resumeOnRebootServiceCallback.mResult.getByteArray("wrapped_blob_key");
        }

        public byte[] unwrap(byte[] bArr, long j) throws RemoteException, TimeoutException, IOException {
            IResumeOnRebootService iResumeOnRebootService = this.mBinder;
            if (iResumeOnRebootService == null || !iResumeOnRebootService.asBinder().isBinderAlive()) {
                throw new RemoteException("Service not bound");
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            ResumeOnRebootServiceCallback resumeOnRebootServiceCallback = new ResumeOnRebootServiceCallback(countDownLatch);
            this.mBinder.unwrap(bArr, new RemoteCallback(resumeOnRebootServiceCallback));
            waitForLatch(countDownLatch, "unWrapSecret", j);
            if (resumeOnRebootServiceCallback.getResult().containsKey("exception_key")) {
                throwTypedException((ParcelableException) resumeOnRebootServiceCallback.getResult().getParcelable("exception_key", ParcelableException.class));
            }
            return resumeOnRebootServiceCallback.getResult().getByteArray("unrwapped_blob_key");
        }

        private void throwTypedException(ParcelableException parcelableException) throws IOException, RemoteException {
            if (parcelableException != null && (parcelableException.getCause() instanceof IOException)) {
                parcelableException.maybeRethrow(IOException.class);
                return;
            }
            throw new RemoteException("ResumeOnRebootServiceConnection wrap/unwrap failed", parcelableException, true, true);
        }

        private void waitForLatch(CountDownLatch countDownLatch, String str, long j) throws RemoteException, TimeoutException {
            try {
                if (countDownLatch.await(j, TimeUnit.SECONDS)) {
                    return;
                }
                throw new TimeoutException("Latch wait for " + str + " elapsed");
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
                throw new RemoteException("Latch wait for " + str + " interrupted");
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class ResumeOnRebootServiceCallback implements RemoteCallback.OnResultListener {
        private Bundle mResult;
        private final CountDownLatch mResultLatch;

        private ResumeOnRebootServiceCallback(CountDownLatch countDownLatch) {
            this.mResultLatch = countDownLatch;
        }

        public void onResult(Bundle bundle) {
            this.mResult = bundle;
            this.mResultLatch.countDown();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Bundle getResult() {
            return this.mResult;
        }
    }
}
