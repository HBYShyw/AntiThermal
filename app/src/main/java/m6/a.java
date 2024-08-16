package m6;

import android.util.Log;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import kotlin.Metadata;
import ma.p;
import ma.q;
import za.k;

/* compiled from: GsonUtils.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0014\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u000e\u0010\u0002\u001a\u0004\u0018\u00010\u0001*\u0004\u0018\u00010\u0000\u001a#\u0010\u0006\u001a\u0004\u0018\u00018\u0000\"\u0004\b\u0000\u0010\u0003*\u0004\u0018\u00010\u00012\u0006\u0010\u0005\u001a\u00020\u0004¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"", "", "b", "T", "Ljava/lang/reflect/Type;", "type", "a", "(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;", "com.oplus.deepthinker.sdk_release"}, k = 2, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class a {
    public static final <T> T a(String str, Type type) {
        Object a10;
        k.e(type, "type");
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            p.a aVar = p.f15184e;
            a10 = p.a(new Gson().fromJson(str, type));
        } catch (Throwable th) {
            p.a aVar2 = p.f15184e;
            a10 = p.a(q.a(th));
        }
        Throwable b10 = p.b(a10);
        if (b10 != null) {
            Log.e("GsonUtils", k.l("fromJson exception: ", b10), b10);
        }
        if (p.c(a10)) {
            return null;
        }
        return (T) a10;
    }

    public static final String b(Object obj) {
        Object a10;
        if (obj == null) {
            return null;
        }
        try {
            p.a aVar = p.f15184e;
            a10 = p.a(new Gson().toJson(obj));
        } catch (Throwable th) {
            p.a aVar2 = p.f15184e;
            a10 = p.a(q.a(th));
        }
        Throwable b10 = p.b(a10);
        if (b10 != null) {
            Log.e("GsonUtils", k.l("toJson exception: ", b10), b10);
        }
        return (String) (p.c(a10) ? null : a10);
    }
}
