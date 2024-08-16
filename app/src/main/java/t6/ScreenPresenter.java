package t6;

import android.content.ContentResolver;
import android.content.Context;
import android.os.PowerManager;
import android.provider.Settings;
import b6.LocalLog;
import com.oplus.battery.R;
import java.lang.reflect.Method;

/* compiled from: ScreenPresenter.java */
/* renamed from: t6.e, reason: use source file name */
/* loaded from: classes.dex */
public class ScreenPresenter extends ThermalPresenter {

    /* renamed from: f, reason: collision with root package name */
    private boolean f18627f;

    /* renamed from: g, reason: collision with root package name */
    private ContentResolver f18628g;

    public ScreenPresenter(Context context) {
        super(context);
        this.f18627f = y5.b.s();
        this.f18628g = this.f18629a.getContentResolver();
    }

    private int f() {
        if (this.f18627f) {
            PowerManager powerManager = (PowerManager) this.f18629a.getSystemService("power");
            Object obj = null;
            try {
                Method declaredMethod = powerManager.getClass().getDeclaredMethod("getDefaultBrightness", new Class[0]);
                declaredMethod.setAccessible(true);
                obj = declaredMethod.invoke(powerManager, new Object[0]);
            } catch (Exception e10) {
                LocalLog.d("ThermalScreenPresenter", "getDefaultBrightness : " + e10);
            }
            if (obj != null) {
                return ((Integer) obj).intValue();
            }
        }
        return 0;
    }

    private int g() {
        if (this.f18627f) {
            Object obj = null;
            try {
                Method declaredMethod = Settings.System.class.getDeclaredMethod("getIntForBrightness", ContentResolver.class, String.class, Integer.TYPE);
                declaredMethod.setAccessible(true);
                obj = declaredMethod.invoke(Settings.System.class.newInstance(), this.f18628g, "screen_brightness", -2);
                LocalLog.l("ThermalScreenPresenter", "getIntForBrightness " + obj);
            } catch (Exception e10) {
                LocalLog.b("ThermalScreenPresenter", "getDefaultBrightness : " + e10);
            }
            if (obj != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("getIntForBrightness ");
                Integer num = (Integer) obj;
                sb2.append(num.intValue());
                LocalLog.l("ThermalScreenPresenter", sb2.toString());
                return num.intValue();
            }
        }
        return Settings.System.getInt(this.f18628g, "screen_brightness", 0);
    }

    private void i(int i10) {
        LocalLog.l("ThermalScreenPresenter", "putIntForBrightness " + i10);
        if (this.f18627f) {
            try {
                Class cls = Integer.TYPE;
                Method declaredMethod = Settings.System.class.getDeclaredMethod("putIntForBrightness", ContentResolver.class, String.class, cls, cls);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(Settings.System.class.newInstance(), this.f18628g, "screen_brightness", Integer.valueOf(i10), -2);
                return;
            } catch (Exception e10) {
                LocalLog.d("ThermalScreenPresenter", "putIntForBrightness : " + e10);
                return;
            }
        }
        Settings.System.putInt(this.f18628g, "screen_brightness", i10);
    }

    @Override // t6.IThermalPresenter
    public void a(int i10) {
        this.f18633e.e(R.string.high_temp_screen_dimming, this.f18629a.getString(R.string.high_temp_dim), i10);
    }

    @Override // t6.IThermalPresenter
    public void b() {
        j();
        this.f18633e.d();
    }

    @Override // t6.IThermalPresenter
    public void c() {
        String string = this.f18629a.getString(R.string.high_temp_screen_dim);
        this.f18633e.f(string + "\n");
    }

    @Override // t6.IThermalPresenter
    public void d() {
        k();
        this.f18633e.c();
    }

    @Override // t6.IThermalPresenter
    public void e() {
        this.f18633e.c();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean h() {
        int f10 = f();
        return f10 > 0 && f10 < g();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void j() {
        if (!this.f18631c.getBoolean("should_restore_brightness", false)) {
            LocalLog.l("ThermalScreenPresenter", "no need resotre brightness");
            return;
        }
        if (this.f18631c.getBoolean("screen_manual", false)) {
            if (1 != Settings.System.getInt(this.f18628g, "screen_brightness_mode", -1)) {
                i(this.f18631c.getInt("screen_bright", 100));
                return;
            } else {
                LocalLog.l("ThermalScreenPresenter", "resetBright: ignore. is automatic mode");
                return;
            }
        }
        Settings.System.putInt(this.f18628g, "screen_brightness_mode", 1);
        LocalLog.l("ThermalScreenPresenter", "resotre brightness automatic mode");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void k() {
        int f10 = f();
        int g6 = g();
        int i10 = Settings.System.getInt(this.f18628g, "screen_brightness_mode", -1);
        if (f10 > 0 && f10 < g6) {
            this.f18632d.putBoolean("should_restore_brightness", true);
            if (i10 == 1) {
                Settings.System.putInt(this.f18628g, "screen_brightness_mode", 0);
                this.f18632d.putBoolean("screen_manual", false);
                LocalLog.l("ThermalScreenPresenter", "setBrightness manual");
            } else {
                this.f18632d.putBoolean("screen_manual", true);
            }
            this.f18632d.putInt("screen_bright", g6);
            this.f18632d.commit();
            i(f10);
            LocalLog.l("ThermalScreenPresenter", "setBrightness old=" + g6 + " new=" + f10);
            return;
        }
        LocalLog.l("ThermalScreenPresenter", "skip setBrightness for too bright");
        this.f18632d.putBoolean("should_restore_brightness", false);
    }
}
