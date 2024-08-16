package com.android.server.autofill;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Slog;
import android.view.autofill.AutofillId;
import android.view.inputmethod.InlineSuggestion;
import android.view.inputmethod.InlineSuggestionsRequest;
import android.view.inputmethod.InlineSuggestionsResponse;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.inputmethod.IInlineSuggestionsRequestCallback;
import com.android.internal.inputmethod.IInlineSuggestionsResponseCallback;
import com.android.internal.inputmethod.InlineSuggestionsRequestInfo;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.autofill.ui.InlineFillUi;
import com.android.server.inputmethod.InputMethodManagerInternal;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AutofillInlineSuggestionsRequestSession {
    private static final String TAG = "AutofillInlineSuggestionsRequestSession";

    @GuardedBy({"mLock"})
    private AutofillId mAutofillId;
    private final ComponentName mComponentName;
    private final Handler mHandler;

    @GuardedBy({"mLock"})
    private AutofillId mImeCurrentFieldId;

    @GuardedBy({"mLock"})
    private boolean mImeInputStarted;

    @GuardedBy({"mLock"})
    private boolean mImeInputViewStarted;

    @GuardedBy({"mLock"})
    private InlineSuggestionsRequest mImeRequest;

    @GuardedBy({"mLock"})
    private Consumer<InlineSuggestionsRequest> mImeRequestConsumer;

    @GuardedBy({"mLock"})
    private boolean mImeRequestReceived;

    @GuardedBy({"mLock"})
    private InlineFillUi mInlineFillUi;
    private final InputMethodManagerInternal mInputMethodManagerInternal;
    private final Object mLock;

    @GuardedBy({"mLock"})
    private boolean mPreviousHasNonPinSuggestionShow;

    @GuardedBy({"mLock"})
    private IInlineSuggestionsResponseCallback mResponseCallback;
    private final InlineFillUi.InlineUiEventCallback mUiCallback;
    private final Bundle mUiExtras;
    private final int mUserId;

    @GuardedBy({"mLock"})
    private Boolean mPreviousResponseIsNotEmpty = null;

    @GuardedBy({"mLock"})
    private boolean mDestroyed = false;

    @GuardedBy({"mLock"})
    private boolean mImeSessionInvalidated = false;
    private boolean mImeShowing = false;

    public AutofillInlineSuggestionsRequestSession(InputMethodManagerInternal inputMethodManagerInternal, int i, ComponentName componentName, Handler handler, Object obj, AutofillId autofillId, Consumer<InlineSuggestionsRequest> consumer, Bundle bundle, InlineFillUi.InlineUiEventCallback inlineUiEventCallback) {
        this.mInputMethodManagerInternal = inputMethodManagerInternal;
        this.mUserId = i;
        this.mComponentName = componentName;
        this.mHandler = handler;
        this.mLock = obj;
        this.mUiExtras = bundle;
        this.mUiCallback = inlineUiEventCallback;
        this.mAutofillId = autofillId;
        this.mImeRequestConsumer = consumer;
    }

    @GuardedBy({"mLock"})
    AutofillId getAutofillIdLocked() {
        return this.mAutofillId;
    }

    @GuardedBy({"mLock"})
    public Optional<InlineSuggestionsRequest> getInlineSuggestionsRequestLocked() {
        if (this.mDestroyed) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.mImeRequest);
    }

    @GuardedBy({"mLock"})
    public boolean onInlineSuggestionsResponseLocked(InlineFillUi inlineFillUi) {
        if (this.mDestroyed) {
            return false;
        }
        if (Helper.sDebug) {
            Slog.d(TAG, "onInlineSuggestionsResponseLocked called for:" + inlineFillUi.getAutofillId());
        }
        if (this.mImeRequest == null || this.mResponseCallback == null || this.mImeSessionInvalidated) {
            return false;
        }
        this.mAutofillId = inlineFillUi.getAutofillId();
        this.mInlineFillUi = inlineFillUi;
        maybeUpdateResponseToImeLocked();
        return true;
    }

    @GuardedBy({"mLock"})
    public void destroySessionLocked() {
        this.mDestroyed = true;
        if (this.mImeRequestReceived) {
            return;
        }
        Slog.w(TAG, "Never received an InlineSuggestionsRequest from the IME for " + this.mAutofillId);
    }

    @GuardedBy({"mLock"})
    public void onCreateInlineSuggestionsRequestLocked() {
        if (this.mDestroyed) {
            return;
        }
        this.mImeSessionInvalidated = false;
        if (Helper.sDebug) {
            Slog.d(TAG, "onCreateInlineSuggestionsRequestLocked called: " + this.mAutofillId);
        }
        this.mInputMethodManagerInternal.onCreateInlineSuggestionsRequest(this.mUserId, new InlineSuggestionsRequestInfo(this.mComponentName, this.mAutofillId, this.mUiExtras), new InlineSuggestionsRequestCallbackImpl());
    }

    @GuardedBy({"mLock"})
    public void resetInlineFillUiLocked() {
        this.mInlineFillUi = null;
    }

    @GuardedBy({"mLock"})
    private void maybeUpdateResponseToImeLocked() {
        if (Helper.sVerbose) {
            Slog.v(TAG, "maybeUpdateResponseToImeLocked called");
        }
        if (this.mDestroyed || this.mResponseCallback == null || !this.mImeInputViewStarted || this.mInlineFillUi == null || !match(this.mAutofillId, this.mImeCurrentFieldId)) {
            return;
        }
        InlineSuggestionsResponse inlineSuggestionsResponse = this.mInlineFillUi.getInlineSuggestionsResponse();
        boolean isEmpty = inlineSuggestionsResponse.getInlineSuggestions().isEmpty();
        if (isEmpty && Boolean.FALSE.equals(this.mPreviousResponseIsNotEmpty)) {
            return;
        }
        maybeNotifyFillUiEventLocked(inlineSuggestionsResponse.getInlineSuggestions());
        updateResponseToImeUncheckLocked(inlineSuggestionsResponse);
        this.mPreviousResponseIsNotEmpty = Boolean.valueOf(!isEmpty);
    }

    @GuardedBy({"mLock"})
    private void updateResponseToImeUncheckLocked(InlineSuggestionsResponse inlineSuggestionsResponse) {
        if (this.mDestroyed) {
            return;
        }
        if (Helper.sDebug) {
            Slog.d(TAG, "Send inline response: " + inlineSuggestionsResponse.getInlineSuggestions().size());
        }
        try {
            this.mResponseCallback.onInlineSuggestionsResponse(this.mAutofillId, inlineSuggestionsResponse);
        } catch (RemoteException unused) {
            Slog.e(TAG, "RemoteException sending InlineSuggestionsResponse to IME");
        }
    }

    @GuardedBy({"mLock"})
    private void maybeNotifyFillUiEventLocked(List<InlineSuggestion> list) {
        if (this.mDestroyed) {
            return;
        }
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= list.size()) {
                break;
            }
            if (!list.get(i).getInfo().isPinned()) {
                z = true;
                break;
            }
            i++;
        }
        if (Helper.sDebug) {
            Slog.d(TAG, "maybeNotifyFillUiEventLoked(): hasSuggestionToShow=" + z + ", mPreviousHasNonPinSuggestionShow=" + this.mPreviousHasNonPinSuggestionShow);
        }
        if (z && !this.mPreviousHasNonPinSuggestionShow) {
            this.mUiCallback.notifyInlineUiShown(this.mAutofillId);
        } else if (!z && this.mPreviousHasNonPinSuggestionShow) {
            this.mUiCallback.notifyInlineUiHidden(this.mAutofillId);
        }
        this.mPreviousHasNonPinSuggestionShow = z;
    }

    public void handleOnReceiveImeRequest(InlineSuggestionsRequest inlineSuggestionsRequest, IInlineSuggestionsResponseCallback iInlineSuggestionsResponseCallback) {
        synchronized (this.mLock) {
            if (!this.mDestroyed && !this.mImeRequestReceived) {
                this.mImeRequestReceived = true;
                this.mImeSessionInvalidated = false;
                if (inlineSuggestionsRequest != null && iInlineSuggestionsResponseCallback != null) {
                    this.mImeRequest = inlineSuggestionsRequest;
                    this.mResponseCallback = iInlineSuggestionsResponseCallback;
                    handleOnReceiveImeStatusUpdated(this.mAutofillId, true, false);
                }
                Consumer<InlineSuggestionsRequest> consumer = this.mImeRequestConsumer;
                if (consumer != null) {
                    consumer.accept(this.mImeRequest);
                    this.mImeRequestConsumer = null;
                }
            }
        }
    }

    public void handleOnReceiveImeStatusUpdated(boolean z, boolean z2) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            this.mImeShowing = z2;
            if (this.mImeCurrentFieldId != null) {
                boolean z3 = true;
                boolean z4 = this.mImeInputStarted != z;
                if (this.mImeInputViewStarted == z2) {
                    z3 = false;
                }
                this.mImeInputStarted = z;
                this.mImeInputViewStarted = z2;
                if (z4 || z3) {
                    maybeUpdateResponseToImeLocked();
                }
            }
        }
    }

    public void handleOnReceiveImeStatusUpdated(AutofillId autofillId, boolean z, boolean z2) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            if (autofillId != null) {
                this.mImeCurrentFieldId = autofillId;
            }
            handleOnReceiveImeStatusUpdated(z, z2);
        }
    }

    public void handleOnReceiveImeSessionInvalidated() {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            this.mImeSessionInvalidated = true;
        }
    }

    public boolean isImeShowing() {
        boolean z;
        synchronized (this.mLock) {
            z = !this.mDestroyed && this.mImeShowing;
        }
        return z;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class InlineSuggestionsRequestCallbackImpl extends IInlineSuggestionsRequestCallback.Stub {
        private final WeakReference<AutofillInlineSuggestionsRequestSession> mSession;

        /* synthetic */ InlineSuggestionsRequestCallbackImpl(AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession, InlineSuggestionsRequestCallbackImplIA inlineSuggestionsRequestCallbackImplIA) {
            this(autofillInlineSuggestionsRequestSession);
        }

        private InlineSuggestionsRequestCallbackImpl(AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession) {
            this.mSession = new WeakReference<>(autofillInlineSuggestionsRequestSession);
        }

        public void onInlineSuggestionsUnsupported() throws RemoteException {
            if (Helper.sDebug) {
                Slog.d(AutofillInlineSuggestionsRequestSession.TAG, "onInlineSuggestionsUnsupported() called.");
            }
            AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession.get();
            if (autofillInlineSuggestionsRequestSession != null) {
                autofillInlineSuggestionsRequestSession.mHandler.sendMessage(PooledLambda.obtainMessage(new AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda0(), autofillInlineSuggestionsRequestSession, (Object) null, (Object) null));
            }
        }

        public void onInlineSuggestionsRequest(InlineSuggestionsRequest inlineSuggestionsRequest, IInlineSuggestionsResponseCallback iInlineSuggestionsResponseCallback) {
            if (Helper.sDebug) {
                Slog.d(AutofillInlineSuggestionsRequestSession.TAG, "onInlineSuggestionsRequest() received: " + inlineSuggestionsRequest);
            }
            AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession.get();
            if (autofillInlineSuggestionsRequestSession != null) {
                autofillInlineSuggestionsRequestSession.mHandler.sendMessage(PooledLambda.obtainMessage(new AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda0(), autofillInlineSuggestionsRequestSession, inlineSuggestionsRequest, iInlineSuggestionsResponseCallback));
            }
        }

        public void onInputMethodStartInput(AutofillId autofillId) throws RemoteException {
            if (Helper.sVerbose) {
                Slog.v(AutofillInlineSuggestionsRequestSession.TAG, "onInputMethodStartInput() received on " + autofillId);
            }
            AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession.get();
            if (autofillInlineSuggestionsRequestSession != null) {
                autofillInlineSuggestionsRequestSession.mHandler.sendMessage(PooledLambda.obtainMessage(new QuadConsumer() { // from class: com.android.server.autofill.AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda1
                    public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
                        ((AutofillInlineSuggestionsRequestSession) obj).handleOnReceiveImeStatusUpdated((AutofillId) obj2, ((Boolean) obj3).booleanValue(), ((Boolean) obj4).booleanValue());
                    }
                }, autofillInlineSuggestionsRequestSession, autofillId, Boolean.TRUE, Boolean.FALSE));
            }
        }

        public void onInputMethodShowInputRequested(boolean z) throws RemoteException {
            if (Helper.sVerbose) {
                Slog.v(AutofillInlineSuggestionsRequestSession.TAG, "onInputMethodShowInputRequested() received: " + z);
            }
        }

        public void onInputMethodStartInputView() {
            if (Helper.sVerbose) {
                Slog.v(AutofillInlineSuggestionsRequestSession.TAG, "onInputMethodStartInputView() received");
            }
            AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession.get();
            if (autofillInlineSuggestionsRequestSession != null) {
                Handler handler = autofillInlineSuggestionsRequestSession.mHandler;
                AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda2 autofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda2 = new AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda2();
                Boolean bool = Boolean.TRUE;
                handler.sendMessage(PooledLambda.obtainMessage(autofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda2, autofillInlineSuggestionsRequestSession, bool, bool));
            }
        }

        public void onInputMethodFinishInputView() {
            if (Helper.sVerbose) {
                Slog.v(AutofillInlineSuggestionsRequestSession.TAG, "onInputMethodFinishInputView() received");
            }
            AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession.get();
            if (autofillInlineSuggestionsRequestSession != null) {
                autofillInlineSuggestionsRequestSession.mHandler.sendMessage(PooledLambda.obtainMessage(new AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda2(), autofillInlineSuggestionsRequestSession, Boolean.TRUE, Boolean.FALSE));
            }
        }

        public void onInputMethodFinishInput() throws RemoteException {
            if (Helper.sVerbose) {
                Slog.v(AutofillInlineSuggestionsRequestSession.TAG, "onInputMethodFinishInput() received");
            }
            AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession.get();
            if (autofillInlineSuggestionsRequestSession != null) {
                Handler handler = autofillInlineSuggestionsRequestSession.mHandler;
                AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda2 autofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda2 = new AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda2();
                Boolean bool = Boolean.FALSE;
                handler.sendMessage(PooledLambda.obtainMessage(autofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda2, autofillInlineSuggestionsRequestSession, bool, bool));
            }
        }

        public void onInlineSuggestionsSessionInvalidated() throws RemoteException {
            if (Helper.sDebug) {
                Slog.d(AutofillInlineSuggestionsRequestSession.TAG, "onInlineSuggestionsSessionInvalidated() called.");
            }
            AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession.get();
            if (autofillInlineSuggestionsRequestSession != null) {
                autofillInlineSuggestionsRequestSession.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.autofill.AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((AutofillInlineSuggestionsRequestSession) obj).handleOnReceiveImeSessionInvalidated();
                    }
                }, autofillInlineSuggestionsRequestSession));
            }
        }
    }

    private static boolean match(AutofillId autofillId, AutofillId autofillId2) {
        return (autofillId == null || autofillId2 == null || autofillId.getViewId() != autofillId2.getViewId()) ? false : true;
    }
}
