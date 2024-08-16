package a5;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import ma.Unit;
import r5.DataConvertHelper;
import za.k;

/* compiled from: DSLCardMemSource.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\t\u0010\nJ\u001a\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0016J\u0013\u0010\b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0096\u0002¨\u0006\u000b"}, d2 = {"La5/d;", "La5/a;", "", "cardId", "", ThermalBaseConfig.Item.ATTR_VALUE, "Lma/f0;", "b", "a", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: a5.d, reason: use source file name */
/* loaded from: classes.dex */
public final class DSLCardMemSource extends BaseCardSource {

    /* renamed from: b, reason: collision with root package name */
    private final String f53b = "DSLCardMemSource";

    /* renamed from: c, reason: collision with root package name */
    private final Map<String, String> f54c = new LinkedHashMap();

    @Override // a5.BaseCardSource
    public byte[] a(String cardId) {
        byte[] c10;
        k.e(cardId, "cardId");
        s5.b.f18066c.c(this.f53b, "get card data id is:" + cardId);
        synchronized (this.f54c) {
            String str = this.f54c.get(cardId);
            c10 = str != null ? DataConvertHelper.c(str) : null;
        }
        return c10;
    }

    @Override // a5.BaseCardSource
    public void b(String str, byte[] bArr) {
        k.e(str, "cardId");
        s5.b bVar = s5.b.f18066c;
        String str2 = this.f53b;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("update cardId is:");
        sb2.append(str);
        sb2.append(" value is null:");
        sb2.append(bArr == null);
        bVar.c(str2, sb2.toString());
        synchronized (this.f54c) {
            try {
                if (bArr != null) {
                    this.f54c.put(str, DataConvertHelper.d(bArr));
                    Unit unit = Unit.f15173a;
                } else {
                    this.f54c.remove(str);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
