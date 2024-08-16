package com.android.server.devicepolicy;

import android.R;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.security.Credentials;
import android.security.KeyChain;
import android.util.PluralsMessageFormatter;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.server.am.AssistDataRequester;
import com.android.server.devicepolicy.DevicePolicyManagerService;
import com.android.server.utils.Slogf;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CertificateMonitor {
    protected static final int MONITORING_CERT_NOTIFICATION_ID = 33;
    private final Handler mHandler;
    private final DevicePolicyManagerService.Injector mInjector;
    private final BroadcastReceiver mRootCaReceiver;
    private final DevicePolicyManagerService mService;

    public CertificateMonitor(DevicePolicyManagerService devicePolicyManagerService, DevicePolicyManagerService.Injector injector, Handler handler) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.devicepolicy.CertificateMonitor.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                CertificateMonitor.this.updateInstalledCertificates(UserHandle.of(intent.getIntExtra("android.intent.extra.user_handle", getSendingUserId())));
            }
        };
        this.mRootCaReceiver = broadcastReceiver;
        this.mService = devicePolicyManagerService;
        this.mInjector = injector;
        this.mHandler = handler;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_STARTED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        intentFilter.addAction("android.security.action.TRUST_STORE_CHANGED");
        intentFilter.setPriority(1000);
        injector.mContext.registerReceiverAsUser(broadcastReceiver, UserHandle.ALL, intentFilter, null, handler);
    }

    public String installCaCert(UserHandle userHandle, byte[] bArr) {
        try {
            byte[] convertToPem = Credentials.convertToPem(new Certificate[]{parseCert(bArr)});
            try {
                KeyChain.KeyChainConnection keyChainBindAsUser = this.mInjector.keyChainBindAsUser(userHandle);
                try {
                    String installCaCertificate = keyChainBindAsUser.getService().installCaCertificate(convertToPem);
                    keyChainBindAsUser.close();
                    return installCaCertificate;
                } catch (Throwable th) {
                    if (keyChainBindAsUser != null) {
                        try {
                            keyChainBindAsUser.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (RemoteException e) {
                Slogf.e("DevicePolicyManager", "installCaCertsToKeyChain(): ", e);
                return null;
            } catch (InterruptedException e2) {
                Slogf.w("DevicePolicyManager", "installCaCertsToKeyChain(): ", e2);
                Thread.currentThread().interrupt();
                return null;
            }
        } catch (IOException | CertificateException e3) {
            Slogf.e("DevicePolicyManager", "Problem converting cert", e3);
            return null;
        }
    }

    public void uninstallCaCerts(UserHandle userHandle, String[] strArr) {
        try {
            KeyChain.KeyChainConnection keyChainBindAsUser = this.mInjector.keyChainBindAsUser(userHandle);
            for (String str : strArr) {
                try {
                    keyChainBindAsUser.getService().deleteCaCertificate(str);
                } catch (Throwable th) {
                    if (keyChainBindAsUser != null) {
                        try {
                            keyChainBindAsUser.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            if (keyChainBindAsUser != null) {
                keyChainBindAsUser.close();
            }
        } catch (RemoteException e) {
            Slogf.e("DevicePolicyManager", "from CaCertUninstaller: ", e);
        } catch (InterruptedException e2) {
            Slogf.w("DevicePolicyManager", "CaCertUninstaller: ", e2);
            Thread.currentThread().interrupt();
        }
    }

    private List<String> getInstalledCaCertificates(UserHandle userHandle) throws RemoteException, RuntimeException {
        try {
            KeyChain.KeyChainConnection keyChainBindAsUser = this.mInjector.keyChainBindAsUser(userHandle);
            try {
                List<String> list = keyChainBindAsUser.getService().getUserCaAliases().getList();
                keyChainBindAsUser.close();
                return list;
            } finally {
            }
        } catch (AssertionError e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e2) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCertificateApprovalsChanged$0(int i) {
        updateInstalledCertificates(UserHandle.of(i));
    }

    public void onCertificateApprovalsChanged(final int i) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.devicepolicy.CertificateMonitor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CertificateMonitor.this.lambda$onCertificateApprovalsChanged$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInstalledCertificates(UserHandle userHandle) {
        int identifier = userHandle.getIdentifier();
        if (this.mInjector.getUserManager().isUserUnlocked(identifier)) {
            try {
                List<String> installedCaCertificates = getInstalledCaCertificates(userHandle);
                this.mService.onInstalledCertificatesChanged(userHandle, installedCaCertificates);
                int size = installedCaCertificates.size() - this.mService.getAcceptedCaCertificates(userHandle).size();
                if (size != 0) {
                    this.mInjector.getNotificationManager().notifyAsUser("DevicePolicyManager", 33, buildNotification(userHandle, size), userHandle);
                } else {
                    this.mInjector.getNotificationManager().cancelAsUser("DevicePolicyManager", 33, userHandle);
                }
            } catch (RemoteException | RuntimeException e) {
                Slogf.e("DevicePolicyManager", e, "Could not retrieve certificates from KeyChain service for user %d", new Object[]{Integer.valueOf(identifier)});
            }
        }
    }

    private Notification buildNotification(UserHandle userHandle, int i) {
        String string;
        String str;
        try {
            Context createContextAsUser = this.mInjector.createContextAsUser(userHandle);
            Resources resources = this.mInjector.getResources();
            int identifier = userHandle.getIdentifier();
            ComponentName lambda$isProfileOwner$69 = this.mService.lambda$isProfileOwner$69(userHandle.getIdentifier());
            int i2 = R.drawable.tab_pressed_holo;
            if (lambda$isProfileOwner$69 != null) {
                str = resources.getString(17041669, this.mService.getProfileOwnerName(userHandle.getIdentifier()));
                identifier = this.mService.getProfileParentId(userHandle.getIdentifier());
            } else {
                if (this.mService.getDeviceOwnerUserId() == userHandle.getIdentifier()) {
                    string = resources.getString(17041669, this.mService.getDeviceOwnerName());
                } else {
                    string = resources.getString(17041668);
                    i2 = R.drawable.stat_sys_warning;
                }
                str = string;
            }
            int i3 = i2;
            Intent intent = new Intent("com.android.settings.MONITORING_CERT_INFO");
            intent.setFlags(268468224);
            intent.putExtra("android.settings.extra.number_of_certificates", i);
            intent.putExtra("android.intent.extra.USER_ID", userHandle.getIdentifier());
            ActivityInfo resolveActivityInfo = intent.resolveActivityInfo(this.mInjector.getPackageManager(), AudioDevice.OUT_FM);
            if (resolveActivityInfo != null) {
                intent.setComponent(resolveActivityInfo.getComponentName());
            }
            PendingIntent pendingIntentGetActivityAsUser = this.mInjector.pendingIntentGetActivityAsUser(createContextAsUser, 0, intent, AudioFormat.DTS_HD, null, UserHandle.of(identifier));
            HashMap hashMap = new HashMap();
            hashMap.put(AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, Integer.valueOf(i));
            return new Notification.Builder(createContextAsUser, SystemNotificationChannels.SECURITY).setSmallIcon(i3).setContentTitle(PluralsMessageFormatter.format(resources, hashMap, 17041670)).setContentText(str).setContentIntent(pendingIntentGetActivityAsUser).setShowWhen(false).setColor(R.color.system_notification_accent_color).build();
        } catch (PackageManager.NameNotFoundException e) {
            Slogf.e("DevicePolicyManager", e, "Create context as %s failed", new Object[]{userHandle});
            return null;
        }
    }

    private static X509Certificate parseCert(byte[] bArr) throws CertificateException {
        return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(bArr));
    }
}
