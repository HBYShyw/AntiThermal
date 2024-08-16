package com.oplus.wrapper.content;

/* loaded from: classes.dex */
public class ContentProviderOperation {
    private final android.content.ContentProviderOperation mContentProviderOperation;

    public ContentProviderOperation(android.content.ContentProviderOperation contentProviderOperation) {
        this.mContentProviderOperation = contentProviderOperation;
    }

    public int getType() {
        return this.mContentProviderOperation.getType();
    }
}
