package com.android.server.location.contexthub;

import android.hardware.location.ContextHubTransaction;
import android.hardware.location.NanoAppState;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class ContextHubServiceTransaction {
    private boolean mIsComplete;
    private final Long mNanoAppId;
    private final String mPackage;
    private final int mTransactionId;
    private final int mTransactionType;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onQueryResponse(int i, List<NanoAppState> list) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int onTransact();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTransactionComplete(int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContextHubServiceTransaction(int i, int i2, String str) {
        this.mIsComplete = false;
        this.mTransactionId = i;
        this.mTransactionType = i2;
        this.mNanoAppId = null;
        this.mPackage = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContextHubServiceTransaction(int i, int i2, long j, String str) {
        this.mIsComplete = false;
        this.mTransactionId = i;
        this.mTransactionType = i2;
        this.mNanoAppId = Long.valueOf(j);
        this.mPackage = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getTransactionId() {
        return this.mTransactionId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getTransactionType() {
        return this.mTransactionType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getTimeout(TimeUnit timeUnit) {
        if (this.mTransactionType == 0) {
            return timeUnit.convert(30L, TimeUnit.SECONDS);
        }
        return timeUnit.convert(5L, TimeUnit.SECONDS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setComplete() {
        this.mIsComplete = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isComplete() {
        return this.mIsComplete;
    }

    public String toString() {
        String str = ContextHubTransaction.typeToString(this.mTransactionType, true) + " (";
        if (this.mNanoAppId != null) {
            str = str + "appId = 0x" + Long.toHexString(this.mNanoAppId.longValue()) + ", ";
        }
        return str + "package = " + this.mPackage + ")";
    }
}
