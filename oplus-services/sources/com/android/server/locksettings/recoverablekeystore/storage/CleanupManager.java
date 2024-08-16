package com.android.server.locksettings.recoverablekeystore.storage;

import android.content.Context;
import android.os.ServiceSpecificException;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class CleanupManager {
    private static final String TAG = "CleanupManager";
    private final ApplicationKeyStorage mApplicationKeyStorage;
    private final RecoverableKeyStoreDb mDatabase;
    private Map<Integer, Long> mSerialNumbers;
    private final RecoverySnapshotStorage mSnapshotStorage;
    private final UserManager mUserManager;

    public static CleanupManager getInstance(Context context, RecoverySnapshotStorage recoverySnapshotStorage, RecoverableKeyStoreDb recoverableKeyStoreDb, ApplicationKeyStorage applicationKeyStorage) {
        return new CleanupManager(recoverySnapshotStorage, recoverableKeyStoreDb, UserManager.get(context), applicationKeyStorage);
    }

    @VisibleForTesting
    CleanupManager(RecoverySnapshotStorage recoverySnapshotStorage, RecoverableKeyStoreDb recoverableKeyStoreDb, UserManager userManager, ApplicationKeyStorage applicationKeyStorage) {
        this.mSnapshotStorage = recoverySnapshotStorage;
        this.mDatabase = recoverableKeyStoreDb;
        this.mUserManager = userManager;
        this.mApplicationKeyStorage = applicationKeyStorage;
    }

    public synchronized void registerRecoveryAgent(int i, int i2) {
        if (this.mSerialNumbers == null) {
            verifyKnownUsers();
        }
        Long l = this.mSerialNumbers.get(Integer.valueOf(i));
        if (l == null) {
            l = -1L;
        }
        if (l.longValue() != -1) {
            return;
        }
        long serialNumberForUser = this.mUserManager.getSerialNumberForUser(UserHandle.of(i));
        if (serialNumberForUser != -1) {
            storeUserSerialNumber(i, serialNumberForUser);
        }
    }

    public synchronized void verifyKnownUsers() {
        this.mSerialNumbers = this.mDatabase.getUserSerialNumbers();
        ArrayList<Integer> arrayList = new ArrayList<Integer>() { // from class: com.android.server.locksettings.recoverablekeystore.storage.CleanupManager.1
        };
        for (Map.Entry<Integer, Long> entry : this.mSerialNumbers.entrySet()) {
            Integer key = entry.getKey();
            Long value = entry.getValue();
            if (value == null) {
                value = -1L;
            }
            long serialNumberForUser = this.mUserManager.getSerialNumberForUser(UserHandle.of(key.intValue()));
            if (serialNumberForUser == -1) {
                arrayList.add(key);
                removeDataForUser(key.intValue());
            } else if (value.longValue() == -1) {
                storeUserSerialNumber(key.intValue(), serialNumberForUser);
            } else if (value.longValue() != serialNumberForUser) {
                arrayList.add(key);
                removeDataForUser(key.intValue());
                storeUserSerialNumber(key.intValue(), serialNumberForUser);
            }
        }
        Iterator<Integer> it = arrayList.iterator();
        while (it.hasNext()) {
            this.mSerialNumbers.remove(it.next());
        }
    }

    private void storeUserSerialNumber(int i, long j) {
        Log.d(TAG, "Storing serial number for user " + i + ".");
        this.mSerialNumbers.put(Integer.valueOf(i), Long.valueOf(j));
        this.mDatabase.setUserSerialNumber(i, j);
    }

    private void removeDataForUser(int i) {
        Log.d(TAG, "Removing data for user " + i + ".");
        for (Integer num : this.mDatabase.getRecoveryAgents(i)) {
            this.mSnapshotStorage.remove(num.intValue());
            removeAllKeysForRecoveryAgent(i, num.intValue());
        }
        this.mDatabase.removeUserFromAllTables(i);
    }

    private void removeAllKeysForRecoveryAgent(int i, int i2) {
        for (String str : this.mDatabase.getAllKeys(i, i2, this.mDatabase.getPlatformKeyGenerationId(i)).keySet()) {
            try {
                this.mApplicationKeyStorage.deleteEntry(i, i2, str);
            } catch (ServiceSpecificException e) {
                Log.e(TAG, "Error while removing recoverable key " + str + " : " + e);
            }
        }
    }
}
