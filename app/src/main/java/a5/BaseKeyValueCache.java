package a5;

import android.content.Context;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.Objects;
import kotlin.Metadata;
import ma.h;
import za.Reflection;

/* compiled from: BaseKeyValueCache.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b&\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u000e\u0010\u000fJ\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0003\u001a\u00020\u0002H&J\u001a\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u0002H&R\u001b\u0010\r\u001a\u00020\b8FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\f¨\u0006\u0010"}, d2 = {"La5/b;", "", "", "key", "a", ThermalBaseConfig.Item.ATTR_VALUE, "", "c", "Landroid/content/Context;", "context$delegate", "Lma/h;", "b", "()Landroid/content/Context;", "context", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: a5.b, reason: use source file name */
/* loaded from: classes.dex */
public abstract class BaseKeyValueCache {

    /* renamed from: a, reason: collision with root package name */
    private final h f49a;

    public BaseKeyValueCache() {
        v5.b bVar = v5.b.f19122c;
        if (bVar.b().get(Reflection.b(Context.class)) != null) {
            h<?> hVar = bVar.b().get(Reflection.b(Context.class));
            Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
            this.f49a = hVar;
            return;
        }
        throw new IllegalStateException("the class are not injected");
    }

    public abstract String a(String key);

    public final Context b() {
        return (Context) this.f49a.getValue();
    }

    public abstract boolean c(String key, String value);
}
