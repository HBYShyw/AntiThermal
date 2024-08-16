package com.android.server.media;

import android.content.ContentResolver;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IMediaSessionServiceWrapper {
    default void updateMediaButtonReceiverInfo(ContentResolver contentResolver, MediaButtonReceiverHolder mediaButtonReceiverHolder, int i) {
    }

    default IMediaSessionServiceExt getExtImpl() {
        return new IMediaSessionServiceExt() { // from class: com.android.server.media.IMediaSessionServiceWrapper.1
        };
    }
}
