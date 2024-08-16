package androidx.core.os;

import android.os.Bundle;
import android.util.Size;
import android.util.SizeF;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import za.k;

/* compiled from: Bundle.kt */
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bÃ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\f\u0010\rJ\"\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0007J\"\u0010\u000b\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\nH\u0007¨\u0006\u000e"}, d2 = {"Landroidx/core/os/c;", "", "Landroid/os/Bundle;", "bundle", "", "key", "Landroid/util/Size;", ThermalBaseConfig.Item.ATTR_VALUE, "Lma/f0;", "a", "Landroid/util/SizeF;", "b", "<init>", "()V", "core-ktx_release"}, k = 1, mv = {1, 7, 1})
/* loaded from: classes.dex */
final class c {

    /* renamed from: a, reason: collision with root package name */
    public static final c f2210a = new c();

    private c() {
    }

    public static final void a(Bundle bundle, String str, Size size) {
        k.e(bundle, "bundle");
        k.e(str, "key");
        bundle.putSize(str, size);
    }

    public static final void b(Bundle bundle, String str, SizeF sizeF) {
        k.e(bundle, "bundle");
        k.e(str, "key");
        bundle.putSizeF(str, sizeF);
    }
}
