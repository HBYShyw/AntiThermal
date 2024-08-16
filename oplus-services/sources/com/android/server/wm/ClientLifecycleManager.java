package com.android.server.wm;

import android.app.IApplicationThread;
import android.app.servertransaction.ActivityLifecycleItem;
import android.app.servertransaction.ClientTransaction;
import android.app.servertransaction.ClientTransactionItem;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
class ClientLifecycleManager {
    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleTransaction(ClientTransaction clientTransaction) throws RemoteException {
        IApplicationThread client = clientTransaction.getClient();
        clientTransaction.schedule();
        if (client instanceof Binder) {
            return;
        }
        clientTransaction.recycle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleTransaction(IApplicationThread iApplicationThread, IBinder iBinder, ActivityLifecycleItem activityLifecycleItem) throws RemoteException {
        scheduleTransaction(transactionWithState(iApplicationThread, iBinder, activityLifecycleItem));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleTransaction(IApplicationThread iApplicationThread, IBinder iBinder, ClientTransactionItem clientTransactionItem) throws RemoteException {
        scheduleTransaction(transactionWithCallback(iApplicationThread, iBinder, clientTransactionItem));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleTransaction(IApplicationThread iApplicationThread, ClientTransactionItem clientTransactionItem) throws RemoteException {
        scheduleTransaction(transactionWithCallback(iApplicationThread, null, clientTransactionItem));
    }

    private static ClientTransaction transactionWithState(IApplicationThread iApplicationThread, IBinder iBinder, ActivityLifecycleItem activityLifecycleItem) {
        ClientTransaction obtain = ClientTransaction.obtain(iApplicationThread, iBinder);
        obtain.setLifecycleStateRequest(activityLifecycleItem);
        return obtain;
    }

    private static ClientTransaction transactionWithCallback(IApplicationThread iApplicationThread, IBinder iBinder, ClientTransactionItem clientTransactionItem) {
        ClientTransaction obtain = ClientTransaction.obtain(iApplicationThread, iBinder);
        obtain.addCallback(clientTransactionItem);
        return obtain;
    }
}
