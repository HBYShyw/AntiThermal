package v8;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import f6.f;
import w4.Affair;
import w4.IAffairCallback;

/* compiled from: PhoneUsageTime.java */
/* renamed from: v8.b, reason: use source file name */
/* loaded from: classes2.dex */
public class PhoneUsageTime implements IAffairCallback {

    /* renamed from: f, reason: collision with root package name */
    private static volatile PhoneUsageTime f19190f;

    /* renamed from: e, reason: collision with root package name */
    private Context f19191e;

    public PhoneUsageTime(Context context) {
        this.f19191e = context;
    }

    public static PhoneUsageTime a(Context context) {
        if (f19190f == null) {
            synchronized (PhoneUsageTime.class) {
                if (f19190f == null) {
                    f19190f = new PhoneUsageTime(context);
                }
            }
        }
        return f19190f;
    }

    public void b() {
        f.t2(this.f19191e, System.currentTimeMillis());
        registerAction();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        Log.d("PhoneUsageTime", "affairType:" + i10);
        if (i10 == 230) {
            f.t2(this.f19191e, System.currentTimeMillis());
            return;
        }
        if (i10 != 231) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long T = currentTimeMillis - f.T(this.f19191e);
        long S = f.S(this.f19191e);
        f.t2(this.f19191e, currentTimeMillis);
        if (T < 3600000) {
            f.s2(this.f19191e, S + T);
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 230);
        Affair.f().g(this, 231);
    }
}
