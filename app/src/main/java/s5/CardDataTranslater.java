package s5;

import java.util.List;
import kotlin.Metadata;
import sd.StringsJVM;
import sd.v;
import za.k;

/* compiled from: CardDataTranslater.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\u001a \u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0000H\u0000\u001a\u000e\u0010\u0006\u001a\u0004\u0018\u00010\u0004*\u00020\u0004H\u0000\u001a\f\u0010\u0007\u001a\u00020\u0000*\u00020\u0004H\u0000\u001a\n\u0010\b\u001a\u00020\u0000*\u00020\u0004¨\u0006\t"}, d2 = {"", "cardType", "cardId", "hostId", "", "c", "d", "b", "a", "com.oplus.card.widget.cardwidget"}, k = 2, mv = {1, 4, 2})
/* renamed from: s5.a, reason: use source file name */
/* loaded from: classes.dex */
public final class CardDataTranslater {
    public static final int a(String str) {
        List q02;
        k.e(str, "$this$getHostId");
        try {
            q02 = v.q0(str, new String[]{"&"}, false, 0, 6, null);
            return Integer.parseInt((String) q02.get(2));
        } catch (Exception e10) {
            b.f18066c.e("", "get card hostId has error " + e10);
            return 0;
        }
    }

    public static final int b(String str) {
        String z10;
        k.e(str, "$this$getIdByWidgetCode");
        try {
            z10 = StringsJVM.z(str, "&", "", false, 4, null);
            return Integer.parseInt(z10);
        } catch (Exception e10) {
            b.f18066c.e("", "get id by widget code has error " + e10);
            return 0;
        }
    }

    public static final String c(int i10, int i11, int i12) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(i10);
        sb2.append('&');
        sb2.append(i11);
        sb2.append('&');
        sb2.append(i12);
        return sb2.toString();
    }

    public static final String d(String str) {
        k.e(str, "$this$getWidgetIdByObserver");
        if (str.length() <= 5) {
            return null;
        }
        String substring = str.substring(5);
        k.d(substring, "(this as java.lang.String).substring(startIndex)");
        return substring;
    }
}
