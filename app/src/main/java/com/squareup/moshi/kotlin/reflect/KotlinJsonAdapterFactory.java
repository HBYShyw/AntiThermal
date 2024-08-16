package com.squareup.moshi.kotlin.reflect;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.moshi._MoshiKotlinTypesExtensionsKt;
import com.squareup.moshi.internal.Util;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapter;
import fb._Ranges;
import gb.KClass;
import gb.KClassifier;
import gb.KFunction;
import gb.KParameter;
import gb.KType;
import gb.KTypeParameter;
import gb.KTypeProjection;
import gb.j;
import gb.n;
import hb.c;
import ib.KCallablesJvm;
import ib.ReflectJvmMapping;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.MapsJVM;
import kotlin.collections.MutableCollections;
import kotlin.collections._Collections;
import kotlin.collections.s;
import xa.JvmClassMapping;
import za.TypeIntrinsics;
import za.k;

/* compiled from: KotlinJsonAdapter.kt */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J.\u0010\u0003\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u000e\u0010\u0007\u001a\n\u0012\u0006\b\u0001\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000bH\u0016¨\u0006\f"}, d2 = {"Lcom/squareup/moshi/kotlin/reflect/KotlinJsonAdapterFactory;", "Lcom/squareup/moshi/JsonAdapter$Factory;", "()V", "create", "Lcom/squareup/moshi/JsonAdapter;", "type", "Ljava/lang/reflect/Type;", "annotations", "", "", "moshi", "Lcom/squareup/moshi/Moshi;", "reflect"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* loaded from: classes2.dex */
public final class KotlinJsonAdapterFactory implements JsonAdapter.Factory {
    /* JADX WARN: Code restructure failed: missing block: B:111:0x01db, code lost:
    
        if (r9 == null) goto L117;
     */
    @Override // com.squareup.moshi.JsonAdapter.Factory
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
        Class<? extends Annotation> cls;
        int u7;
        int e10;
        int c10;
        List Q;
        int u10;
        Object obj;
        List B0;
        String name;
        Type f10;
        Object obj2;
        k.e(type, "type");
        k.e(annotations, "annotations");
        k.e(moshi, "moshi");
        boolean z10 = true;
        if (!annotations.isEmpty()) {
            return null;
        }
        Class<?> rawType = _MoshiKotlinTypesExtensionsKt.getRawType(type);
        if (rawType.isInterface() || rawType.isEnum()) {
            return null;
        }
        cls = KotlinJsonAdapterKt.KOTLIN_METADATA;
        if (!rawType.isAnnotationPresent(cls) || Util.isPlatformType(rawType)) {
            return null;
        }
        try {
            JsonAdapter<?> generatedAdapter = Util.generatedAdapter(moshi, type, rawType);
            if (generatedAdapter != null) {
                return generatedAdapter;
            }
        } catch (RuntimeException e11) {
            if (!(e11.getCause() instanceof ClassNotFoundException)) {
                throw e11;
            }
        }
        if (!rawType.isLocalClass()) {
            KClass e12 = JvmClassMapping.e(rawType);
            if (!e12.n()) {
                if (!e12.r()) {
                    if (e12.w() == null) {
                        if (!e12.t()) {
                            KFunction b10 = c.b(e12);
                            if (b10 == null) {
                                return null;
                            }
                            List<KParameter> parameters = b10.getParameters();
                            u7 = s.u(parameters, 10);
                            e10 = MapsJVM.e(u7);
                            c10 = _Ranges.c(e10, 16);
                            LinkedHashMap linkedHashMap = new LinkedHashMap(c10);
                            for (Object obj3 : parameters) {
                                linkedHashMap.put(((KParameter) obj3).getName(), obj3);
                            }
                            KCallablesJvm.b(b10, true);
                            LinkedHashMap linkedHashMap2 = new LinkedHashMap();
                            for (n nVar : c.a(e12)) {
                                KParameter kParameter = (KParameter) linkedHashMap.get(nVar.getName());
                                KCallablesJvm.b(nVar, z10);
                                Iterator<T> it = nVar.i().iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        obj = null;
                                        break;
                                    }
                                    obj = it.next();
                                    if (((Annotation) obj) instanceof Json) {
                                        break;
                                    }
                                }
                                Json json = (Json) obj;
                                B0 = _Collections.B0(nVar.i());
                                if (kParameter != null) {
                                    MutableCollections.z(B0, kParameter.i());
                                    if (json == null) {
                                        Iterator<T> it2 = kParameter.i().iterator();
                                        while (true) {
                                            if (!it2.hasNext()) {
                                                obj2 = null;
                                                break;
                                            }
                                            obj2 = it2.next();
                                            if (((Annotation) obj2) instanceof Json) {
                                                break;
                                            }
                                        }
                                        json = (Json) obj2;
                                    }
                                }
                                Field b11 = ReflectJvmMapping.b(nVar);
                                if (Modifier.isTransient(b11 != null ? b11.getModifiers() : 0)) {
                                    if (!((kParameter == null || kParameter.z()) ? z10 : false)) {
                                        throw new IllegalArgumentException(("No default value for transient constructor " + kParameter).toString());
                                    }
                                } else if ((json == null || json.ignore() != z10) ? false : z10) {
                                    if (!((kParameter == null || kParameter.z()) ? z10 : false)) {
                                        throw new IllegalArgumentException(("No default value for ignored constructor " + kParameter).toString());
                                    }
                                } else if ((kParameter == null || k.a(kParameter.getType(), nVar.f())) ? z10 : false) {
                                    if ((nVar instanceof j) || kParameter != null) {
                                        if (json != null && (name = json.name()) != null) {
                                            if (k.a(name, Json.UNSET_NAME)) {
                                                name = null;
                                            }
                                        }
                                        name = nVar.getName();
                                        String str = name;
                                        KClassifier c11 = nVar.f().c();
                                        if (c11 instanceof KClass) {
                                            KClass kClass = (KClass) c11;
                                            if (kClass.q()) {
                                                f10 = JvmClassMapping.b(kClass);
                                                if (!nVar.f().b().isEmpty()) {
                                                    List<KTypeProjection> b12 = nVar.f().b();
                                                    ArrayList arrayList = new ArrayList();
                                                    Iterator<T> it3 = b12.iterator();
                                                    while (it3.hasNext()) {
                                                        KType c12 = ((KTypeProjection) it3.next()).c();
                                                        Type f11 = c12 != null ? ReflectJvmMapping.f(c12) : null;
                                                        if (f11 != null) {
                                                            arrayList.add(f11);
                                                        }
                                                    }
                                                    Object[] array = arrayList.toArray(new Type[0]);
                                                    k.c(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                                                    Type[] typeArr = (Type[]) array;
                                                    f10 = Types.newParameterizedType(f10, (Type[]) Arrays.copyOf(typeArr, typeArr.length));
                                                }
                                            } else {
                                                f10 = ReflectJvmMapping.f(nVar.f());
                                            }
                                        } else if (c11 instanceof KTypeParameter) {
                                            f10 = ReflectJvmMapping.f(nVar.f());
                                        } else {
                                            throw new IllegalStateException("Not possible!".toString());
                                        }
                                        Type resolve = Util.resolve(type, rawType, f10);
                                        Object[] array2 = B0.toArray(new Annotation[0]);
                                        k.c(array2, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                                        JsonAdapter adapter = moshi.adapter(resolve, Util.jsonAnnotations((Annotation[]) array2), nVar.getName());
                                        String name2 = nVar.getName();
                                        k.d(adapter, "adapter");
                                        k.c(nVar, "null cannot be cast to non-null type kotlin.reflect.KProperty1<kotlin.Any, kotlin.Any?>");
                                        linkedHashMap2.put(name2, new KotlinJsonAdapter.Binding(str, adapter, nVar, kParameter, kParameter != null ? kParameter.j() : -1));
                                        z10 = true;
                                    }
                                } else {
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append('\'');
                                    sb2.append(nVar.getName());
                                    sb2.append("' has a constructor parameter of type ");
                                    k.b(kParameter);
                                    sb2.append(kParameter.getType());
                                    sb2.append(" but a property of type ");
                                    sb2.append(nVar.f());
                                    sb2.append('.');
                                    throw new IllegalArgumentException(sb2.toString().toString());
                                }
                            }
                            ArrayList arrayList2 = new ArrayList();
                            for (KParameter kParameter2 : b10.getParameters()) {
                                KotlinJsonAdapter.Binding binding = (KotlinJsonAdapter.Binding) TypeIntrinsics.b(linkedHashMap2).remove(kParameter2.getName());
                                if (binding != null || kParameter2.z()) {
                                    arrayList2.add(binding);
                                } else {
                                    throw new IllegalArgumentException(("No property for required constructor " + kParameter2).toString());
                                }
                            }
                            int size = arrayList2.size();
                            Iterator it4 = linkedHashMap2.entrySet().iterator();
                            while (true) {
                                int i10 = size;
                                if (!it4.hasNext()) {
                                    break;
                                }
                                size = i10 + 1;
                                arrayList2.add(KotlinJsonAdapter.Binding.copy$default((KotlinJsonAdapter.Binding) ((Map.Entry) it4.next()).getValue(), null, null, null, null, i10, 15, null));
                            }
                            Q = _Collections.Q(arrayList2);
                            u10 = s.u(Q, 10);
                            ArrayList arrayList3 = new ArrayList(u10);
                            Iterator it5 = Q.iterator();
                            while (it5.hasNext()) {
                                arrayList3.add(((KotlinJsonAdapter.Binding) it5.next()).getJsonName());
                            }
                            Object[] array3 = arrayList3.toArray(new String[0]);
                            k.c(array3, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                            String[] strArr = (String[]) array3;
                            JsonReader.Options of = JsonReader.Options.of((String[]) Arrays.copyOf(strArr, strArr.length));
                            k.d(of, "options");
                            return new KotlinJsonAdapter(b10, arrayList2, Q, of).nullSafe();
                        }
                        throw new IllegalArgumentException(("Cannot reflectively serialize sealed class " + rawType.getName() + ". Please register an adapter.").toString());
                    }
                    throw new IllegalArgumentException(("Cannot serialize object declaration " + rawType.getName()).toString());
                }
                throw new IllegalArgumentException(("Cannot serialize inner class " + rawType.getName()).toString());
            }
            throw new IllegalArgumentException(("Cannot serialize abstract class " + rawType.getName()).toString());
        }
        throw new IllegalArgumentException(("Cannot serialize local class or object expression " + rawType.getName()).toString());
    }
}
