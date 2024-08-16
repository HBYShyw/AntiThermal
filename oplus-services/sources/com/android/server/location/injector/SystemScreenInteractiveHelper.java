package com.android.server.location.injector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import com.android.internal.util.Preconditions;
import com.android.server.location.common.OplusLbsFactory;
import com.android.server.location.interfaces.IOplusLBSMainClass;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SystemScreenInteractiveHelper extends ScreenInteractiveHelper {
    private final Context mContext;
    private volatile boolean mIsInteractive;
    private boolean mReady;

    public SystemScreenInteractiveHelper(Context context) {
        this.mContext = context;
    }

    public void onSystemReady() {
        if (this.mReady) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        this.mContext.registerReceiverAsUser(new BroadcastReceiver() { // from class: com.android.server.location.injector.SystemScreenInteractiveHelper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                boolean z;
                if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                    z = true;
                } else if (!"android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                    return;
                } else {
                    z = false;
                }
                SystemScreenInteractiveHelper.this.onScreenInteractiveChanged(z);
            }
        }, UserHandle.ALL, intentFilter, null, ((IOplusLBSMainClass) OplusLbsFactory.getInstance().getFeature(IOplusLBSMainClass.DEFAULT, this.mContext)).getHandler(0));
        this.mReady = true;
    }

    void onScreenInteractiveChanged(boolean z) {
        if (z == this.mIsInteractive) {
            return;
        }
        this.mIsInteractive = z;
        notifyScreenInteractiveChanged(z);
    }

    @Override // com.android.server.location.injector.ScreenInteractiveHelper
    public boolean isInteractive() {
        Preconditions.checkState(this.mReady);
        return this.mIsInteractive;
    }
}
