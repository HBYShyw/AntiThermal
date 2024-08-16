package t9;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import org.json.JSONObject;
import za.k;

/* compiled from: StartActivityClickEntity.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u000e\u0010\u000fJ\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0002J\u0016\u0010\n\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\u0002J\u000e\u0010\r\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u000b¨\u0006\u0010"}, d2 = {"Lt9/b;", "Lt9/a;", "", "packageName", "Lma/f0;", "e", "action", "d", "key", ThermalBaseConfig.Item.ATTR_VALUE, "f", "", "flag", "c", "<init>", "()V", "com.oplus.smartengine.smartenginehelper"}, k = 1, mv = {1, 4, 2})
/* renamed from: t9.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class StartActivityClickEntity extends ClickEntity {

    /* renamed from: b, reason: collision with root package name */
    private JSONObject f18699b;

    public StartActivityClickEntity() {
        b().put("type", "activity");
    }

    public final void c(int i10) {
        b().put("flag", i10 | b().optInt("flag", 0));
    }

    public final void d(String str) {
        k.e(str, "action");
        b().put("action", str);
    }

    public final void e(String str) {
        k.e(str, "packageName");
        b().put("packageName", str);
    }

    public final void f(String str, String str2) {
        k.e(str, "key");
        k.e(str2, ThermalBaseConfig.Item.ATTR_VALUE);
        if (this.f18699b == null) {
            this.f18699b = new JSONObject();
            b().put("params", this.f18699b);
        }
        JSONObject jSONObject = this.f18699b;
        if (jSONObject != null) {
            jSONObject.put(str, str2);
        }
    }
}
