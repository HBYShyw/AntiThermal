package com.coui.component.responsiveui.status;

import java.util.Arrays;
import kotlin.Metadata;

/* compiled from: FoldingState.kt */
@Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"}, d2 = {"Lcom/coui/component/responsiveui/status/FoldingState;", "", "(Ljava/lang/String;I)V", "toString", "", "FOLD", "UNFOLD", "UNKNOWN", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1}, xi = 48)
/* loaded from: classes.dex */
public enum FoldingState {
    FOLD,
    UNFOLD,
    UNKNOWN;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static FoldingState[] valuesCustom() {
        FoldingState[] valuesCustom = values();
        return (FoldingState[]) Arrays.copyOf(valuesCustom, valuesCustom.length);
    }

    @Override // java.lang.Enum
    public String toString() {
        return name();
    }
}
