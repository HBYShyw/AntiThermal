package com.android.server;

import android.R;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.BroadcastOptions;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.BundleMerger;
import android.os.DropBoxManager;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.ShellCommand;
import android.os.StatFs;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.text.TextUtils;
import android.text.format.TimeMigrationUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.IDropBoxManagerService;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.ObjectUtils;
import com.android.server.DropBoxManagerInternal;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.GZIPOutputStream;
import libcore.io.IoUtils;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DropBoxManagerService extends SystemService {
    private static final long COMPRESS_THRESHOLD_BYTES = 16384;
    private static final int DEFAULT_AGE_SECONDS = 259200;
    private static final int DEFAULT_MAX_FILES = 1000;
    private static final int DEFAULT_MAX_FILES_LOWRAM = 300;
    private static final int DEFAULT_QUOTA_KB = 40960;
    private static final int DEFAULT_QUOTA_PERCENT = 10;
    private static final int DEFAULT_RESERVE_PERCENT = 0;
    private static final boolean PROFILE_DUMP = false;
    private static final int PROTO_MAX_DATA_BYTES = 262144;
    private static final int QUOTA_RESCAN_MILLIS = 5000;
    private static final String TAG = "DropBoxManagerService";
    private FileList mAllFiles;
    private int mBlockSize;
    private volatile boolean mBooted;
    private int mCachedQuotaBlocks;
    private long mCachedQuotaUptimeMillis;
    private final ContentResolver mContentResolver;
    private final File mDropBoxDir;
    private ArrayMap<String, FileList> mFilesByTag;
    private final DropBoxManagerBroadcastHandler mHandler;
    private long mLowPriorityRateLimitPeriod;
    private ArraySet<String> mLowPriorityTags;
    private int mMaxFiles;
    private final BroadcastReceiver mReceiver;
    private StatFs mStatFs;
    private final IDropBoxManagerService.Stub mStub;
    private static final List<String> DISABLED_BY_DEFAULT_TAGS = List.of("data_app_wtf", "system_app_wtf", "system_server_wtf");
    private static IDropBoxManagerServiceExt mDmsExt = (IDropBoxManagerServiceExt) ExtLoader.type(IDropBoxManagerServiceExt.class).create();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class ShellCmd extends ShellCommand {
        private ShellCmd() {
        }

        public int onCommand(String str) {
            char c;
            if (str == null) {
                return handleDefaultCommands(str);
            }
            PrintWriter outPrintWriter = getOutPrintWriter();
            try {
                switch (str.hashCode()) {
                    case -1412652367:
                        if (str.equals("restore-defaults")) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    case -529247831:
                        if (str.equals("add-low-priority")) {
                            c = 1;
                            break;
                        }
                        c = 65535;
                        break;
                    case -444925274:
                        if (str.equals("remove-low-priority")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1936917209:
                        if (str.equals("set-rate-limit")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
                if (c == 0) {
                    DropBoxManagerService.this.setLowPriorityRateLimit(Long.parseLong(getNextArgRequired()));
                } else if (c == 1) {
                    DropBoxManagerService.this.addLowPriorityTag(getNextArgRequired());
                } else if (c == 2) {
                    DropBoxManagerService.this.removeLowPriorityTag(getNextArgRequired());
                } else if (c == 3) {
                    DropBoxManagerService.this.restoreDefaults();
                } else {
                    return handleDefaultCommands(str);
                }
            } catch (Exception e) {
                outPrintWriter.println(e);
            }
            return 0;
        }

        public void onHelp() {
            PrintWriter outPrintWriter = getOutPrintWriter();
            outPrintWriter.println("Dropbox manager service commands:");
            outPrintWriter.println("  help");
            outPrintWriter.println("    Print this help text.");
            outPrintWriter.println("  set-rate-limit PERIOD");
            outPrintWriter.println("    Sets low priority broadcast rate limit period to PERIOD ms");
            outPrintWriter.println("  add-low-priority TAG");
            outPrintWriter.println("    Add TAG to dropbox low priority list");
            outPrintWriter.println("  remove-low-priority TAG");
            outPrintWriter.println("    Remove TAG from dropbox low priority list");
            outPrintWriter.println("  restore-defaults");
            outPrintWriter.println("    restore dropbox settings to defaults");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class DropBoxManagerBroadcastHandler extends Handler {
        static final int MSG_SEND_BROADCAST = 1;
        static final int MSG_SEND_DEFERRED_BROADCAST = 2;

        @GuardedBy({"mLock"})
        private final ArrayMap<String, Intent> mDeferredMap;
        private final Object mLock;

        DropBoxManagerBroadcastHandler(Looper looper) {
            super(looper);
            this.mLock = new Object();
            this.mDeferredMap = new ArrayMap<>();
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Intent remove;
            int i = message.what;
            if (i == 1) {
                prepareAndSendBroadcast((Intent) message.obj, null);
                return;
            }
            if (i != 2) {
                return;
            }
            synchronized (this.mLock) {
                remove = this.mDeferredMap.remove((String) message.obj);
            }
            if (remove != null) {
                prepareAndSendBroadcast(remove, createBroadcastOptions(remove));
            }
        }

        private void prepareAndSendBroadcast(Intent intent, Bundle bundle) {
            if (!DropBoxManagerService.this.mBooted) {
                intent.addFlags(1073741824);
            }
            DropBoxManagerService.this.getContext().sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.READ_LOGS", bundle);
        }

        private Intent createIntent(String str, long j) {
            Intent intent = new Intent("android.intent.action.DROPBOX_ENTRY_ADDED");
            intent.putExtra("tag", str);
            intent.putExtra("time", j);
            intent.putExtra("android.os.extra.DROPPED_COUNT", 0);
            return intent;
        }

        private Bundle createBroadcastOptions(Intent intent) {
            BundleMerger bundleMerger = new BundleMerger();
            bundleMerger.setDefaultMergeStrategy(1);
            bundleMerger.setMergeStrategy("time", 4);
            bundleMerger.setMergeStrategy("android.os.extra.DROPPED_COUNT", 25);
            return BroadcastOptions.makeBasic().setDeliveryGroupPolicy(2).setDeliveryGroupMatchingKey("android.intent.action.DROPBOX_ENTRY_ADDED", intent.getStringExtra("tag")).setDeliveryGroupExtrasMerger(bundleMerger).setDeferralPolicy(2).toBundle();
        }

        public void sendBroadcast(String str, long j) {
            sendMessage(obtainMessage(1, createIntent(str, j)));
        }

        public void maybeDeferBroadcast(String str, long j) {
            synchronized (this.mLock) {
                Intent intent = this.mDeferredMap.get(str);
                if (intent == null) {
                    this.mDeferredMap.put(str, createIntent(str, j));
                    sendMessageDelayed(obtainMessage(2, str), DropBoxManagerService.this.mLowPriorityRateLimitPeriod);
                } else {
                    intent.putExtra("time", j);
                    intent.putExtra("android.os.extra.DROPPED_COUNT", intent.getIntExtra("android.os.extra.DROPPED_COUNT", 0) + 1);
                }
            }
        }
    }

    public DropBoxManagerService(Context context) {
        this(context, new File("/data/system/dropbox"), FgThread.get().getLooper());
    }

    @VisibleForTesting
    public DropBoxManagerService(Context context, File file, Looper looper) {
        super(context);
        this.mAllFiles = null;
        this.mFilesByTag = null;
        this.mLowPriorityRateLimitPeriod = 0L;
        this.mLowPriorityTags = null;
        this.mStatFs = null;
        this.mBlockSize = 0;
        this.mCachedQuotaBlocks = 0;
        this.mCachedQuotaUptimeMillis = 0L;
        this.mBooted = false;
        this.mMaxFiles = -1;
        this.mReceiver = new BroadcastReceiver() { // from class: com.android.server.DropBoxManagerService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                DropBoxManagerService.this.mCachedQuotaUptimeMillis = 0L;
                new Thread() { // from class: com.android.server.DropBoxManagerService.1.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        try {
                            DropBoxManagerService.this.init();
                            DropBoxManagerService.this.trimToFit();
                        } catch (IOException e) {
                            Slog.e(DropBoxManagerService.TAG, "Can't init", e);
                        }
                    }
                }.start();
            }
        };
        this.mStub = new IDropBoxManagerService.Stub() { // from class: com.android.server.DropBoxManagerService.2
            public void addData(String str, byte[] bArr, int i) {
                DropBoxManagerService.this.addData(str, bArr, i);
            }

            public void addFile(String str, ParcelFileDescriptor parcelFileDescriptor, int i) {
                DropBoxManagerService.this.addFile(str, parcelFileDescriptor, i);
            }

            public boolean isTagEnabled(String str) {
                return DropBoxManagerService.this.isTagEnabled(str);
            }

            public DropBoxManager.Entry getNextEntry(String str, long j, String str2) {
                return getNextEntryWithAttribution(str, j, str2, null);
            }

            public DropBoxManager.Entry getNextEntryWithAttribution(String str, long j, String str2, String str3) {
                return DropBoxManagerService.this.getNextEntry(str, j, str2, str3);
            }

            public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
                DropBoxManagerService.this.dump(fileDescriptor, printWriter, strArr);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
                new ShellCmd().exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
            }
        };
        this.mDropBoxDir = file;
        this.mContentResolver = getContext().getContentResolver();
        this.mHandler = new DropBoxManagerBroadcastHandler(looper);
        LocalServices.addService(DropBoxManagerInternal.class, new DropBoxManagerInternalImpl());
        mDmsExt.init(context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("dropbox", this.mStub);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i != 500) {
            if (i != 1000) {
                return;
            }
            this.mBooted = true;
        } else {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.DEVICE_STORAGE_LOW");
            getContext().registerReceiver(this.mReceiver, intentFilter);
            this.mContentResolver.registerContentObserver(Settings.Global.CONTENT_URI, true, new ContentObserver(new Handler()) { // from class: com.android.server.DropBoxManagerService.3
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    DropBoxManagerService.this.mReceiver.onReceive(DropBoxManagerService.this.getContext(), null);
                }
            });
            getLowPriorityResourceConfigs();
        }
    }

    public IDropBoxManagerService getServiceStub() {
        return this.mStub;
    }

    public void addData(String str, byte[] bArr, int i) {
        mDmsExt.handleEapData(str, bArr, i);
        addEntry(str, new ByteArrayInputStream(bArr), bArr.length, i);
        mDmsExt.addSystemLogFile(str, bArr, i);
    }

    public void addFile(String str, ParcelFileDescriptor parcelFileDescriptor, int i) {
        try {
            StructStat fstat = Os.fstat(parcelFileDescriptor.getFileDescriptor());
            if (!OsConstants.S_ISREG(fstat.st_mode)) {
                throw new IllegalArgumentException(str + " entry must be real file");
            }
            addEntry(str, new ParcelFileDescriptor.AutoCloseInputStream(parcelFileDescriptor), fstat.st_size, i);
        } catch (ErrnoException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void addEntry(String str, InputStream inputStream, long j, int i) {
        boolean z;
        if ((i & 4) != 0 || j <= COMPRESS_THRESHOLD_BYTES) {
            z = false;
        } else {
            i |= 4;
            z = true;
        }
        addEntry(str, new SimpleEntrySource(inputStream, j, z), i);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class SimpleEntrySource implements DropBoxManagerInternal.EntrySource {
        private final boolean forceCompress;
        private final InputStream in;
        private final long length;

        public SimpleEntrySource(InputStream inputStream, long j, boolean z) {
            this.in = inputStream;
            this.length = j;
            this.forceCompress = z;
        }

        @Override // com.android.server.DropBoxManagerInternal.EntrySource
        public long length() {
            return this.length;
        }

        @Override // com.android.server.DropBoxManagerInternal.EntrySource
        public void writeTo(FileDescriptor fileDescriptor) throws IOException {
            if (this.forceCompress) {
                GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(new FileOutputStream(fileDescriptor));
                FileUtils.copy(this.in, gZIPOutputStream);
                gZIPOutputStream.close();
                return;
            }
            FileUtils.copy(this.in, new FileOutputStream(fileDescriptor));
        }

        @Override // com.android.server.DropBoxManagerInternal.EntrySource, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            FileUtils.closeQuietly(this.in);
        }
    }

    public void addEntry(String str, DropBoxManagerInternal.EntrySource entrySource, int i) {
        File file;
        File file2 = null;
        try {
            try {
                Slog.i(TAG, "add tag=" + str + " isTagEnabled=" + isTagEnabled(str) + " flags=0x" + Integer.toHexString(i));
                if ((i & 1) != 0) {
                    throw new IllegalArgumentException();
                }
                init();
                if (!isTagEnabled(str)) {
                    IoUtils.closeQuietly(entrySource);
                    return;
                }
                long length = entrySource.length();
                long trimToFit = trimToFit();
                if (length > trimToFit) {
                    Slog.w(TAG, "Dropping: " + str + " (" + length + " > " + trimToFit + " bytes)");
                    logDropboxDropped(6, str, 0L);
                    file = null;
                } else {
                    file = new File(this.mDropBoxDir, "drop" + Thread.currentThread().getId() + ".tmp");
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        try {
                            entrySource.writeTo(fileOutputStream.getFD());
                            fileOutputStream.close();
                        } catch (Throwable th) {
                            try {
                                fileOutputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                        }
                    } catch (IOException e) {
                        e = e;
                        file2 = file;
                        Slog.e(TAG, "Can't write: " + str, e);
                        logDropboxDropped(5, str, 0L);
                        IoUtils.closeQuietly(entrySource);
                        if (file2 != null) {
                            file2.delete();
                            return;
                        }
                        return;
                    } catch (Throwable th3) {
                        th = th3;
                        file2 = file;
                        IoUtils.closeQuietly(entrySource);
                        if (file2 != null) {
                            file2.delete();
                        }
                        throw th;
                    }
                }
                long createEntry = createEntry(file, str, i);
                ArraySet<String> arraySet = this.mLowPriorityTags;
                if (arraySet == null || !arraySet.contains(str)) {
                    this.mHandler.sendBroadcast(str, createEntry);
                } else {
                    this.mHandler.maybeDeferBroadcast(str, createEntry);
                }
                mDmsExt.addDropBoxFile(createEntry, str, i);
                IoUtils.closeQuietly(entrySource);
            } catch (IOException e2) {
                e = e2;
            }
        } catch (Throwable th4) {
            th = th4;
        }
    }

    private void logDropboxDropped(int i, String str, long j) {
        FrameworkStatsLog.write(FrameworkStatsLog.DROPBOX_ENTRY_DROPPED, i, str, j);
    }

    public boolean isTagEnabled(String str) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (DISABLED_BY_DEFAULT_TAGS.contains(str)) {
                return "enabled".equals(Settings.Global.getString(this.mContentResolver, "dropbox:" + str));
            }
            ContentResolver contentResolver = this.mContentResolver;
            return !"disabled".equals(Settings.Global.getString(contentResolver, "dropbox:" + str));
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean checkPermission(int i, String str, String str2) {
        if (getContext().checkCallingPermission("android.permission.PEEK_DROPBOX_DATA") == 0) {
            return true;
        }
        getContext().enforceCallingOrSelfPermission("android.permission.READ_LOGS", TAG);
        int noteOp = ((AppOpsManager) getContext().getSystemService(AppOpsManager.class)).noteOp(43, i, str, str2, (String) null);
        if (noteOp != 0) {
            if (noteOp != 3) {
                return false;
            }
            getContext().enforceCallingOrSelfPermission("android.permission.PACKAGE_USAGE_STATS", TAG);
        }
        return true;
    }

    public synchronized DropBoxManager.Entry getNextEntry(String str, long j, String str2, String str3) {
        if (!checkPermission(Binder.getCallingUid(), str2, str3)) {
            return null;
        }
        try {
            init();
            FileList fileList = str == null ? this.mAllFiles : this.mFilesByTag.get(str);
            if (fileList == null) {
                return null;
            }
            for (EntryFile entryFile : fileList.contents.tailSet(new EntryFile(j + 1))) {
                if (entryFile.tag != null) {
                    if ((entryFile.flags & 1) != 0) {
                        return new DropBoxManager.Entry(entryFile.tag, entryFile.timestampMillis);
                    }
                    File file = entryFile.getFile(this.mDropBoxDir);
                    try {
                        return new DropBoxManager.Entry(entryFile.tag, entryFile.timestampMillis, file, entryFile.flags);
                    } catch (IOException e) {
                        Slog.wtf(TAG, "Can't read: " + file, e);
                    }
                }
            }
            return null;
        } catch (IOException e2) {
            Slog.e(TAG, "Can't init", e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setLowPriorityRateLimit(long j) {
        this.mLowPriorityRateLimitPeriod = j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void addLowPriorityTag(String str) {
        this.mLowPriorityTags.add(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void removeLowPriorityTag(String str) {
        this.mLowPriorityTags.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void restoreDefaults() {
        getLowPriorityResourceConfigs();
    }

    /* JADX WARN: Removed duplicated region for block: B:117:0x02c1 A[Catch: all -> 0x030b, TryCatch #5 {, blocks: (B:4:0x0007, B:10:0x0015, B:11:0x0018, B:14:0x0029, B:16:0x002c, B:18:0x0036, B:21:0x0042, B:23:0x004c, B:26:0x0057, B:31:0x0063, B:33:0x006d, B:35:0x0078, B:37:0x0082, B:39:0x0092, B:43:0x0098, B:54:0x00c8, B:57:0x00cd, B:59:0x0115, B:60:0x011e, B:62:0x0124, B:64:0x0133, B:65:0x0138, B:66:0x0146, B:68:0x014c, B:71:0x0159, B:73:0x015d, B:74:0x0162, B:77:0x0176, B:79:0x0181, B:84:0x018b, B:86:0x0190, B:87:0x0196, B:89:0x01a1, B:90:0x01a6, B:93:0x01b2, B:96:0x01ca, B:98:0x01e3, B:150:0x02a7, B:153:0x02ac, B:117:0x02c1, B:159:0x02b3, B:163:0x02b8, B:161:0x02bb, B:113:0x0266, B:115:0x026b, B:201:0x01d2, B:202:0x01d7, B:209:0x02cd, B:211:0x02d4, B:213:0x02e3, B:217:0x02d9, B:218:0x02de, B:223:0x02ee), top: B:3:0x0007, inners: #7 }] */
    /* JADX WARN: Removed duplicated region for block: B:150:0x02a7 A[Catch: all -> 0x030b, TRY_ENTER, TRY_LEAVE, TryCatch #5 {, blocks: (B:4:0x0007, B:10:0x0015, B:11:0x0018, B:14:0x0029, B:16:0x002c, B:18:0x0036, B:21:0x0042, B:23:0x004c, B:26:0x0057, B:31:0x0063, B:33:0x006d, B:35:0x0078, B:37:0x0082, B:39:0x0092, B:43:0x0098, B:54:0x00c8, B:57:0x00cd, B:59:0x0115, B:60:0x011e, B:62:0x0124, B:64:0x0133, B:65:0x0138, B:66:0x0146, B:68:0x014c, B:71:0x0159, B:73:0x015d, B:74:0x0162, B:77:0x0176, B:79:0x0181, B:84:0x018b, B:86:0x0190, B:87:0x0196, B:89:0x01a1, B:90:0x01a6, B:93:0x01b2, B:96:0x01ca, B:98:0x01e3, B:150:0x02a7, B:153:0x02ac, B:117:0x02c1, B:159:0x02b3, B:163:0x02b8, B:161:0x02bb, B:113:0x0266, B:115:0x026b, B:201:0x01d2, B:202:0x01d7, B:209:0x02cd, B:211:0x02d4, B:213:0x02e3, B:217:0x02d9, B:218:0x02de, B:223:0x02ee), top: B:3:0x0007, inners: #7 }] */
    /* JADX WARN: Removed duplicated region for block: B:152:0x02ac A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:159:0x02b3 A[Catch: all -> 0x030b, TRY_ENTER, TRY_LEAVE, TryCatch #5 {, blocks: (B:4:0x0007, B:10:0x0015, B:11:0x0018, B:14:0x0029, B:16:0x002c, B:18:0x0036, B:21:0x0042, B:23:0x004c, B:26:0x0057, B:31:0x0063, B:33:0x006d, B:35:0x0078, B:37:0x0082, B:39:0x0092, B:43:0x0098, B:54:0x00c8, B:57:0x00cd, B:59:0x0115, B:60:0x011e, B:62:0x0124, B:64:0x0133, B:65:0x0138, B:66:0x0146, B:68:0x014c, B:71:0x0159, B:73:0x015d, B:74:0x0162, B:77:0x0176, B:79:0x0181, B:84:0x018b, B:86:0x0190, B:87:0x0196, B:89:0x01a1, B:90:0x01a6, B:93:0x01b2, B:96:0x01ca, B:98:0x01e3, B:150:0x02a7, B:153:0x02ac, B:117:0x02c1, B:159:0x02b3, B:163:0x02b8, B:161:0x02bb, B:113:0x0266, B:115:0x026b, B:201:0x01d2, B:202:0x01d7, B:209:0x02cd, B:211:0x02d4, B:213:0x02e3, B:217:0x02d9, B:218:0x02de, B:223:0x02ee), top: B:3:0x0007, inners: #7 }] */
    /* JADX WARN: Removed duplicated region for block: B:162:0x02b8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public synchronized void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Iterator<EntryFile> it;
        InputStreamReader inputStreamReader;
        File file;
        if (DumpUtils.checkDumpAndUsageStatsPermission(getContext(), TAG, printWriter)) {
            try {
                init();
                StringBuilder sb = new StringBuilder();
                ArrayList<String> arrayList = new ArrayList<>();
                boolean z = false;
                boolean z2 = false;
                boolean z3 = false;
                for (int i = 0; strArr != null && i < strArr.length; i++) {
                    if (!strArr[i].equals("-p") && !strArr[i].equals("--print")) {
                        if (!strArr[i].equals("-f") && !strArr[i].equals("--file")) {
                            if (!strArr[i].equals("--proto")) {
                                if (!strArr[i].equals("-h") && !strArr[i].equals("--help")) {
                                    if (strArr[i].startsWith("-")) {
                                        sb.append("Unknown argument: ");
                                        sb.append(strArr[i]);
                                        sb.append("\n");
                                    } else {
                                        arrayList.add(strArr[i]);
                                    }
                                }
                                printWriter.println("Dropbox (dropbox) dump options:");
                                printWriter.println("  [-h|--help] [-p|--print] [-f|--file] [timestamp]");
                                printWriter.println("    -h|--help: print this help");
                                printWriter.println("    -p|--print: print full contents of each entry");
                                printWriter.println("    -f|--file: print path of each entry's file");
                                printWriter.println("    --proto: dump data to proto");
                                printWriter.println("  [timestamp] optionally filters to only those entries.");
                                return;
                            }
                            z = true;
                        }
                        z3 = true;
                    }
                    z2 = true;
                }
                if (z) {
                    dumpProtoLocked(fileDescriptor, arrayList);
                    return;
                }
                sb.append("Drop box contents: ");
                sb.append(this.mAllFiles.contents.size());
                sb.append(" entries\n");
                sb.append("Max entries: ");
                sb.append(this.mMaxFiles);
                sb.append("\n");
                sb.append("Low priority rate limit period: ");
                sb.append(this.mLowPriorityRateLimitPeriod);
                sb.append(" ms\n");
                sb.append("Low priority tags: ");
                sb.append(this.mLowPriorityTags);
                sb.append("\n");
                if (!arrayList.isEmpty()) {
                    sb.append("Searching for:");
                    Iterator<String> it2 = arrayList.iterator();
                    while (it2.hasNext()) {
                        String next = it2.next();
                        sb.append(" ");
                        sb.append(next);
                    }
                    sb.append("\n");
                }
                sb.append("\n");
                Iterator<EntryFile> it3 = this.mAllFiles.contents.iterator();
                int i2 = 0;
                while (it3.hasNext()) {
                    EntryFile next2 = it3.next();
                    if (matchEntry(next2, arrayList)) {
                        int i3 = i2 + 1;
                        if (z2) {
                            sb.append("========================================\n");
                        }
                        sb.append(TimeMigrationUtils.formatMillisWithFixedFormat(next2.timestampMillis));
                        sb.append(" ");
                        String str = next2.tag;
                        if (str == null) {
                            str = "(no tag)";
                        }
                        sb.append(str);
                        File file2 = next2.getFile(this.mDropBoxDir);
                        if (file2 == null) {
                            sb.append(" (no file)\n");
                        } else if ((next2.flags & 1) != 0) {
                            sb.append(" (contents lost)\n");
                        } else {
                            sb.append(" (");
                            if ((next2.flags & 4) != 0) {
                                sb.append("compressed ");
                            }
                            sb.append((next2.flags & 2) != 0 ? "text" : "data");
                            sb.append(", ");
                            sb.append(file2.length());
                            sb.append(" bytes)\n");
                            if (z3 || (z2 && (next2.flags & 2) == 0)) {
                                if (!z2) {
                                    sb.append("    ");
                                }
                                sb.append(file2.getPath());
                                sb.append("\n");
                            }
                            if ((next2.flags & 2) == 0 || !z2) {
                                it = it3;
                            } else {
                                DropBoxManager.Entry entry = null;
                                InputStreamReader inputStreamReader2 = null;
                                entry = null;
                                try {
                                    try {
                                        String str2 = next2.tag;
                                        it = it3;
                                        try {
                                            long j = next2.timestampMillis;
                                            int i4 = next2.flags;
                                            file = file2;
                                            try {
                                                DropBoxManager.Entry entry2 = new DropBoxManager.Entry(str2, j, file, i4);
                                                if (z2) {
                                                    try {
                                                        inputStreamReader = new InputStreamReader(entry2.getInputStream());
                                                        try {
                                                            try {
                                                                char[] cArr = new char[4096];
                                                                boolean z4 = false;
                                                                while (true) {
                                                                    int read = inputStreamReader.read(cArr);
                                                                    if (read <= 0) {
                                                                        break;
                                                                    }
                                                                    try {
                                                                        sb.append(cArr, 0, read);
                                                                        z4 = cArr[read + (-1)] == '\n';
                                                                        if (sb.length() > 65536) {
                                                                            printWriter.write(sb.toString());
                                                                            try {
                                                                                sb.setLength(0);
                                                                            } catch (IOException e) {
                                                                                e = e;
                                                                                entry = entry2;
                                                                                try {
                                                                                    sb.append("*** ");
                                                                                    sb.append(e.toString());
                                                                                    sb.append("\n");
                                                                                    Slog.e(TAG, "Can't read: " + file, e);
                                                                                    if (entry != null) {
                                                                                    }
                                                                                    if (inputStreamReader != null) {
                                                                                    }
                                                                                    if (z2) {
                                                                                    }
                                                                                    it3 = it;
                                                                                    i2 = i3;
                                                                                } catch (Throwable th) {
                                                                                    th = th;
                                                                                    if (entry != null) {
                                                                                        entry.close();
                                                                                    }
                                                                                    if (inputStreamReader != null) {
                                                                                        try {
                                                                                            inputStreamReader.close();
                                                                                        } catch (IOException unused) {
                                                                                        }
                                                                                    }
                                                                                    throw th;
                                                                                }
                                                                            }
                                                                        }
                                                                    } catch (IOException e2) {
                                                                        e = e2;
                                                                    }
                                                                }
                                                                if (!z4) {
                                                                    try {
                                                                        sb.append("\n");
                                                                    } catch (IOException e3) {
                                                                        e = e3;
                                                                        entry = entry2;
                                                                        sb.append("*** ");
                                                                        sb.append(e.toString());
                                                                        sb.append("\n");
                                                                        Slog.e(TAG, "Can't read: " + file, e);
                                                                        if (entry != null) {
                                                                        }
                                                                        if (inputStreamReader != null) {
                                                                        }
                                                                        if (z2) {
                                                                        }
                                                                        it3 = it;
                                                                        i2 = i3;
                                                                    }
                                                                }
                                                                inputStreamReader2 = inputStreamReader;
                                                            } catch (Throwable th2) {
                                                                th = th2;
                                                                entry = entry2;
                                                                if (entry != null) {
                                                                }
                                                                if (inputStreamReader != null) {
                                                                }
                                                                throw th;
                                                            }
                                                        } catch (IOException e4) {
                                                            e = e4;
                                                        }
                                                    } catch (IOException e5) {
                                                        e = e5;
                                                        inputStreamReader = null;
                                                    } catch (Throwable th3) {
                                                        th = th3;
                                                        inputStreamReader = null;
                                                    }
                                                }
                                                entry2.close();
                                                if (inputStreamReader2 != null) {
                                                    inputStreamReader2.close();
                                                }
                                            } catch (IOException e6) {
                                                e = e6;
                                                inputStreamReader = null;
                                                sb.append("*** ");
                                                sb.append(e.toString());
                                                sb.append("\n");
                                                Slog.e(TAG, "Can't read: " + file, e);
                                                if (entry != null) {
                                                }
                                                if (inputStreamReader != null) {
                                                }
                                                if (z2) {
                                                }
                                                it3 = it;
                                                i2 = i3;
                                            }
                                        } catch (IOException e7) {
                                            e = e7;
                                            file = file2;
                                            inputStreamReader = null;
                                            sb.append("*** ");
                                            sb.append(e.toString());
                                            sb.append("\n");
                                            Slog.e(TAG, "Can't read: " + file, e);
                                            if (entry != null) {
                                                entry.close();
                                            }
                                            if (inputStreamReader != null) {
                                                try {
                                                    inputStreamReader.close();
                                                } catch (IOException unused2) {
                                                }
                                            }
                                            if (z2) {
                                            }
                                            it3 = it;
                                            i2 = i3;
                                        }
                                    } catch (Throwable th4) {
                                        th = th4;
                                        inputStreamReader = null;
                                    }
                                } catch (IOException e8) {
                                    e = e8;
                                    it = it3;
                                }
                            }
                            if (z2) {
                                sb.append("\n");
                            }
                            it3 = it;
                            i2 = i3;
                        }
                        it = it3;
                        it3 = it;
                        i2 = i3;
                    }
                }
                if (i2 == 0) {
                    sb.append("(No entries found.)\n");
                }
                if (strArr == null || strArr.length == 0) {
                    if (!z2) {
                        sb.append("\n");
                    }
                    sb.append("Usage: dumpsys dropbox [--print|--file] [YYYY-mm-dd] [HH:MM:SS] [tag]\n");
                }
                printWriter.write(sb.toString());
            } catch (IOException e9) {
                printWriter.println("Can't initialize: " + e9);
                Slog.e(TAG, "Can't init", e9);
            }
        }
    }

    private boolean matchEntry(EntryFile entryFile, ArrayList<String> arrayList) {
        String formatMillisWithFixedFormat = TimeMigrationUtils.formatMillisWithFixedFormat(entryFile.timestampMillis);
        int size = arrayList.size();
        boolean z = true;
        for (int i = 0; i < size && z; i++) {
            String str = arrayList.get(i);
            z = formatMillisWithFixedFormat.contains(str) || str.equals(entryFile.tag);
        }
        return z;
    }

    private void dumpProtoLocked(FileDescriptor fileDescriptor, ArrayList<String> arrayList) {
        File file;
        DropBoxManager.Entry entry;
        ProtoOutputStream protoOutputStream = new ProtoOutputStream(fileDescriptor);
        Iterator<EntryFile> it = this.mAllFiles.contents.iterator();
        while (it.hasNext()) {
            EntryFile next = it.next();
            if (matchEntry(next, arrayList) && (file = next.getFile(this.mDropBoxDir)) != null && (next.flags & 1) == 0) {
                long start = protoOutputStream.start(2246267895809L);
                protoOutputStream.write(1112396529665L, next.timestampMillis);
                try {
                    entry = new DropBoxManager.Entry(next.tag, next.timestampMillis, file, next.flags);
                } catch (IOException e) {
                    Slog.e(TAG, "Can't read: " + file, e);
                }
                try {
                    InputStream inputStream = entry.getInputStream();
                    if (inputStream != null) {
                        try {
                            byte[] bArr = new byte[262144];
                            int i = 0;
                            int i2 = 0;
                            while (i >= 0) {
                                i2 += i;
                                if (i2 >= 262144) {
                                    break;
                                } else {
                                    i = inputStream.read(bArr, i2, 262144 - i2);
                                }
                            }
                            protoOutputStream.write(1151051235330L, Arrays.copyOf(bArr, i2));
                        } catch (Throwable th) {
                            try {
                                inputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                            break;
                        }
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entry.close();
                    protoOutputStream.end(start);
                } catch (Throwable th3) {
                    try {
                        entry.close();
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                    }
                    throw th3;
                    break;
                }
            }
        }
        protoOutputStream.flush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class FileList implements Comparable<FileList> {
        public int blocks;
        public final TreeSet<EntryFile> contents;

        private FileList() {
            this.blocks = 0;
            this.contents = new TreeSet<>();
        }

        @Override // java.lang.Comparable
        public final int compareTo(FileList fileList) {
            int i = this.blocks;
            int i2 = fileList.blocks;
            if (i != i2) {
                return i2 - i;
            }
            if (this == fileList) {
                return 0;
            }
            if (hashCode() < fileList.hashCode()) {
                return -1;
            }
            return hashCode() > fileList.hashCode() ? 1 : 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class EntryFile implements Comparable<EntryFile> {
        public final int blocks;
        public final int flags;
        public final String tag;
        public final long timestampMillis;

        @Override // java.lang.Comparable
        public final int compareTo(EntryFile entryFile) {
            int compare = Long.compare(this.timestampMillis, entryFile.timestampMillis);
            if (compare != 0) {
                return compare;
            }
            int compare2 = ObjectUtils.compare(this.tag, entryFile.tag);
            if (compare2 != 0) {
                return compare2;
            }
            int compare3 = Integer.compare(this.flags, entryFile.flags);
            return compare3 != 0 ? compare3 : Integer.compare(hashCode(), entryFile.hashCode());
        }

        public EntryFile(File file, File file2, String str, long j, int i, int i2) throws IOException {
            if ((i & 1) != 0) {
                throw new IllegalArgumentException();
            }
            this.tag = TextUtils.safeIntern(str);
            this.timestampMillis = j;
            this.flags = i;
            File file3 = getFile(file2);
            if (!file.renameTo(file3)) {
                throw new IOException("Can't rename " + file + " to " + file3);
            }
            long j2 = i2;
            this.blocks = (int) (((file3.length() + j2) - 1) / j2);
        }

        public EntryFile(File file, String str, long j) throws IOException {
            this.tag = TextUtils.safeIntern(str);
            this.timestampMillis = j;
            this.flags = 1;
            this.blocks = 0;
            new FileOutputStream(getFile(file)).close();
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x007a A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:6:0x0083  */
        /* JADX WARN: Removed duplicated region for block: B:9:0x00a5  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public EntryFile(File file, int i) {
            String decode;
            int i2;
            boolean z;
            long parseLong;
            String name = file.getName();
            int lastIndexOf = name.lastIndexOf(64);
            if (lastIndexOf < 0) {
                z = true;
                i2 = 0;
                decode = null;
            } else {
                decode = Uri.decode(name.substring(0, lastIndexOf));
                if (name.endsWith(".gz")) {
                    name = name.substring(0, name.length() - 3);
                    i2 = 4;
                } else {
                    i2 = 0;
                }
                if (name.endsWith(".lost")) {
                    i2 |= 1;
                    name = name.substring(lastIndexOf + 1, name.length() - 5);
                } else if (name.endsWith(".txt")) {
                    i2 |= 2;
                    name = name.substring(lastIndexOf + 1, name.length() - 4);
                } else if (name.endsWith(".dat")) {
                    name = name.substring(lastIndexOf + 1, name.length() - 4);
                } else {
                    z = true;
                    if (!z) {
                        try {
                            parseLong = Long.parseLong(name);
                        } catch (NumberFormatException unused) {
                            z = true;
                        }
                        if (!z) {
                            Slog.wtf(DropBoxManagerService.TAG, "Invalid filename: " + file);
                            file.delete();
                            this.tag = null;
                            this.flags = 1;
                            this.timestampMillis = 0L;
                            this.blocks = 0;
                            return;
                        }
                        long length = file.length();
                        long j = i;
                        this.blocks = (int) (((length + j) - 1) / j);
                        this.tag = TextUtils.safeIntern(decode);
                        this.flags = i2;
                        this.timestampMillis = parseLong;
                        return;
                    }
                }
                z = false;
                if (!z) {
                }
            }
            parseLong = 0;
            if (!z) {
            }
        }

        public EntryFile(long j) {
            this.tag = null;
            this.timestampMillis = j;
            this.flags = 1;
            this.blocks = 0;
        }

        public boolean hasFile() {
            return this.tag != null;
        }

        private String getExtension() {
            if ((this.flags & 1) != 0) {
                return ".lost";
            }
            StringBuilder sb = new StringBuilder();
            sb.append((this.flags & 2) != 0 ? ".txt" : ".dat");
            sb.append((this.flags & 4) != 0 ? ".gz" : "");
            return sb.toString();
        }

        public String getFilename() {
            if (!hasFile()) {
                return null;
            }
            return Uri.encode(this.tag) + "@" + this.timestampMillis + getExtension();
        }

        public File getFile(File file) {
            if (hasFile()) {
                return new File(file, getFilename());
            }
            return null;
        }

        public void deleteFile(File file) {
            if (hasFile()) {
                getFile(file).delete();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void init() throws IOException {
        if (this.mStatFs == null) {
            if (!this.mDropBoxDir.isDirectory() && !this.mDropBoxDir.mkdirs()) {
                throw new IOException("Can't mkdir: " + this.mDropBoxDir);
            }
            try {
                StatFs statFs = new StatFs(this.mDropBoxDir.getPath());
                this.mStatFs = statFs;
                this.mBlockSize = statFs.getBlockSize();
            } catch (IllegalArgumentException unused) {
                throw new IOException("Can't statfs: " + this.mDropBoxDir);
            }
        }
        if (this.mAllFiles == null) {
            File[] listFiles = this.mDropBoxDir.listFiles();
            if (listFiles == null) {
                throw new IOException("Can't list files: " + this.mDropBoxDir);
            }
            this.mAllFiles = new FileList();
            this.mFilesByTag = new ArrayMap<>();
            for (File file : listFiles) {
                if (file.getName().endsWith(".tmp")) {
                    Slog.i(TAG, "Cleaning temp file: " + file);
                    file.delete();
                } else {
                    EntryFile entryFile = new EntryFile(file, this.mBlockSize);
                    if (entryFile.hasFile()) {
                        enrollEntry(entryFile);
                    }
                }
            }
        }
    }

    private synchronized void enrollEntry(EntryFile entryFile) {
        this.mAllFiles.contents.add(entryFile);
        this.mAllFiles.blocks += entryFile.blocks;
        if (entryFile.hasFile() && entryFile.blocks > 0) {
            FileList fileList = this.mFilesByTag.get(entryFile.tag);
            if (fileList == null) {
                fileList = new FileList();
                this.mFilesByTag.put(TextUtils.safeIntern(entryFile.tag), fileList);
            }
            fileList.contents.add(entryFile);
            fileList.blocks += entryFile.blocks;
        }
    }

    private synchronized long createEntry(File file, String str, int i) throws IOException {
        long currentTimeMillis;
        EntryFile[] entryFileArr;
        currentTimeMillis = System.currentTimeMillis();
        SortedSet<EntryFile> tailSet = this.mAllFiles.contents.tailSet(new EntryFile(IDeviceIdleControllerExt.ADVANCE_TIME + currentTimeMillis));
        if (tailSet.isEmpty()) {
            entryFileArr = null;
        } else {
            entryFileArr = (EntryFile[]) tailSet.toArray(new EntryFile[tailSet.size()]);
            tailSet.clear();
        }
        if (!this.mAllFiles.contents.isEmpty()) {
            currentTimeMillis = Math.max(currentTimeMillis, this.mAllFiles.contents.last().timestampMillis + 1);
        }
        if (entryFileArr != null) {
            long j = currentTimeMillis;
            for (EntryFile entryFile : entryFileArr) {
                this.mAllFiles.blocks -= entryFile.blocks;
                FileList fileList = this.mFilesByTag.get(entryFile.tag);
                if (fileList != null && fileList.contents.remove(entryFile)) {
                    fileList.blocks -= entryFile.blocks;
                }
                if ((entryFile.flags & 1) == 0) {
                    enrollEntry(new EntryFile(entryFile.getFile(this.mDropBoxDir), this.mDropBoxDir, entryFile.tag, j, entryFile.flags, this.mBlockSize));
                    j++;
                } else {
                    enrollEntry(new EntryFile(this.mDropBoxDir, entryFile.tag, j));
                    j++;
                }
            }
            currentTimeMillis = j;
        }
        if (file == null) {
            enrollEntry(new EntryFile(this.mDropBoxDir, str, currentTimeMillis));
        } else {
            enrollEntry(new EntryFile(file, this.mDropBoxDir, str, currentTimeMillis, i, this.mBlockSize));
        }
        return currentTimeMillis;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long trimToFit() throws IOException {
        int i = Settings.Global.getInt(this.mContentResolver, "dropbox_age_seconds", DEFAULT_AGE_SECONDS);
        this.mMaxFiles = Settings.Global.getInt(this.mContentResolver, "dropbox_max_files", ActivityManager.isLowRamDeviceStatic() ? 300 : 1000);
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - (i * 1000);
        while (!this.mAllFiles.contents.isEmpty()) {
            EntryFile first = this.mAllFiles.contents.first();
            if (first.timestampMillis > j && this.mAllFiles.contents.size() < this.mMaxFiles) {
                break;
            }
            logDropboxDropped(4, first.tag, currentTimeMillis - first.timestampMillis);
            FileList fileList = this.mFilesByTag.get(first.tag);
            if (fileList != null && fileList.contents.remove(first)) {
                fileList.blocks -= first.blocks;
            }
            if (this.mAllFiles.contents.remove(first)) {
                this.mAllFiles.blocks -= first.blocks;
            }
            first.deleteFile(this.mDropBoxDir);
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        int i2 = 0;
        if (uptimeMillis > this.mCachedQuotaUptimeMillis + 5000) {
            int i3 = Settings.Global.getInt(this.mContentResolver, "dropbox_quota_percent", 10);
            int i4 = Settings.Global.getInt(this.mContentResolver, "dropbox_reserve_percent", 0);
            int i5 = Settings.Global.getInt(this.mContentResolver, "dropbox_quota_kb", DEFAULT_QUOTA_KB);
            try {
                this.mStatFs.restat(this.mDropBoxDir.getPath());
                long availableBlocksLong = this.mStatFs.getAvailableBlocksLong();
                long blockCountLong = availableBlocksLong - ((this.mStatFs.getBlockCountLong() * i4) / 100);
                int intExact = Math.toIntExact(Math.max(0L, Math.min((i3 * blockCountLong) / 100, 2147483647L)));
                int i6 = this.mBlockSize;
                int i7 = (i5 * 1024) / i6;
                mDmsExt.dumpLowStorageLog(availableBlocksLong, blockCountLong, i3, i6, i7);
                this.mCachedQuotaBlocks = Math.min(i7, intExact);
                this.mCachedQuotaUptimeMillis = uptimeMillis;
            } catch (IllegalArgumentException unused) {
                throw new IOException("Can't restat: " + this.mDropBoxDir);
            }
        }
        int i8 = this.mAllFiles.blocks;
        if (i8 > this.mCachedQuotaBlocks) {
            TreeSet treeSet = new TreeSet(this.mFilesByTag.values());
            Iterator it = treeSet.iterator();
            while (it.hasNext()) {
                FileList fileList2 = (FileList) it.next();
                if (i2 > 0 && fileList2.blocks <= (this.mCachedQuotaBlocks - i8) / i2) {
                    break;
                }
                i8 -= fileList2.blocks;
                i2++;
            }
            int i9 = (this.mCachedQuotaBlocks - i8) / i2;
            Iterator it2 = treeSet.iterator();
            while (it2.hasNext()) {
                FileList fileList3 = (FileList) it2.next();
                if (this.mAllFiles.blocks < this.mCachedQuotaBlocks) {
                    break;
                }
                while (fileList3.blocks > i9 && !fileList3.contents.isEmpty()) {
                    EntryFile first2 = fileList3.contents.first();
                    logDropboxDropped(3, first2.tag, currentTimeMillis - first2.timestampMillis);
                    if (fileList3.contents.remove(first2)) {
                        fileList3.blocks -= first2.blocks;
                    }
                    if (this.mAllFiles.contents.remove(first2)) {
                        this.mAllFiles.blocks -= first2.blocks;
                    }
                    try {
                        first2.deleteFile(this.mDropBoxDir);
                        enrollEntry(new EntryFile(this.mDropBoxDir, first2.tag, first2.timestampMillis));
                    } catch (IOException e) {
                        Slog.e(TAG, "Can't write tombstone file", e);
                    }
                }
            }
        }
        return this.mCachedQuotaBlocks * this.mBlockSize;
    }

    private void getLowPriorityResourceConfigs() {
        this.mLowPriorityRateLimitPeriod = Resources.getSystem().getInteger(R.integer.config_minNumVisibleRecentTasks_lowRam);
        String[] stringArray = Resources.getSystem().getStringArray(R.array.config_notificationSignalExtractors);
        int length = stringArray.length;
        if (length == 0) {
            this.mLowPriorityTags = null;
            return;
        }
        this.mLowPriorityTags = new ArraySet<>(length);
        for (String str : stringArray) {
            this.mLowPriorityTags.add(str);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class DropBoxManagerInternalImpl extends DropBoxManagerInternal {
        private DropBoxManagerInternalImpl() {
        }

        @Override // com.android.server.DropBoxManagerInternal
        public void addEntry(String str, DropBoxManagerInternal.EntrySource entrySource, int i) {
            DropBoxManagerService.this.addEntry(str, entrySource, i);
        }
    }
}
