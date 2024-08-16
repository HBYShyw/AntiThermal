package com.android.server.soundtrigger;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final /* synthetic */ class SoundTriggerService$LocalSoundTriggerService$SessionImpl$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ SoundTriggerHelper f$0;

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.onAppOpStateChanged(((Boolean) obj).booleanValue());
    }
}
