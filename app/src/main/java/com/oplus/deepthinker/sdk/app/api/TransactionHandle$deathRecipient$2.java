package com.oplus.deepthinker.sdk.app.api;

import android.os.IBinder;
import kotlin.Metadata;
import za.Lambda;
import za.k;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: TransactionHandle.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "Landroid/os/IBinder$DeathRecipient;", "T", "invoke"}, k = 3, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class TransactionHandle$deathRecipient$2 extends Lambda implements ya.a<IBinder.DeathRecipient> {
    final /* synthetic */ TransactionHandle<T> this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TransactionHandle$deathRecipient$2(TransactionHandle<T> transactionHandle) {
        super(0);
        this.this$0 = transactionHandle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: invoke$lambda-0, reason: not valid java name */
    public static final void m9invoke$lambda0(TransactionHandle transactionHandle) {
        k.e(transactionHandle, "this$0");
        transactionHandle.onRemoteDisconnected();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // ya.a
    public final IBinder.DeathRecipient invoke() {
        final TransactionHandle<T> transactionHandle = this.this$0;
        return new IBinder.DeathRecipient() { // from class: com.oplus.deepthinker.sdk.app.api.c
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                TransactionHandle$deathRecipient$2.m9invoke$lambda0(TransactionHandle.this);
            }
        };
    }
}
