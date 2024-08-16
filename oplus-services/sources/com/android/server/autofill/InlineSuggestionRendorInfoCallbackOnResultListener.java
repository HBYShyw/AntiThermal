package com.android.server.autofill;

import android.os.Bundle;
import android.os.RemoteCallback;
import android.util.Slog;
import android.view.autofill.AutofillId;
import android.view.inputmethod.InlineSuggestionsRequest;
import java.lang.ref.WeakReference;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class InlineSuggestionRendorInfoCallbackOnResultListener implements RemoteCallback.OnResultListener {
    private static final String TAG = "InlineSuggestionRendorInfoCallbackOnResultListener";
    private final AutofillId mFocusedId;
    private final Consumer<InlineSuggestionsRequest> mInlineSuggestionsRequestConsumer;
    private final int mRequestIdCopy;
    private final WeakReference<Session> mSessionWeakReference;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InlineSuggestionRendorInfoCallbackOnResultListener(WeakReference<Session> weakReference, int i, Consumer<InlineSuggestionsRequest> consumer, AutofillId autofillId) {
        this.mRequestIdCopy = i;
        this.mInlineSuggestionsRequestConsumer = consumer;
        this.mSessionWeakReference = weakReference;
        this.mFocusedId = autofillId;
    }

    public void onResult(Bundle bundle) {
        Session session = this.mSessionWeakReference.get();
        if (session == null) {
            Slog.wtf(TAG, "Session is null before trying to call onResult");
            return;
        }
        synchronized (session.mLock) {
            if (session.mDestroyed) {
                Slog.wtf(TAG, "Session is destroyed before trying to call onResult");
            } else {
                session.mInlineSessionController.onCreateInlineSuggestionsRequestLocked(this.mFocusedId, session.inlineSuggestionsRequestCacheDecorator(this.mInlineSuggestionsRequestConsumer, this.mRequestIdCopy), bundle);
            }
        }
    }
}
