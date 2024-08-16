package com.android.server.backup.internal;

import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.server.backup.BackupRestoreTask;
import com.android.server.backup.OperationStorage;
import com.google.android.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class LifecycleOperationStorage implements OperationStorage {
    private static final String TAG = "LifecycleOperationStorage";
    private final int mUserId;
    private final Object mOperationsLock = new Object();

    @GuardedBy({"mOperationsLock"})
    private final SparseArray<Operation> mOperations = new SparseArray<>();

    @GuardedBy({"mOperationsLock"})
    private final Map<String, Set<Integer>> mOpTokensByPackage = new HashMap();

    public LifecycleOperationStorage(int i) {
        this.mUserId = i;
    }

    @Override // com.android.server.backup.OperationStorage
    public void registerOperation(int i, int i2, BackupRestoreTask backupRestoreTask, int i3) {
        registerOperationForPackages(i, i2, Sets.newHashSet(), backupRestoreTask, i3);
    }

    @Override // com.android.server.backup.OperationStorage
    public void registerOperationForPackages(int i, int i2, Set<String> set, BackupRestoreTask backupRestoreTask, int i3) {
        synchronized (this.mOperationsLock) {
            this.mOperations.put(i, new Operation(i2, backupRestoreTask, i3));
            for (String str : set) {
                Set<Integer> set2 = this.mOpTokensByPackage.get(str);
                if (set2 == null) {
                    set2 = new HashSet<>();
                }
                set2.add(Integer.valueOf(i));
                this.mOpTokensByPackage.put(str, set2);
            }
        }
    }

    @Override // com.android.server.backup.OperationStorage
    public void removeOperation(int i) {
        synchronized (this.mOperationsLock) {
            this.mOperations.remove(i);
            for (String str : this.mOpTokensByPackage.keySet()) {
                Set<Integer> set = this.mOpTokensByPackage.get(str);
                if (set != null) {
                    set.remove(Integer.valueOf(i));
                    this.mOpTokensByPackage.put(str, set);
                }
            }
        }
    }

    @Override // com.android.server.backup.OperationStorage
    public int numOperations() {
        int size;
        synchronized (this.mOperationsLock) {
            size = this.mOperations.size();
        }
        return size;
    }

    @Override // com.android.server.backup.OperationStorage
    public boolean isBackupOperationInProgress() {
        synchronized (this.mOperationsLock) {
            for (int i = 0; i < this.mOperations.size(); i++) {
                Operation valueAt = this.mOperations.valueAt(i);
                if (valueAt.type == 2 && valueAt.state == 0) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override // com.android.server.backup.OperationStorage
    public Set<Integer> operationTokensForPackage(String str) {
        HashSet newHashSet;
        synchronized (this.mOperationsLock) {
            Set<Integer> set = this.mOpTokensByPackage.get(str);
            newHashSet = Sets.newHashSet();
            if (set != null) {
                newHashSet.addAll(set);
            }
        }
        return newHashSet;
    }

    @Override // com.android.server.backup.OperationStorage
    public Set<Integer> operationTokensForOpType(int i) {
        HashSet newHashSet = Sets.newHashSet();
        synchronized (this.mOperationsLock) {
            for (int i2 = 0; i2 < this.mOperations.size(); i2++) {
                Operation valueAt = this.mOperations.valueAt(i2);
                int keyAt = this.mOperations.keyAt(i2);
                if (valueAt.type == i) {
                    newHashSet.add(Integer.valueOf(keyAt));
                }
            }
        }
        return newHashSet;
    }

    @Override // com.android.server.backup.OperationStorage
    public Set<Integer> operationTokensForOpState(int i) {
        HashSet newHashSet = Sets.newHashSet();
        synchronized (this.mOperationsLock) {
            for (int i2 = 0; i2 < this.mOperations.size(); i2++) {
                Operation valueAt = this.mOperations.valueAt(i2);
                int keyAt = this.mOperations.keyAt(i2);
                if (valueAt.state == i) {
                    newHashSet.add(Integer.valueOf(keyAt));
                }
            }
        }
        return newHashSet;
    }

    public boolean waitUntilOperationComplete(int i, IntConsumer intConsumer) {
        Operation operation;
        int i2;
        synchronized (this.mOperationsLock) {
            while (true) {
                operation = this.mOperations.get(i);
                if (operation != null) {
                    i2 = operation.state;
                    if (i2 != 0) {
                        break;
                    }
                    try {
                        this.mOperationsLock.wait();
                    } catch (InterruptedException e) {
                        Slog.w(TAG, "Waiting on mOperationsLock: ", e);
                    }
                } else {
                    i2 = 0;
                    break;
                }
            }
        }
        removeOperation(i);
        if (operation != null) {
            intConsumer.accept(operation.type);
        }
        return i2 == 1;
    }

    public void onOperationComplete(int i, long j, Consumer<BackupRestoreTask> consumer) {
        Operation operation;
        BackupRestoreTask backupRestoreTask;
        synchronized (this.mOperationsLock) {
            operation = this.mOperations.get(i);
            if (operation != null) {
                int i2 = operation.state;
                if (i2 == -1) {
                    this.mOperations.remove(i);
                } else if (i2 == 1) {
                    Slog.w(TAG, "[UserID:" + this.mUserId + "] Received duplicate ack for token=" + Integer.toHexString(i));
                    this.mOperations.remove(i);
                } else if (i2 == 0) {
                    operation.state = 1;
                }
                operation = null;
            }
            this.mOperationsLock.notifyAll();
        }
        if (operation == null || (backupRestoreTask = operation.callback) == null) {
            return;
        }
        consumer.accept(backupRestoreTask);
    }

    public void cancelOperation(int i, final boolean z, IntConsumer intConsumer) {
        final Operation operation;
        synchronized (this.mOperationsLock) {
            operation = this.mOperations.get(i);
            int i2 = operation != null ? operation.state : -1;
            if (i2 == 1) {
                Slog.w(TAG, "[UserID:" + this.mUserId + "] Operation already got an ack.Should have been removed from mCurrentOperations.");
                this.mOperations.delete(i);
                operation = null;
            } else if (i2 == 0) {
                Slog.v(TAG, "[UserID:" + this.mUserId + "] Cancel: token=" + Integer.toHexString(i));
                operation.state = -1;
                intConsumer.accept(operation.type);
            }
            this.mOperationsLock.notifyAll();
        }
        if (operation == null || operation.callback == null) {
            return;
        }
        new Thread(new Runnable() { // from class: com.android.server.backup.internal.LifecycleOperationStorage.1
            @Override // java.lang.Runnable
            public void run() {
                operation.callback.handleCancel(z);
            }
        }, "callbackCancel").start();
    }
}
