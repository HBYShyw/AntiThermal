package com.android.server.backup;

import android.content.ContentResolver;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.KeyValueListParser;
import android.util.KeyValueSettingObserver;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BackupAgentTimeoutParameters extends KeyValueSettingObserver {

    @VisibleForTesting
    public static final long DEFAULT_FULL_BACKUP_AGENT_TIMEOUT_MILLIS = 300000;

    @VisibleForTesting
    public static final long DEFAULT_KV_BACKUP_AGENT_TIMEOUT_MILLIS = 30000;

    @VisibleForTesting
    public static final long DEFAULT_QUOTA_EXCEEDED_TIMEOUT_MILLIS = 3000;

    @VisibleForTesting
    public static final long DEFAULT_RESTORE_AGENT_FINISHED_TIMEOUT_MILLIS = 30000;

    @VisibleForTesting
    public static final long DEFAULT_RESTORE_AGENT_TIMEOUT_MILLIS = 60000;

    @VisibleForTesting
    public static final long DEFAULT_RESTORE_SESSION_TIMEOUT_MILLIS = 60000;

    @VisibleForTesting
    public static final long DEFAULT_RESTORE_SYSTEM_AGENT_TIMEOUT_MILLIS = 180000;

    @VisibleForTesting
    public static final long DEFAULT_SHARED_BACKUP_AGENT_TIMEOUT_MILLIS = 1800000;

    @VisibleForTesting
    public static final String SETTING = "backup_agent_timeout_parameters";

    @VisibleForTesting
    public static final String SETTING_FULL_BACKUP_AGENT_TIMEOUT_MILLIS = "full_backup_agent_timeout_millis";

    @VisibleForTesting
    public static final String SETTING_KV_BACKUP_AGENT_TIMEOUT_MILLIS = "kv_backup_agent_timeout_millis";

    @VisibleForTesting
    public static final String SETTING_QUOTA_EXCEEDED_TIMEOUT_MILLIS = "quota_exceeded_timeout_millis";

    @VisibleForTesting
    public static final String SETTING_RESTORE_AGENT_FINISHED_TIMEOUT_MILLIS = "restore_agent_finished_timeout_millis";

    @VisibleForTesting
    public static final String SETTING_RESTORE_AGENT_TIMEOUT_MILLIS = "restore_agent_timeout_millis";

    @VisibleForTesting
    public static final String SETTING_RESTORE_SESSION_TIMEOUT_MILLIS = "restore_session_timeout_millis";

    @VisibleForTesting
    public static final String SETTING_RESTORE_SYSTEM_AGENT_TIMEOUT_MILLIS = "restore_system_agent_timeout_millis";

    @VisibleForTesting
    public static final String SETTING_SHARED_BACKUP_AGENT_TIMEOUT_MILLIS = "shared_backup_agent_timeout_millis";

    @GuardedBy({"mLock"})
    private long mFullBackupAgentTimeoutMillis;

    @GuardedBy({"mLock"})
    private long mKvBackupAgentTimeoutMillis;
    private final Object mLock;

    @GuardedBy({"mLock"})
    private long mQuotaExceededTimeoutMillis;

    @GuardedBy({"mLock"})
    private long mRestoreAgentFinishedTimeoutMillis;

    @GuardedBy({"mLock"})
    private long mRestoreAgentTimeoutMillis;

    @GuardedBy({"mLock"})
    private long mRestoreSessionTimeoutMillis;

    @GuardedBy({"mLock"})
    private long mRestoreSystemAgentTimeoutMillis;

    @GuardedBy({"mLock"})
    private long mSharedBackupAgentTimeoutMillis;

    public BackupAgentTimeoutParameters(Handler handler, ContentResolver contentResolver) {
        super(handler, contentResolver, Settings.Global.getUriFor(SETTING));
        this.mLock = new Object();
    }

    public String getSettingValue(ContentResolver contentResolver) {
        return Settings.Global.getString(contentResolver, SETTING);
    }

    public void update(KeyValueListParser keyValueListParser) {
        synchronized (this.mLock) {
            this.mKvBackupAgentTimeoutMillis = keyValueListParser.getLong(SETTING_KV_BACKUP_AGENT_TIMEOUT_MILLIS, 30000L);
            this.mFullBackupAgentTimeoutMillis = keyValueListParser.getLong(SETTING_FULL_BACKUP_AGENT_TIMEOUT_MILLIS, 300000L);
            this.mSharedBackupAgentTimeoutMillis = keyValueListParser.getLong(SETTING_SHARED_BACKUP_AGENT_TIMEOUT_MILLIS, 1800000L);
            this.mRestoreAgentTimeoutMillis = keyValueListParser.getLong(SETTING_RESTORE_AGENT_TIMEOUT_MILLIS, 60000L);
            this.mRestoreSystemAgentTimeoutMillis = keyValueListParser.getLong(SETTING_RESTORE_SYSTEM_AGENT_TIMEOUT_MILLIS, 180000L);
            this.mRestoreAgentFinishedTimeoutMillis = keyValueListParser.getLong(SETTING_RESTORE_AGENT_FINISHED_TIMEOUT_MILLIS, 30000L);
            this.mRestoreSessionTimeoutMillis = keyValueListParser.getLong(SETTING_RESTORE_SESSION_TIMEOUT_MILLIS, 60000L);
            this.mQuotaExceededTimeoutMillis = keyValueListParser.getLong(SETTING_QUOTA_EXCEEDED_TIMEOUT_MILLIS, 3000L);
        }
    }

    public long getKvBackupAgentTimeoutMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mKvBackupAgentTimeoutMillis;
        }
        return j;
    }

    public long getFullBackupAgentTimeoutMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mFullBackupAgentTimeoutMillis;
        }
        return j;
    }

    public long getSharedBackupAgentTimeoutMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mSharedBackupAgentTimeoutMillis;
        }
        return j;
    }

    public long getRestoreAgentTimeoutMillis(int i) {
        long j;
        synchronized (this.mLock) {
            j = UserHandle.isCore(i) ? this.mRestoreSystemAgentTimeoutMillis : this.mRestoreAgentTimeoutMillis;
        }
        return j;
    }

    public long getRestoreSessionTimeoutMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mRestoreSessionTimeoutMillis;
        }
        return j;
    }

    public long getRestoreAgentFinishedTimeoutMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mRestoreAgentFinishedTimeoutMillis;
        }
        return j;
    }

    public long getQuotaExceededTimeoutMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.mQuotaExceededTimeoutMillis;
        }
        return j;
    }
}
