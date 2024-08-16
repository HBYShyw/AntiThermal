package com.android.server.backup.internal;

import android.app.backup.IBackupObserver;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.EventLog;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.EventLogTags;
import com.android.server.backup.BackupManagerService;
import com.android.server.backup.TransportManager;
import com.android.server.backup.UserBackupManagerService;
import com.android.server.backup.transport.BackupTransportClient;
import com.android.server.backup.transport.TransportConnection;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class PerformInitializeTask implements Runnable {
    private final UserBackupManagerService mBackupManagerService;
    private final File mBaseStateDir;
    private final OnTaskFinishedListener mListener;
    private IBackupObserver mObserver;
    private final String[] mQueue;
    private final TransportManager mTransportManager;

    public PerformInitializeTask(UserBackupManagerService userBackupManagerService, String[] strArr, IBackupObserver iBackupObserver, OnTaskFinishedListener onTaskFinishedListener) {
        this(userBackupManagerService, userBackupManagerService.getTransportManager(), strArr, iBackupObserver, onTaskFinishedListener, userBackupManagerService.getBaseStateDir());
    }

    @VisibleForTesting
    PerformInitializeTask(UserBackupManagerService userBackupManagerService, TransportManager transportManager, String[] strArr, IBackupObserver iBackupObserver, OnTaskFinishedListener onTaskFinishedListener, File file) {
        this.mBackupManagerService = userBackupManagerService;
        this.mTransportManager = transportManager;
        this.mQueue = strArr;
        this.mObserver = iBackupObserver;
        this.mListener = onTaskFinishedListener;
        this.mBaseStateDir = file;
    }

    private void notifyResult(String str, int i) {
        try {
            IBackupObserver iBackupObserver = this.mObserver;
            if (iBackupObserver != null) {
                iBackupObserver.onResult(str, i);
            }
        } catch (RemoteException unused) {
            this.mObserver = null;
        }
    }

    private void notifyFinished(int i) {
        try {
            IBackupObserver iBackupObserver = this.mObserver;
            if (iBackupObserver != null) {
                iBackupObserver.backupFinished(i);
            }
        } catch (RemoteException unused) {
            this.mObserver = null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.Runnable
    public void run() {
        ArrayList<TransportConnection> arrayList = new ArrayList(this.mQueue.length);
        int i = 0;
        try {
            try {
                int i2 = 0;
                for (String str : this.mQueue) {
                    try {
                        TransportConnection transportClient = this.mTransportManager.getTransportClient(str, "PerformInitializeTask.run()");
                        if (transportClient == null) {
                            Slog.e(BackupManagerService.TAG, "Requested init for " + str + " but not found");
                        } else {
                            arrayList.add(transportClient);
                            Slog.i(BackupManagerService.TAG, "Initializing (wiping) backup transport storage: " + str);
                            String transportDirName = this.mTransportManager.getTransportDirName(transportClient.getTransportComponent());
                            EventLog.writeEvent(EventLogTags.BACKUP_START, transportDirName);
                            long elapsedRealtime = SystemClock.elapsedRealtime();
                            BackupTransportClient connectOrThrow = transportClient.connectOrThrow("PerformInitializeTask.run()");
                            int initializeDevice = connectOrThrow.initializeDevice();
                            if (initializeDevice != 0) {
                                Slog.e(BackupManagerService.TAG, "Transport error in initializeDevice()");
                            } else {
                                initializeDevice = connectOrThrow.finishBackup();
                                if (initializeDevice != 0) {
                                    Slog.e(BackupManagerService.TAG, "Transport error in finishBackup()");
                                }
                            }
                            if (initializeDevice == 0) {
                                Slog.i(BackupManagerService.TAG, "Device init successful");
                                int elapsedRealtime2 = (int) (SystemClock.elapsedRealtime() - elapsedRealtime);
                                EventLog.writeEvent(EventLogTags.BACKUP_INITIALIZE, new Object[0]);
                                this.mBackupManagerService.resetBackupState(new File(this.mBaseStateDir, transportDirName));
                                EventLog.writeEvent(EventLogTags.BACKUP_SUCCESS, 0, Integer.valueOf(elapsedRealtime2));
                                this.mBackupManagerService.recordInitPending(false, str, transportDirName);
                                notifyResult(str, 0);
                            } else {
                                EventLog.writeEvent(EventLogTags.BACKUP_TRANSPORT_FAILURE, "(initialize)");
                                this.mBackupManagerService.recordInitPending(true, str, transportDirName);
                                notifyResult(str, initializeDevice);
                                try {
                                    long requestBackupTime = connectOrThrow.requestBackupTime();
                                    Slog.w(BackupManagerService.TAG, "Init failed on " + str + " resched in " + requestBackupTime);
                                    this.mBackupManagerService.getAlarmManager().set(0, System.currentTimeMillis() + requestBackupTime, this.mBackupManagerService.getRunInitIntent());
                                    i2 = initializeDevice;
                                } catch (Exception e) {
                                    e = e;
                                    i = initializeDevice;
                                    Slog.e(BackupManagerService.TAG, "Unexpected error performing init", e);
                                    ArrayList arrayList2 = arrayList;
                                    for (TransportConnection transportConnection : arrayList) {
                                        TransportManager transportManager = this.mTransportManager;
                                        transportManager.disposeOfTransportClient(transportConnection, "PerformInitializeTask.run()");
                                        arrayList2 = transportManager;
                                    }
                                    notifyFinished(-1000);
                                    arrayList = arrayList2;
                                    this.mListener.onFinished("PerformInitializeTask.run()");
                                } catch (Throwable th) {
                                    th = th;
                                    i = initializeDevice;
                                    Iterator it = arrayList.iterator();
                                    while (it.hasNext()) {
                                        this.mTransportManager.disposeOfTransportClient((TransportConnection) it.next(), "PerformInitializeTask.run()");
                                    }
                                    notifyFinished(i);
                                    this.mListener.onFinished("PerformInitializeTask.run()");
                                    throw th;
                                }
                            }
                        }
                    } catch (Exception e2) {
                        e = e2;
                        i = i2;
                    } catch (Throwable th2) {
                        th = th2;
                        i = i2;
                    }
                }
                ArrayList arrayList3 = arrayList;
                for (TransportConnection transportConnection2 : arrayList) {
                    TransportManager transportManager2 = this.mTransportManager;
                    transportManager2.disposeOfTransportClient(transportConnection2, "PerformInitializeTask.run()");
                    arrayList3 = transportManager2;
                }
                notifyFinished(i2);
                arrayList = arrayList3;
            } catch (Exception e3) {
                e = e3;
            }
            this.mListener.onFinished("PerformInitializeTask.run()");
        } catch (Throwable th3) {
            th = th3;
        }
    }
}
