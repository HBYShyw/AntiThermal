package com.squareup.moshi.kotlin.reflect;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.internal.Util;
import gb.KFunction;
import gb.KParameter;
import gb.j;
import gb.n;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.AbstractMutableMap;
import kotlin.collections.r;
import kotlin.collections.s;
import za.k;

/* compiled from: KotlinJsonAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000\u0002:\u0002$%BW\u0012\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001d\u0012\u001c\u0010\u0012\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00028\u0000\u0012\u0006\u0012\u0004\u0018\u00010\u0011\u0018\u00010\u00100\u000f\u0012\u001a\u0010\u0016\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0006\u0012\u0004\u0018\u00010\u00110\u00100\u000f\u0012\u0006\u0010\u0019\u001a\u00020\u0018¢\u0006\u0004\b\"\u0010#J\u0017\u0010\u0005\u001a\u00028\u00002\u0006\u0010\u0004\u001a\u00020\u0003H\u0016¢\u0006\u0004\b\u0005\u0010\u0006J!\u0010\u000b\u001a\u00020\n2\u0006\u0010\b\u001a\u00020\u00072\b\u0010\t\u001a\u0004\u0018\u00018\u0000H\u0016¢\u0006\u0004\b\u000b\u0010\fJ\b\u0010\u000e\u001a\u00020\rH\u0016R-\u0010\u0012\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00028\u0000\u0012\u0006\u0012\u0004\u0018\u00010\u0011\u0018\u00010\u00100\u000f8\u0006¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R+\u0010\u0016\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0006\u0012\u0004\u0018\u00010\u00110\u00100\u000f8\u0006¢\u0006\f\n\u0004\b\u0016\u0010\u0013\u001a\u0004\b\u0017\u0010\u0015R\u0017\u0010\u0019\u001a\u00020\u00188\u0006¢\u0006\f\n\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u001b\u0010\u001cR\u001d\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001d8\u0006¢\u0006\f\n\u0004\b\u001e\u0010\u001f\u001a\u0004\b \u0010!¨\u0006&"}, d2 = {"Lcom/squareup/moshi/kotlin/reflect/KotlinJsonAdapter;", "T", "Lcom/squareup/moshi/JsonAdapter;", "Lcom/squareup/moshi/JsonReader;", "reader", "fromJson", "(Lcom/squareup/moshi/JsonReader;)Ljava/lang/Object;", "Lcom/squareup/moshi/JsonWriter;", "writer", ThermalBaseConfig.Item.ATTR_VALUE, "Lma/f0;", "toJson", "(Lcom/squareup/moshi/JsonWriter;Ljava/lang/Object;)V", "", "toString", "", "Lcom/squareup/moshi/kotlin/reflect/KotlinJsonAdapter$Binding;", "", "allBindings", "Ljava/util/List;", "getAllBindings", "()Ljava/util/List;", "nonIgnoredBindings", "getNonIgnoredBindings", "Lcom/squareup/moshi/JsonReader$Options;", "options", "Lcom/squareup/moshi/JsonReader$Options;", "getOptions", "()Lcom/squareup/moshi/JsonReader$Options;", "Lgb/h;", "constructor", "Lgb/h;", "getConstructor", "()Lgb/h;", "<init>", "(Lgb/h;Ljava/util/List;Ljava/util/List;Lcom/squareup/moshi/JsonReader$Options;)V", "Binding", "IndexedParameterMap", "reflect"}, k = 1, mv = {1, 7, 1})
/* loaded from: classes2.dex */
public final class KotlinJsonAdapter<T> extends JsonAdapter<T> {
    private final List<Binding<T, Object>> allBindings;
    private final KFunction<T> constructor;
    private final List<Binding<T, Object>> nonIgnoredBindings;
    private final JsonReader.Options options;

    /* compiled from: KotlinJsonAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0013\b\u0086\b\u0018\u0000*\u0004\b\u0001\u0010\u0001*\u0004\b\u0002\u0010\u00022\u00020\u0003BC\u0012\u0006\u0010\u0015\u001a\u00020\u000b\u0012\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00028\u00020\r\u0012\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\u000f\u0012\b\u0010\u0018\u001a\u0004\u0018\u00010\u0011\u0012\u0006\u0010\u0019\u001a\u00020\u0013¢\u0006\u0004\b/\u00100J\u0015\u0010\u0005\u001a\u00028\u00022\u0006\u0010\u0004\u001a\u00028\u0001¢\u0006\u0004\b\u0005\u0010\u0006J\u001d\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00028\u00012\u0006\u0010\u0004\u001a\u00028\u0002¢\u0006\u0004\b\t\u0010\nJ\t\u0010\f\u001a\u00020\u000bHÆ\u0003J\u000f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00020\rHÆ\u0003J\u0015\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\u000fHÆ\u0003J\u000b\u0010\u0012\u001a\u0004\u0018\u00010\u0011HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0013HÆ\u0003J[\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\u00002\b\b\u0002\u0010\u0015\u001a\u00020\u000b2\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00028\u00020\r2\u0014\b\u0002\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\u000f2\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u00112\b\b\u0002\u0010\u0019\u001a\u00020\u0013HÆ\u0001J\t\u0010\u001b\u001a\u00020\u000bHÖ\u0001J\t\u0010\u001c\u001a\u00020\u0013HÖ\u0001J\u0013\u0010\u001f\u001a\u00020\u001e2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0003HÖ\u0003R\u0017\u0010\u0015\u001a\u00020\u000b8\u0006¢\u0006\f\n\u0004\b\u0015\u0010 \u001a\u0004\b!\u0010\"R\u001d\u0010\u0016\u001a\b\u0012\u0004\u0012\u00028\u00020\r8\u0006¢\u0006\f\n\u0004\b\u0016\u0010#\u001a\u0004\b$\u0010%R\u0017\u0010\u0019\u001a\u00020\u00138\u0006¢\u0006\f\n\u0004\b\u0019\u0010&\u001a\u0004\b'\u0010(R#\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\u000f8\u0006¢\u0006\f\n\u0004\b\u0017\u0010)\u001a\u0004\b*\u0010+R\u0019\u0010\u0018\u001a\u0004\u0018\u00010\u00118\u0006¢\u0006\f\n\u0004\b\u0018\u0010,\u001a\u0004\b-\u0010.¨\u00061"}, d2 = {"Lcom/squareup/moshi/kotlin/reflect/KotlinJsonAdapter$Binding;", "K", "P", "", ThermalBaseConfig.Item.ATTR_VALUE, "get", "(Ljava/lang/Object;)Ljava/lang/Object;", "result", "Lma/f0;", "set", "(Ljava/lang/Object;Ljava/lang/Object;)V", "", "component1", "Lcom/squareup/moshi/JsonAdapter;", "component2", "Lgb/n;", "component3", "Lgb/k;", "component4", "", "component5", "jsonName", "adapter", "property", "parameter", "propertyIndex", "copy", "toString", "hashCode", "other", "", "equals", "Ljava/lang/String;", "getJsonName", "()Ljava/lang/String;", "Lcom/squareup/moshi/JsonAdapter;", "getAdapter", "()Lcom/squareup/moshi/JsonAdapter;", "I", "getPropertyIndex", "()I", "Lgb/n;", "getProperty", "()Lgb/n;", "Lgb/k;", "getParameter", "()Lgb/k;", "<init>", "(Ljava/lang/String;Lcom/squareup/moshi/JsonAdapter;Lgb/n;Lgb/k;I)V", "reflect"}, k = 1, mv = {1, 7, 1})
    /* loaded from: classes2.dex */
    public static final /* data */ class Binding<K, P> {
        private final JsonAdapter<P> adapter;
        private final String jsonName;
        private final KParameter parameter;
        private final n<K, P> property;
        private final int propertyIndex;

        /* JADX WARN: Multi-variable type inference failed */
        public Binding(String str, JsonAdapter<P> jsonAdapter, n<K, ? extends P> nVar, KParameter kParameter, int i10) {
            k.e(str, "jsonName");
            k.e(jsonAdapter, "adapter");
            k.e(nVar, "property");
            this.jsonName = str;
            this.adapter = jsonAdapter;
            this.property = nVar;
            this.parameter = kParameter;
            this.propertyIndex = i10;
        }

        public static /* synthetic */ Binding copy$default(Binding binding, String str, JsonAdapter jsonAdapter, n nVar, KParameter kParameter, int i10, int i11, Object obj) {
            if ((i11 & 1) != 0) {
                str = binding.jsonName;
            }
            if ((i11 & 2) != 0) {
                jsonAdapter = binding.adapter;
            }
            JsonAdapter jsonAdapter2 = jsonAdapter;
            if ((i11 & 4) != 0) {
                nVar = binding.property;
            }
            n nVar2 = nVar;
            if ((i11 & 8) != 0) {
                kParameter = binding.parameter;
            }
            KParameter kParameter2 = kParameter;
            if ((i11 & 16) != 0) {
                i10 = binding.propertyIndex;
            }
            return binding.copy(str, jsonAdapter2, nVar2, kParameter2, i10);
        }

        /* renamed from: component1, reason: from getter */
        public final String getJsonName() {
            return this.jsonName;
        }

        public final JsonAdapter<P> component2() {
            return this.adapter;
        }

        public final n<K, P> component3() {
            return this.property;
        }

        /* renamed from: component4, reason: from getter */
        public final KParameter getParameter() {
            return this.parameter;
        }

        /* renamed from: component5, reason: from getter */
        public final int getPropertyIndex() {
            return this.propertyIndex;
        }

        public final Binding<K, P> copy(String jsonName, JsonAdapter<P> adapter, n<K, ? extends P> property, KParameter parameter, int propertyIndex) {
            k.e(jsonName, "jsonName");
            k.e(adapter, "adapter");
            k.e(property, "property");
            return new Binding<>(jsonName, adapter, property, parameter, propertyIndex);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Binding)) {
                return false;
            }
            Binding binding = (Binding) other;
            return k.a(this.jsonName, binding.jsonName) && k.a(this.adapter, binding.adapter) && k.a(this.property, binding.property) && k.a(this.parameter, binding.parameter) && this.propertyIndex == binding.propertyIndex;
        }

        public final P get(K value) {
            return this.property.get(value);
        }

        public final JsonAdapter<P> getAdapter() {
            return this.adapter;
        }

        public final String getJsonName() {
            return this.jsonName;
        }

        public final KParameter getParameter() {
            return this.parameter;
        }

        public final n<K, P> getProperty() {
            return this.property;
        }

        public final int getPropertyIndex() {
            return this.propertyIndex;
        }

        public int hashCode() {
            int hashCode = ((((this.jsonName.hashCode() * 31) + this.adapter.hashCode()) * 31) + this.property.hashCode()) * 31;
            KParameter kParameter = this.parameter;
            return ((hashCode + (kParameter == null ? 0 : kParameter.hashCode())) * 31) + Integer.hashCode(this.propertyIndex);
        }

        public final void set(K result, P value) {
            Object obj;
            obj = KotlinJsonAdapterKt.ABSENT_VALUE;
            if (value != obj) {
                n<K, P> nVar = this.property;
                k.c(nVar, "null cannot be cast to non-null type kotlin.reflect.KMutableProperty1<K of com.squareup.moshi.kotlin.reflect.KotlinJsonAdapter.Binding, P of com.squareup.moshi.kotlin.reflect.KotlinJsonAdapter.Binding>");
                ((j) nVar).x(result, value);
            }
        }

        public String toString() {
            return "Binding(jsonName=" + this.jsonName + ", adapter=" + this.adapter + ", property=" + this.property + ", parameter=" + this.parameter + ", propertyIndex=" + this.propertyIndex + ')';
        }
    }

    /* compiled from: KotlinJsonAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\u0010'\n\u0002\b\u0006\u0018\u00002\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u0001B%\u0012\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00020\n\u0012\u000e\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\r¢\u0006\u0004\b\u0015\u0010\u0016J\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003H\u0016J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0002H\u0016J\u0013\u0010\t\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0002H\u0096\u0002R\u001a\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00020\n8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000b\u0010\fR\u001c\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\r8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000e\u0010\u000fR(\u0010\u0014\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u00110\u00108VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u0017"}, d2 = {"Lcom/squareup/moshi/kotlin/reflect/KotlinJsonAdapter$IndexedParameterMap;", "Lkotlin/collections/f;", "Lgb/k;", "", "key", ThermalBaseConfig.Item.ATTR_VALUE, "put", "", "containsKey", "get", "", "parameterKeys", "Ljava/util/List;", "", "parameterValues", "[Ljava/lang/Object;", "", "", "getEntries", "()Ljava/util/Set;", "entries", "<init>", "(Ljava/util/List;[Ljava/lang/Object;)V", "reflect"}, k = 1, mv = {1, 7, 1})
    /* loaded from: classes2.dex */
    public static final class IndexedParameterMap extends AbstractMutableMap<KParameter, Object> {
        private final List<KParameter> parameterKeys;
        private final Object[] parameterValues;

        /* JADX WARN: Multi-variable type inference failed */
        public IndexedParameterMap(List<? extends KParameter> list, Object[] objArr) {
            k.e(list, "parameterKeys");
            k.e(objArr, "parameterValues");
            this.parameterKeys = list;
            this.parameterValues = objArr;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public final /* bridge */ boolean containsKey(Object obj) {
            if (obj instanceof KParameter) {
                return containsKey((KParameter) obj);
            }
            return false;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public final /* bridge */ Object get(Object obj) {
            if (obj instanceof KParameter) {
                return get((KParameter) obj);
            }
            return null;
        }

        @Override // kotlin.collections.AbstractMutableMap
        public Set<Map.Entry<KParameter, Object>> getEntries() {
            int u7;
            Object obj;
            List<KParameter> list = this.parameterKeys;
            u7 = s.u(list, 10);
            ArrayList arrayList = new ArrayList(u7);
            int i10 = 0;
            for (T t7 : list) {
                int i11 = i10 + 1;
                if (i10 < 0) {
                    r.t();
                }
                arrayList.add(new AbstractMap.SimpleEntry((KParameter) t7, this.parameterValues[i10]));
                i10 = i11;
            }
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            for (T t10 : arrayList) {
                Object value = ((AbstractMap.SimpleEntry) t10).getValue();
                obj = KotlinJsonAdapterKt.ABSENT_VALUE;
                if (value != obj) {
                    linkedHashSet.add(t10);
                }
            }
            return linkedHashSet;
        }

        public /* bridge */ Object getOrDefault(KParameter kParameter, Object obj) {
            return super.getOrDefault((Object) kParameter, (KParameter) obj);
        }

        @Override // kotlin.collections.AbstractMutableMap, java.util.AbstractMap, java.util.Map
        public Object put(KParameter key, Object value) {
            k.e(key, "key");
            return null;
        }

        public /* bridge */ Object remove(KParameter kParameter) {
            return super.remove((Object) kParameter);
        }

        public boolean containsKey(KParameter key) {
            Object obj;
            k.e(key, "key");
            Object obj2 = this.parameterValues[key.j()];
            obj = KotlinJsonAdapterKt.ABSENT_VALUE;
            return obj2 != obj;
        }

        public Object get(KParameter key) {
            Object obj;
            k.e(key, "key");
            Object obj2 = this.parameterValues[key.j()];
            obj = KotlinJsonAdapterKt.ABSENT_VALUE;
            if (obj2 != obj) {
                return obj2;
            }
            return null;
        }

        @Override // java.util.Map
        public final /* bridge */ Object getOrDefault(Object obj, Object obj2) {
            return !(obj instanceof KParameter) ? obj2 : getOrDefault((KParameter) obj, obj2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public final /* bridge */ Object remove(Object obj) {
            if (obj instanceof KParameter) {
                return remove((KParameter) obj);
            }
            return null;
        }

        public /* bridge */ boolean remove(KParameter kParameter, Object obj) {
            return super.remove((Object) kParameter, obj);
        }

        @Override // java.util.Map
        public final /* bridge */ boolean remove(Object obj, Object obj2) {
            if (obj instanceof KParameter) {
                return remove((KParameter) obj, obj2);
            }
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public KotlinJsonAdapter(KFunction<? extends T> kFunction, List<Binding<T, Object>> list, List<Binding<T, Object>> list2, JsonReader.Options options) {
        k.e(kFunction, "constructor");
        k.e(list, "allBindings");
        k.e(list2, "nonIgnoredBindings");
        k.e(options, "options");
        this.constructor = kFunction;
        this.allBindings = list;
        this.nonIgnoredBindings = list2;
        this.options = options;
    }

    @Override // com.squareup.moshi.JsonAdapter
    public T fromJson(JsonReader reader) {
        T p10;
        Object obj;
        Object obj2;
        Object obj3;
        k.e(reader, "reader");
        int size = this.constructor.getParameters().size();
        int size2 = this.allBindings.size();
        Object[] objArr = new Object[size2];
        for (int i10 = 0; i10 < size2; i10++) {
            obj3 = KotlinJsonAdapterKt.ABSENT_VALUE;
            objArr[i10] = obj3;
        }
        reader.beginObject();
        while (reader.hasNext()) {
            int selectName = reader.selectName(this.options);
            if (selectName == -1) {
                reader.skipName();
                reader.skipValue();
            } else {
                Binding<T, Object> binding = this.nonIgnoredBindings.get(selectName);
                int propertyIndex = binding.getPropertyIndex();
                Object obj4 = objArr[propertyIndex];
                obj2 = KotlinJsonAdapterKt.ABSENT_VALUE;
                if (obj4 == obj2) {
                    objArr[propertyIndex] = binding.getAdapter().fromJson(reader);
                    if (objArr[propertyIndex] == null && !binding.getProperty().f().l()) {
                        JsonDataException unexpectedNull = Util.unexpectedNull(binding.getProperty().getName(), binding.getJsonName(), reader);
                        k.d(unexpectedNull, "unexpectedNull(\n        …         reader\n        )");
                        throw unexpectedNull;
                    }
                } else {
                    throw new JsonDataException("Multiple values for '" + binding.getProperty().getName() + "' at " + reader.getPath());
                }
            }
        }
        reader.endObject();
        boolean z10 = this.allBindings.size() == size;
        for (int i11 = 0; i11 < size; i11++) {
            Object obj5 = objArr[i11];
            obj = KotlinJsonAdapterKt.ABSENT_VALUE;
            if (obj5 == obj) {
                if (this.constructor.getParameters().get(i11).z()) {
                    z10 = false;
                } else {
                    if (!this.constructor.getParameters().get(i11).getType().l()) {
                        String name = this.constructor.getParameters().get(i11).getName();
                        Binding<T, Object> binding2 = this.allBindings.get(i11);
                        JsonDataException missingProperty = Util.missingProperty(name, binding2 != null ? binding2.getJsonName() : null, reader);
                        k.d(missingProperty, "missingProperty(\n       …       reader\n          )");
                        throw missingProperty;
                    }
                    objArr[i11] = null;
                }
            }
        }
        if (z10) {
            p10 = this.constructor.d(Arrays.copyOf(objArr, size2));
        } else {
            p10 = this.constructor.p(new IndexedParameterMap(this.constructor.getParameters(), objArr));
        }
        int size3 = this.allBindings.size();
        while (size < size3) {
            Binding binding3 = this.allBindings.get(size);
            k.b(binding3);
            binding3.set(p10, objArr[size]);
            size++;
        }
        return p10;
    }

    public final List<Binding<T, Object>> getAllBindings() {
        return this.allBindings;
    }

    public final KFunction<T> getConstructor() {
        return this.constructor;
    }

    public final List<Binding<T, Object>> getNonIgnoredBindings() {
        return this.nonIgnoredBindings;
    }

    public final JsonReader.Options getOptions() {
        return this.options;
    }

    @Override // com.squareup.moshi.JsonAdapter
    public void toJson(JsonWriter writer, T value) {
        k.e(writer, "writer");
        Objects.requireNonNull(value, "value == null");
        writer.beginObject();
        for (Binding<T, Object> binding : this.allBindings) {
            if (binding != null) {
                writer.name(binding.getJsonName());
                binding.getAdapter().toJson(writer, (JsonWriter) binding.get(value));
            }
        }
        writer.endObject();
    }

    public String toString() {
        return "KotlinJsonAdapter(" + this.constructor.f() + ')';
    }
}
