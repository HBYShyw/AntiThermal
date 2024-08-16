package t6;

import android.content.Context;
import android.net.ConnectivityManager;
import b6.LocalLog;
import com.oplus.battery.R;
import f6.StatusUtils;

/* compiled from: HotSpotPresenter.java */
/* renamed from: t6.b, reason: use source file name */
/* loaded from: classes.dex */
public class HotSpotPresenter extends ScreenPresenter {
    public HotSpotPresenter(Context context) {
        super(context);
    }

    @Override // t6.ScreenPresenter, t6.IThermalPresenter
    public void a(int i10) {
        int i11;
        boolean m10 = m();
        if (m10) {
            this.f18633e.e(R.string.high_temp_hotspot_closing, this.f18629a.getString(R.string.high_temp_close), i10);
            i11 = 1;
        } else {
            i11 = 0;
        }
        this.f18632d.putInt("hotspot_presenter_type_bak", i11);
        this.f18632d.commit();
        LocalLog.l("ThermalHotSpotPresenter", "onNotifyIn: type=" + i11 + ", isHotspot=" + m10);
    }

    @Override // t6.ScreenPresenter, t6.IThermalPresenter
    public void b() {
        this.f18633e.d();
    }

    @Override // t6.ScreenPresenter, t6.IThermalPresenter
    public void c() {
        if (this.f18631c.getInt("hotspot_presenter_type_bak", 0) == 1) {
            String string = this.f18629a.getString(R.string.high_temp_hotspot_closed);
            this.f18633e.f(string + "\n");
        }
    }

    @Override // t6.ScreenPresenter, t6.IThermalPresenter
    public void d() {
        if (this.f18631c.getInt("hotspot_presenter_type_bak", 0) == 1) {
            l();
        }
        this.f18633e.c();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void l() {
        boolean m10 = m();
        this.f18632d.putBoolean("hotspot_on", m10);
        this.f18632d.commit();
        ((ConnectivityManager) this.f18629a.getSystemService("connectivity")).stopTethering(0);
        LocalLog.l("ThermalHotSpotPresenter", "closeHotSpot. isHotspotOn=" + m10);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean m() {
        return StatusUtils.a(this.f18629a).b(this.f18629a);
    }
}
