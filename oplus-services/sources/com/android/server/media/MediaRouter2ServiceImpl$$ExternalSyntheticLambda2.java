package com.android.server.media;

import android.media.MediaRoute2Info;
import com.android.internal.util.function.QuadConsumer;
import com.android.server.media.MediaRouter2ServiceImpl;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final /* synthetic */ class MediaRouter2ServiceImpl$$ExternalSyntheticLambda2 implements QuadConsumer {
    public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
        ((MediaRouter2ServiceImpl.UserHandler) obj).setRouteVolumeOnHandler(((Long) obj2).longValue(), (MediaRoute2Info) obj3, ((Integer) obj4).intValue());
    }
}
