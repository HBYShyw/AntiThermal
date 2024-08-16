package com.android.server.notification;

import android.app.NotificationHistory;
import android.os.Handler;
import android.util.AtomicFile;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.notification.NotificationHistoryFilter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NotificationHistoryDatabase {
    private static final boolean DEBUG = NotificationManagerService.DBG;
    private static final int DEFAULT_CURRENT_VERSION = 1;
    private static final int HISTORY_RETENTION_DAYS = 1;
    private static final long INVALID_FILE_TIME_MS = -1;
    private static final String TAG = "NotiHistoryDatabase";
    private static final long WRITE_BUFFER_INTERVAL_MS = 1200000;
    private final Handler mFileWriteHandler;
    private final File mHistoryDir;
    private final File mVersionFile;
    private final Object mLock = new Object();
    private int mCurrentVersion = 1;

    @VisibleForTesting
    final List<AtomicFile> mHistoryFiles = new ArrayList();

    @VisibleForTesting
    NotificationHistory mBuffer = new NotificationHistory();
    private final WriteBufferRunnable mWriteBufferRunnable = new WriteBufferRunnable();

    public NotificationHistoryDatabase(Handler handler, File file) {
        this.mFileWriteHandler = handler;
        this.mVersionFile = new File(file, "version");
        this.mHistoryDir = new File(file, "history");
    }

    public void init() {
        synchronized (this.mLock) {
            try {
                if (!this.mHistoryDir.exists() && !this.mHistoryDir.mkdir()) {
                    throw new IllegalStateException("could not create history directory");
                }
                this.mVersionFile.createNewFile();
            } catch (Exception e) {
                Slog.e(TAG, "could not create needed files", e);
            }
            checkVersionAndBuildLocked();
            indexFilesLocked();
            prune();
        }
    }

    private void indexFilesLocked() {
        this.mHistoryFiles.clear();
        File[] listFiles = this.mHistoryDir.listFiles();
        if (listFiles == null) {
            return;
        }
        Arrays.sort(listFiles, new Comparator() { // from class: com.android.server.notification.NotificationHistoryDatabase$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$indexFilesLocked$0;
                lambda$indexFilesLocked$0 = NotificationHistoryDatabase.lambda$indexFilesLocked$0((File) obj, (File) obj2);
                return lambda$indexFilesLocked$0;
            }
        });
        for (File file : listFiles) {
            this.mHistoryFiles.add(new AtomicFile(file));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$indexFilesLocked$0(File file, File file2) {
        return Long.compare(safeParseLong(file2.getName()), safeParseLong(file.getName()));
    }

    private void checkVersionAndBuildLocked() {
        int i;
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(this.mVersionFile));
        } catch (IOException | NumberFormatException unused) {
            i = 0;
        }
        try {
            i = Integer.parseInt(bufferedReader.readLine());
            bufferedReader.close();
            if (i == this.mCurrentVersion || !this.mVersionFile.exists()) {
                return;
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.mVersionFile));
                try {
                    bufferedWriter.write(Integer.toString(this.mCurrentVersion));
                    bufferedWriter.write("\n");
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } finally {
                }
            } catch (IOException e) {
                Slog.e(TAG, "Failed to write new version");
                throw new RuntimeException(e);
            }
        } catch (Throwable th) {
            try {
                bufferedReader.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public void forceWriteToDisk() {
        this.mFileWriteHandler.post(this.mWriteBufferRunnable);
    }

    public void onPackageRemoved(String str) {
        this.mFileWriteHandler.post(new RemovePackageRunnable(str));
    }

    public void deleteNotificationHistoryItem(String str, long j) {
        this.mFileWriteHandler.post(new RemoveNotificationRunnable(str, j));
    }

    public void deleteConversations(String str, Set<String> set) {
        this.mFileWriteHandler.post(new RemoveConversationRunnable(str, set));
    }

    public void deleteNotificationChannel(String str, String str2) {
        this.mFileWriteHandler.post(new RemoveChannelRunnable(str, str2));
    }

    public void addNotification(NotificationHistory.HistoricalNotification historicalNotification) {
        synchronized (this.mLock) {
            this.mBuffer.addNewNotificationToWrite(historicalNotification);
            if (this.mBuffer.getHistoryCount() == 1) {
                this.mFileWriteHandler.postDelayed(this.mWriteBufferRunnable, WRITE_BUFFER_INTERVAL_MS);
            }
        }
    }

    public NotificationHistory readNotificationHistory() {
        NotificationHistory notificationHistory;
        synchronized (this.mLock) {
            notificationHistory = new NotificationHistory();
            notificationHistory.addNotificationsToWrite(this.mBuffer);
            for (AtomicFile atomicFile : this.mHistoryFiles) {
                try {
                    readLocked(atomicFile, notificationHistory, new NotificationHistoryFilter.Builder().build());
                } catch (Exception e) {
                    Slog.e(TAG, "error reading " + atomicFile.getBaseFile().getAbsolutePath(), e);
                }
            }
        }
        return notificationHistory;
    }

    public NotificationHistory readNotificationHistory(String str, String str2, int i) {
        NotificationHistory notificationHistory;
        synchronized (this.mLock) {
            notificationHistory = new NotificationHistory();
            for (AtomicFile atomicFile : this.mHistoryFiles) {
                try {
                    readLocked(atomicFile, notificationHistory, new NotificationHistoryFilter.Builder().setPackage(str).setChannel(str, str2).setMaxNotifications(i).build());
                } catch (Exception e) {
                    Slog.e(TAG, "error reading " + atomicFile.getBaseFile().getAbsolutePath(), e);
                }
                if (i == notificationHistory.getHistoryCount()) {
                    break;
                }
            }
        }
        return notificationHistory;
    }

    public void disableHistory() {
        synchronized (this.mLock) {
            Iterator<AtomicFile> it = this.mHistoryFiles.iterator();
            while (it.hasNext()) {
                it.next().delete();
            }
            this.mHistoryDir.delete();
            this.mHistoryFiles.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prune() {
        prune(1, System.currentTimeMillis());
    }

    void prune(int i, long j) {
        synchronized (this.mLock) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTimeInMillis(j);
            gregorianCalendar.add(5, i * (-1));
            for (int size = this.mHistoryFiles.size() - 1; size >= 0; size--) {
                AtomicFile atomicFile = this.mHistoryFiles.get(size);
                long safeParseLong = safeParseLong(atomicFile.getBaseFile().getName());
                if (DEBUG) {
                    Slog.d(TAG, "File " + atomicFile.getBaseFile().getName() + " created on " + safeParseLong);
                }
                if (safeParseLong <= gregorianCalendar.getTimeInMillis()) {
                    deleteFile(atomicFile);
                }
            }
        }
    }

    void removeFilePathFromHistory(String str) {
        if (str == null) {
            return;
        }
        Iterator<AtomicFile> it = this.mHistoryFiles.iterator();
        while (it.hasNext()) {
            AtomicFile next = it.next();
            if (next != null && str.equals(next.getBaseFile().getAbsolutePath())) {
                it.remove();
                return;
            }
        }
    }

    private void deleteFile(AtomicFile atomicFile) {
        if (DEBUG) {
            Slog.d(TAG, "Removed " + atomicFile.getBaseFile().getName());
        }
        atomicFile.delete();
        removeFilePathFromHistory(atomicFile.getBaseFile().getAbsolutePath());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeLocked(AtomicFile atomicFile, NotificationHistory notificationHistory) throws IOException {
        FileOutputStream startWrite = atomicFile.startWrite();
        try {
            NotificationHistoryProtoHelper.write(startWrite, notificationHistory, this.mCurrentVersion);
            atomicFile.finishWrite(startWrite);
            atomicFile.failWrite(null);
        } catch (Throwable th) {
            atomicFile.failWrite(startWrite);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void readLocked(AtomicFile atomicFile, NotificationHistory notificationHistory, NotificationHistoryFilter notificationHistoryFilter) throws IOException {
        FileInputStream fileInputStream = null;
        try {
            try {
                fileInputStream = atomicFile.openRead();
                NotificationHistoryProtoHelper.read(fileInputStream, notificationHistory, notificationHistoryFilter);
            } catch (FileNotFoundException e) {
                Slog.e(TAG, "Cannot open " + atomicFile.getBaseFile().getAbsolutePath(), e);
                throw e;
            }
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    private static long safeParseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException unused) {
            return -1L;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class WriteBufferRunnable implements Runnable {
        WriteBufferRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            run(new AtomicFile(new File(NotificationHistoryDatabase.this.mHistoryDir, String.valueOf(System.currentTimeMillis()))));
        }

        void run(AtomicFile atomicFile) {
            synchronized (NotificationHistoryDatabase.this.mLock) {
                if (NotificationHistoryDatabase.DEBUG) {
                    Slog.d(NotificationHistoryDatabase.TAG, "WriteBufferRunnable " + atomicFile.getBaseFile().getAbsolutePath());
                }
                try {
                    NotificationHistoryDatabase notificationHistoryDatabase = NotificationHistoryDatabase.this;
                    notificationHistoryDatabase.writeLocked(atomicFile, notificationHistoryDatabase.mBuffer);
                    NotificationHistoryDatabase.this.mHistoryFiles.add(0, atomicFile);
                    NotificationHistoryDatabase.this.mBuffer = new NotificationHistory();
                } catch (IOException e) {
                    Slog.e(NotificationHistoryDatabase.TAG, "Failed to write buffer to disk. not flushing buffer", e);
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class RemovePackageRunnable implements Runnable {
        private String mPkg;

        public RemovePackageRunnable(String str) {
            this.mPkg = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (NotificationHistoryDatabase.DEBUG) {
                Slog.d(NotificationHistoryDatabase.TAG, "RemovePackageRunnable " + this.mPkg);
            }
            synchronized (NotificationHistoryDatabase.this.mLock) {
                NotificationHistoryDatabase.this.mBuffer.removeNotificationsFromWrite(this.mPkg);
                for (AtomicFile atomicFile : NotificationHistoryDatabase.this.mHistoryFiles) {
                    try {
                        NotificationHistory notificationHistory = new NotificationHistory();
                        NotificationHistoryDatabase.readLocked(atomicFile, notificationHistory, new NotificationHistoryFilter.Builder().build());
                        notificationHistory.removeNotificationsFromWrite(this.mPkg);
                        NotificationHistoryDatabase.this.writeLocked(atomicFile, notificationHistory);
                    } catch (Exception e) {
                        Slog.e(NotificationHistoryDatabase.TAG, "Cannot clean up file on pkg removal " + atomicFile.getBaseFile().getAbsolutePath(), e);
                    }
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class RemoveNotificationRunnable implements Runnable {
        private NotificationHistory mNotificationHistory;
        private String mPkg;
        private long mPostedTime;

        public RemoveNotificationRunnable(String str, long j) {
            this.mPkg = str;
            this.mPostedTime = j;
        }

        @VisibleForTesting
        void setNotificationHistory(NotificationHistory notificationHistory) {
            this.mNotificationHistory = notificationHistory;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (NotificationHistoryDatabase.DEBUG) {
                Slog.d(NotificationHistoryDatabase.TAG, "RemoveNotificationRunnable");
            }
            synchronized (NotificationHistoryDatabase.this.mLock) {
                NotificationHistoryDatabase.this.mBuffer.removeNotificationFromWrite(this.mPkg, this.mPostedTime);
                for (AtomicFile atomicFile : NotificationHistoryDatabase.this.mHistoryFiles) {
                    try {
                        NotificationHistory notificationHistory = this.mNotificationHistory;
                        if (notificationHistory == null) {
                            notificationHistory = new NotificationHistory();
                        }
                        NotificationHistoryDatabase.readLocked(atomicFile, notificationHistory, new NotificationHistoryFilter.Builder().build());
                        if (notificationHistory.removeNotificationFromWrite(this.mPkg, this.mPostedTime)) {
                            NotificationHistoryDatabase.this.writeLocked(atomicFile, notificationHistory);
                        }
                    } catch (Exception e) {
                        Slog.e(NotificationHistoryDatabase.TAG, "Cannot clean up file on notification removal " + atomicFile.getBaseFile().getName(), e);
                    }
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class RemoveConversationRunnable implements Runnable {
        private Set<String> mConversationIds;
        private NotificationHistory mNotificationHistory;
        private String mPkg;

        public RemoveConversationRunnable(String str, Set<String> set) {
            this.mPkg = str;
            this.mConversationIds = set;
        }

        @VisibleForTesting
        void setNotificationHistory(NotificationHistory notificationHistory) {
            this.mNotificationHistory = notificationHistory;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (NotificationHistoryDatabase.DEBUG) {
                Slog.d(NotificationHistoryDatabase.TAG, "RemoveConversationRunnable " + this.mPkg + " " + this.mConversationIds);
            }
            synchronized (NotificationHistoryDatabase.this.mLock) {
                NotificationHistoryDatabase.this.mBuffer.removeConversationsFromWrite(this.mPkg, this.mConversationIds);
                for (AtomicFile atomicFile : NotificationHistoryDatabase.this.mHistoryFiles) {
                    try {
                        NotificationHistory notificationHistory = this.mNotificationHistory;
                        if (notificationHistory == null) {
                            notificationHistory = new NotificationHistory();
                        }
                        NotificationHistoryDatabase.readLocked(atomicFile, notificationHistory, new NotificationHistoryFilter.Builder().build());
                        if (notificationHistory.removeConversationsFromWrite(this.mPkg, this.mConversationIds)) {
                            NotificationHistoryDatabase.this.writeLocked(atomicFile, notificationHistory);
                        }
                    } catch (Exception e) {
                        Slog.e(NotificationHistoryDatabase.TAG, "Cannot clean up file on conversation removal " + atomicFile.getBaseFile().getName(), e);
                    }
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class RemoveChannelRunnable implements Runnable {
        private String mChannelId;
        private NotificationHistory mNotificationHistory;
        private String mPkg;

        RemoveChannelRunnable(String str, String str2) {
            this.mPkg = str;
            this.mChannelId = str2;
        }

        @VisibleForTesting
        void setNotificationHistory(NotificationHistory notificationHistory) {
            this.mNotificationHistory = notificationHistory;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (NotificationHistoryDatabase.DEBUG) {
                Slog.d(NotificationHistoryDatabase.TAG, "RemoveChannelRunnable");
            }
            synchronized (NotificationHistoryDatabase.this.mLock) {
                NotificationHistoryDatabase.this.mBuffer.removeChannelFromWrite(this.mPkg, this.mChannelId);
                for (AtomicFile atomicFile : NotificationHistoryDatabase.this.mHistoryFiles) {
                    try {
                        NotificationHistory notificationHistory = this.mNotificationHistory;
                        if (notificationHistory == null) {
                            notificationHistory = new NotificationHistory();
                        }
                        NotificationHistoryDatabase.readLocked(atomicFile, notificationHistory, new NotificationHistoryFilter.Builder().build());
                        if (notificationHistory.removeChannelFromWrite(this.mPkg, this.mChannelId)) {
                            NotificationHistoryDatabase.this.writeLocked(atomicFile, notificationHistory);
                        }
                    } catch (Exception e) {
                        Slog.e(NotificationHistoryDatabase.TAG, "Cannot clean up file on channel removal " + atomicFile.getBaseFile().getName(), e);
                    }
                }
            }
        }
    }
}
