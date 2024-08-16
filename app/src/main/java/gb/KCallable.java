package gb;

import java.util.List;
import java.util.Map;

/* compiled from: KCallable.kt */
/* renamed from: gb.c, reason: use source file name */
/* loaded from: classes2.dex */
public interface KCallable<R> extends KAnnotatedElement {
    R d(Object... objArr);

    KType f();

    String getName();

    List<KParameter> getParameters();

    R p(Map<KParameter, ? extends Object> map);
}
