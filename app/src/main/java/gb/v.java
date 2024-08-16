package gb;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import za.DefaultConstructorMarker;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: TypesJVM.kt */
/* loaded from: classes2.dex */
public final class v implements WildcardType, Type {

    /* renamed from: g, reason: collision with root package name */
    public static final a f11637g = new a(null);

    /* renamed from: h, reason: collision with root package name */
    private static final v f11638h = new v(null, null);

    /* renamed from: e, reason: collision with root package name */
    private final Type f11639e;

    /* renamed from: f, reason: collision with root package name */
    private final Type f11640f;

    /* compiled from: TypesJVM.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final v a() {
            return v.f11638h;
        }
    }

    public v(Type type, Type type2) {
        this.f11639e = type;
        this.f11640f = type2;
    }

    public boolean equals(Object obj) {
        if (obj instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) obj;
            if (Arrays.equals(getUpperBounds(), wildcardType.getUpperBounds()) && Arrays.equals(getLowerBounds(), wildcardType.getLowerBounds())) {
                return true;
            }
        }
        return false;
    }

    @Override // java.lang.reflect.WildcardType
    public Type[] getLowerBounds() {
        Type type = this.f11640f;
        return type == null ? new Type[0] : new Type[]{type};
    }

    @Override // java.lang.reflect.Type
    public String getTypeName() {
        String h10;
        String h11;
        if (this.f11640f != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("? super ");
            h11 = u.h(this.f11640f);
            sb2.append(h11);
            return sb2.toString();
        }
        Type type = this.f11639e;
        if (type == null || za.k.a(type, Object.class)) {
            return "?";
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("? extends ");
        h10 = u.h(this.f11639e);
        sb3.append(h10);
        return sb3.toString();
    }

    @Override // java.lang.reflect.WildcardType
    public Type[] getUpperBounds() {
        Type[] typeArr = new Type[1];
        Type type = this.f11639e;
        if (type == null) {
            type = Object.class;
        }
        typeArr[0] = type;
        return typeArr;
    }

    public int hashCode() {
        return Arrays.hashCode(getLowerBounds()) ^ Arrays.hashCode(getUpperBounds());
    }

    public String toString() {
        return getTypeName();
    }
}
