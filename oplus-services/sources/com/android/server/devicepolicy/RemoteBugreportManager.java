package com.android.server.devicepolicy;

import android.R;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Icon;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Pair;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.server.devicepolicy.DevicePolicyManagerService;
import com.android.server.utils.Slogf;
import java.io.FileNotFoundException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class RemoteBugreportManager {
    static final String BUGREPORT_MIMETYPE = "application/vnd.android.bugreport";
    private static final String CTL_STOP = "ctl.stop";
    private static final int NOTIFICATION_ID = 678432343;
    private static final String REMOTE_BUGREPORT_SERVICE = "bugreportd";
    private static final long REMOTE_BUGREPORT_TIMEOUT_MILLIS = 600000;
    private final Context mContext;
    private final Handler mHandler;
    private final DevicePolicyManagerService.Injector mInjector;
    private final DevicePolicyManagerService mService;
    private final SecureRandom mRng = new SecureRandom();
    private final AtomicLong mRemoteBugreportNonce = new AtomicLong();
    private final AtomicBoolean mRemoteBugreportServiceIsActive = new AtomicBoolean();
    private final AtomicBoolean mRemoteBugreportSharingAccepted = new AtomicBoolean();
    private final Runnable mRemoteBugreportTimeoutRunnable = new Runnable() { // from class: com.android.server.devicepolicy.RemoteBugreportManager$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            RemoteBugreportManager.this.lambda$new$0();
        }
    };
    private final BroadcastReceiver mRemoteBugreportFinishedReceiver = new BroadcastReceiver() { // from class: com.android.server.devicepolicy.RemoteBugreportManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.REMOTE_BUGREPORT_DISPATCH".equals(intent.getAction()) && RemoteBugreportManager.this.mRemoteBugreportServiceIsActive.get()) {
                RemoteBugreportManager.this.onBugreportFinished(intent);
            }
        }
    };
    private final BroadcastReceiver mRemoteBugreportConsentReceiver = new BroadcastReceiver() { // from class: com.android.server.devicepolicy.RemoteBugreportManager.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            RemoteBugreportManager.this.mInjector.getNotificationManager().cancel("DevicePolicyManager", RemoteBugreportManager.NOTIFICATION_ID);
            if ("com.android.server.action.REMOTE_BUGREPORT_SHARING_ACCEPTED".equals(action)) {
                RemoteBugreportManager.this.onBugreportSharingAccepted();
            } else if ("com.android.server.action.REMOTE_BUGREPORT_SHARING_DECLINED".equals(action)) {
                RemoteBugreportManager.this.onBugreportSharingDeclined();
            }
            RemoteBugreportManager.this.mContext.unregisterReceiver(RemoteBugreportManager.this.mRemoteBugreportConsentReceiver);
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface RemoteBugreportNotificationType {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.mRemoteBugreportServiceIsActive.get()) {
            onBugreportFailed();
        }
    }

    public RemoteBugreportManager(DevicePolicyManagerService devicePolicyManagerService, DevicePolicyManagerService.Injector injector) {
        this.mService = devicePolicyManagerService;
        this.mInjector = injector;
        this.mContext = devicePolicyManagerService.mContext;
        this.mHandler = devicePolicyManagerService.mHandler;
    }

    private Notification buildNotification(int i) {
        Intent intent = new Intent("android.settings.SHOW_REMOTE_BUGREPORT_DIALOG");
        intent.addFlags(268468224);
        intent.putExtra("android.app.extra.bugreport_notification_type", i);
        ActivityInfo resolveActivityInfo = intent.resolveActivityInfo(this.mContext.getPackageManager(), AudioDevice.OUT_FM);
        if (resolveActivityInfo != null) {
            intent.setComponent(resolveActivityInfo.getComponentName());
        } else {
            Slogf.wtf("DevicePolicyManager", "Failed to resolve intent for remote bugreport dialog");
        }
        Notification.Builder extend = new Notification.Builder(this.mContext, SystemNotificationChannels.DEVICE_ADMIN).setSmallIcon(R.drawable.sym_keyboard_num5).setOngoing(true).setLocalOnly(true).setContentIntent(PendingIntent.getActivityAsUser(this.mContext, i, intent, 67108864, null, UserHandle.CURRENT)).setColor(this.mContext.getColor(R.color.system_notification_accent_color)).extend(new Notification.TvExtender());
        if (i == 2) {
            extend.setContentTitle(this.mContext.getString(17041621)).setProgress(0, 0, true);
        } else if (i == 1) {
            extend.setContentTitle(this.mContext.getString(17041737)).setProgress(0, 0, true);
        } else if (i == 3) {
            PendingIntent broadcast = PendingIntent.getBroadcast(this.mContext, NOTIFICATION_ID, new Intent("com.android.server.action.REMOTE_BUGREPORT_SHARING_ACCEPTED"), AudioFormat.AAC_ADIF);
            extend.addAction(new Notification.Action.Builder((Icon) null, this.mContext.getString(R.string.face_acquired_too_dark), PendingIntent.getBroadcast(this.mContext, NOTIFICATION_ID, new Intent("com.android.server.action.REMOTE_BUGREPORT_SHARING_DECLINED"), AudioFormat.AAC_ADIF)).build()).addAction(new Notification.Action.Builder((Icon) null, this.mContext.getString(17041616), broadcast).build()).setContentTitle(this.mContext.getString(17041618)).setContentText(this.mContext.getString(17041617)).setStyle(new Notification.BigTextStyle().bigText(this.mContext.getString(17041617)));
        }
        return extend.build();
    }

    public boolean requestBugreport() {
        long nextLong;
        if (this.mRemoteBugreportServiceIsActive.get() || this.mService.getDeviceOwnerRemoteBugreportUriAndHash() != null) {
            Slogf.d("DevicePolicyManager", "Remote bugreport wasn't started because there's already one running");
            return false;
        }
        long binderClearCallingIdentity = this.mInjector.binderClearCallingIdentity();
        do {
            try {
                nextLong = this.mRng.nextLong();
            } catch (RemoteException e) {
                Slogf.e("DevicePolicyManager", "Failed to make remote calls to start bugreportremote service", e);
                return false;
            } finally {
                this.mInjector.binderRestoreCallingIdentity(binderClearCallingIdentity);
            }
        } while (nextLong == 0);
        this.mInjector.getIActivityManager().requestRemoteBugReport(nextLong);
        this.mRemoteBugreportNonce.set(nextLong);
        this.mRemoteBugreportServiceIsActive.set(true);
        this.mRemoteBugreportSharingAccepted.set(false);
        registerRemoteBugreportReceivers();
        this.mInjector.getNotificationManager().notifyAsUser("DevicePolicyManager", NOTIFICATION_ID, buildNotification(1), UserHandle.ALL);
        this.mHandler.postDelayed(this.mRemoteBugreportTimeoutRunnable, 600000L);
        return true;
    }

    private void registerRemoteBugreportReceivers() {
        try {
            this.mContext.registerReceiver(this.mRemoteBugreportFinishedReceiver, new IntentFilter("android.intent.action.REMOTE_BUGREPORT_DISPATCH", BUGREPORT_MIMETYPE), 2);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            Slogf.w("DevicePolicyManager", e, "Failed to set type %s", new Object[]{BUGREPORT_MIMETYPE});
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.server.action.REMOTE_BUGREPORT_SHARING_DECLINED");
        intentFilter.addAction("com.android.server.action.REMOTE_BUGREPORT_SHARING_ACCEPTED");
        this.mContext.registerReceiver(this.mRemoteBugreportConsentReceiver, intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBugreportFinished(Intent intent) {
        long longExtra = intent.getLongExtra("android.intent.extra.REMOTE_BUGREPORT_NONCE", 0L);
        if (longExtra == 0 || this.mRemoteBugreportNonce.get() != longExtra) {
            Slogf.w("DevicePolicyManager", "Invalid nonce provided, ignoring " + longExtra);
            return;
        }
        this.mHandler.removeCallbacks(this.mRemoteBugreportTimeoutRunnable);
        this.mRemoteBugreportServiceIsActive.set(false);
        Uri data = intent.getData();
        String uri = data != null ? data.toString() : null;
        String stringExtra = intent.getStringExtra("android.intent.extra.REMOTE_BUGREPORT_HASH");
        if (this.mRemoteBugreportSharingAccepted.get()) {
            shareBugreportWithDeviceOwnerIfExists(uri, stringExtra);
            this.mInjector.getNotificationManager().cancel("DevicePolicyManager", NOTIFICATION_ID);
        } else {
            this.mService.setDeviceOwnerRemoteBugreportUriAndHash(uri, stringExtra);
            this.mInjector.getNotificationManager().notifyAsUser("DevicePolicyManager", NOTIFICATION_ID, buildNotification(3), UserHandle.ALL);
        }
        this.mContext.unregisterReceiver(this.mRemoteBugreportFinishedReceiver);
    }

    private void onBugreportFailed() {
        this.mRemoteBugreportServiceIsActive.set(false);
        this.mInjector.systemPropertiesSet(CTL_STOP, REMOTE_BUGREPORT_SERVICE);
        this.mRemoteBugreportSharingAccepted.set(false);
        this.mService.setDeviceOwnerRemoteBugreportUriAndHash(null, null);
        this.mInjector.getNotificationManager().cancel("DevicePolicyManager", NOTIFICATION_ID);
        Bundle bundle = new Bundle();
        bundle.putInt("android.app.extra.BUGREPORT_FAILURE_REASON", 0);
        this.mService.sendDeviceOwnerCommand("android.app.action.BUGREPORT_FAILED", bundle);
        this.mContext.unregisterReceiver(this.mRemoteBugreportConsentReceiver);
        this.mContext.unregisterReceiver(this.mRemoteBugreportFinishedReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBugreportSharingAccepted() {
        this.mRemoteBugreportSharingAccepted.set(true);
        Pair<String, String> deviceOwnerRemoteBugreportUriAndHash = this.mService.getDeviceOwnerRemoteBugreportUriAndHash();
        if (deviceOwnerRemoteBugreportUriAndHash != null) {
            shareBugreportWithDeviceOwnerIfExists((String) deviceOwnerRemoteBugreportUriAndHash.first, (String) deviceOwnerRemoteBugreportUriAndHash.second);
        } else if (this.mRemoteBugreportServiceIsActive.get()) {
            this.mInjector.getNotificationManager().notifyAsUser("DevicePolicyManager", NOTIFICATION_ID, buildNotification(2), UserHandle.ALL);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBugreportSharingDeclined() {
        if (this.mRemoteBugreportServiceIsActive.get()) {
            this.mInjector.systemPropertiesSet(CTL_STOP, REMOTE_BUGREPORT_SERVICE);
            this.mRemoteBugreportServiceIsActive.set(false);
            this.mHandler.removeCallbacks(this.mRemoteBugreportTimeoutRunnable);
            this.mContext.unregisterReceiver(this.mRemoteBugreportFinishedReceiver);
        }
        this.mRemoteBugreportSharingAccepted.set(false);
        this.mService.setDeviceOwnerRemoteBugreportUriAndHash(null, null);
        this.mService.sendDeviceOwnerCommand("android.app.action.BUGREPORT_SHARING_DECLINED", null);
    }

    private void shareBugreportWithDeviceOwnerIfExists(String str, String str2) {
        try {
            try {
            } catch (FileNotFoundException unused) {
                Bundle bundle = new Bundle();
                bundle.putInt("android.app.extra.BUGREPORT_FAILURE_REASON", 1);
                this.mService.sendDeviceOwnerCommand("android.app.action.BUGREPORT_FAILED", bundle);
            }
            if (str == null) {
                throw new FileNotFoundException();
            }
            this.mService.sendBugreportToDeviceOwner(Uri.parse(str), str2);
        } finally {
            this.mRemoteBugreportSharingAccepted.set(false);
            this.mService.setDeviceOwnerRemoteBugreportUriAndHash(null, null);
        }
    }

    public void checkForPendingBugreportAfterBoot() {
        if (this.mService.getDeviceOwnerRemoteBugreportUriAndHash() == null) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.server.action.REMOTE_BUGREPORT_SHARING_DECLINED");
        intentFilter.addAction("com.android.server.action.REMOTE_BUGREPORT_SHARING_ACCEPTED");
        this.mContext.registerReceiver(this.mRemoteBugreportConsentReceiver, intentFilter);
        this.mInjector.getNotificationManager().notifyAsUser("DevicePolicyManager", NOTIFICATION_ID, buildNotification(3), UserHandle.ALL);
    }
}
