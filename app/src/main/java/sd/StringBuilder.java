package sd;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: StringBuilder.kt */
/* renamed from: sd.r, reason: use source file name */
/* loaded from: classes2.dex */
public class StringBuilder extends StringBuilderJVM {
    public static java.lang.StringBuilder i(java.lang.StringBuilder sb2, String... strArr) {
        za.k.e(sb2, "<this>");
        za.k.e(strArr, ThermalBaseConfig.Item.ATTR_VALUE);
        for (String str : strArr) {
            sb2.append(str);
        }
        return sb2;
    }
}
