package com.android.server.media;

import android.media.MediaRoute2Info;
import com.android.internal.util.function.QuintConsumer;
import com.android.server.media.MediaRouter2ServiceImpl;

/* compiled from: R8$$SyntheticClass */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final /* synthetic */ class MediaRouter2ServiceImpl$$ExternalSyntheticLambda19 implements QuintConsumer {
    public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        ((MediaRouter2ServiceImpl.UserHandler) obj).transferToRouteOnHandler(((Long) obj2).longValue(), (MediaRouter2ServiceImpl.RouterRecord) obj3, (String) obj4, (MediaRoute2Info) obj5);
    }
}
