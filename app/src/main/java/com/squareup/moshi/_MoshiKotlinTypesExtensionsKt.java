package com.squareup.moshi;

import com.squareup.moshi.internal.Util;
import gb.KClass;
import gb.KType;
import gb.u;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Set;
import kotlin.Metadata;
import xa.JvmClassMapping;
import za.k;

/* compiled from: -MoshiKotlinTypesExtensions.kt */
@Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a'\u0010\u0003\u001a\n\u0012\u0004\u0012\u00020\u0000\u0018\u00010\u0002\"\n\b\u0000\u0010\u0001\u0018\u0001*\u00020\u0000*\b\u0012\u0004\u0012\u00020\u00000\u0002H\u0086\b\u001a\u0011\u0010\u0005\u001a\u00020\u0004\"\u0006\b\u0000\u0010\u0001\u0018\u0001H\u0087\b\u001a\u0011\u0010\u0006\u001a\u00020\u0004\"\u0006\b\u0000\u0010\u0001\u0018\u0001H\u0087\b\u001a\f\u0010\t\u001a\u00020\b*\u00020\u0007H\u0007\u001a\u000e\u0010\t\u001a\u00020\b*\u0006\u0012\u0002\b\u00030\n\u001a\n\u0010\t\u001a\u00020\b*\u00020\u000b\"\u0019\u0010\u000f\u001a\u0006\u0012\u0002\b\u00030\f*\u00020\u000b8F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e¨\u0006\u0010"}, d2 = {"", "T", "", "nextAnnotations", "Ljava/lang/reflect/WildcardType;", "subtypeOf", "supertypeOf", "Lgb/o;", "Ljava/lang/reflect/GenericArrayType;", "asArrayType", "Lgb/d;", "Ljava/lang/reflect/Type;", "Ljava/lang/Class;", "getRawType", "(Ljava/lang/reflect/Type;)Ljava/lang/Class;", "rawType", "moshi"}, k = 2, mv = {1, 7, 1})
/* loaded from: classes2.dex */
public final class _MoshiKotlinTypesExtensionsKt {
    public static final GenericArrayType asArrayType(KType kType) {
        k.e(kType, "<this>");
        return asArrayType(u.f(kType));
    }

    public static final Class<?> getRawType(Type type) {
        k.e(type, "<this>");
        Class<?> rawType = Types.getRawType(type);
        k.d(rawType, "getRawType(this)");
        return rawType;
    }

    public static final /* synthetic */ <T extends Annotation> Set<Annotation> nextAnnotations(Set<? extends Annotation> set) {
        k.e(set, "<this>");
        k.i(4, "T");
        return Types.nextAnnotations(set, Annotation.class);
    }

    public static final /* synthetic */ <T> WildcardType subtypeOf() {
        k.i(6, "T");
        Type f10 = u.f(null);
        if (f10 instanceof Class) {
            f10 = Util.boxIfPrimitive((Class) f10);
            k.d(f10, "boxIfPrimitive(type)");
        }
        WildcardType subtypeOf = Types.subtypeOf(f10);
        k.d(subtypeOf, "subtypeOf(type)");
        return subtypeOf;
    }

    public static final /* synthetic */ <T> WildcardType supertypeOf() {
        k.i(6, "T");
        Type f10 = u.f(null);
        if (f10 instanceof Class) {
            f10 = Util.boxIfPrimitive((Class) f10);
            k.d(f10, "boxIfPrimitive(type)");
        }
        WildcardType supertypeOf = Types.supertypeOf(f10);
        k.d(supertypeOf, "supertypeOf(type)");
        return supertypeOf;
    }

    public static final GenericArrayType asArrayType(KClass<?> kClass) {
        k.e(kClass, "<this>");
        return asArrayType(JvmClassMapping.b(kClass));
    }

    public static final GenericArrayType asArrayType(Type type) {
        k.e(type, "<this>");
        GenericArrayType arrayOf = Types.arrayOf(type);
        k.d(arrayOf, "arrayOf(this)");
        return arrayOf;
    }
}
