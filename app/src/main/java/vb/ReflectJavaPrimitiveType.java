package vb;

import java.util.Collection;
import java.util.List;
import mb.PrimitiveType;
import xc.JvmPrimitiveType;

/* compiled from: ReflectJavaPrimitiveType.kt */
/* renamed from: vb.x, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectJavaPrimitiveType extends ReflectJavaType implements fc.v {

    /* renamed from: b, reason: collision with root package name */
    private final Class<?> f19258b;

    /* renamed from: c, reason: collision with root package name */
    private final Collection<fc.a> f19259c;

    /* renamed from: d, reason: collision with root package name */
    private final boolean f19260d;

    public ReflectJavaPrimitiveType(Class<?> cls) {
        List j10;
        za.k.e(cls, "reflectType");
        this.f19258b = cls;
        j10 = kotlin.collections.r.j();
        this.f19259c = j10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // vb.ReflectJavaType
    /* renamed from: X, reason: merged with bridge method [inline-methods] */
    public Class<?> W() {
        return this.f19258b;
    }

    @Override // fc.v
    public PrimitiveType getType() {
        if (za.k.a(W(), Void.TYPE)) {
            return null;
        }
        return JvmPrimitiveType.b(W().getName()).f();
    }

    @Override // fc.d
    public Collection<fc.a> i() {
        return this.f19259c;
    }

    @Override // fc.d
    public boolean k() {
        return this.f19260d;
    }
}
