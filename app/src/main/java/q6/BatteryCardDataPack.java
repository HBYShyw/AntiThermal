package q6;

import android.text.TextUtils;
import b6.LocalLog;
import java.util.Locale;
import n5.BaseDataPack;
import s9.DSLCoder;

/* compiled from: BatteryCardDataPack.java */
/* renamed from: q6.b, reason: use source file name */
/* loaded from: classes.dex */
public class BatteryCardDataPack extends BaseDataPack {

    /* renamed from: e, reason: collision with root package name */
    private BatteryCardData f16903e;

    public BatteryCardDataPack(BatteryCardData batteryCardData, int i10) {
        this.f16903e = batteryCardData;
    }

    private void f(DSLCoder dSLCoder) {
        if (TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1) {
            dSLCoder.c("percent", "layout_constraintStart_toStartOf", "parent");
            dSLCoder.c("percent", "layout_constraintStart_toEndOf", "");
            dSLCoder.d("percent", 16);
            dSLCoder.d("batteryLevel", 0);
            dSLCoder.c("batteryLevel", "layout_constraintStart_toStartOf", "");
            dSLCoder.c("batteryLevel", "layout_constraintStart_toEndOf", "percent");
            return;
        }
        dSLCoder.c("batteryLevel", "layout_constraintStart_toStartOf", "parent");
        dSLCoder.c("batteryLevel", "layout_constraintStart_toEndOf", "");
        dSLCoder.d("batteryLevel", 16);
        dSLCoder.d("percent", 0);
        dSLCoder.c("percent", "layout_constraintStart_toStartOf", "");
        dSLCoder.c("percent", "layout_constraintStart_toEndOf", "batteryLevel");
    }

    private void g(DSLCoder dSLCoder) {
        dSLCoder.c("filling_view1", "layout_constraintVertical_weight", Integer.valueOf(100 - this.f16903e.b()));
        dSLCoder.c("filling_view2", "layout_constraintVertical_weight", Integer.valueOf(this.f16903e.b()));
    }

    private void h(DSLCoder dSLCoder) {
        if (this.f16903e.h() != 0 && this.f16903e.d() != 5) {
            dSLCoder.g("bubble", 0);
        } else {
            dSLCoder.g("bubble", 4);
        }
    }

    @Override // n5.BaseDataPack
    public boolean c(DSLCoder dSLCoder) {
        LocalLog.a("BatteryCardDataPack", "level = " + this.f16903e.c() + ", left time = " + this.f16903e.i() + ", charge = " + this.f16903e.d() + ", power save = " + this.f16903e.j());
        dSLCoder.e("parent1", this.f16903e.f());
        dSLCoder.f("batteryLevel", this.f16903e.c());
        dSLCoder.f("percent", this.f16903e.g());
        g(dSLCoder);
        if (this.f16903e.h() == 0) {
            dSLCoder.f("batterystatus", "");
            if (this.f16903e.j()) {
                dSLCoder.b("parent3", "@drawable/ic_card_save_mode");
            } else if (this.f16903e.b() <= 20) {
                dSLCoder.b("parent3", "@drawable/ic_card_low_power");
            }
        } else {
            dSLCoder.f("batterystatus", this.f16903e.e());
        }
        h(dSLCoder);
        f(dSLCoder);
        return true;
    }
}
