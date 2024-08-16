package com.android.server.devicepolicy;

import android.app.admin.DevicePolicyEventLogger;
import android.app.admin.StartInstallingUpdateCallback;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import com.android.server.devicepolicy.DevicePolicyManagerService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class UpdateInstaller {
    static final String TAG = "UpdateInstaller";
    private StartInstallingUpdateCallback mCallback;
    private DevicePolicyConstants mConstants;
    protected Context mContext;
    protected File mCopiedUpdateFile;
    private DevicePolicyManagerService.Injector mInjector;
    private ParcelFileDescriptor mUpdateFileDescriptor;

    public abstract void installUpdateInThread();

    /* JADX INFO: Access modifiers changed from: protected */
    public UpdateInstaller(Context context, ParcelFileDescriptor parcelFileDescriptor, StartInstallingUpdateCallback startInstallingUpdateCallback, DevicePolicyManagerService.Injector injector, DevicePolicyConstants devicePolicyConstants) {
        this.mContext = context;
        this.mCallback = startInstallingUpdateCallback;
        this.mUpdateFileDescriptor = parcelFileDescriptor;
        this.mInjector = injector;
        this.mConstants = devicePolicyConstants;
    }

    public void startInstallUpdate() {
        this.mCopiedUpdateFile = null;
        if (!isBatteryLevelSufficient()) {
            notifyCallbackOnError(5, "The battery level must be above " + this.mConstants.BATTERY_THRESHOLD_NOT_CHARGING + " while not charging or above " + this.mConstants.BATTERY_THRESHOLD_CHARGING + " while charging");
            return;
        }
        Thread thread = new Thread(new Runnable() { // from class: com.android.server.devicepolicy.UpdateInstaller$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                UpdateInstaller.this.lambda$startInstallUpdate$0();
            }
        });
        thread.setPriority(10);
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startInstallUpdate$0() {
        File copyUpdateFileToDataOtaPackageDir = copyUpdateFileToDataOtaPackageDir();
        this.mCopiedUpdateFile = copyUpdateFileToDataOtaPackageDir;
        if (copyUpdateFileToDataOtaPackageDir == null) {
            notifyCallbackOnError(1, "Error while copying file.");
        } else {
            installUpdateInThread();
        }
    }

    private boolean isBatteryLevelSufficient() {
        Intent registerReceiver = this.mContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        float calculateBatteryPercentage = calculateBatteryPercentage(registerReceiver);
        if (registerReceiver.getIntExtra("plugged", -1) > 0) {
            if (calculateBatteryPercentage >= this.mConstants.BATTERY_THRESHOLD_CHARGING) {
                return true;
            }
        } else if (calculateBatteryPercentage >= this.mConstants.BATTERY_THRESHOLD_NOT_CHARGING) {
            return true;
        }
        return false;
    }

    private float calculateBatteryPercentage(Intent intent) {
        return (intent.getIntExtra("level", -1) * 100) / intent.getIntExtra("scale", -1);
    }

    private File copyUpdateFileToDataOtaPackageDir() {
        try {
            File createNewFileWithPermissions = createNewFileWithPermissions();
            copyToFile(createNewFileWithPermissions);
            return createNewFileWithPermissions;
        } catch (IOException e) {
            Log.w(TAG, "Failed to copy update file to OTA directory", e);
            notifyCallbackOnError(1, Log.getStackTraceString(e));
            return null;
        }
    }

    private File createNewFileWithPermissions() throws IOException {
        File createTempFile = File.createTempFile("update", ".zip", new File(Environment.getDataDirectory() + "/ota_package"));
        FileUtils.setPermissions(createTempFile, 484, -1, -1);
        return createTempFile;
    }

    private void copyToFile(File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new ParcelFileDescriptor.AutoCloseInputStream(this.mUpdateFileDescriptor);
            try {
                FileUtils.copy(autoCloseInputStream, fileOutputStream);
                autoCloseInputStream.close();
                fileOutputStream.close();
            } finally {
            }
        } catch (Throwable th) {
            try {
                fileOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    void cleanupUpdateFile() {
        File file = this.mCopiedUpdateFile;
        if (file == null || !file.exists()) {
            return;
        }
        this.mCopiedUpdateFile.delete();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void notifyCallbackOnError(int i, String str) {
        cleanupUpdateFile();
        DevicePolicyEventLogger.createEvent(74).setInt(i).write();
        try {
            this.mCallback.onStartInstallingUpdateError(i, str);
        } catch (RemoteException e) {
            Log.d(TAG, "Error while calling callback", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void notifyCallbackOnSuccess() {
        cleanupUpdateFile();
        this.mInjector.powerManagerReboot("deviceowner");
    }
}
