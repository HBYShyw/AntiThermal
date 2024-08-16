package vb;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import vb.ReflectJavaType;

/* compiled from: ReflectJavaField.kt */
/* renamed from: vb.r, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReflectJavaField extends t implements fc.n {

    /* renamed from: a, reason: collision with root package name */
    private final Field f19254a;

    public ReflectJavaField(Field field) {
        za.k.e(field, "member");
        this.f19254a = field;
    }

    @Override // fc.n
    public boolean M() {
        return X().isEnumConstant();
    }

    @Override // fc.n
    public boolean U() {
        return false;
    }

    @Override // vb.t
    /* renamed from: Z, reason: merged with bridge method [inline-methods] */
    public Field X() {
        return this.f19254a;
    }

    @Override // fc.n
    /* renamed from: a0, reason: merged with bridge method [inline-methods] */
    public ReflectJavaType getType() {
        ReflectJavaType.a aVar = ReflectJavaType.f19262a;
        Type genericType = X().getGenericType();
        za.k.d(genericType, "member.genericType");
        return aVar.a(genericType);
    }
}
