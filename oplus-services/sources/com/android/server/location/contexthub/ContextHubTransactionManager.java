package com.android.server.location.contexthub;

import android.hardware.location.IContextHubTransactionCallback;
import android.hardware.location.NanoAppBinary;
import android.hardware.location.NanoAppState;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ContextHubTransactionManager {
    private static final int MAX_PENDING_REQUESTS = 10000;
    private static final int NUM_TRANSACTION_RECORDS = 20;
    private static final String TAG = "ContextHubTransactionManager";
    private final ContextHubClientManager mClientManager;
    private final IContextHubWrapper mContextHubProxy;
    private final NanoAppStateManager mNanoAppStateManager;
    private final ArrayDeque<ContextHubServiceTransaction> mTransactionQueue = new ArrayDeque<>();
    private final AtomicInteger mNextAvailableId = new AtomicInteger();
    private final ScheduledThreadPoolExecutor mTimeoutExecutor = new ScheduledThreadPoolExecutor(1);
    private ScheduledFuture<?> mTimeoutFuture = null;
    private final ConcurrentLinkedEvictingDeque<TransactionRecord> mTransactionRecordDeque = new ConcurrentLinkedEvictingDeque<>(20);

    /* JADX INFO: Access modifiers changed from: private */
    public int toStatsTransactionResult(int i) {
        if (i == 0) {
            return 0;
        }
        switch (i) {
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            default:
                return 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class TransactionRecord {
        private final long mTimestamp = System.currentTimeMillis();
        private final String mTransaction;

        TransactionRecord(String str) {
            this.mTransaction = str;
        }

        public String toString() {
            return ContextHubServiceUtil.formatDateFromTimestamp(this.mTimestamp) + " " + this.mTransaction;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContextHubTransactionManager(IContextHubWrapper iContextHubWrapper, ContextHubClientManager contextHubClientManager, NanoAppStateManager nanoAppStateManager) {
        this.mContextHubProxy = iContextHubWrapper;
        this.mClientManager = contextHubClientManager;
        this.mNanoAppStateManager = nanoAppStateManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContextHubServiceTransaction createLoadTransaction(final int i, final NanoAppBinary nanoAppBinary, final IContextHubTransactionCallback iContextHubTransactionCallback, String str) {
        return new ContextHubServiceTransaction(this.mNextAvailableId.getAndIncrement(), 0, nanoAppBinary.getNanoAppId(), str) { // from class: com.android.server.location.contexthub.ContextHubTransactionManager.1
            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            public int onTransact() {
                try {
                    return ContextHubTransactionManager.this.mContextHubProxy.loadNanoapp(i, nanoAppBinary, getTransactionId());
                } catch (RemoteException e) {
                    Log.e(ContextHubTransactionManager.TAG, "RemoteException while trying to load nanoapp with ID 0x" + Long.toHexString(nanoAppBinary.getNanoAppId()), e);
                    return 1;
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            public void onTransactionComplete(int i2) {
                ContextHubStatsLog.write(401, nanoAppBinary.getNanoAppId(), nanoAppBinary.getNanoAppVersion(), 1, ContextHubTransactionManager.this.toStatsTransactionResult(i2));
                ContextHubEventLogger.getInstance().logNanoappLoad(i, nanoAppBinary.getNanoAppId(), nanoAppBinary.getNanoAppVersion(), nanoAppBinary.getBinary().length, i2 == 0);
                if (i2 == 0) {
                    ContextHubTransactionManager.this.mNanoAppStateManager.addNanoAppInstance(i, nanoAppBinary.getNanoAppId(), nanoAppBinary.getNanoAppVersion());
                }
                try {
                    iContextHubTransactionCallback.onTransactionComplete(i2);
                    if (i2 == 0) {
                        ContextHubTransactionManager.this.mClientManager.onNanoAppLoaded(i, nanoAppBinary.getNanoAppId());
                    }
                } catch (RemoteException e) {
                    Log.e(ContextHubTransactionManager.TAG, "RemoteException while calling client onTransactionComplete", e);
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContextHubServiceTransaction createUnloadTransaction(final int i, final long j, final IContextHubTransactionCallback iContextHubTransactionCallback, String str) {
        return new ContextHubServiceTransaction(this.mNextAvailableId.getAndIncrement(), 1, j, str) { // from class: com.android.server.location.contexthub.ContextHubTransactionManager.2
            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            public int onTransact() {
                try {
                    return ContextHubTransactionManager.this.mContextHubProxy.unloadNanoapp(i, j, getTransactionId());
                } catch (RemoteException e) {
                    Log.e(ContextHubTransactionManager.TAG, "RemoteException while trying to unload nanoapp with ID 0x" + Long.toHexString(j), e);
                    return 1;
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            public void onTransactionComplete(int i2) {
                ContextHubStatsLog.write(401, j, 0, 2, ContextHubTransactionManager.this.toStatsTransactionResult(i2));
                ContextHubEventLogger.getInstance().logNanoappUnload(i, j, i2 == 0);
                if (i2 == 0) {
                    ContextHubTransactionManager.this.mNanoAppStateManager.removeNanoAppInstance(i, j);
                }
                try {
                    iContextHubTransactionCallback.onTransactionComplete(i2);
                    if (i2 == 0) {
                        ContextHubTransactionManager.this.mClientManager.onNanoAppUnloaded(i, j);
                    }
                } catch (RemoteException e) {
                    Log.e(ContextHubTransactionManager.TAG, "RemoteException while calling client onTransactionComplete", e);
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContextHubServiceTransaction createEnableTransaction(final int i, final long j, final IContextHubTransactionCallback iContextHubTransactionCallback, String str) {
        return new ContextHubServiceTransaction(this.mNextAvailableId.getAndIncrement(), 2, str) { // from class: com.android.server.location.contexthub.ContextHubTransactionManager.3
            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            public int onTransact() {
                try {
                    return ContextHubTransactionManager.this.mContextHubProxy.enableNanoapp(i, j, getTransactionId());
                } catch (RemoteException e) {
                    Log.e(ContextHubTransactionManager.TAG, "RemoteException while trying to enable nanoapp with ID 0x" + Long.toHexString(j), e);
                    return 1;
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            public void onTransactionComplete(int i2) {
                try {
                    iContextHubTransactionCallback.onTransactionComplete(i2);
                } catch (RemoteException e) {
                    Log.e(ContextHubTransactionManager.TAG, "RemoteException while calling client onTransactionComplete", e);
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContextHubServiceTransaction createDisableTransaction(final int i, final long j, final IContextHubTransactionCallback iContextHubTransactionCallback, String str) {
        return new ContextHubServiceTransaction(this.mNextAvailableId.getAndIncrement(), 3, str) { // from class: com.android.server.location.contexthub.ContextHubTransactionManager.4
            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            public int onTransact() {
                try {
                    return ContextHubTransactionManager.this.mContextHubProxy.disableNanoapp(i, j, getTransactionId());
                } catch (RemoteException e) {
                    Log.e(ContextHubTransactionManager.TAG, "RemoteException while trying to disable nanoapp with ID 0x" + Long.toHexString(j), e);
                    return 1;
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            public void onTransactionComplete(int i2) {
                try {
                    iContextHubTransactionCallback.onTransactionComplete(i2);
                } catch (RemoteException e) {
                    Log.e(ContextHubTransactionManager.TAG, "RemoteException while calling client onTransactionComplete", e);
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContextHubServiceTransaction createQueryTransaction(final int i, final IContextHubTransactionCallback iContextHubTransactionCallback, String str) {
        return new ContextHubServiceTransaction(this.mNextAvailableId.getAndIncrement(), 4, str) { // from class: com.android.server.location.contexthub.ContextHubTransactionManager.5
            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            public int onTransact() {
                try {
                    return ContextHubTransactionManager.this.mContextHubProxy.queryNanoapps(i);
                } catch (RemoteException e) {
                    Log.e(ContextHubTransactionManager.TAG, "RemoteException while trying to query for nanoapps", e);
                    return 1;
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            public void onTransactionComplete(int i2) {
                onQueryResponse(i2, Collections.emptyList());
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.location.contexthub.ContextHubServiceTransaction
            public void onQueryResponse(int i2, List<NanoAppState> list) {
                try {
                    iContextHubTransactionCallback.onQueryResponse(i2, list);
                } catch (RemoteException e) {
                    Log.e(ContextHubTransactionManager.TAG, "RemoteException while calling client onQueryComplete", e);
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addTransaction(ContextHubServiceTransaction contextHubServiceTransaction) throws IllegalStateException {
        if (this.mTransactionQueue.size() == 10000) {
            throw new IllegalStateException("Transaction queue is full (capacity = 10000)");
        }
        this.mTransactionQueue.add(contextHubServiceTransaction);
        this.mTransactionRecordDeque.add(new TransactionRecord(contextHubServiceTransaction.toString()));
        if (this.mTransactionQueue.size() == 1) {
            startNextTransaction();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onTransactionResponse(int i, boolean z) {
        ContextHubServiceTransaction peek = this.mTransactionQueue.peek();
        if (peek == null) {
            Log.w(TAG, "Received unexpected transaction response (no transaction pending)");
            return;
        }
        if (peek.getTransactionId() != i) {
            Log.w(TAG, "Received unexpected transaction response (expected ID = " + peek.getTransactionId() + ", received ID = " + i + ")");
            return;
        }
        peek.onTransactionComplete(z ? 0 : 5);
        removeTransactionAndStartNext();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onQueryResponse(List<NanoAppState> list) {
        ContextHubServiceTransaction peek = this.mTransactionQueue.peek();
        if (peek == null) {
            Log.w(TAG, "Received unexpected query response (no transaction pending)");
            return;
        }
        if (peek.getTransactionType() != 4) {
            Log.w(TAG, "Received unexpected query response (expected " + peek + ")");
            return;
        }
        peek.onQueryResponse(0, list);
        removeTransactionAndStartNext();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onHubReset() {
        if (this.mTransactionQueue.peek() == null) {
            return;
        }
        removeTransactionAndStartNext();
    }

    private void removeTransactionAndStartNext() {
        this.mTimeoutFuture.cancel(false);
        this.mTransactionQueue.remove().setComplete();
        if (this.mTransactionQueue.isEmpty()) {
            return;
        }
        startNextTransaction();
    }

    private void startNextTransaction() {
        int i = 1;
        while (i != 0 && !this.mTransactionQueue.isEmpty()) {
            final ContextHubServiceTransaction peek = this.mTransactionQueue.peek();
            int onTransact = peek.onTransact();
            if (onTransact == 0) {
                Runnable runnable = new Runnable() { // from class: com.android.server.location.contexthub.ContextHubTransactionManager$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContextHubTransactionManager.this.lambda$startNextTransaction$0(peek);
                    }
                };
                TimeUnit timeUnit = TimeUnit.SECONDS;
                try {
                    this.mTimeoutFuture = this.mTimeoutExecutor.schedule(runnable, peek.getTimeout(timeUnit), timeUnit);
                } catch (Exception e) {
                    Log.e(TAG, "Error when schedule a timer", e);
                }
            } else {
                peek.onTransactionComplete(ContextHubServiceUtil.toTransactionResult(onTransact));
                this.mTransactionQueue.remove();
            }
            i = onTransact;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startNextTransaction$0(ContextHubServiceTransaction contextHubServiceTransaction) {
        synchronized (this) {
            if (!contextHubServiceTransaction.isComplete()) {
                Log.d(TAG, contextHubServiceTransaction + " timed out");
                contextHubServiceTransaction.onTransactionComplete(6);
                removeTransactionAndStartNext();
            }
        }
    }

    public String toString() {
        int i;
        ContextHubServiceTransaction[] contextHubServiceTransactionArr;
        StringBuilder sb = new StringBuilder(100);
        synchronized (this) {
            contextHubServiceTransactionArr = (ContextHubServiceTransaction[]) this.mTransactionQueue.toArray(new ContextHubServiceTransaction[0]);
        }
        for (i = 0; i < contextHubServiceTransactionArr.length; i++) {
            sb.append(i + ": " + contextHubServiceTransactionArr[i] + "\n");
        }
        sb.append("Transaction History:\n");
        Iterator<TransactionRecord> descendingIterator = this.mTransactionRecordDeque.descendingIterator();
        while (descendingIterator.hasNext()) {
            sb.append(descendingIterator.next() + "\n");
        }
        return sb.toString();
    }
}
