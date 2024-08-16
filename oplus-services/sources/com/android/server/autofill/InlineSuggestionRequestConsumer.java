package com.android.server.autofill;

import android.util.Slog;
import android.view.inputmethod.InlineSuggestionsRequest;
import com.android.server.autofill.Session;
import java.lang.ref.WeakReference;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class InlineSuggestionRequestConsumer implements Consumer<InlineSuggestionsRequest> {
    static final String TAG = "InlineSuggestionRequestConsumer";
    private final WeakReference<Session.AssistDataReceiverImpl> mAssistDataReceiverWeakReference;
    private final WeakReference<ViewState> mViewStateWeakReference;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InlineSuggestionRequestConsumer(WeakReference<Session.AssistDataReceiverImpl> weakReference, WeakReference<ViewState> weakReference2) {
        this.mAssistDataReceiverWeakReference = weakReference;
        this.mViewStateWeakReference = weakReference2;
    }

    @Override // java.util.function.Consumer
    public void accept(InlineSuggestionsRequest inlineSuggestionsRequest) {
        Session.AssistDataReceiverImpl assistDataReceiverImpl = this.mAssistDataReceiverWeakReference.get();
        ViewState viewState = this.mViewStateWeakReference.get();
        if (assistDataReceiverImpl == null) {
            Slog.wtf(TAG, "assistDataReceiver is null when accepting new inline suggestionrequests");
        } else if (viewState == null) {
            Slog.wtf(TAG, "view state is null when accepting new inline suggestion requests");
        } else {
            assistDataReceiverImpl.handleInlineSuggestionRequest(inlineSuggestionsRequest, viewState);
        }
    }
}
