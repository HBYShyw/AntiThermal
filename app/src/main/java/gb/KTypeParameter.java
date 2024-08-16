package gb;

import java.util.List;

/* compiled from: KTypeParameter.kt */
/* renamed from: gb.p, reason: use source file name */
/* loaded from: classes2.dex */
public interface KTypeParameter extends KClassifier {
    String getName();

    List<KType> getUpperBounds();

    KVariance s();
}
