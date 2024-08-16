package com.android.server.storage;

import android.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManagerInternal;
import android.os.Binder;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.ShellCommand;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.provider.DeviceConfig;
import android.util.ArrayMap;
import android.util.DataUnit;
import android.util.Slog;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.EventLogTags;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.job.controllers.JobStatus;
import com.android.server.utils.PriorityDump;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DeviceStorageMonitorService extends SystemService {
    private static final long DEFAULT_CHECK_INTERVAL = 30000;
    public static final String EXTRA_SEQUENCE = "seq";
    private static final long HIGH_CHECK_INTERVAL = 36000000;
    private static final long LOW_CHECK_INTERVAL = 60000;
    private static final int MSG_CHECK_HIGH = 2;
    private static final int MSG_CHECK_LOW = 1;
    static final int OPTION_FORCE_UPDATE = 1;
    static final String SERVICE = "devicestoragemonitor";
    private static final String TAG = "DeviceStorageMonitorService";
    private static final String TV_NOTIFICATION_CHANNEL_ID = "devicestoragemonitor.tv";
    private DeviceStorageMonitorServiceWrapper dsmsWrapper;
    private CacheFileDeletedObserver mCacheFileDeletedObserver;
    public IDeviceStorageMonitorServiceExt mDSSext;
    private volatile int mForceLevel;
    private final Handler mHandler;
    private final HandlerThread mHandlerThread;
    private final DeviceStorageMonitorInternal mLocalService;
    private NotificationManager mNotifManager;
    private final Binder mRemoteService;
    private final AtomicInteger mSeq;
    private final ArrayMap<UUID, State> mStates;
    private static final long DEFAULT_LOG_DELTA_BYTES = DataUnit.MEBIBYTES.toBytes(64);
    private static final long BOOT_IMAGE_STORAGE_REQUIREMENT = DataUnit.MEBIBYTES.toBytes(250);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class State {
        private static final int LEVEL_FULL = 2;
        private static final int LEVEL_LOW = 1;
        private static final int LEVEL_NORMAL = 0;
        private static final int LEVEL_UNKNOWN = -1;
        public long lastUsableBytes;
        public int level;

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isEntering(int i, int i2, int i3) {
            return i3 >= i && (i2 < i || i2 == -1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isLeaving(int i, int i2, int i3) {
            return i3 < i && (i2 >= i || i2 == -1);
        }

        private State() {
            this.level = 0;
            this.lastUsableBytes = JobStatus.NO_LATEST_RUNTIME;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String levelToString(int i) {
            return i != -1 ? i != 0 ? i != 1 ? i != 2 ? Integer.toString(i) : "FULL" : "LOW" : PriorityDump.PRIORITY_ARG_NORMAL : "UNKNOWN";
        }
    }

    private State findOrCreateState(UUID uuid) {
        State state = this.mStates.get(uuid);
        if (state != null) {
            return state;
        }
        State state2 = new State();
        this.mStates.put(uuid, state2);
        return state2;
    }

    private void checkLow() {
        int i;
        StorageManager storageManager = (StorageManager) getContext().getSystemService(StorageManager.class);
        int i2 = this.mSeq.get();
        for (VolumeInfo volumeInfo : storageManager.getWritablePrivateVolumes()) {
            File path = volumeInfo.getPath();
            long storageFullBytes = storageManager.getStorageFullBytes(path);
            long storageLowBytes = storageManager.getStorageLowBytes(path);
            if (path.getUsableSpace() < (3 * storageLowBytes) / 2) {
                try {
                    ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).freeStorage(volumeInfo.getFsUuid(), storageLowBytes * 2, 0);
                } catch (IOException e) {
                    Slog.w(TAG, e);
                }
            }
            UUID convert = StorageManager.convert(volumeInfo.getFsUuid());
            State findOrCreateState = findOrCreateState(convert);
            long totalSpace = path.getTotalSpace();
            long usableSpace = path.getUsableSpace();
            int i3 = findOrCreateState.level;
            if (this.mForceLevel != -1) {
                i = this.mForceLevel;
                i3 = -1;
            } else if (usableSpace <= storageFullBytes) {
                i = 2;
            } else {
                i = (usableSpace > storageLowBytes && (!StorageManager.UUID_DEFAULT.equals(convert) || usableSpace >= BOOT_IMAGE_STORAGE_REQUIREMENT)) ? 0 : 1;
            }
            if (Math.abs(findOrCreateState.lastUsableBytes - usableSpace) > DEFAULT_LOG_DELTA_BYTES || i3 != i) {
                EventLogTags.writeStorageState(convert.toString(), i3, i, usableSpace, totalSpace);
                findOrCreateState.lastUsableBytes = usableSpace;
            }
            updateNotifications(volumeInfo, i3, i);
            updateBroadcasts(volumeInfo, i3, i, i2);
            findOrCreateState.level = i;
        }
        if (!this.mHandler.hasMessages(1)) {
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(1), LOW_CHECK_INTERVAL);
        }
        if (this.mHandler.hasMessages(2)) {
            return;
        }
        Handler handler2 = this.mHandler;
        handler2.sendMessageDelayed(handler2.obtainMessage(2), HIGH_CHECK_INTERVAL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkHigh() {
        StorageManager storageManager = (StorageManager) getContext().getSystemService(StorageManager.class);
        int i = DeviceConfig.getInt("storage_native_boot", "storage_threshold_percent_high", 20);
        for (VolumeInfo volumeInfo : storageManager.getWritablePrivateVolumes()) {
            File path = volumeInfo.getPath();
            if (path.getUsableSpace() < (path.getTotalSpace() * i) / 100) {
                try {
                    ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).freeAllAppCacheAboveQuota(volumeInfo.getFsUuid());
                } catch (IOException e) {
                    Slog.w(TAG, e);
                }
            }
        }
        if (this.mHandler.hasMessages(2)) {
            return;
        }
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(2), HIGH_CHECK_INTERVAL);
    }

    public DeviceStorageMonitorService(Context context) {
        super(context);
        this.mSeq = new AtomicInteger(1);
        this.mForceLevel = -1;
        this.mStates = new ArrayMap<>();
        this.mLocalService = new DeviceStorageMonitorInternal() { // from class: com.android.server.storage.DeviceStorageMonitorService.2
            @Override // com.android.server.storage.DeviceStorageMonitorInternal
            public void checkMemory() {
                DeviceStorageMonitorService.this.mHandler.removeMessages(1);
                DeviceStorageMonitorService.this.mHandler.obtainMessage(1).sendToTarget();
            }

            @Override // com.android.server.storage.DeviceStorageMonitorInternal
            public boolean isMemoryLow() {
                return Environment.getDataDirectory().getUsableSpace() < getMemoryLowThreshold();
            }

            @Override // com.android.server.storage.DeviceStorageMonitorInternal
            public long getMemoryLowThreshold() {
                return DeviceStorageMonitorService.this.dsmsWrapper.getExtImpl().getMemoryLowThresholdInternal();
            }
        };
        this.mRemoteService = new Binder() { // from class: com.android.server.storage.DeviceStorageMonitorService.3
            @Override // android.os.Binder
            protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
                if (DumpUtils.checkDumpPermission(DeviceStorageMonitorService.this.getContext(), DeviceStorageMonitorService.TAG, printWriter)) {
                    DeviceStorageMonitorService.this.dumpImpl(fileDescriptor, printWriter, strArr);
                }
            }

            public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
                new Shell().exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
            }
        };
        this.dsmsWrapper = new DeviceStorageMonitorServiceWrapper();
        this.mDSSext = (IDeviceStorageMonitorServiceExt) ExtLoader.type(IDeviceStorageMonitorServiceExt.class).base(this).create();
        HandlerThread handlerThread = new HandlerThread(TAG, 10);
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper()) { // from class: com.android.server.storage.DeviceStorageMonitorService.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 1) {
                    DeviceStorageMonitorService.this.dsmsWrapper.getExtImpl().dataCheck(DeviceStorageMonitorService.this.mHandler);
                } else {
                    if (i != 2) {
                        return;
                    }
                    DeviceStorageMonitorService.this.checkHigh();
                }
            }
        };
    }

    public void onStart() {
        Context context = getContext();
        this.mNotifManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        CacheFileDeletedObserver cacheFileDeletedObserver = new CacheFileDeletedObserver();
        this.mCacheFileDeletedObserver = cacheFileDeletedObserver;
        cacheFileDeletedObserver.startWatching();
        if (context.getPackageManager().hasSystemFeature("android.software.leanback")) {
            this.mNotifManager.createNotificationChannel(new NotificationChannel(TV_NOTIFICATION_CHANNEL_ID, context.getString(R.string.factorytest_failed), 4));
        }
        publishBinderService(SERVICE, this.mRemoteService);
        publishLocalService(DeviceStorageMonitorInternal.class, this.mLocalService);
        this.dsmsWrapper.getExtImpl().onStart(this.mHandler, getContext(), this);
    }

    public void onBootPhase(int i) {
        super.onBootPhase(i);
        if (i == 1000) {
            this.dsmsWrapper.getExtImpl().onBootPhase();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class Shell extends ShellCommand {
        Shell() {
        }

        public int onCommand(String str) {
            return DeviceStorageMonitorService.this.onShellCommand(this, str);
        }

        public void onHelp() {
            DeviceStorageMonitorService.dumpHelp(getOutPrintWriter());
        }
    }

    int parseOptions(Shell shell) {
        int i = 0;
        while (true) {
            String nextOption = shell.getNextOption();
            if (nextOption == null) {
                return i;
            }
            if ("-f".equals(nextOption)) {
                i |= 1;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    int onShellCommand(Shell shell, String str) {
        char c;
        if (str == null) {
            return shell.handleDefaultCommands(str);
        }
        PrintWriter outPrintWriter = shell.getOutPrintWriter();
        switch (str.hashCode()) {
            case 88200241:
                if (str.equals("force-full")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 108404047:
                if (str.equals("reset")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1526871410:
                if (str.equals("force-low")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1692300408:
                if (str.equals("force-not-low")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                this.dsmsWrapper.getExtImpl().shellCmdForceFull(shell, getContext(), "android.permission.DEVICE_POWER", this.mHandler);
                return 0;
            case 1:
                int parseOptions = parseOptions(shell);
                getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                this.mForceLevel = -1;
                int incrementAndGet = this.mSeq.incrementAndGet();
                if ((parseOptions & 1) != 0) {
                    this.mHandler.removeMessages(1);
                    this.mHandler.obtainMessage(1).sendToTarget();
                    outPrintWriter.println(incrementAndGet);
                }
                return 0;
            case 2:
                int parseOptions2 = parseOptions(shell);
                getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                this.mForceLevel = 1;
                this.dsmsWrapper.getExtImpl().setCmdForceLevel(str);
                int incrementAndGet2 = this.mSeq.incrementAndGet();
                if ((parseOptions2 & 1) != 0) {
                    this.mHandler.removeMessages(1);
                    this.mHandler.obtainMessage(1).sendToTarget();
                    outPrintWriter.println(incrementAndGet2);
                }
                return 0;
            case 3:
                int parseOptions3 = parseOptions(shell);
                getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                this.mForceLevel = 0;
                this.dsmsWrapper.getExtImpl().setCmdForceLevel(str);
                int incrementAndGet3 = this.mSeq.incrementAndGet();
                if ((parseOptions3 & 1) != 0) {
                    this.mHandler.removeMessages(1);
                    this.mHandler.obtainMessage(1).sendToTarget();
                    outPrintWriter.println(incrementAndGet3);
                }
                return 0;
            default:
                return shell.handleDefaultCommands(str);
        }
    }

    static void dumpHelp(PrintWriter printWriter) {
        printWriter.println("Device storage monitor service (devicestoragemonitor) commands:");
        printWriter.println("  help");
        printWriter.println("    Print this help text.");
        printWriter.println("  force-low [-f]");
        printWriter.println("    Force storage to be low, freezing storage state.");
        printWriter.println("    -f: force a storage change broadcast be sent, prints new sequence.");
        printWriter.println("  force-not-low [-f]");
        printWriter.println("    Force storage to not be low, freezing storage state.");
        printWriter.println("    -f: force a storage change broadcast be sent, prints new sequence.");
        printWriter.println("  reset [-f]");
        printWriter.println("    Unfreeze storage state, returning to current real values.");
        printWriter.println("    -f: force a storage change broadcast be sent, prints new sequence.");
    }

    void dumpImpl(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        PrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        if (this.dsmsWrapper.getExtImpl().simulationTest(strArr, indentingPrintWriter)) {
            return;
        }
        if (strArr == null || strArr.length == 0 || "-a".equals(strArr[0])) {
            StorageManager storageManager = (StorageManager) getContext().getSystemService(StorageManager.class);
            indentingPrintWriter.println("Known volumes:");
            indentingPrintWriter.increaseIndent();
            for (int i = 0; i < this.mStates.size(); i++) {
                UUID keyAt = this.mStates.keyAt(i);
                State valueAt = this.mStates.valueAt(i);
                if (StorageManager.UUID_DEFAULT.equals(keyAt)) {
                    indentingPrintWriter.println("Default:");
                } else {
                    indentingPrintWriter.println(keyAt + ":");
                }
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.printPair("level", State.levelToString(valueAt.level));
                indentingPrintWriter.printPair("lastUsableBytes", Long.valueOf(valueAt.lastUsableBytes));
                indentingPrintWriter.println();
                Iterator it = storageManager.getWritablePrivateVolumes().iterator();
                while (true) {
                    if (it.hasNext()) {
                        VolumeInfo volumeInfo = (VolumeInfo) it.next();
                        File path = volumeInfo.getPath();
                        if (Objects.equals(keyAt, StorageManager.convert(volumeInfo.getFsUuid()))) {
                            indentingPrintWriter.print("lowBytes=");
                            indentingPrintWriter.print(storageManager.getStorageLowBytes(path));
                            indentingPrintWriter.print(" fullBytes=");
                            indentingPrintWriter.println(storageManager.getStorageFullBytes(path));
                            indentingPrintWriter.print("path=");
                            indentingPrintWriter.println(path);
                            break;
                        }
                    }
                }
                indentingPrintWriter.decreaseIndent();
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.printPair("mSeq", Integer.valueOf(this.mSeq.get()));
            indentingPrintWriter.printPair("mForceState", State.levelToString(this.mForceLevel));
            indentingPrintWriter.println();
            indentingPrintWriter.println();
            this.dsmsWrapper.getExtImpl().dumpImpl(indentingPrintWriter);
            return;
        }
        new Shell().exec(this.mRemoteService, (FileDescriptor) null, fileDescriptor, (FileDescriptor) null, strArr, (ShellCallback) null, new ResultReceiver(null));
    }

    private void updateNotifications(VolumeInfo volumeInfo, int i, int i2) {
        Context context = getContext();
        UUID convert = StorageManager.convert(volumeInfo.getFsUuid());
        if (State.isEntering(1, i, i2)) {
            Intent intent = new Intent("android.os.storage.action.MANAGE_STORAGE");
            intent.putExtra("android.os.storage.extra.UUID", convert);
            intent.addFlags(268435456);
            CharSequence text = context.getText(R.string.notification_header_divider_symbol_with_spaces);
            CharSequence text2 = context.getText(R.string.notification_channel_wfc);
            Notification build = new Notification.Builder(context, SystemNotificationChannels.ALERTS).setSmallIcon(R.drawable.sym_keyboard_feedback_return).setTicker(text).setColor(context.getColor(R.color.system_notification_accent_color)).setContentTitle(text).setContentText(text2).setContentIntent(PendingIntent.getActivityAsUser(context, 0, intent, 67108864, null, UserHandle.CURRENT)).setStyle(new Notification.BigTextStyle().bigText(text2)).setVisibility(1).setCategory("sys").extend(new Notification.TvExtender().setChannelId(TV_NOTIFICATION_CHANNEL_ID)).build();
            build.flags |= 32;
            this.mNotifManager.notifyAsUser(convert.toString(), 23, build, UserHandle.ALL);
            FrameworkStatsLog.write(130, Objects.toString(volumeInfo.getDescription()), 2);
            return;
        }
        if (State.isLeaving(1, i, i2)) {
            this.mNotifManager.cancelAsUser(convert.toString(), 23, UserHandle.ALL);
            FrameworkStatsLog.write(130, Objects.toString(volumeInfo.getDescription()), 1);
        }
    }

    private void updateBroadcasts(VolumeInfo volumeInfo, int i, int i2, int i3) {
        if (Objects.equals(StorageManager.UUID_PRIVATE_INTERNAL, volumeInfo.getFsUuid())) {
            Intent putExtra = new Intent("android.intent.action.DEVICE_STORAGE_LOW").addFlags(85983232).putExtra(EXTRA_SEQUENCE, i3);
            Intent putExtra2 = new Intent("android.intent.action.DEVICE_STORAGE_OK").addFlags(85983232).putExtra(EXTRA_SEQUENCE, i3);
            if (State.isEntering(1, i, i2)) {
                getContext().sendStickyBroadcastAsUser(putExtra, UserHandle.ALL);
            } else if (State.isLeaving(1, i, i2)) {
                getContext().removeStickyBroadcastAsUser(putExtra, UserHandle.ALL);
                getContext().sendBroadcastAsUser(putExtra2, UserHandle.ALL);
            }
            Intent putExtra3 = new Intent("android.intent.action.DEVICE_STORAGE_FULL").addFlags(67108864).putExtra(EXTRA_SEQUENCE, i3);
            Intent putExtra4 = new Intent("android.intent.action.DEVICE_STORAGE_NOT_FULL").addFlags(67108864).putExtra(EXTRA_SEQUENCE, i3);
            if (State.isEntering(2, i, i2)) {
                getContext().sendStickyBroadcastAsUser(putExtra3, UserHandle.ALL);
            } else if (State.isLeaving(2, i, i2)) {
                getContext().removeStickyBroadcastAsUser(putExtra3, UserHandle.ALL);
                getContext().sendBroadcastAsUser(putExtra4, UserHandle.ALL);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class CacheFileDeletedObserver extends FileObserver {
        public CacheFileDeletedObserver() {
            super(Environment.getDownloadCacheDirectory().getAbsolutePath(), 512);
        }

        @Override // android.os.FileObserver
        public void onEvent(int i, String str) {
            EventLogTags.writeCacheFileDeleted(str);
        }
    }

    public IDeviceStorageMonitorServiceWrapper getWrapper() {
        return this.dsmsWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class DeviceStorageMonitorServiceWrapper implements IDeviceStorageMonitorServiceWrapper {
        @Override // com.android.server.storage.IDeviceStorageMonitorServiceWrapper
        public long highCheckInterVal() {
            return DeviceStorageMonitorService.HIGH_CHECK_INTERVAL;
        }

        @Override // com.android.server.storage.IDeviceStorageMonitorServiceWrapper
        public int msgChecHigh() {
            return 2;
        }

        @Override // com.android.server.storage.IDeviceStorageMonitorServiceWrapper
        public int msgCheckLow() {
            return 1;
        }

        private DeviceStorageMonitorServiceWrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IDeviceStorageMonitorServiceExt getExtImpl() {
            return DeviceStorageMonitorService.this.mDSSext;
        }

        @Override // com.android.server.storage.IDeviceStorageMonitorServiceWrapper
        public AtomicInteger mSeq() {
            return DeviceStorageMonitorService.this.mSeq;
        }
    }
}
