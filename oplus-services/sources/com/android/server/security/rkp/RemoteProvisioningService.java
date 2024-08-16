package com.android.server.security.rkp;

import android.content.Context;
import android.os.Binder;
import android.os.OutcomeReceiver;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.security.rkp.IGetRegistrationCallback;
import android.security.rkp.IRemoteProvisioning;
import android.security.rkp.service.RegistrationProxy;
import android.util.Log;
import com.android.internal.util.DumpUtils;
import com.android.server.SystemService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RemoteProvisioningService extends SystemService {
    private static final Duration CREATE_REGISTRATION_TIMEOUT = Duration.ofSeconds(10);
    public static final String TAG = "RemoteProvisionSysSvc";
    private final RemoteProvisioningImpl mBinderImpl;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class RegistrationReceiver implements OutcomeReceiver<RegistrationProxy, Exception> {
        private final IGetRegistrationCallback mCallback;
        private final Executor mExecutor;

        RegistrationReceiver(Executor executor, IGetRegistrationCallback iGetRegistrationCallback) {
            this.mExecutor = executor;
            this.mCallback = iGetRegistrationCallback;
        }

        @Override // android.os.OutcomeReceiver
        public void onResult(RegistrationProxy registrationProxy) {
            try {
                this.mCallback.onSuccess(new RemoteProvisioningRegistration(registrationProxy, this.mExecutor));
            } catch (RemoteException e) {
                Log.e(RemoteProvisioningService.TAG, "Error calling success callback " + this.mCallback.asBinder().hashCode(), e);
            }
        }

        @Override // android.os.OutcomeReceiver
        public void onError(Exception exc) {
            try {
                this.mCallback.onError(exc.toString());
            } catch (RemoteException e) {
                Log.e(RemoteProvisioningService.TAG, "Error calling error callback " + this.mCallback.asBinder().hashCode(), e);
            }
        }
    }

    public RemoteProvisioningService(Context context) {
        super(context);
        this.mBinderImpl = new RemoteProvisioningImpl();
    }

    public void onStart() {
        publishBinderService("remote_provisioning", this.mBinderImpl);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class RemoteProvisioningImpl extends IRemoteProvisioning.Stub {
        private RemoteProvisioningImpl() {
        }

        public void getRegistration(String str, IGetRegistrationCallback iGetRegistrationCallback) throws RemoteException {
            int callingUidOrThrow = Binder.getCallingUidOrThrow();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            Executor mainExecutor = RemoteProvisioningService.this.getContext().getMainExecutor();
            try {
                Log.i(RemoteProvisioningService.TAG, "getRegistration(" + str + ")");
                RegistrationProxy.createAsync(RemoteProvisioningService.this.getContext(), callingUidOrThrow, str, RemoteProvisioningService.CREATE_REGISTRATION_TIMEOUT, mainExecutor, new RegistrationReceiver(mainExecutor, iGetRegistrationCallback));
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(RemoteProvisioningService.this.getContext(), RemoteProvisioningService.TAG, printWriter)) {
                new RemoteProvisioningShellCommand().dump(printWriter);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public int handleShellCommand(ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2, ParcelFileDescriptor parcelFileDescriptor3, String[] strArr) {
            return new RemoteProvisioningShellCommand().exec(this, parcelFileDescriptor.getFileDescriptor(), parcelFileDescriptor2.getFileDescriptor(), parcelFileDescriptor3.getFileDescriptor(), strArr);
        }
    }
}
