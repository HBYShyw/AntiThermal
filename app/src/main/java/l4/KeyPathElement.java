package l4;

import java.util.List;
import t4.EffectiveValueCallback;

/* compiled from: KeyPathElement.java */
/* renamed from: l4.g, reason: use source file name */
/* loaded from: classes.dex */
public interface KeyPathElement {
    <T> void c(T t7, EffectiveValueCallback<T> effectiveValueCallback);

    void d(KeyPath keyPath, int i10, List<KeyPath> list, KeyPath keyPath2);
}
