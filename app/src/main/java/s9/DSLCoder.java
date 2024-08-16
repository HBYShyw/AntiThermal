package s9;

import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.nio.charset.Charset;
import java.util.Objects;
import kotlin.Metadata;
import org.json.JSONObject;
import sd.Charsets;
import t9.StartActivityClickEntity;
import za.k;

/* compiled from: DSLCoder.kt */
@Metadata(bv = {}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0015\u001a\u00020\u0013¢\u0006\u0004\b\u0016\u0010\u0017J\u0016\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0002J\u0016\u0010\b\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002J\u0016\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\tJ\u0016\u0010\r\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\tJ\u0016\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u000eJ\u001e\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0001J\u0006\u0010\u0014\u001a\u00020\u0013¨\u0006\u0018"}, d2 = {"Ls9/a;", "", "", "id", ThermalBaseConfig.Item.ATTR_VALUE, "Lma/f0;", "f", "src", "b", "", "size", "d", "visibility", "g", "Lt9/b;", "startActivityClickEntity", "e", TriggerEvent.NOTIFICATION_TAG, "c", "", "a", "byteArray", "<init>", "([B)V", "com.oplus.smartengine.smartenginehelper"}, k = 1, mv = {1, 4, 2})
/* renamed from: s9.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class DSLCoder {

    /* renamed from: a, reason: collision with root package name */
    private final JSONObject f18189a;

    public DSLCoder(byte[] bArr) {
        k.e(bArr, "byteArray");
        this.f18189a = new JSONObject(new String(bArr, Charsets.f18469b));
    }

    public final byte[] a() {
        String jSONObject = this.f18189a.toString();
        k.d(jSONObject, "jsonObject.toString()");
        Charset charset = Charsets.f18469b;
        Objects.requireNonNull(jSONObject, "null cannot be cast to non-null type java.lang.String");
        byte[] bytes = jSONObject.getBytes(charset);
        k.d(bytes, "(this as java.lang.String).getBytes(charset)");
        return bytes;
    }

    public final void b(String str, String str2) {
        k.e(str, "id");
        k.e(str2, "src");
        DSLUtils.f18190a.a(this.f18189a, str, "background", str2);
    }

    public final void c(String str, String str2, Object obj) {
        k.e(str, "id");
        k.e(str2, TriggerEvent.NOTIFICATION_TAG);
        k.e(obj, ThermalBaseConfig.Item.ATTR_VALUE);
        DSLUtils.f18190a.a(this.f18189a, str, str2, obj);
    }

    public final void d(String str, int i10) {
        k.e(str, "id");
        DSLUtils.f18190a.a(this.f18189a, str, "layout_marginStart", Integer.valueOf(i10));
    }

    public final void e(String str, StartActivityClickEntity startActivityClickEntity) {
        k.e(str, "id");
        k.e(startActivityClickEntity, "startActivityClickEntity");
        DSLUtils.f18190a.a(this.f18189a, str, "onClick", startActivityClickEntity.getF18698a());
    }

    public final void f(String str, String str2) {
        k.e(str, "id");
        k.e(str2, ThermalBaseConfig.Item.ATTR_VALUE);
        DSLUtils.f18190a.a(this.f18189a, str, "text", str2);
    }

    public final void g(String str, int i10) {
        k.e(str, "id");
        DSLUtils.f18190a.a(this.f18189a, str, "visibility", Integer.valueOf(i10));
    }
}
