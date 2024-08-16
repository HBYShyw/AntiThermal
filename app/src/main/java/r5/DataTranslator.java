package r5;

import android.os.Bundle;
import b5.CardAction;
import com.oplus.cardwidget.proto.CardActionProto;
import com.oplus.cardwidget.proto.UIDataProto;
import kotlin.Metadata;
import za.k;

/* compiled from: DataTranslator.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\t\u0010\nJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\b\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¨\u0006\u000b"}, d2 = {"Lr5/d;", "Lr5/f;", "", "data", "Lb5/a;", "a", "Landroid/os/Bundle;", "bundle", "b", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: r5.d, reason: use source file name */
/* loaded from: classes.dex */
public final class DataTranslator implements IDataHandle {

    /* renamed from: a, reason: collision with root package name */
    private final String f17532a = "DataTranslator";

    @Override // r5.IDataHandle
    public CardAction a(byte[] data) {
        k.e(data, "data");
        CardActionProto e10 = DataConvertHelper.e(data);
        CardAction h10 = DataConvertHelper.h(e10);
        s5.b.f18066c.c("State." + this.f17532a, "onDecode data size is " + data.length + " action is: " + h10);
        return DataConvertHelper.h(e10);
    }

    @Override // r5.IDataHandle
    public byte[] b(Bundle bundle) {
        String string;
        k.e(bundle, "bundle");
        UIDataProto g6 = DataConvertHelper.g(bundle);
        s5.b bVar = s5.b.f18066c;
        if (bVar.i() && (string = bundle.getString("widget_code")) != null) {
            String str = "Update." + this.f17532a;
            k.d(string, "widgetCode");
            bVar.d(str, string, "onEncode data is " + g6);
        }
        byte[] byteArray = g6.toByteArray();
        k.d(byteArray, "this.toByteArray()");
        return byteArray;
    }
}
