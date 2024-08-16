package u5;

import com.oplus.cardwidget.proto.CardActionProto;
import java.util.Map;
import kotlin.Metadata;
import za.k;

/* compiled from: DataConverterUtil.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\n\u0010\u0002\u001a\u00020\u0001*\u00020\u0000\u001a\n\u0010\u0004\u001a\u00020\u0003*\u00020\u0000¨\u0006\u0005"}, d2 = {"Lcom/oplus/cardwidget/proto/CardActionProto;", "Lu5/a;", "b", "", "a", "com.oplus.card.widget.cardwidget"}, k = 2, mv = {1, 4, 2})
/* renamed from: u5.c, reason: use source file name */
/* loaded from: classes.dex */
public final class DataConverterUtil {
    public static final String a(CardActionProto cardActionProto) {
        String str;
        k.e(cardActionProto, "$this$getLifeCircleAction");
        Action b10 = b(cardActionProto);
        return (b10.getAction() == 2 && (str = b10.b().get("life_circle")) != null) ? str : "";
    }

    public static final Action b(CardActionProto cardActionProto) {
        k.e(cardActionProto, "$this$toAction");
        int action = cardActionProto.getAction();
        Map<String, String> paramMap = cardActionProto.getParamMap();
        k.d(paramMap, "this.paramMap");
        return new Action(false, action, paramMap, 1, null);
    }
}
