package p6;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import b6.LocalLog;
import w4.Affair;
import w4.IAffairCallback;
import w6.PluginSupporter;
import w6.RegionPluginUtil;

/* compiled from: AABResultObserver.java */
/* renamed from: p6.a, reason: use source file name */
/* loaded from: classes.dex */
public class AABResultObserver implements IAffairCallback {

    /* renamed from: h, reason: collision with root package name */
    private static final Uri f16586h = Uri.parse("content://com.oplus.deepthinker.provider.feature/appunuse/com.oplus.aab");

    /* renamed from: i, reason: collision with root package name */
    private static volatile AABResultObserver f16587i = null;

    /* renamed from: e, reason: collision with root package name */
    private Context f16588e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f16589f = false;

    /* renamed from: g, reason: collision with root package name */
    private ContentObserver f16590g = new a(new Handler());

    /* compiled from: AABResultObserver.java */
    /* renamed from: p6.a$a */
    /* loaded from: classes.dex */
    class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            super.onChange(z10);
            LocalLog.a("AppPredictManager", "aab result change");
            Intent intent = new Intent();
            intent.setAction("oplus.intent.action.ACTION_AAB_RESULT_CHANGE");
            intent.setPackage(AABResultObserver.this.f16588e.getPackageName());
            AABResultObserver.this.f16588e.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
        }
    }

    private AABResultObserver(Context context) {
        this.f16588e = null;
        this.f16588e = context;
    }

    public static AABResultObserver b(Context context) {
        if (f16587i == null) {
            synchronized (AABResultObserver.class) {
                if (f16587i == null) {
                    f16587i = new AABResultObserver(context);
                }
            }
        }
        return f16587i;
    }

    public void c() {
        LocalLog.a("AABResultObserver", "init aab observer");
        registerAction();
        if (this.f16588e.getUserId() == 0) {
            try {
                this.f16588e.getContentResolver().registerContentObserver(f16586h, false, this.f16590g);
            } catch (Exception e10) {
                this.f16589f = true;
                LocalLog.b("AABResultObserver", "Failed to registerContentObserver for AAB " + e10);
            }
        }
    }

    public void d() {
        LocalLog.a("AABResultObserver", "stop aab observer");
        if (this.f16588e.getUserId() == 0 && !this.f16589f) {
            this.f16588e.getContentResolver().unregisterContentObserver(this.f16590g);
        }
        this.f16589f = false;
        e();
    }

    public void e() {
        Affair.f().i(this, 1301);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (i10 == 1301) {
            LocalLog.a("AppPredictManager", "aab result change in " + this.f16588e.getUserId());
            RegionPluginUtil o10 = PluginSupporter.m().o();
            if (o10 != null) {
                o10.e(0L, true);
            }
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 1301);
    }
}
