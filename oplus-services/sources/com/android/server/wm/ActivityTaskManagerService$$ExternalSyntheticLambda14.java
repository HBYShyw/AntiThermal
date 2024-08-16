package com.android.server.wm;

import android.app.ActivityManagerInternal;
import java.util.function.BiConsumer;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final /* synthetic */ class ActivityTaskManagerService$$ExternalSyntheticLambda14 implements BiConsumer {
    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        ((ActivityManagerInternal) obj).updateOomLevelsForDisplay(((Integer) obj2).intValue());
    }
}
