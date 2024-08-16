package nd;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import gb.KClass;

/* compiled from: AttributeArrayOwner.kt */
/* renamed from: nd.e, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AttributeArrayOwner<K, T> extends a<K, T> {

    /* renamed from: e, reason: collision with root package name */
    private c<T> f16015e;

    protected AttributeArrayOwner(c<T> cVar) {
        za.k.e(cVar, "arrayMap");
        this.f16015e = cVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // nd.a
    public final c<T> d() {
        return this.f16015e;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public final void f(KClass<? extends K> kClass, T t7) {
        za.k.e(kClass, "tClass");
        za.k.e(t7, ThermalBaseConfig.Item.ATTR_VALUE);
        int d10 = e().d(kClass);
        int d11 = this.f16015e.d();
        if (d11 != 0) {
            if (d11 == 1) {
                c<T> cVar = this.f16015e;
                za.k.c(cVar, "null cannot be cast to non-null type org.jetbrains.kotlin.util.OneElementArrayMap<T of org.jetbrains.kotlin.util.AttributeArrayOwner>");
                o oVar = (o) cVar;
                if (oVar.f() == d10) {
                    this.f16015e = new o(t7, d10);
                    return;
                } else {
                    d dVar = new d();
                    this.f16015e = dVar;
                    dVar.e(oVar.f(), oVar.g());
                }
            }
            this.f16015e.e(d10, t7);
            return;
        }
        this.f16015e = new o(t7, d10);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public AttributeArrayOwner() {
        this(r0);
        i iVar = i.f16028e;
        za.k.c(iVar, "null cannot be cast to non-null type org.jetbrains.kotlin.util.ArrayMap<T of org.jetbrains.kotlin.util.AttributeArrayOwner>");
    }
}
