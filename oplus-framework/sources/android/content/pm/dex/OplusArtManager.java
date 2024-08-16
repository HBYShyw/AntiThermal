package android.content.pm.dex;

import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.dex.ISnapshotRuntimeProfileCallback;
import android.content.res.OplusThemeResources;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import libcore.io.Streams;

/* loaded from: classes.dex */
public class OplusArtManager {
    private static final String TAG = "OplusArtManager";

    public static boolean runSnapshotApplicationProfile(String str, String str2, String str3) {
        Log.d(TAG, "runSnapdshotApplicationProfile, callingPackage = " + str2 + "packageName = " + str);
        boolean equals = OplusThemeResources.FRAMEWORK_PACKAGE.equals(str);
        String str4 = null;
        IPackageManager asInterface = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
        if (!equals) {
            try {
                PackageInfo packageInfo = asInterface.getPackageInfo(str, 0L, 0);
                if (packageInfo == null) {
                    Log.d(TAG, "Snapshot Profile: Package not found " + str);
                    return false;
                }
                str4 = packageInfo.applicationInfo.getBaseCodePath();
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException : " + e.getMessage());
            }
        }
        SnapshotRuntimeProfileCallback snapshotRuntimeProfileCallback = new SnapshotRuntimeProfileCallback();
        try {
        } catch (Exception e2) {
            Log.e(TAG, "Snapshot Profile Exception : " + e2.getMessage());
        }
        if (asInterface.getArtManager().isRuntimeProfilingEnabled(equals ? 1 : 0, str2)) {
            asInterface.getArtManager().snapshotRuntimeProfile(equals ? 1 : 0, str, str4, snapshotRuntimeProfileCallback, str2);
            if (!snapshotRuntimeProfileCallback.waitTillDone()) {
                Log.e(TAG, "Error: Snapshot profile callback not called");
                return false;
            }
            try {
                ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new ParcelFileDescriptor.AutoCloseInputStream(snapshotRuntimeProfileCallback.mProfileReadFd);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(str3);
                    try {
                        Streams.copy(autoCloseInputStream, fileOutputStream);
                        fileOutputStream.close();
                        autoCloseInputStream.close();
                        return true;
                    } finally {
                    }
                } finally {
                }
            } catch (Exception e3) {
                Log.e(TAG, "Error when reading the profile fd: " + e3.getMessage());
                return false;
            }
        }
        Log.e(TAG, "Error: Runtime profiling is not enabled");
        return false;
    }

    /* loaded from: classes.dex */
    private static class SnapshotRuntimeProfileCallback extends ISnapshotRuntimeProfileCallback.Stub {
        private CountDownLatch mDoneSignal;
        private int mErrCode;
        private ParcelFileDescriptor mProfileReadFd;
        private boolean mSuccess;

        private SnapshotRuntimeProfileCallback() {
            this.mSuccess = false;
            this.mErrCode = -1;
            this.mProfileReadFd = null;
            this.mDoneSignal = new CountDownLatch(1);
        }

        public void onSuccess(ParcelFileDescriptor profileReadFd) {
            Log.i(OplusArtManager.TAG, "SnapshotRuntimeProfileCallback onSuccess");
            this.mSuccess = true;
            try {
                this.mProfileReadFd = profileReadFd.dup();
            } catch (IOException e) {
                Log.e(OplusArtManager.TAG, "SnapshotRuntimeProfileCallback onSuccess IOException : " + e.getMessage());
            }
            this.mDoneSignal.countDown();
        }

        public void onError(int errCode) {
            Log.i(OplusArtManager.TAG, "SnapshotRuntimeProfileCallback onError, errorCode = " + errCode);
            this.mSuccess = false;
            this.mErrCode = errCode;
            this.mDoneSignal.countDown();
        }

        boolean waitTillDone() {
            boolean done = false;
            try {
                done = this.mDoneSignal.await(10000000L, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                Log.e(OplusArtManager.TAG, "waitTillDone Exception = " + e.getMessage());
            }
            return done && this.mSuccess;
        }
    }
}
