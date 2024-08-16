package com.android.server.accessibility.magnification;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final /* synthetic */ class MagnificationThumbnail$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ MagnificationThumbnail f$0;

    public /* synthetic */ MagnificationThumbnail$$ExternalSyntheticLambda1(MagnificationThumbnail magnificationThumbnail) {
        this.f$0 = magnificationThumbnail;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.hideThumbnailMainThread();
    }
}
