package com.android.server.people.data;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final /* synthetic */ class AbstractProtoDiskReadWriter$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ AbstractProtoDiskReadWriter f$0;

    public /* synthetic */ AbstractProtoDiskReadWriter$$ExternalSyntheticLambda1(AbstractProtoDiskReadWriter abstractProtoDiskReadWriter) {
        this.f$0 = abstractProtoDiskReadWriter;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.flushScheduledData();
    }
}
