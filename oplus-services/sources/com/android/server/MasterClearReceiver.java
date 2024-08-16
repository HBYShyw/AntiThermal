package com.android.server;

import android.R;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.RecoverySystem;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.internal.util.FunctionalUtils;
import com.android.server.utils.Slogf;
import java.io.IOException;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class MasterClearReceiver extends BroadcastReceiver {
    private static final String TAG = "MasterClear";
    private IMasterClearReceiverExt mMasterClearReceiverExt = (IMasterClearReceiverExt) ExtLoader.type(IMasterClearReceiverExt.class).create();
    private boolean mWipeEsims;
    private boolean mWipeExternalStorage;

    @Override // android.content.BroadcastReceiver
    public void onReceive(final Context context, Intent intent) {
        final String str;
        if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE") && !"google.com".equals(intent.getStringExtra("from"))) {
            Slog.w(TAG, "Ignoring master clear request -- not from trusted server.");
            return;
        }
        if ("android.intent.action.MASTER_CLEAR".equals(intent.getAction())) {
            Slog.w(TAG, "The request uses the deprecated Intent#ACTION_MASTER_CLEAR, Intent#ACTION_FACTORY_RESET should be used instead.");
        }
        if (intent.hasExtra("android.intent.extra.FORCE_MASTER_CLEAR")) {
            Slog.w(TAG, "The request uses the deprecated Intent#EXTRA_FORCE_MASTER_CLEAR, Intent#EXTRA_FORCE_FACTORY_RESET should be used instead.");
        }
        String string = context.getString(R.string.config_wwan_data_service_package);
        if ("android.intent.action.FACTORY_RESET".equals(intent.getAction()) && !TextUtils.isEmpty(string)) {
            Slog.i(TAG, "Re-directing intent to " + string);
            intent.setPackage(string).setComponent(null);
            context.sendBroadcastAsUser(intent, UserHandle.SYSTEM);
            return;
        }
        final boolean booleanExtra = intent.getBooleanExtra("shutdown", false);
        String stringExtra = intent.getStringExtra("android.intent.extra.REASON");
        if ("android.intent.action.MASTER_CLEAR".equals(intent.getAction())) {
            if ("MasterClearConfirm".equals(stringExtra)) {
                stringExtra = stringExtra + ",packageName=com.android.settings";
            } else {
                stringExtra = stringExtra + "," + intent.getStringExtra("android.intent.extra.REASON");
            }
        }
        if (intent.getStringExtra("PACKAGE_NAME") == null || "".equals(intent.getStringExtra("PACKAGE_NAME"))) {
            str = stringExtra;
        } else {
            str = stringExtra + ",packageName=" + intent.getStringExtra("PACKAGE_NAME");
        }
        this.mWipeExternalStorage = intent.getBooleanExtra("android.intent.extra.WIPE_EXTERNAL_STORAGE", false);
        this.mWipeEsims = intent.getBooleanExtra("com.android.internal.intent.extra.WIPE_ESIMS", false);
        final boolean z = intent.getBooleanExtra("android.intent.extra.FORCE_MASTER_CLEAR", false) || intent.getBooleanExtra("android.intent.extra.FORCE_FACTORY_RESET", false);
        final int sendingUserId = getSendingUserId();
        if (sendingUserId != 0 && !UserManager.isHeadlessSystemUserMode()) {
            Slogf.w(TAG, "ACTION_FACTORY_RESET received on a non-system user %d, WIPING THE USER!!", new Object[]{Integer.valueOf(sendingUserId)});
            if (((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.MasterClearReceiver$$ExternalSyntheticLambda1
                public final Object getOrThrow() {
                    Boolean lambda$onReceive$0;
                    lambda$onReceive$0 = MasterClearReceiver.this.lambda$onReceive$0(context, sendingUserId, str);
                    return lambda$onReceive$0;
                }
            })).booleanValue()) {
                return;
            }
            Slogf.e(TAG, "Failed to wipe user %d", new Object[]{Integer.valueOf(sendingUserId)});
            return;
        }
        Slog.w(TAG, "!!! FACTORY RESET !!!");
        Thread thread = new Thread("Reboot") { // from class: com.android.server.MasterClearReceiver.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    MasterClearReceiver.this.mMasterClearReceiverExt.handleFactoryReset(MasterClearReceiver.this.mWipeEsims);
                    Slog.i(MasterClearReceiver.TAG, "Calling RecoverySystem.rebootWipeUserData(context, shutdown=" + booleanExtra + ", reason=" + str + ", forceWipe=" + z + ", wipeEsims=" + MasterClearReceiver.this.mWipeEsims + ")");
                    RecoverySystem.rebootWipeUserData(context, booleanExtra, str, z, MasterClearReceiver.this.mWipeEsims);
                    Slog.wtf(MasterClearReceiver.TAG, "Still running after master clear?!");
                } catch (IOException e) {
                    Slog.e(MasterClearReceiver.TAG, "Can't perform master clear/factory reset", e);
                } catch (SecurityException e2) {
                    Slog.e(MasterClearReceiver.TAG, "Can't perform master clear/factory reset", e2);
                }
            }
        };
        if (this.mWipeExternalStorage) {
            Slog.i(TAG, "Wiping external storage on async task");
            new WipeDataTask(context, thread).execute(new Void[0]);
            return;
        }
        Slog.i(TAG, "NOT wiping external storage; starting thread " + thread.getName());
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$onReceive$0(Context context, int i, String str) throws Exception {
        return Boolean.valueOf(wipeUser(context, i, str));
    }

    private boolean wipeUser(Context context, int i, String str) {
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        if (!UserManager.isRemoveResultSuccessful(userManager.removeUserWhenPossible(UserHandle.of(i), false))) {
            Slogf.e(TAG, "Can't remove user %d", new Object[]{Integer.valueOf(i)});
            return false;
        }
        if (getCurrentForegroundUserId() == i) {
            try {
                if (!ActivityManager.getService().switchUser(0)) {
                    Slogf.w(TAG, "Can't switch from current user %d, user will get removed when it is stopped.", new Object[]{Integer.valueOf(i)});
                }
            } catch (RemoteException unused) {
                Slogf.w(TAG, "Can't switch from current user %d, user will get removed when it is stopped.", new Object[]{Integer.valueOf(i)});
            }
        }
        if (userManager.isManagedProfile(i)) {
            sendWipeProfileNotification(context, str);
        }
        return true;
    }

    private void sendWipeProfileNotification(Context context, String str) {
        ((NotificationManager) context.getSystemService(NotificationManager.class)).notify(1001, new Notification.Builder(context, SystemNotificationChannels.DEVICE_ADMIN).setSmallIcon(R.drawable.stat_sys_warning).setContentTitle(getWorkProfileDeletedTitle(context)).setContentText(str).setColor(context.getColor(R.color.system_notification_accent_color)).setStyle(new Notification.BigTextStyle().bigText(str)).build());
    }

    private String getWorkProfileDeletedTitle(final Context context) {
        return ((DevicePolicyManager) context.getSystemService(DevicePolicyManager.class)).getResources().getString("Core.WORK_PROFILE_DELETED_TITLE", new Supplier() { // from class: com.android.server.MasterClearReceiver$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                String string;
                string = context.getString(17041953);
                return string;
            }
        });
    }

    private int getCurrentForegroundUserId() {
        try {
            return ActivityManager.getCurrentUser();
        } catch (Exception e) {
            Slogf.e(TAG, "Can't get current user", e);
            return -10000;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class WipeDataTask extends AsyncTask<Void, Void, Void> {
        private final Thread mChainedTask;
        private final Context mContext;
        private final ProgressDialog mProgressDialog;

        public WipeDataTask(Context context, Thread thread) {
            this.mContext = context;
            this.mChainedTask = thread;
            this.mProgressDialog = new ProgressDialog(context, R.style.Theme.DeviceDefault.Settings.Dialog.Alert);
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            this.mProgressDialog.setIndeterminate(true);
            this.mProgressDialog.getWindow().setType(2003);
            this.mProgressDialog.setMessage(this.mContext.getText(R.string.whichGiveAccessToApplicationLabel));
            this.mProgressDialog.show();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            Slog.w(MasterClearReceiver.TAG, "Wiping adoptable disks");
            if (!MasterClearReceiver.this.mWipeExternalStorage) {
                return null;
            }
            ((StorageManager) this.mContext.getSystemService("storage")).wipeAdoptableDisks();
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void r1) {
            this.mProgressDialog.dismiss();
            this.mChainedTask.start();
        }
    }
}
