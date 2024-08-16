package gb;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import kotlin.collections._Arrays;
import za.FunctionReferenceImpl;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: TypesJVM.kt */
/* loaded from: classes2.dex */
public final class s implements ParameterizedType, Type {

    /* renamed from: e, reason: collision with root package name */
    private final Class<?> f11630e;

    /* renamed from: f, reason: collision with root package name */
    private final Type f11631f;

    /* renamed from: g, reason: collision with root package name */
    private final Type[] f11632g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TypesJVM.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a extends FunctionReferenceImpl implements ya.l<Type, String> {

        /* renamed from: n, reason: collision with root package name */
        public static final a f11633n = new a();

        a() {
            super(1, u.class, "typeToString", "typeToString(Ljava/lang/reflect/Type;)Ljava/lang/String;", 1);
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final String invoke(Type type) {
            String h10;
            za.k.e(type, "p0");
            h10 = u.h(type);
            return h10;
        }
    }

    public s(Class<?> cls, Type type, List<? extends Type> list) {
        za.k.e(cls, "rawType");
        za.k.e(list, "typeArguments");
        this.f11630e = cls;
        this.f11631f = type;
        this.f11632g = (Type[]) list.toArray(new Type[0]);
    }

    public boolean equals(Object obj) {
        if (obj instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) obj;
            if (za.k.a(this.f11630e, parameterizedType.getRawType()) && za.k.a(this.f11631f, parameterizedType.getOwnerType()) && Arrays.equals(getActualTypeArguments(), parameterizedType.getActualTypeArguments())) {
                return true;
            }
        }
        return false;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type[] getActualTypeArguments() {
        return this.f11632g;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getOwnerType() {
        return this.f11631f;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getRawType() {
        return this.f11630e;
    }

    @Override // java.lang.reflect.Type
    public String getTypeName() {
        String h10;
        String h11;
        StringBuilder sb2 = new StringBuilder();
        Type type = this.f11631f;
        if (type != null) {
            h11 = u.h(type);
            sb2.append(h11);
            sb2.append("$");
            sb2.append(this.f11630e.getSimpleName());
        } else {
            h10 = u.h(this.f11630e);
            sb2.append(h10);
        }
        Type[] typeArr = this.f11632g;
        if (!(typeArr.length == 0)) {
            _Arrays.M(typeArr, sb2, null, "<", ">", 0, null, a.f11633n, 50, null);
        }
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public int hashCode() {
        int hashCode = this.f11630e.hashCode();
        Type type = this.f11631f;
        return Arrays.hashCode(getActualTypeArguments()) ^ (hashCode ^ (type != null ? type.hashCode() : 0));
    }

    public String toString() {
        return getTypeName();
    }
}
