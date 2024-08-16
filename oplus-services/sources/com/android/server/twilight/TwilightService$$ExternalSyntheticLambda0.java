package com.android.server.twilight;

import android.location.Location;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final /* synthetic */ class TwilightService$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ TwilightService f$0;

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.onLocationChanged((Location) obj);
    }
}
