package o9;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import b6.LocalLog;
import c6.NotifyUtil;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;

/* compiled from: AffairSubUserNotify.java */
/* renamed from: o9.c, reason: use source file name */
/* loaded from: classes2.dex */
public class AffairSubUserNotify implements IAffairCallback {

    /* renamed from: e, reason: collision with root package name */
    private static Context f16284e = null;

    /* renamed from: f, reason: collision with root package name */
    private static final String f16285f = "c";

    /* compiled from: AffairSubUserNotify.java */
    /* renamed from: o9.c$a */
    /* loaded from: classes2.dex */
    private static class a {

        /* renamed from: a, reason: collision with root package name */
        private static final AffairSubUserNotify f16286a = new AffairSubUserNotify();
    }

    public static AffairSubUserNotify a() {
        f16284e = GuardElfContext.e().c();
        return a.f16286a;
    }

    public void b() {
        registerAction();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        String stringExtra = intent.getStringExtra("pkgname");
        String str = f16285f;
        LocalLog.a(str, "sub user need to notify pkg: " + stringExtra);
        int intExtra = intent.getIntExtra("type", -1);
        LocalLog.a(str, "sub user need to notify pkg type: " + intExtra);
        NotifyUtil v7 = NotifyUtil.v(f16284e);
        if (intExtra == 0) {
            v7.E(stringExtra, true);
            return;
        }
        if (intExtra == 1) {
            v7.E(stringExtra, false);
        } else if (intExtra == 2) {
            v7.D(stringExtra, true);
        } else {
            if (intExtra != 3) {
                return;
            }
            v7.D(stringExtra, false);
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 1207);
    }
}
