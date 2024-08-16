package com.oplus.wrapper.app.servertransaction;

import android.app.ActivityThread;
import android.os.IBinder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ClientTransaction {
    private final android.app.servertransaction.ClientTransaction mClientTransaction;

    private ClientTransaction(android.app.servertransaction.ClientTransaction clientTransaction) {
        this.mClientTransaction = clientTransaction;
    }

    public static ClientTransaction obtain(IBinder activityToken) {
        return new ClientTransaction(android.app.servertransaction.ClientTransaction.obtain(ActivityThread.currentActivityThread().getApplicationThread(), activityToken));
    }

    public List<ClientTransactionItem> getCallbacks() {
        List<android.app.servertransaction.ClientTransactionItem> clientTransactionItemList = this.mClientTransaction.getCallbacks();
        if (clientTransactionItemList == null) {
            return null;
        }
        List<ClientTransactionItem> wrapperList = new ArrayList<>();
        for (android.app.servertransaction.ClientTransactionItem item : clientTransactionItemList) {
            ClientTransactionItem itemWrapper = new ClientTransactionItem(item);
            wrapperList.add(itemWrapper);
        }
        return wrapperList;
    }
}
