package r5;

import android.os.Bundle;
import android.util.Base64;
import b5.CardAction;
import com.oplus.cardwidget.proto.CardActionProto;
import com.oplus.cardwidget.proto.UIDataProto;
import kotlin.Metadata;
import org.json.JSONObject;
import s5.CardDataTranslater;
import sd.Charsets;
import sd.StringsJVM;
import za.k;

/* compiled from: DataConvertHelper.kt */
@Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\u001a\f\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u0000\u001a\f\u0010\u0005\u001a\u00020\u0004*\u00020\u0003H\u0000\u001a\f\u0010\u0007\u001a\u00020\u0000*\u00020\u0006H\u0000\u001a\f\u0010\t\u001a\u00020\b*\u00020\u0006H\u0000\u001a\f\u0010\n\u001a\u00020\u0006*\u00020\bH\u0000\u001a\u000e\u0010\u000b\u001a\u0004\u0018\u00010\b*\u00020\u0006H\u0000\u001a\f\u0010\r\u001a\u00020\f*\u00020\bH\u0000\u001a\f\u0010\u000e\u001a\u00020\f*\u00020\bH\u0000¨\u0006\u000f"}, d2 = {"Lcom/oplus/cardwidget/proto/CardActionProto;", "Lb5/a;", "h", "Landroid/os/Bundle;", "Lcom/oplus/cardwidget/proto/UIDataProto;", "g", "", "e", "", "d", "c", "a", "", "b", "f", "com.oplus.card.widget.cardwidget"}, k = 2, mv = {1, 4, 2})
/* renamed from: r5.c, reason: use source file name */
/* loaded from: classes.dex */
public final class DataConvertHelper {
    public static final String a(byte[] bArr) {
        k.e(bArr, "$this$checkIsEffectJsonData");
        String str = new String(bArr, Charsets.f18469b);
        if (b(str)) {
            return str;
        }
        return null;
    }

    public static final boolean b(String str) {
        k.e(str, "$this$checkIsJsonString");
        try {
            new JSONObject(str);
            return true;
        } catch (Exception e10) {
            s5.b.f18066c.e("DataConvertHelper", "checkIsEffectJsonData has error e:" + e10.getMessage());
            return false;
        }
    }

    public static final byte[] c(String str) {
        k.e(str, "$this$convertToByteArray");
        byte[] bytes = str.getBytes(Charsets.f18469b);
        k.d(bytes, "(this as java.lang.String).getBytes(charset)");
        byte[] decode = Base64.decode(bytes, 0);
        k.d(decode, "Base64.decode(this.toByteArray(), Base64.DEFAULT)");
        return decode;
    }

    public static final String d(byte[] bArr) {
        k.e(bArr, "$this$convertToString");
        byte[] encode = Base64.encode(bArr, 0);
        k.d(encode, "Base64.encode(this, Base64.DEFAULT)");
        return new String(encode, Charsets.f18469b);
    }

    public static final CardActionProto e(byte[] bArr) {
        k.e(bArr, "$this$getCardActionProto");
        CardActionProto parseFrom = CardActionProto.parseFrom(bArr);
        k.d(parseFrom, "CardActionProto.parseFrom(this)");
        return parseFrom;
    }

    public static final boolean f(String str) {
        boolean q10;
        k.e(str, "$this$isEffectLayoutName");
        boolean z10 = str.length() > 0;
        q10 = StringsJVM.q(str, ".json", false, 2, null);
        return q10 & z10;
    }

    public static final UIDataProto g(Bundle bundle) {
        k.e(bundle, "$this$packUiData");
        UIDataProto.Builder newBuilder = UIDataProto.newBuilder();
        String string = bundle.getString("widget_code");
        if (string != null) {
            newBuilder.setCardId(CardDataTranslater.b(string));
        }
        String string2 = bundle.getString("data");
        if (string2 != null) {
            newBuilder.setData(string2);
        }
        String string3 = bundle.getString("name");
        if (string3 != null) {
            newBuilder.setName(string3);
        }
        newBuilder.setCompress(UIDataProto.DataCompress.forNumber(bundle.getInt("compress")));
        newBuilder.setForceChangeCardUI(bundle.getBoolean("forceChange"));
        String string4 = bundle.getString("layoutName");
        if (string4 != null) {
            newBuilder.setLayoutName(string4);
        }
        UIDataProto build = newBuilder.build();
        k.d(build, "builder.build()");
        return build;
    }

    public static final CardAction h(CardActionProto cardActionProto) {
        k.e(cardActionProto, "$this$toCardAction");
        return new CardAction(CardDataTranslater.c(cardActionProto.getCardType(), cardActionProto.getCardId(), cardActionProto.getHostId()), cardActionProto.getAction(), cardActionProto.getParamMap());
    }
}
