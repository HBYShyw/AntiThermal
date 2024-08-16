package com.oplus.wrapper.media;

import android.media.MediaRouter;

/* loaded from: classes.dex */
public class MediaRouter {
    private final android.media.MediaRouter mMediaRouter;

    public MediaRouter(android.media.MediaRouter mediaRouter) {
        this.mMediaRouter = mediaRouter;
    }

    public MediaRouter.RouteInfo getSelectedRoute() {
        return this.mMediaRouter.getSelectedRoute();
    }
}
