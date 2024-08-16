package com.oplus.wrapper.media.session;

/* loaded from: classes.dex */
public class MediaSession {
    private android.media.session.MediaSession mMediaSession;

    public MediaSession(android.media.session.MediaSession mediaSession) {
        this.mMediaSession = mediaSession;
    }

    public String getCallingPackage() {
        return this.mMediaSession.getCallingPackage();
    }
}
