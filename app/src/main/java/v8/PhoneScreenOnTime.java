package v8;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import f6.f;
import w4.Affair;
import w4.IAffairCallback;

/* compiled from: PhoneScreenOnTime.java */
/* renamed from: v8.a, reason: use source file name */
/* loaded from: classes2.dex */
public class PhoneScreenOnTime implements IAffairCallback {

    /* renamed from: g, reason: collision with root package name */
    private static volatile PhoneScreenOnTime f19187g;

    /* renamed from: e, reason: collision with root package name */
    private Context f19188e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f19189f = true;

    public PhoneScreenOnTime(Context context) {
        this.f19188e = context;
    }

    public static PhoneScreenOnTime a(Context context) {
        if (f19187g == null) {
            synchronized (PhoneScreenOnTime.class) {
                if (f19187g == null) {
                    f19187g = new PhoneScreenOnTime(context);
                }
            }
        }
        return f19187g;
    }

    public void b() {
        Display defaultDisplay = ((WindowManager) this.f19188e.getSystemService("window")).getDefaultDisplay();
        if (defaultDisplay != null) {
            this.f19189f = defaultDisplay.getState() == 2;
            Log.d("PhoneScreenOnTime", "init: isScreenOn " + this.f19189f);
        }
        f.v2(this.f19188e, System.currentTimeMillis());
        registerAction();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        Log.d("PhoneScreenOnTime", "affairType:" + i10);
        if (i10 == 230) {
            f.v2(this.f19188e, System.currentTimeMillis());
            return;
        }
        if (i10 == 231) {
            if (this.f19189f) {
                long currentTimeMillis = System.currentTimeMillis();
                long V = currentTimeMillis - f.V(this.f19188e);
                long q02 = f.q0(this.f19188e);
                f.v2(this.f19188e, currentTimeMillis);
                if (V < 3600000) {
                    f.M2(this.f19188e, q02 + V);
                    return;
                }
                return;
            }
            return;
        }
        if (i10 == 310) {
            f.v2(this.f19188e, System.currentTimeMillis());
            this.f19189f = true;
        } else {
            if (i10 != 311) {
                return;
            }
            long currentTimeMillis2 = System.currentTimeMillis() - f.V(this.f19188e);
            long q03 = f.q0(this.f19188e);
            if (currentTimeMillis2 < 3600000) {
                f.M2(this.f19188e, q03 + currentTimeMillis2);
            }
            this.f19189f = false;
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 230);
        Affair.f().g(this, 231);
        Affair.f().g(this, 310);
        Affair.f().g(this, 311);
    }
}
