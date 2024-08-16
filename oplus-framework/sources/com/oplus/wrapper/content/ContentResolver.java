package com.oplus.wrapper.content;

import android.database.ContentObserver;
import android.net.Uri;

/* loaded from: classes.dex */
public class ContentResolver {
    private final android.content.ContentResolver mContentResolver;

    public ContentResolver(android.content.ContentResolver contentResolver) {
        this.mContentResolver = contentResolver;
    }

    public void registerContentObserver(Uri uri, boolean notifyForDescendents, ContentObserver observer, int userHandle) {
        this.mContentResolver.registerContentObserver(uri, notifyForDescendents, observer, userHandle);
    }
}
