package com.oplus.performance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.oplus.deepsleep.ControllerCenter;
import r6.ProcessCpuManager;

/* loaded from: classes.dex */
public class EapConfigReceiver extends BroadcastReceiver {

    /* renamed from: a, reason: collision with root package name */
    private final String f9980a = "oplus.intent.action.BOOT_COMPLETED";

    /* renamed from: b, reason: collision with root package name */
    private final String f9981b = "oplus.intent.action.OPLUS_OTA_UPDATE_SUCCESSED";

    /* renamed from: c, reason: collision with root package name */
    private final String f9982c = "oplus.intent.action.OPLUS_RECOVER_UPDATE_SUCCESSED";

    /* renamed from: d, reason: collision with root package name */
    private Context f9983d;

    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        this.f9983d = context;
        String action = intent.getAction();
        if ("oplus.intent.action.BOOT_COMPLETED".equals(action)) {
            ControllerCenter controllerCenter = ControllerCenter.getInstance(context.getApplicationContext());
            if (controllerCenter != null) {
                controllerCenter.onBootComplete();
            }
            new Thread(new a(), "bootThread").start();
            return;
        }
        if (TextUtils.equals(action, "oplus.intent.action.OPLUS_OTA_UPDATE_SUCCESSED") || TextUtils.equals(action, "oplus.intent.action.OPLUS_RECOVER_UPDATE_SUCCESSED")) {
            ProcessCpuManager.j(this.f9983d).q(true);
        }
    }
}
