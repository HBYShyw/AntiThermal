package a5;

import android.content.Context;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.Objects;
import kotlin.Metadata;
import ma.h;
import za.Reflection;

/* compiled from: BaseCardSource.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b&\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\t\u0010\nJ\u001a\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H&J\u0013\u0010\b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0003\u001a\u00020\u0002H¦\u0002¨\u0006\u000b"}, d2 = {"La5/a;", "", "", "cardId", "", ThermalBaseConfig.Item.ATTR_VALUE, "Lma/f0;", "b", "a", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: a5.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class BaseCardSource {

    /* renamed from: a, reason: collision with root package name */
    private final h f48a;

    public BaseCardSource() {
        v5.b bVar = v5.b.f19122c;
        if (bVar.b().get(Reflection.b(Context.class)) != null) {
            h<?> hVar = bVar.b().get(Reflection.b(Context.class));
            Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
            this.f48a = hVar;
            return;
        }
        throw new IllegalStateException("the class are not injected");
    }

    public abstract byte[] a(String cardId);

    public abstract void b(String str, byte[] bArr);
}
