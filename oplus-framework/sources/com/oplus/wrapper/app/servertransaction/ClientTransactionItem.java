package com.oplus.wrapper.app.servertransaction;

/* loaded from: classes.dex */
public class ClientTransactionItem {
    private final android.app.servertransaction.ClientTransactionItem mClientTransactionItem;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ClientTransactionItem(android.app.servertransaction.ClientTransactionItem clientTransactionItem) {
        this.mClientTransactionItem = clientTransactionItem;
    }

    public int getPostExecutionState() {
        return this.mClientTransactionItem.getPostExecutionState();
    }

    public int describeContents() {
        return this.mClientTransactionItem.describeContents();
    }
}
