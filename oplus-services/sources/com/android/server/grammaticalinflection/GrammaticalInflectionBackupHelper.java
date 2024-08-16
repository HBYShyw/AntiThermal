package com.android.server.grammaticalinflection;

import android.app.backup.BackupManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.util.Log;
import android.util.SparseArray;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Clock;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class GrammaticalInflectionBackupHelper {
    private static final Duration STAGE_DATA_RETENTION_PERIOD = Duration.ofDays(3);
    private static final String SYSTEM_BACKUP_PACKAGE_KEY = "android";
    private static final String TAG = "GrammaticalInflectionBackupHelper";
    private final SparseArray<StagedData> mCache = new SparseArray<>();
    private final Object mCacheLock = new Object();
    private final Clock mClock = Clock.systemUTC();
    private final GrammaticalInflectionService mGrammaticalGenderService;
    private final PackageManager mPackageManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class StagedData {
        final long mCreationTimeMillis;
        final HashMap<String, Integer> mPackageStates = new HashMap<>();

        StagedData(long j) {
            this.mCreationTimeMillis = j;
        }
    }

    public GrammaticalInflectionBackupHelper(GrammaticalInflectionService grammaticalInflectionService, PackageManager packageManager) {
        this.mGrammaticalGenderService = grammaticalInflectionService;
        this.mPackageManager = packageManager;
    }

    public byte[] getBackupPayload(int i) {
        synchronized (this.mCacheLock) {
            cleanStagedDataForOldEntries();
        }
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (ApplicationInfo applicationInfo : this.mPackageManager.getInstalledApplicationsAsUser(PackageManager.ApplicationInfoFlags.of(0L), i)) {
            int applicationGrammaticalGender = this.mGrammaticalGenderService.getApplicationGrammaticalGender(applicationInfo.packageName, i);
            if (applicationGrammaticalGender != 0) {
                hashMap.put(applicationInfo.packageName, Integer.valueOf(applicationGrammaticalGender));
            }
        }
        if (hashMap.isEmpty()) {
            return null;
        }
        return convertToByteArray(hashMap);
    }

    public void stageAndApplyRestoredPayload(byte[] bArr, int i) {
        synchronized (this.mCacheLock) {
            cleanStagedDataForOldEntries();
            HashMap<String, Integer> readFromByteArray = readFromByteArray(bArr);
            if (readFromByteArray.isEmpty()) {
                return;
            }
            StagedData stagedData = new StagedData(this.mClock.millis());
            for (Map.Entry<String, Integer> entry : readFromByteArray.entrySet()) {
                if (isPackageInstalledForUser(entry.getKey(), i)) {
                    if (!hasSetBeforeRestoring(entry.getKey(), i)) {
                        this.mGrammaticalGenderService.setRequestedApplicationGrammaticalGender(entry.getKey(), i, entry.getValue().intValue());
                    }
                } else if (entry.getValue().intValue() != 0) {
                    stagedData.mPackageStates.put(entry.getKey(), entry.getValue());
                }
            }
            this.mCache.append(i, stagedData);
        }
    }

    private boolean hasSetBeforeRestoring(String str, int i) {
        return this.mGrammaticalGenderService.getApplicationGrammaticalGender(str, i) != 0;
    }

    public void onPackageAdded(String str, int i) {
        int intValue;
        synchronized (this.mCacheLock) {
            int userId = UserHandle.getUserId(i);
            StagedData stagedData = this.mCache.get(userId);
            if (stagedData != null && stagedData.mPackageStates.containsKey(str) && (intValue = stagedData.mPackageStates.get(str).intValue()) != 0) {
                this.mGrammaticalGenderService.setRequestedApplicationGrammaticalGender(str, userId, intValue);
            }
        }
    }

    public void onPackageDataCleared() {
        notifyBackupManager();
    }

    public void onPackageRemoved() {
        notifyBackupManager();
    }

    public static void notifyBackupManager() {
        BackupManager.dataChanged(SYSTEM_BACKUP_PACKAGE_KEY);
    }

    private byte[] convertToByteArray(HashMap<String, Integer> hashMap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                try {
                    objectOutputStream.writeObject(hashMap);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    objectOutputStream.close();
                    byteArrayOutputStream.close();
                    return byteArray;
                } finally {
                }
            } finally {
            }
        } catch (IOException e) {
            Log.e(TAG, "cannot convert payload to byte array.", e);
            return null;
        }
    }

    private HashMap<String, Integer> readFromByteArray(byte[] bArr) {
        HashMap<String, Integer> hashMap;
        Exception e;
        Throwable th;
        HashMap<String, Integer> hashMap2 = new HashMap<>();
        try {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    try {
                        hashMap = (HashMap) objectInputStream.readObject();
                        try {
                            objectInputStream.close();
                            byteArrayInputStream.close();
                        } catch (Throwable th2) {
                            th = th2;
                            try {
                                byteArrayInputStream.close();
                            } catch (Throwable th3) {
                                th.addSuppressed(th3);
                            }
                            throw th;
                        }
                    } finally {
                    }
                } catch (Throwable th4) {
                    th = th4;
                    byteArrayInputStream.close();
                    throw th;
                }
            } catch (IOException | ClassNotFoundException e2) {
                hashMap = hashMap2;
                e = e2;
                Log.e(TAG, "cannot convert payload to HashMap.", e);
                e.printStackTrace();
                return hashMap;
            }
        } catch (IOException | ClassNotFoundException e3) {
            e = e3;
            Log.e(TAG, "cannot convert payload to HashMap.", e);
            e.printStackTrace();
            return hashMap;
        }
        return hashMap;
    }

    private void cleanStagedDataForOldEntries() {
        for (int i = 0; i < this.mCache.size(); i++) {
            int keyAt = this.mCache.keyAt(i);
            if (this.mCache.get(keyAt).mCreationTimeMillis < this.mClock.millis() - STAGE_DATA_RETENTION_PERIOD.toMillis()) {
                this.mCache.remove(keyAt);
            }
        }
    }

    private boolean isPackageInstalledForUser(String str, int i) {
        PackageInfo packageInfo;
        try {
            packageInfo = this.mPackageManager.getPackageInfoAsUser(str, 0, i);
        } catch (PackageManager.NameNotFoundException unused) {
            packageInfo = null;
        }
        return packageInfo != null;
    }
}
