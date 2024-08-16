package com.android.server.wm;

import java.util.ArrayList;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final /* synthetic */ class Task$$ExternalSyntheticLambda44 implements Consumer {
    public final /* synthetic */ ArrayList f$0;

    public /* synthetic */ Task$$ExternalSyntheticLambda44(ArrayList arrayList) {
        this.f$0 = arrayList;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.add((ActivityRecord) obj);
    }
}
