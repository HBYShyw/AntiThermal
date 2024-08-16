package com.android.server.wm;

import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final /* synthetic */ class Transition$ChangeInfo$$ExternalSyntheticLambda0 implements Predicate {
    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return ((ActivityRecord) obj).hasStartingWindow();
    }
}
