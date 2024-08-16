package e0;

import android.content.Context;
import java.util.List;

/* compiled from: Initializer.java */
/* renamed from: e0.a, reason: use source file name */
/* loaded from: classes.dex */
public interface Initializer<T> {
    List<Class<? extends Initializer<?>>> a();

    T b(Context context);
}
