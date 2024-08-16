package com.android.server.autofill.ui;

import android.content.IntentSender;
import android.os.IBinder;
import android.service.autofill.IInlineSuggestionUiCallback;
import android.service.autofill.InlinePresentation;
import android.util.Slog;
import com.android.server.LocalServices;
import com.android.server.autofill.Helper;
import com.android.server.autofill.RemoteInlineSuggestionRenderService;
import com.android.server.autofill.ui.InlineFillUi;
import com.android.server.inputmethod.InputMethodManagerInternal;
import java.util.Objects;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class RemoteInlineSuggestionViewConnector {
    private static final String TAG = "RemoteInlineSuggestionViewConnector";
    private final int mDisplayId;
    private final IBinder mHostInputToken;
    private final InlinePresentation mInlinePresentation;
    private final Runnable mOnAutofillCallback;
    private final Runnable mOnErrorCallback;
    private final Runnable mOnInflateCallback;
    private final RemoteInlineSuggestionRenderService mRemoteRenderService;
    private final int mSessionId;
    private final Consumer<IntentSender> mStartIntentSenderFromClientApp;
    private final int mUserId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteInlineSuggestionViewConnector(InlineFillUi.InlineFillUiInfo inlineFillUiInfo, InlinePresentation inlinePresentation, Runnable runnable, final InlineFillUi.InlineSuggestionUiCallback inlineSuggestionUiCallback) {
        this.mRemoteRenderService = inlineFillUiInfo.mRemoteRenderService;
        this.mInlinePresentation = inlinePresentation;
        this.mHostInputToken = inlineFillUiInfo.mInlineRequest.getHostInputToken();
        this.mDisplayId = inlineFillUiInfo.mInlineRequest.getHostDisplayId();
        this.mUserId = inlineFillUiInfo.mUserId;
        this.mSessionId = inlineFillUiInfo.mSessionId;
        this.mOnAutofillCallback = runnable;
        Objects.requireNonNull(inlineSuggestionUiCallback);
        this.mOnErrorCallback = new Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionViewConnector$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                InlineFillUi.InlineSuggestionUiCallback.this.onError();
            }
        };
        this.mOnInflateCallback = new Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionViewConnector$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                InlineFillUi.InlineSuggestionUiCallback.this.onInflate();
            }
        };
        this.mStartIntentSenderFromClientApp = new Consumer() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionViewConnector$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                InlineFillUi.InlineSuggestionUiCallback.this.startIntentSender((IntentSender) obj);
            }
        };
    }

    public boolean renderSuggestion(int i, int i2, IInlineSuggestionUiCallback iInlineSuggestionUiCallback) {
        if (Helper.sanitizeSlice(this.mInlinePresentation.getSlice()) == null) {
            if (Helper.sDebug) {
                Slog.d(TAG, "Skipped rendering inline suggestion.");
            }
            return false;
        }
        if (this.mRemoteRenderService == null) {
            return false;
        }
        if (Helper.sDebug) {
            Slog.d(TAG, "Request to recreate the UI");
        }
        this.mRemoteRenderService.renderSuggestion(iInlineSuggestionUiCallback, this.mInlinePresentation, i, i2, this.mHostInputToken, this.mDisplayId, this.mUserId, this.mSessionId);
        return true;
    }

    public void onClick() {
        this.mOnAutofillCallback.run();
    }

    public void onError() {
        this.mOnErrorCallback.run();
    }

    public void onRender() {
        this.mOnInflateCallback.run();
    }

    public void onTransferTouchFocusToImeWindow(IBinder iBinder, int i) {
        if (((InputMethodManagerInternal) LocalServices.getService(InputMethodManagerInternal.class)).transferTouchFocusToImeWindow(iBinder, i)) {
            return;
        }
        Slog.e(TAG, "Cannot transfer touch focus from suggestion to IME");
        this.mOnErrorCallback.run();
    }

    public void onStartIntentSender(IntentSender intentSender) {
        this.mStartIntentSenderFromClientApp.accept(intentSender);
    }
}
