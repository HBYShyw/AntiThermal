package d7;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import b6.LocalLog;

/* compiled from: RomUpdateHelper.java */
/* renamed from: d7.a, reason: use source file name */
/* loaded from: classes.dex */
public class RomUpdateHelper {

    /* renamed from: a, reason: collision with root package name */
    private final Context f10772a;

    /* renamed from: b, reason: collision with root package name */
    private boolean f10773b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f10774c = true;

    /* renamed from: d, reason: collision with root package name */
    private ContentObserver f10775d;

    /* renamed from: e, reason: collision with root package name */
    private b f10776e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: RomUpdateHelper.java */
    /* renamed from: d7.a$a */
    /* loaded from: classes.dex */
    public class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            super.onChange(z10);
            synchronized (RomUpdateHelper.this) {
                boolean z11 = true;
                int i10 = Settings.Secure.getInt(RomUpdateHelper.this.f10772a.getContentResolver(), "guardelf_smart_doze_support", 1);
                RomUpdateHelper romUpdateHelper = RomUpdateHelper.this;
                if (i10 != 1) {
                    z11 = false;
                }
                romUpdateHelper.f10774c = z11;
            }
            RomUpdateHelper.this.f();
            LocalLog.a("SmartDoze:RomUpdateHelper", "rus switch change");
        }
    }

    /* compiled from: RomUpdateHelper.java */
    /* renamed from: d7.a$b */
    /* loaded from: classes.dex */
    public interface b {
        void a();
    }

    public RomUpdateHelper(Context context) {
        this.f10772a = context;
        g();
    }

    private void e() {
        this.f10774c = Settings.Secure.getInt(this.f10772a.getContentResolver(), "guardelf_smart_doze_support", 1) == 1;
    }

    public synchronized void c() {
        if (this.f10773b) {
            LocalLog.a("SmartDoze:RomUpdateHelper", "initialize");
        } else {
            if (this.f10772a != null) {
                e();
                this.f10773b = true;
                return;
            }
            throw new IllegalArgumentException("context is null!");
        }
    }

    public synchronized boolean d() {
        return this.f10774c;
    }

    public void f() {
        b bVar = this.f10776e;
        if (bVar != null) {
            bVar.a();
        }
    }

    public void g() {
        this.f10775d = new a(this.f10772a.getMainThreadHandler());
        this.f10772a.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("guardelf_smart_doze_support"), true, this.f10775d);
    }

    public void h(b bVar) {
        this.f10776e = bVar;
    }

    public void i() {
        if (this.f10775d != null) {
            this.f10772a.getContentResolver().unregisterContentObserver(this.f10775d);
        }
    }
}
