package android.os;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IRecoverySystem;
import android.provider.oplus.Telephony;
import android.telephony.euicc.EuiccManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/* loaded from: classes.dex */
public class OplusRecoverySystem {
    private static final File LOG_FILE;
    private static final int OPEN_ESIM = 1;
    private static final int OPLUS_CHANGE_ESIM_STATUS = 26;
    private static final String OPLUS_TELEPHONY_DESCRIPTOR = "com.android.internal.telephony.IOplusTelephonyExt";
    private static final String PACKAGE_NAME_WIPING_EUICC_DATA_CALLBACK = "android";
    private static final File RECOVERY_DIR;
    private static final String TAG = "OplusRecoverySystem";
    private static final String TELEPHONY_SERVICE_NAME = "oplus_telephony_ext";
    private static final long WAIT_OPEN_ESIM_DELAY_TIME = 2000;

    static {
        File file = new File("/cache/recovery");
        RECOVERY_DIR = file;
        LOG_FILE = new File(file, "log");
    }

    private static String sanitizeArg(String arg) {
        return arg.replace((char) 0, '?').replace('\n', '?');
    }

    private static void bootCommand(Context context, String... args) throws IOException {
        LOG_FILE.delete();
        StringBuilder command = new StringBuilder();
        for (String arg : args) {
            if (!TextUtils.isEmpty(arg)) {
                command.append(arg);
                command.append("\n");
            }
        }
        String resetTime = "--resetTime=" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String resetPackage = "--resetPackage=" + SystemProperties.get("oplus.factory.reset.pkgname", "");
        command.append(resetTime);
        command.append("\n");
        command.append(resetPackage);
        command.append("\n");
        try {
            IBinder service = ServiceManager.getService("recovery");
            IRecoverySystem recoveryService = IRecoverySystem.Stub.asInterface(service);
            recoveryService.rebootRecoveryWithCommand(command.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        throw new IOException("Reboot failed (no permissions?)");
    }

    @Deprecated
    public static void clearBackupProperty() {
        Log.d(TAG, "clearBackupProperty!");
    }

    public static void rebootFormatUserData(Context context, boolean shutdown, String reason, boolean force, boolean wipeEuicc) throws IOException {
        Log.d(TAG, "rebootFormatUserData!");
        UserManager um = (UserManager) context.getSystemService(Telephony.Carriers.USER);
        if (!force && um.hasUserRestriction("no_factory_reset")) {
            throw new SecurityException("Wiping data is not allowed for this user.");
        }
        final ConditionVariable condition = new ConditionVariable();
        Intent intent = new Intent("android.intent.action.MASTER_CLEAR_NOTIFICATION");
        intent.addFlags(285212672);
        context.sendOrderedBroadcastAsUser(intent, UserHandle.SYSTEM, "android.permission.MASTER_CLEAR", new BroadcastReceiver() { // from class: android.os.OplusRecoverySystem.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent2) {
                condition.open();
            }
        }, null, 0, null, null);
        condition.block();
        if (wipeEuicc) {
            RecoverySystem.wipeEuiccData(context, "android");
        }
        String shutdownArg = null;
        if (shutdown) {
            shutdownArg = "--shutdown_after";
        }
        String reasonArg = null;
        if (!TextUtils.isEmpty(reason)) {
            reasonArg = "--reason=" + sanitizeArg(reason);
        }
        String localeArg = "--locale=" + Locale.getDefault().toLanguageTag();
        bootCommand(context, shutdownArg, "--format_data", reasonArg, localeArg);
    }

    public static void rebootFormatUserDataBackup(Context context, boolean shutdown, String reason, boolean force, boolean wipeEuicc) throws IOException {
        Log.d(TAG, "rebootFormatUserDataBackup!");
        EuiccManager euiccManager = (EuiccManager) context.getSystemService("euicc");
        euiccManager.isEnabled();
        if (!euiccManager.isEnabled() && wipeEuicc) {
            openEsimFromRemoteService(1);
        }
        UserManager um = (UserManager) context.getSystemService(Telephony.Carriers.USER);
        if (!force && um.hasUserRestriction("no_factory_reset")) {
            throw new SecurityException("Wiping data is not allowed for this user.");
        }
        final ConditionVariable condition = new ConditionVariable();
        Intent intent = new Intent("android.intent.action.MASTER_CLEAR_NOTIFICATION");
        intent.addFlags(285212672);
        context.sendOrderedBroadcastAsUser(intent, UserHandle.SYSTEM, "android.permission.MASTER_CLEAR", new BroadcastReceiver() { // from class: android.os.OplusRecoverySystem.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent2) {
                condition.open();
            }
        }, null, 0, null, null);
        condition.block();
        if (!euiccManager.isEnabled() && wipeEuicc) {
            try {
                Thread.currentThread();
                Thread.sleep(WAIT_OPEN_ESIM_DELAY_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (wipeEuicc) {
            RecoverySystem.wipeEuiccData(context, "android");
        }
        String shutdownArg = null;
        if (shutdown) {
            shutdownArg = "--shutdown_after";
        }
        String reasonArg = null;
        if (!TextUtils.isEmpty(reason)) {
            reasonArg = "--reason=" + sanitizeArg(reason);
        }
        String localeArg = "--locale=" + Locale.getDefault().toLanguageTag();
        bootCommand(context, shutdownArg, "--format_data_backup", reasonArg, localeArg);
    }

    private static void openEsimFromRemoteService(int newState) {
        IBinder mRemote = ServiceManager.getService("oplus_telephony_ext");
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        if (mRemote == null) {
            Log.d(TAG, "open remote service fail!");
            return;
        }
        try {
            try {
                data.writeInterfaceToken("com.android.internal.telephony.IOplusTelephonyExt");
                data.writeInt(newState);
                mRemote.transact(26, data, reply, 0);
                reply.readException();
            } catch (Exception e) {
                Log.d(TAG, "openEsimFromRemoteService ERROR !!! " + e);
            }
        } finally {
            data.recycle();
            reply.recycle();
        }
    }
}
