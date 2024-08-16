package t6;

import android.content.Context;
import b6.LocalLog;
import com.oplus.battery.R;

/* compiled from: HotScreenPresenter.java */
/* renamed from: t6.a, reason: use source file name */
/* loaded from: classes.dex */
public class HotScreenPresenter extends HotSpotPresenter {
    public HotScreenPresenter(Context context) {
        super(context);
    }

    @Override // t6.HotSpotPresenter, t6.ScreenPresenter, t6.IThermalPresenter
    public void a(int i10) {
        int i11;
        boolean h10 = h();
        boolean m10 = m();
        if (h10 && m10) {
            this.f18633e.e(R.string.hotspot_screen_closing, this.f18629a.getString(R.string.high_temp_close), i10);
            i11 = 3;
        } else if (h10) {
            this.f18633e.e(R.string.high_temp_screen_dimming, this.f18629a.getString(R.string.high_temp_dim), i10);
            i11 = 2;
        } else if (m10) {
            this.f18633e.e(R.string.high_temp_hotspot_closing, this.f18629a.getString(R.string.high_temp_close), i10);
            i11 = 1;
        } else {
            i11 = 0;
        }
        this.f18632d.putInt("presenter_type_bak", i11);
        this.f18632d.commit();
        LocalLog.l("ThermalHotScreenPresenter", "onNotifyIn: type=" + i11 + ", isBright=" + h10 + ", isHotspot=" + m10);
    }

    @Override // t6.HotSpotPresenter, t6.ScreenPresenter, t6.IThermalPresenter
    public void b() {
        int i10 = this.f18631c.getInt("presenter_type_bak", 0);
        if (i10 == 3) {
            j();
        } else if (i10 == 2) {
            j();
        }
        this.f18633e.d();
        LocalLog.l("ThermalHotScreenPresenter", "onTemperatureOut: type=" + i10);
    }

    @Override // t6.HotSpotPresenter, t6.ScreenPresenter, t6.IThermalPresenter
    public void c() {
        int i10 = this.f18631c.getInt("presenter_type_bak", 0);
        if (i10 == 3) {
            String string = this.f18629a.getString(R.string.hotspot_screen_closed);
            this.f18633e.f(string + "\n");
        } else if (i10 == 2) {
            String string2 = this.f18629a.getString(R.string.high_temp_screen_dim);
            this.f18633e.f(string2 + "\n");
        } else if (i10 == 1) {
            String string3 = this.f18629a.getString(R.string.high_temp_hotspot_closed);
            this.f18633e.f(string3 + "\n");
        }
        LocalLog.l("ThermalHotScreenPresenter", "onNotifyOut: type=" + i10);
    }

    @Override // t6.HotSpotPresenter, t6.ScreenPresenter, t6.IThermalPresenter
    public void d() {
        int i10 = this.f18631c.getInt("presenter_type_bak", 0);
        if (i10 == 3) {
            k();
            l();
        } else if (i10 == 2) {
            k();
        } else if (i10 == 1) {
            l();
        }
        this.f18633e.c();
        LocalLog.l("ThermalHotScreenPresenter", "onTemperatureIn: type=" + i10);
    }
}
