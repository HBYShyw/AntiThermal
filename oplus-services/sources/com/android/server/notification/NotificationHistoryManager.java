package com.android.server.notification;

import android.app.NotificationHistory;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FunctionalUtils;
import com.android.server.IoThread;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NotificationHistoryManager {
    private static final boolean DEBUG = NotificationManagerService.DBG;

    @VisibleForTesting
    static final String DIRECTORY_PER_USER = "notification_history";
    private static final String TAG = "NotificationHistory";
    private final Context mContext;

    @VisibleForTesting
    final SettingsObserver mSettingsObserver;
    private final UserManager mUserManager;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final SparseArray<NotificationHistoryDatabase> mUserState = new SparseArray<>();

    @GuardedBy({"mLock"})
    private final SparseBooleanArray mUserUnlockedStates = new SparseBooleanArray();

    @GuardedBy({"mLock"})
    private final SparseArray<List<String>> mUserPendingPackageRemovals = new SparseArray<>();

    @GuardedBy({"mLock"})
    private final SparseBooleanArray mHistoryEnabled = new SparseBooleanArray();

    @GuardedBy({"mLock"})
    private final SparseBooleanArray mUserPendingHistoryDisables = new SparseBooleanArray();
    private INotificationHistoryManagerExt mNHM = (INotificationHistoryManagerExt) ExtLoader.type(INotificationHistoryManagerExt.class).base(this).create();

    public NotificationHistoryManager(Context context, Handler handler) {
        this.mContext = context;
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mSettingsObserver = new SettingsObserver(handler);
    }

    @VisibleForTesting
    void onDestroy() {
        this.mSettingsObserver.stopObserving();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBootPhaseAppsCanStart() {
        try {
            NotificationHistoryJobService.scheduleJob(this.mContext);
        } catch (Throwable th) {
            Slog.e(TAG, "Failed to schedule cleanup job", th);
        }
        this.mSettingsObserver.observe();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUserUnlocked(int i) {
        synchronized (this.mLock) {
            this.mUserUnlockedStates.put(i, true);
            NotificationHistoryDatabase userHistoryAndInitializeIfNeededLocked = getUserHistoryAndInitializeIfNeededLocked(i);
            if (userHistoryAndInitializeIfNeededLocked == null) {
                Slog.i(TAG, "Attempted to unlock gone/disabled user " + i);
                return;
            }
            List<String> list = this.mUserPendingPackageRemovals.get(i);
            if (list != null) {
                for (int i2 = 0; i2 < list.size(); i2++) {
                    userHistoryAndInitializeIfNeededLocked.onPackageRemoved(list.get(i2));
                }
                this.mUserPendingPackageRemovals.put(i, null);
            }
            if (this.mUserPendingHistoryDisables.get(i)) {
                disableHistory(userHistoryAndInitializeIfNeededLocked, i);
            }
        }
    }

    public void onUserStopped(int i) {
        synchronized (this.mLock) {
            this.mUserUnlockedStates.put(i, false);
            this.mUserState.put(i, null);
        }
    }

    public void onUserRemoved(int i) {
        synchronized (this.mLock) {
            this.mUserPendingPackageRemovals.put(i, null);
            this.mHistoryEnabled.put(i, false);
            this.mUserPendingHistoryDisables.put(i, false);
            onUserStopped(i);
        }
    }

    public void onPackageRemoved(int i, String str) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.get(i, false)) {
                if (this.mHistoryEnabled.get(i, false)) {
                    List<String> list = this.mUserPendingPackageRemovals.get(i, new ArrayList());
                    list.add(str);
                    this.mUserPendingPackageRemovals.put(i, list);
                }
                return;
            }
            NotificationHistoryDatabase notificationHistoryDatabase = this.mUserState.get(i);
            if (notificationHistoryDatabase == null) {
                return;
            }
            notificationHistoryDatabase.onPackageRemoved(str);
        }
    }

    public void cleanupHistoryFiles() {
        NotificationHistoryDatabase notificationHistoryDatabase;
        synchronized (this.mLock) {
            int size = this.mUserUnlockedStates.size();
            for (int i = 0; i < size; i++) {
                if (this.mUserUnlockedStates.valueAt(i) && (notificationHistoryDatabase = this.mUserState.get(this.mUserUnlockedStates.keyAt(i))) != null) {
                    notificationHistoryDatabase.prune();
                }
            }
        }
    }

    public void deleteNotificationHistoryItem(String str, int i, long j) {
        synchronized (this.mLock) {
            int userId = UserHandle.getUserId(i);
            NotificationHistoryDatabase userHistoryAndInitializeIfNeededLocked = getUserHistoryAndInitializeIfNeededLocked(userId);
            if (userHistoryAndInitializeIfNeededLocked == null) {
                Slog.w(TAG, "Attempted to remove notif for locked/gone/disabled user " + userId);
                return;
            }
            userHistoryAndInitializeIfNeededLocked.deleteNotificationHistoryItem(str, j);
        }
    }

    public void deleteConversations(String str, int i, Set<String> set) {
        synchronized (this.mLock) {
            int userId = UserHandle.getUserId(i);
            NotificationHistoryDatabase userHistoryAndInitializeIfNeededLocked = getUserHistoryAndInitializeIfNeededLocked(userId);
            if (userHistoryAndInitializeIfNeededLocked == null) {
                Slog.w(TAG, "Attempted to remove conversation for locked/gone/disabled user " + userId);
                return;
            }
            userHistoryAndInitializeIfNeededLocked.deleteConversations(str, set);
        }
    }

    public void deleteNotificationChannel(String str, int i, String str2) {
        synchronized (this.mLock) {
            int userId = UserHandle.getUserId(i);
            NotificationHistoryDatabase userHistoryAndInitializeIfNeededLocked = getUserHistoryAndInitializeIfNeededLocked(userId);
            if (userHistoryAndInitializeIfNeededLocked == null) {
                Slog.w(TAG, "Attempted to remove channel for locked/gone/disabled user " + userId);
                return;
            }
            userHistoryAndInitializeIfNeededLocked.deleteNotificationChannel(str, str2);
        }
    }

    public void triggerWriteToDisk() {
        NotificationHistoryDatabase notificationHistoryDatabase;
        synchronized (this.mLock) {
            int size = this.mUserState.size();
            for (int i = 0; i < size; i++) {
                int keyAt = this.mUserState.keyAt(i);
                if (this.mUserUnlockedStates.get(keyAt) && (notificationHistoryDatabase = this.mUserState.get(keyAt)) != null) {
                    notificationHistoryDatabase.forceWriteToDisk();
                }
            }
        }
    }

    public void addNotification(final NotificationHistory.HistoricalNotification historicalNotification) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationHistoryManager$$ExternalSyntheticLambda0
            public final void runOrThrow() {
                NotificationHistoryManager.this.lambda$addNotification$0(historicalNotification);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addNotification$0(NotificationHistory.HistoricalNotification historicalNotification) throws Exception {
        synchronized (this.mLock) {
            NotificationHistoryDatabase userHistoryAndInitializeIfNeededLocked = getUserHistoryAndInitializeIfNeededLocked(historicalNotification.getUserId());
            if (userHistoryAndInitializeIfNeededLocked == null) {
                Slog.w(TAG, "Attempted to add notif for locked/gone/disabled user " + historicalNotification.getUserId());
                return;
            }
            userHistoryAndInitializeIfNeededLocked.addNotification(historicalNotification);
        }
    }

    public NotificationHistory readNotificationHistory(int[] iArr) {
        synchronized (this.mLock) {
            NotificationHistory notificationHistory = new NotificationHistory();
            if (iArr == null) {
                return notificationHistory;
            }
            for (int i : iArr) {
                NotificationHistoryDatabase userHistoryAndInitializeIfNeededLocked = getUserHistoryAndInitializeIfNeededLocked(i);
                if (userHistoryAndInitializeIfNeededLocked == null) {
                    Slog.i(TAG, "Attempted to read history for locked/gone/disabled user " + i);
                } else {
                    notificationHistory.addNotificationsToWrite(userHistoryAndInitializeIfNeededLocked.readNotificationHistory());
                }
            }
            return notificationHistory;
        }
    }

    public NotificationHistory readFilteredNotificationHistory(int i, String str, String str2, int i2) {
        synchronized (this.mLock) {
            NotificationHistoryDatabase userHistoryAndInitializeIfNeededLocked = getUserHistoryAndInitializeIfNeededLocked(i);
            if (userHistoryAndInitializeIfNeededLocked == null) {
                Slog.i(TAG, "Attempted to read history for locked/gone/disabled user " + i);
                return new NotificationHistory();
            }
            return userHistoryAndInitializeIfNeededLocked.readNotificationHistory(str, str2, i2);
        }
    }

    boolean isHistoryEnabled(int i) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mHistoryEnabled.get(i);
        }
        return z;
    }

    void onHistoryEnabledChanged(int i, boolean z) {
        synchronized (this.mLock) {
            if (z) {
                this.mHistoryEnabled.put(i, z);
            }
            NotificationHistoryDatabase userHistoryAndInitializeIfNeededLocked = getUserHistoryAndInitializeIfNeededLocked(i);
            if (userHistoryAndInitializeIfNeededLocked == null) {
                this.mUserPendingHistoryDisables.put(i, !z);
            } else if (!z) {
                disableHistory(userHistoryAndInitializeIfNeededLocked, i);
            }
        }
    }

    private void disableHistory(NotificationHistoryDatabase notificationHistoryDatabase, int i) {
        notificationHistoryDatabase.disableHistory();
        this.mUserPendingHistoryDisables.put(i, false);
        this.mHistoryEnabled.put(i, false);
        this.mUserState.put(i, null);
    }

    @GuardedBy({"mLock"})
    private NotificationHistoryDatabase getUserHistoryAndInitializeIfNeededLocked(int i) {
        if (!this.mHistoryEnabled.get(i)) {
            if (DEBUG) {
                Slog.i(TAG, "History disabled for user " + i);
            }
            this.mUserState.put(i, null);
            return null;
        }
        NotificationHistoryDatabase notificationHistoryDatabase = this.mUserState.get(i);
        if (notificationHistoryDatabase == null) {
            notificationHistoryDatabase = NotificationHistoryDatabaseFactory.create(this.mContext, IoThread.getHandler(), new File(Environment.getDataSystemCeDirectory(i), DIRECTORY_PER_USER));
            if (this.mUserUnlockedStates.get(i)) {
                try {
                    notificationHistoryDatabase.init();
                    this.mUserState.put(i, notificationHistoryDatabase);
                } catch (Exception e) {
                    if (this.mUserManager.isUserUnlocked(i)) {
                        throw e;
                    }
                    Slog.w(TAG, "Attempted to initialize service for stopped or removed user " + i);
                    return null;
                }
            } else {
                Slog.w(TAG, "Attempted to initialize service for stopped or removed user " + i);
                return null;
            }
        }
        return notificationHistoryDatabase;
    }

    @VisibleForTesting
    boolean isUserUnlocked(int i) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mUserUnlockedStates.get(i);
        }
        return z;
    }

    @VisibleForTesting
    boolean doesHistoryExistForUser(int i) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mUserState.get(i) != null;
        }
        return z;
    }

    @VisibleForTesting
    void replaceNotificationHistoryDatabase(int i, NotificationHistoryDatabase notificationHistoryDatabase) {
        synchronized (this.mLock) {
            if (this.mUserState.get(i) != null) {
                this.mUserState.put(i, notificationHistoryDatabase);
            }
        }
    }

    @VisibleForTesting
    List<String> getPendingPackageRemovalsForUser(int i) {
        List<String> list;
        synchronized (this.mLock) {
            list = this.mUserPendingPackageRemovals.get(i);
        }
        return list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class SettingsObserver extends ContentObserver {
        private final Uri NOTIFICATION_HISTORY_URI;

        SettingsObserver(Handler handler) {
            super(handler);
            this.NOTIFICATION_HISTORY_URI = Settings.Secure.getUriFor("notification_history_enabled");
        }

        void observe() {
            NotificationHistoryManager.this.mContext.getContentResolver().registerContentObserver(this.NOTIFICATION_HISTORY_URI, false, this, -1);
            synchronized (NotificationHistoryManager.this.mLock) {
                for (UserInfo userInfo : NotificationHistoryManager.this.mUserManager.getUsers()) {
                    if (!userInfo.isProfile()) {
                        update(null, userInfo.id);
                    }
                }
            }
        }

        void stopObserving() {
            NotificationHistoryManager.this.mContext.getContentResolver().unregisterContentObserver(this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri, int i) {
            update(uri, i);
        }

        public void update(Uri uri, int i) {
            ContentResolver contentResolver = NotificationHistoryManager.this.mContext.getContentResolver();
            if (uri == null || this.NOTIFICATION_HISTORY_URI.equals(uri)) {
                boolean z = Settings.Secure.getIntForUser(contentResolver, "notification_history_enabled", 0, i) != 0;
                for (int i2 : NotificationHistoryManager.this.mUserManager.getProfileIds(i, true)) {
                    NotificationHistoryManager.this.onHistoryEnabledChanged(i2, z);
                    NotificationHistoryManager.this.mNHM.update(i2, z);
                }
            }
        }
    }
}
