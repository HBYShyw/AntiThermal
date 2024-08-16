package com.oplus.deepthinker.sdk.app.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager;
import com.oplus.deepthinker.sdk.app.api.TransactionHandle;
import i6.IDeepThinkerBridge;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Metadata;
import za.k;

/* compiled from: DefaultTransactionHandle.kt */
@Metadata(bv = {}, d1 = {"\u00001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005*\u0001\u000b\b\u0016\u0018\u0000 \u00122\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0012B\u0017\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u000f\u001a\u00020\u000e¢\u0006\u0004\b\u0010\u0010\u0011J\b\u0010\u0004\u001a\u00020\u0003H\u0016R\u0014\u0010\u0006\u001a\u00020\u00058\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0006\u0010\u0007R\u0014\u0010\t\u001a\u00020\b8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\t\u0010\nR\u0014\u0010\f\u001a\u00020\u000b8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\f\u0010\r¨\u0006\u0013"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/api/DefaultTransactionHandle;", "Lcom/oplus/deepthinker/sdk/app/api/TransactionHandle;", "Li6/a;", "Lma/f0;", "release", "Landroid/content/Context;", "context", "Landroid/content/Context;", "Ljava/util/concurrent/atomic/AtomicInteger;", "threadCount", "Ljava/util/concurrent/atomic/AtomicInteger;", "com/oplus/deepthinker/sdk/app/api/DefaultTransactionHandle$serviceConn$1", "serviceConn", "Lcom/oplus/deepthinker/sdk/app/api/DefaultTransactionHandle$serviceConn$1;", "", "intentAction", "<init>", "(Landroid/content/Context;Ljava/lang/String;)V", "Companion", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public class DefaultTransactionHandle extends TransactionHandle<IDeepThinkerBridge> {
    private static final int THREAD_SUM = 3;
    private final Context context;
    private final DefaultTransactionHandle$serviceConn$1 serviceConn;
    private final AtomicInteger threadCount;

    /* JADX WARN: Type inference failed for: r2v5, types: [com.oplus.deepthinker.sdk.app.api.DefaultTransactionHandle$serviceConn$1] */
    public DefaultTransactionHandle(Context context, final String str) {
        k.e(context, "context");
        k.e(str, "intentAction");
        this.context = context;
        setTargetExecutor(Executors.newFixedThreadPool(3, new ThreadFactory() { // from class: com.oplus.deepthinker.sdk.app.api.a
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                Thread m7_init_$lambda0;
                m7_init_$lambda0 = DefaultTransactionHandle.m7_init_$lambda0(DefaultTransactionHandle.this, runnable);
                return m7_init_$lambda0;
            }
        }));
        setRequestRemoteInvoker(new TransactionHandle.IRemoteCallback() { // from class: com.oplus.deepthinker.sdk.app.api.DefaultTransactionHandle.2
            @Override // com.oplus.deepthinker.sdk.app.api.TransactionHandle.IRemoteCallback
            public void onConnectRemote() {
                Intent intent = new Intent(str);
                intent.setPackage(IOplusDeepThinkerManager.SERVICE_PKG);
                this.context.bindService(intent, this.serviceConn, 1);
            }

            @Override // com.oplus.deepthinker.sdk.app.api.TransactionHandle.IRemoteCallback
            public void onRemoteDisconnected() {
            }
        });
        this.threadCount = new AtomicInteger(0);
        this.serviceConn = new ServiceConnection() { // from class: com.oplus.deepthinker.sdk.app.api.DefaultTransactionHandle$serviceConn$1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (iBinder == null) {
                    return;
                }
                DefaultTransactionHandle defaultTransactionHandle = DefaultTransactionHandle.this;
                IDeepThinkerBridge z10 = IDeepThinkerBridge.a.z(iBinder);
                k.d(z10, "asInterface(service)");
                defaultTransactionHandle.onRemoteConnected(z10);
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                DefaultTransactionHandle.this.setRemote(null);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: _init_$lambda-0, reason: not valid java name */
    public static final Thread m7_init_$lambda0(DefaultTransactionHandle defaultTransactionHandle, Runnable runnable) {
        k.e(defaultTransactionHandle, "this$0");
        return new Thread(runnable, k.l("DPTKSDK", Integer.valueOf(defaultTransactionHandle.threadCount.incrementAndGet())));
    }

    @Override // com.oplus.deepthinker.sdk.app.api.TransactionHandle
    public void release() {
        super.release();
        this.context.unbindService(this.serviceConn);
    }
}
