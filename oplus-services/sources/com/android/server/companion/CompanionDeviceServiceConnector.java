package com.android.server.companion;

import android.annotation.SuppressLint;
import android.companion.AssociationInfo;
import android.companion.ICompanionDeviceService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Handler;
import android.os.IBinder;
import com.android.internal.infra.ServiceConnector;
import com.android.server.ServiceThread;
import java.util.function.Function;

/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"LongLogTag"})
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CompanionDeviceServiceConnector extends ServiceConnector.Impl<ICompanionDeviceService> {
    private static final boolean DEBUG = false;
    private static final String TAG = "CDM_CompanionServiceConnector";
    private static final long UNBIND_POST_DELAY_MS = 5000;
    private static volatile ServiceThread sServiceThread;
    private final ComponentName mComponentName;
    private boolean mIsPrimary;
    private Listener mListener;
    private final int mUserId;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Listener {
        void onBindingDied(int i, String str, CompanionDeviceServiceConnector companionDeviceServiceConnector);
    }

    protected long getAutoDisconnectTimeoutMs() {
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServiceConnectionStatusChanged(ICompanionDeviceService iCompanionDeviceService, boolean z) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CompanionDeviceServiceConnector newInstance(Context context, int i, ComponentName componentName, boolean z, boolean z2) {
        return new CompanionDeviceServiceConnector(context, i, componentName, z ? AudioFormat.EVRC : 65536, z2);
    }

    private CompanionDeviceServiceConnector(Context context, int i, ComponentName componentName, int i2, boolean z) {
        super(context, buildIntent(componentName), i2, i, (Function) null);
        this.mUserId = i;
        this.mComponentName = componentName;
        this.mIsPrimary = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postOnDeviceAppeared(final AssociationInfo associationInfo) {
        post(new ServiceConnector.VoidJob() { // from class: com.android.server.companion.CompanionDeviceServiceConnector$$ExternalSyntheticLambda0
            public final void runNoResult(Object obj) {
                ((ICompanionDeviceService) obj).onDeviceAppeared(associationInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postOnDeviceDisappeared(final AssociationInfo associationInfo) {
        post(new ServiceConnector.VoidJob() { // from class: com.android.server.companion.CompanionDeviceServiceConnector$$ExternalSyntheticLambda2
            public final void runNoResult(Object obj) {
                ((ICompanionDeviceService) obj).onDeviceDisappeared(associationInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postUnbind() {
        getJobHandler().postDelayed(new Runnable() { // from class: com.android.server.companion.CompanionDeviceServiceConnector$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CompanionDeviceServiceConnector.this.unbind();
            }
        }, UNBIND_POST_DELAY_MS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPrimary() {
        return this.mIsPrimary;
    }

    ComponentName getComponentName() {
        return this.mComponentName;
    }

    public void binderDied() {
        super.binderDied();
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onBindingDied(this.mUserId, this.mComponentName.getPackageName(), this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: binderAsInterface, reason: merged with bridge method [inline-methods] */
    public ICompanionDeviceService m2828binderAsInterface(IBinder iBinder) {
        return ICompanionDeviceService.Stub.asInterface(iBinder);
    }

    protected Handler getJobHandler() {
        return getServiceThread().getThreadHandler();
    }

    private static Intent buildIntent(ComponentName componentName) {
        return new Intent("android.companion.CompanionDeviceService").setComponent(componentName);
    }

    private static ServiceThread getServiceThread() {
        if (sServiceThread == null) {
            synchronized (CompanionDeviceManagerService.class) {
                if (sServiceThread == null) {
                    sServiceThread = new ServiceThread("companion-device-service-connector", 0, false);
                    sServiceThread.start();
                }
            }
        }
        return sServiceThread;
    }
}
