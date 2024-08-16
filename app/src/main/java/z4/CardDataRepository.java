package z4;

import a5.BaseCardSource;
import a5.BaseKeyValueCache;
import android.content.Context;
import c5.ICardLayout;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import ma.h;
import ma.o;
import q5.FileSourceHelper;
import r5.DataConvertHelper;
import v5.b;
import za.Reflection;
import za.k;

/* compiled from: CardDataRepository.kt */
@Metadata(bv = {}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b)\u0010*J\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0012\u0010\u0006\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0003\u001a\u00020\u0002J\u0019\u0010\b\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\b\u0010\tJ!\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\n\u001a\u0004\u0018\u00010\u0002H\u0000¢\u0006\u0004\b\f\u0010\rJ!\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\u0005H\u0000¢\u0006\u0004\b\u000f\u0010\u0010J\u001f\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\u0012\u0010\rJ\u001f\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0014H\u0000¢\u0006\u0004\b\u0016\u0010\u0017J\u0017\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\u0018\u0010\u0019J%\u0010\u001c\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0005\u0012\u0004\u0012\u00020\u001b0\u001a2\u0006\u0010\u0003\u001a\u00020\u0002H\u0000¢\u0006\u0004\b\u001c\u0010\u001dR\u001b\u0010#\u001a\u00020\u001e8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u001f\u0010 \u001a\u0004\b!\u0010\"R\u001b\u0010(\u001a\u00020$8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b%\u0010 \u001a\u0004\b&\u0010'¨\u0006+"}, d2 = {"Lz4/a;", "", "", "widgetCode", "a", "", "b", "d", "e", "(Ljava/lang/String;)Ljava/lang/String;", "time", "Lma/f0;", "i", "(Ljava/lang/String;Ljava/lang/String;)V", "layoutData", "k", "(Ljava/lang/String;[B)V", "layoutName", "l", "key", "Lc5/a;", "holder", "h", "(Ljava/lang/String;Lc5/a;)V", "j", "(Ljava/lang/String;)V", "Lma/o;", "", "g", "(Ljava/lang/String;)Lma/o;", "La5/a;", "layoutDataSource$delegate", "Lma/h;", "c", "()La5/a;", "layoutDataSource", "La5/b;", "paramCache$delegate", "f", "()La5/b;", "paramCache", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: z4.a, reason: use source file name */
/* loaded from: classes.dex */
public final class CardDataRepository {

    /* renamed from: b, reason: collision with root package name */
    private static final h f20235b;

    /* renamed from: c, reason: collision with root package name */
    private static final h f20236c;

    /* renamed from: d, reason: collision with root package name */
    public static final CardDataRepository f20237d = new CardDataRepository();

    /* renamed from: a, reason: collision with root package name */
    private static final ConcurrentHashMap<String, ICardLayout> f20234a = new ConcurrentHashMap<>();

    static {
        b bVar = b.f19122c;
        if (bVar.b().get(Reflection.b(BaseCardSource.class)) != null) {
            h<?> hVar = bVar.b().get(Reflection.b(BaseCardSource.class));
            Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
            f20235b = hVar;
            if (bVar.b().get(Reflection.b(BaseKeyValueCache.class)) != null) {
                h<?> hVar2 = bVar.b().get(Reflection.b(BaseKeyValueCache.class));
                Objects.requireNonNull(hVar2, "null cannot be cast to non-null type kotlin.Lazy<T>");
                f20236c = hVar2;
                return;
            }
            throw new IllegalStateException("the class are not injected");
        }
        throw new IllegalStateException("the class are not injected");
    }

    private CardDataRepository() {
    }

    private final String a(String widgetCode) {
        s5.b.f18066c.c("CardDataRepository", "get layout name active widgetCode:" + widgetCode);
        ICardLayout iCardLayout = f20234a.get(widgetCode);
        if (iCardLayout != null) {
            return iCardLayout.i(widgetCode);
        }
        return null;
    }

    private final byte[] b(String widgetCode) {
        s5.b.f18066c.c("CardDataRepository", "getLayoutData key:" + widgetCode);
        return c().a("layoutData:" + widgetCode);
    }

    private final BaseCardSource c() {
        return (BaseCardSource) f20235b.getValue();
    }

    private final BaseKeyValueCache f() {
        return (BaseKeyValueCache) f20236c.getValue();
    }

    public final String d(String widgetCode) {
        k.e(widgetCode, "widgetCode");
        String a10 = f().a("layoutName:" + widgetCode);
        if (a10 != null && DataConvertHelper.f(a10)) {
            s5.b.f18066c.c("CardDataRepository", "getLayoutName key:" + widgetCode + " layoutName: " + a10);
            return a10;
        }
        s5.b.f18066c.d("CardDataRepository", widgetCode, "getLayoutName: return null");
        return null;
    }

    public final String e(String widgetCode) {
        k.e(widgetCode, "widgetCode");
        s5.b.f18066c.c("CardDataRepository", "getLayoutUpdateTime key:" + widgetCode);
        return f().a("updateTime:" + widgetCode);
    }

    public final o<byte[], Boolean> g(String widgetCode) {
        byte[] a10;
        String a11;
        k.e(widgetCode, "widgetCode");
        byte[] b10 = b(widgetCode);
        if (b10 != null) {
            String a12 = DataConvertHelper.a(b10);
            if (a12 != null) {
                s5.b bVar = s5.b.f18066c;
                if (bVar.i()) {
                    bVar.d("CardDataRepository", widgetCode, "getCardLayoutInfo local data size is:" + a12.length());
                }
                return new o<>(b10, Boolean.FALSE);
            }
            s5.b.f18066c.c("CardDataRepository", "current layout data is invalid: " + b10);
        } else {
            s5.b.f18066c.c("CardDataRepository", "get local layoutData is null");
        }
        String d10 = d(widgetCode);
        if (d10 == null) {
            d10 = a(widgetCode);
        }
        b bVar2 = b.f19122c;
        if (bVar2.b().get(Reflection.b(Context.class)) != null) {
            h<?> hVar = bVar2.b().get(Reflection.b(Context.class));
            Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
            h<?> hVar2 = hVar;
            if (d10 != null && (a10 = FileSourceHelper.a(d10, (Context) hVar2.getValue())) != null && (a11 = DataConvertHelper.a(a10)) != null) {
                s5.b bVar3 = s5.b.f18066c;
                if (bVar3.i()) {
                    bVar3.d("CardDataRepository", widgetCode, "getCardLayoutInfo: create data size is:" + a11.length() + " layoutName is: " + d10);
                }
                CardDataRepository cardDataRepository = f20237d;
                boolean z10 = cardDataRepository.e(widgetCode) == null;
                cardDataRepository.i(widgetCode, String.valueOf(System.currentTimeMillis()));
                cardDataRepository.l(widgetCode, d10);
                cardDataRepository.k(widgetCode, a10);
                return new o<>(a10, Boolean.valueOf(z10));
            }
            s5.b.f18066c.e("CardDataRepository", "card layout is invalid widgetCode:" + widgetCode + " layoutName:" + d10);
            return new o<>(null, Boolean.FALSE);
        }
        throw new IllegalStateException("the class are not injected");
    }

    public final void h(String key, ICardLayout holder) {
        k.e(key, "key");
        k.e(holder, "holder");
        s5.b.f18066c.c("CardDataRepository", "registerLayoutHolder key:" + key + " holder is " + holder);
        f20234a.put(key, holder);
    }

    public final void i(String widgetCode, String time) {
        k.e(widgetCode, "widgetCode");
        s5.b.f18066c.c("CardDataRepository", "setLayoutUpdateTime key:" + widgetCode + " time is:" + time);
        BaseKeyValueCache f10 = f();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("updateTime:");
        sb2.append(widgetCode);
        f10.c(sb2.toString(), time);
    }

    public final void j(String key) {
        k.e(key, "key");
        s5.b.f18066c.c("CardDataRepository", "unregisterLayoutHolder key:" + key);
        f20234a.remove(key);
    }

    public final void k(String widgetCode, byte[] layoutData) {
        k.e(widgetCode, "widgetCode");
        s5.b bVar = s5.b.f18066c;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("updateLayoutData key:");
        sb2.append(widgetCode);
        sb2.append(" data is null:");
        sb2.append(layoutData == null);
        bVar.c("CardDataRepository", sb2.toString());
        c().b("layoutData:" + widgetCode, layoutData);
    }

    public final void l(String widgetCode, String layoutName) {
        k.e(widgetCode, "widgetCode");
        k.e(layoutName, "layoutName");
        s5.b.f18066c.c("CardDataRepository", "updateLayoutName key:" + widgetCode + " $ name:" + layoutName);
        BaseKeyValueCache f10 = f();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("layoutName:");
        sb2.append(widgetCode);
        f10.c(sb2.toString(), layoutName);
    }
}
