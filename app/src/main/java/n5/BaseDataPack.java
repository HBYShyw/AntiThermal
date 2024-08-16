package n5;

import android.content.Context;
import android.os.Bundle;
import java.util.Objects;
import kotlin.Metadata;
import ma.h;
import ma.o;
import o5.IDataCompress;
import s9.DSLCoder;
import sd.Charsets;
import v5.b;
import za.DefaultConstructorMarker;
import za.Reflection;
import za.k;

/* compiled from: BaseDataPack.kt */
@Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\b&\u0018\u00002\u00020\u0001:\u0001\fB\u0007¢\u0006\u0004\b\u0015\u0010\u0016J\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0002J \u0010\f\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\tH\u0002J\"\u0010\r\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\tH\u0016J\u0010\u0010\u000e\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\u0004H&R\u001b\u0010\u0014\u001a\u00020\u000f8FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u0017"}, d2 = {"Ln5/a;", "", "", "dslData", "Ls9/a;", "d", "", "widgetCode", "coder", "", "forceUpdate", "Landroid/os/Bundle;", "a", "e", "c", "Lo5/b;", "dataCompress$delegate", "Lma/h;", "b", "()Lo5/b;", "dataCompress", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: n5.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class BaseDataPack {

    /* renamed from: d, reason: collision with root package name */
    public static final a f15873d = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final String f15874a = "Update.BaseDataPack";

    /* renamed from: b, reason: collision with root package name */
    private final h f15875b;

    /* renamed from: c, reason: collision with root package name */
    private final h f15876c;

    /* compiled from: BaseDataPack.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\t\u0010\nR\u0014\u0010\u0003\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0014\u0010\u0005\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0005\u0010\u0004R\u0014\u0010\u0006\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0004R\u0014\u0010\u0007\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0007\u0010\u0004R\u0014\u0010\b\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\b\u0010\u0004¨\u0006\u000b"}, d2 = {"Ln5/a$a;", "", "", "KEY_DATA_COMPRESS", "Ljava/lang/String;", "KEY_DSL_DATA", "KEY_DSL_NAME", "KEY_FORCE_CHANGE_UI", "KEY_LAYOUT_NAME", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: n5.a$a */
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public BaseDataPack() {
        b bVar = b.f19122c;
        if (bVar.b().get(Reflection.b(Context.class)) != null) {
            h<?> hVar = bVar.b().get(Reflection.b(Context.class));
            Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
            this.f15875b = hVar;
            if (bVar.b().get(Reflection.b(IDataCompress.class)) != null) {
                h<?> hVar2 = bVar.b().get(Reflection.b(IDataCompress.class));
                Objects.requireNonNull(hVar2, "null cannot be cast to non-null type kotlin.Lazy<T>");
                this.f15876c = hVar2;
                return;
            }
            throw new IllegalStateException("the class are not injected");
        }
        throw new IllegalStateException("the class are not injected");
    }

    private final Bundle a(String widgetCode, DSLCoder coder, boolean forceUpdate) {
        s5.b bVar = s5.b.f18066c;
        bVar.d(this.f15874a, widgetCode, "createPatch begin...");
        o<String, Integer> a10 = b().a(new String(coder.a(), Charsets.f18469b));
        Bundle bundle = new Bundle();
        bundle.putString("widget_code", widgetCode);
        bundle.putString("data", a10.c());
        bundle.putInt("compress", a10.d().intValue());
        bundle.putBoolean("forceChange", forceUpdate);
        bVar.d(this.f15874a, widgetCode, "layout data.first encompress size is " + a10.c().length());
        return bundle;
    }

    private final DSLCoder d(byte[] dslData) {
        if (dslData == null) {
            return null;
        }
        s5.b.f18066c.c(this.f15874a, "onPrepare");
        return new DSLCoder(dslData);
    }

    public final IDataCompress b() {
        return (IDataCompress) this.f15876c.getValue();
    }

    public abstract boolean c(DSLCoder coder);

    public Bundle e(String widgetCode, byte[] dslData, boolean forceUpdate) {
        k.e(widgetCode, "widgetCode");
        k.e(dslData, "dslData");
        s5.b.f18066c.d(this.f15874a, widgetCode, "onProcess begin... forceUpdate: " + forceUpdate);
        DSLCoder d10 = d(dslData);
        if (d10 == null || !c(d10)) {
            return null;
        }
        return a(widgetCode, d10, forceUpdate);
    }
}
