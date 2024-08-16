package a9;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import f6.f;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;
import x5.UploadDataUtil;

/* compiled from: SuperEnduranceController.java */
/* renamed from: a9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class SuperEnduranceController implements IAffairCallback {

    /* renamed from: k, reason: collision with root package name */
    private static volatile SuperEnduranceController f106k;

    /* renamed from: e, reason: collision with root package name */
    private int f107e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f108f = false;

    /* renamed from: g, reason: collision with root package name */
    private boolean f109g = false;

    /* renamed from: h, reason: collision with root package name */
    private Context f110h;

    /* renamed from: i, reason: collision with root package name */
    private Handler f111i;

    /* renamed from: j, reason: collision with root package name */
    ContentObserver f112j;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SuperEnduranceController.java */
    /* renamed from: a9.a$a */
    /* loaded from: classes2.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            SuperEnduranceController.this.i("boot");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SuperEnduranceController.java */
    /* renamed from: a9.a$b */
    /* loaded from: classes2.dex */
    public class b extends ContentObserver {

        /* compiled from: SuperEnduranceController.java */
        /* renamed from: a9.a$b$a */
        /* loaded from: classes2.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                Intent intent = new Intent("com.android.internal.intent.action.REQUEST_SHUTDOWN");
                intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
                intent.putExtra("android.intent.extra.REASON", "userrequested");
                intent.setFlags(268435456);
                SuperEnduranceController.this.f110h.startActivityAsUser(intent, UserHandle.CURRENT);
            }
        }

        b(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            SuperEnduranceController superEnduranceController = SuperEnduranceController.this;
            superEnduranceController.f109g = f.Y0(superEnduranceController.f110h);
            if (SuperEnduranceController.this.f107e != 1 || SuperEnduranceController.this.f109g) {
                return;
            }
            LocalLog.l("SuperEnduranceController", "force exit, power off!");
            SuperEnduranceController.this.f111i.postDelayed(new a(), 1000L);
        }
    }

    private SuperEnduranceController(Context context) {
        this.f110h = context;
    }

    private void g() {
        this.f111i.postDelayed(new a(), 3000L);
    }

    public static SuperEnduranceController h(Context context) {
        if (f106k == null) {
            synchronized (SuperEnduranceController.class) {
                if (f106k == null) {
                    f106k = new SuperEnduranceController(context);
                }
            }
        }
        return f106k;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i(String str) {
        if (this.f107e == 1 && this.f108f) {
            if (this.f109g) {
                return;
            }
            LocalLog.l("SuperEnduranceController", "turn on SuperEnduranceMode: type:" + str);
            f.s3(this.f110h, true);
            UploadDataUtil.S0(this.f110h).M0(true, str);
            SuperEnduranceUtils.b(true);
            SuperEnduranceUtils.a(this.f110h, true);
            this.f109g = true;
            return;
        }
        if (this.f109g) {
            LocalLog.l("SuperEnduranceController", "turn off SuperEnduranceMode: type:" + str + " mSuperPowerState:" + this.f108f + " mCurrentLevel:" + this.f107e);
            f.s3(this.f110h, false);
            UploadDataUtil.S0(this.f110h).M0(false, str);
            SuperEnduranceUtils.b(false);
            SuperEnduranceUtils.a(this.f110h, false);
            this.f109g = false;
        }
    }

    private void l() {
        this.f107e = GuardElfContext.e().d().getIntProperty(4);
        this.f108f = f.a1(this.f110h);
        this.f109g = false;
        if (this.f107e != 1 && f.Y0(this.f110h)) {
            f.s3(this.f110h, false);
        }
        g();
    }

    private void n() {
        this.f112j = new b(this.f111i);
        this.f110h.getContentResolver().registerContentObserver(Settings.System.getUriFor("super_endurance_mode_state"), false, this.f112j);
        registerAction();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (i10 != 204) {
            return;
        }
        this.f107e = intent.getIntExtra("level", 0);
        i("level");
    }

    public void j(boolean z10) {
        this.f108f = z10;
        i("mode");
    }

    public void k(Handler handler) {
        LocalLog.l("SuperEnduranceController", "init");
        this.f111i = handler;
        l();
        n();
    }

    public void m() {
        Context context = this.f110h;
        if (context != null && this.f112j != null) {
            context.getContentResolver().unregisterContentObserver(this.f112j);
        }
        o();
    }

    public void o() {
        Affair.f().i(this, 903);
        Affair.f().i(this, EventType.SCENE_MODE_CAMERA);
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 903);
        Affair.f().g(this, EventType.SCENE_MODE_CAMERA);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
        if (i10 != 903) {
            return;
        }
        j(bundle.getBoolean("s_powersave_state"));
    }
}
