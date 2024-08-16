package com.squareup.moshi;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.internal.NonNullJsonAdapter;
import com.squareup.moshi.internal.NullSafeJsonAdapter;
import gb.KType;
import gb.u;
import kotlin.Metadata;
import za.k;

/* compiled from: -MoshiKotlinExtensions.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001a\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001b\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\"\u0006\b\u0000\u0010\u0000\u0018\u0001*\u00020\u0001H\u0087\b\u001a#\u0010\u0005\u001a\u00020\u0004\"\u0006\b\u0000\u0010\u0000\u0018\u0001*\u00020\u00042\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002H\u0087\b\u001a \u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\"\u0004\b\u0000\u0010\u0000*\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0007¨\u0006\b"}, d2 = {"T", "Lcom/squareup/moshi/Moshi;", "Lcom/squareup/moshi/JsonAdapter;", "adapter", "Lcom/squareup/moshi/Moshi$Builder;", "addAdapter", "Lgb/o;", "ktype", "moshi"}, k = 2, mv = {1, 7, 1})
/* loaded from: classes2.dex */
public final class _MoshiKotlinExtensionsKt {
    public static final /* synthetic */ <T> JsonAdapter<T> adapter(Moshi moshi) {
        k.e(moshi, "<this>");
        k.i(6, "T");
        return adapter(moshi, null);
    }

    public static final /* synthetic */ <T> Moshi.Builder addAdapter(Moshi.Builder builder, JsonAdapter<T> jsonAdapter) {
        k.e(builder, "<this>");
        k.e(jsonAdapter, "adapter");
        k.i(6, "T");
        Moshi.Builder add = builder.add(u.f(null), jsonAdapter);
        k.d(add, "add(typeOf<T>().javaType, adapter)");
        return add;
    }

    public static final <T> JsonAdapter<T> adapter(Moshi moshi, KType kType) {
        k.e(moshi, "<this>");
        k.e(kType, "ktype");
        JsonAdapter<T> adapter = moshi.adapter(u.f(kType));
        if ((adapter instanceof NullSafeJsonAdapter) || (adapter instanceof NonNullJsonAdapter)) {
            return adapter;
        }
        if (kType.l()) {
            JsonAdapter<T> nullSafe = adapter.nullSafe();
            k.d(nullSafe, "{\n    adapter.nullSafe()\n  }");
            return nullSafe;
        }
        JsonAdapter<T> nonNull = adapter.nonNull();
        k.d(nonNull, "{\n    adapter.nonNull()\n  }");
        return nonNull;
    }
}
