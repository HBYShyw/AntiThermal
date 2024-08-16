package com.android.server.backup;

import android.app.IWallpaperManager;
import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupHelper;
import android.app.backup.FullBackup;
import android.app.backup.FullBackupDataOutput;
import android.app.backup.WallpaperBackupHelper;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArraySet;
import android.util.Slog;
import com.android.server.am.IOplusSceneManager;
import com.google.android.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SystemBackupAgent extends BackupAgentHelper {
    private static final String ACCOUNT_MANAGER_HELPER = "account_manager";
    private static final String APP_GENDER_HELPER = "app_gender";
    private static final String APP_LOCALES_HELPER = "app_locales";
    private static final String NOTIFICATION_HELPER = "notifications";
    private static final String PEOPLE_HELPER = "people";
    private static final String PERMISSION_HELPER = "permissions";
    private static final String PREFERRED_HELPER = "preferred_activities";
    private static final String SHORTCUT_MANAGER_HELPER = "shortcut_manager";
    private static final String SLICES_HELPER = "slices";
    private static final String SYNC_SETTINGS_HELPER = "account_sync_settings";
    private static final String TAG = "SystemBackupAgent";
    private static final String USAGE_STATS_HELPER = "usage_stats";
    private static final String WALLPAPER_HELPER = "wallpaper";
    private static final String WALLPAPER_IMAGE_FILENAME = "wallpaper";
    private static final String WALLPAPER_IMAGE_KEY = "/data/data/com.android.settings/files/wallpaper";
    private static final Set<String> sEligibleHelpersForNonSystemUser;
    private static final Set<String> sEligibleHelpersForProfileUser;
    private static final String WALLPAPER_IMAGE_DIR = Environment.getUserSystemDirectory(0).getAbsolutePath();
    public static final String WALLPAPER_IMAGE = new File(Environment.getUserSystemDirectory(0), IOplusSceneManager.APP_SCENE_DEFAULT_LIVE_WALLPAPER).getAbsolutePath();
    private static final String WALLPAPER_INFO_DIR = Environment.getUserSystemDirectory(0).getAbsolutePath();
    private static final String WALLPAPER_INFO_FILENAME = "wallpaper_info.xml";
    public static final String WALLPAPER_INFO = new File(Environment.getUserSystemDirectory(0), WALLPAPER_INFO_FILENAME).getAbsolutePath();
    private int mUserId = 0;
    private boolean mIsProfileUser = false;

    @Override // android.app.backup.BackupAgent
    public void onFullBackup(FullBackupDataOutput fullBackupDataOutput) throws IOException {
    }

    static {
        ArraySet newArraySet = Sets.newArraySet(new String[]{PERMISSION_HELPER, NOTIFICATION_HELPER, SYNC_SETTINGS_HELPER, APP_LOCALES_HELPER});
        sEligibleHelpersForProfileUser = newArraySet;
        sEligibleHelpersForNonSystemUser = SetUtils.union(newArraySet, Sets.newArraySet(new String[]{ACCOUNT_MANAGER_HELPER, USAGE_STATS_HELPER, PREFERRED_HELPER, SHORTCUT_MANAGER_HELPER}));
    }

    public void onCreate(UserHandle userHandle, int i) {
        super.onCreate(userHandle, i);
        int identifier = userHandle.getIdentifier();
        this.mUserId = identifier;
        if (identifier != 0) {
            this.mIsProfileUser = ((UserManager) createContextAsUser(userHandle, 0).getSystemService(UserManager.class)).isProfile();
        }
        addHelperIfEligibleForUser(SYNC_SETTINGS_HELPER, new AccountSyncSettingsBackupHelper(this, this.mUserId));
        addHelperIfEligibleForUser(PREFERRED_HELPER, new PreferredActivityBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(NOTIFICATION_HELPER, new NotificationBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(PERMISSION_HELPER, new PermissionBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(USAGE_STATS_HELPER, new UsageStatsBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(SHORTCUT_MANAGER_HELPER, new ShortcutBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(ACCOUNT_MANAGER_HELPER, new AccountManagerBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(SLICES_HELPER, new SliceBackupHelper(this));
        addHelperIfEligibleForUser(PEOPLE_HELPER, new PeopleBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(APP_LOCALES_HELPER, new AppSpecificLocalesBackupHelper(this.mUserId));
        addHelperIfEligibleForUser(APP_GENDER_HELPER, new AppGrammaticalGenderBackupHelper(this.mUserId));
    }

    @Override // android.app.backup.BackupAgentHelper, android.app.backup.BackupAgent
    public void onRestore(BackupDataInput backupDataInput, int i, ParcelFileDescriptor parcelFileDescriptor) throws IOException {
        addHelper(IOplusSceneManager.APP_SCENE_DEFAULT_LIVE_WALLPAPER, new WallpaperBackupHelper(this, new String[]{WALLPAPER_IMAGE_KEY}));
        addHelper("system_files", new WallpaperBackupHelper(this, new String[]{WALLPAPER_IMAGE_KEY}));
        super.onRestore(backupDataInput, i, parcelFileDescriptor);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0053 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onRestoreFile(ParcelFileDescriptor parcelFileDescriptor, long j, int i, String str, String str2, long j2, long j3) throws IOException {
        boolean z;
        File file;
        File file2;
        IWallpaperManager service;
        Slog.i(TAG, "Restoring file domain=" + str + " path=" + str2);
        if (str.equals("r")) {
            z = true;
            if (str2.equals(WALLPAPER_INFO_FILENAME)) {
                file = new File(WALLPAPER_INFO);
            } else if (str2.equals(IOplusSceneManager.APP_SCENE_DEFAULT_LIVE_WALLPAPER)) {
                file = new File(WALLPAPER_IMAGE);
            }
            file2 = file;
            if (file2 == null) {
                try {
                    Slog.w(TAG, "Skipping unrecognized system file: [ " + str + " : " + str2 + " ]");
                } catch (IOException unused) {
                    if (z) {
                        new File(WALLPAPER_IMAGE).delete();
                        new File(WALLPAPER_INFO).delete();
                        return;
                    }
                    return;
                }
            }
            FullBackup.restoreFile(parcelFileDescriptor, j, i, j2, j3, file2);
            if (z || (service = ServiceManager.getService(IOplusSceneManager.APP_SCENE_DEFAULT_LIVE_WALLPAPER)) == null) {
            }
            try {
                service.settingsRestored();
                return;
            } catch (RemoteException e) {
                Slog.e(TAG, "Couldn't restore settings\n" + e);
                return;
            }
        }
        z = false;
        file = null;
        file2 = file;
        if (file2 == null) {
        }
        FullBackup.restoreFile(parcelFileDescriptor, j, i, j2, j3, file2);
        if (z) {
        }
    }

    private void addHelperIfEligibleForUser(String str, BackupHelper backupHelper) {
        if (isHelperEligibleForUser(str)) {
            addHelper(str, backupHelper);
        }
    }

    private boolean isHelperEligibleForUser(String str) {
        if (this.mUserId == 0) {
            return true;
        }
        if (this.mIsProfileUser) {
            return sEligibleHelpersForProfileUser.contains(str);
        }
        return sEligibleHelpersForNonSystemUser.contains(str);
    }
}
