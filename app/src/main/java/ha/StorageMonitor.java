package ha;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;

/* compiled from: StorageMonitor.java */
/* renamed from: ha.b, reason: use source file name */
/* loaded from: classes2.dex */
public class StorageMonitor implements IAffairCallback {

    /* renamed from: e, reason: collision with root package name */
    private int f12029e = -1;

    /* renamed from: f, reason: collision with root package name */
    private Context f12030f;

    /* compiled from: StorageMonitor.java */
    /* renamed from: ha.b$a */
    /* loaded from: classes2.dex */
    private static class a {

        /* renamed from: a, reason: collision with root package name */
        private static final StorageMonitor f12031a = new StorageMonitor();
    }

    public StorageMonitor() {
        this.f12030f = null;
        this.f12030f = GuardElfContext.e().c();
    }

    public static StorageMonitor a() {
        return a.f12031a;
    }

    public void b() {
        registerAction();
    }

    public void c(int i10) {
        this.f12029e = i10;
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
        if (i10 != 300) {
            return;
        }
        String string = bundle.getString("pre_app_pkgname", "");
        if ("com.android.incallui".equals(bundle.getString("next_app_pkgname", ""))) {
            StorageUtils.o(true);
            return;
        }
        if ("com.android.incallui".equals(string)) {
            StorageUtils.o(false);
            if (this.f12029e != -1) {
                StorageMonitorService.Y(this.f12030f).H0(this.f12029e, false);
                this.f12029e = -1;
            }
        }
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 300);
    }
}
