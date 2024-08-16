package com.android.server.autofill;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.Handler;
import android.view.autofill.AutofillId;
import android.view.inputmethod.InlineSuggestionsRequest;
import com.android.internal.annotations.GuardedBy;
import com.android.server.autofill.ui.InlineFillUi;
import com.android.server.inputmethod.InputMethodManagerInternal;
import java.util.Optional;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AutofillInlineSessionController {
    private final ComponentName mComponentName;
    private final Handler mHandler;

    @GuardedBy({"mLock"})
    private InlineFillUi mInlineFillUi;
    private final InputMethodManagerInternal mInputMethodManagerInternal;
    private final Object mLock;

    @GuardedBy({"mLock"})
    private AutofillInlineSuggestionsRequestSession mSession;
    private final InlineFillUi.InlineUiEventCallback mUiCallback;
    private final int mUserId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutofillInlineSessionController(InputMethodManagerInternal inputMethodManagerInternal, int i, ComponentName componentName, Handler handler, Object obj, InlineFillUi.InlineUiEventCallback inlineUiEventCallback) {
        this.mInputMethodManagerInternal = inputMethodManagerInternal;
        this.mUserId = i;
        this.mComponentName = componentName;
        this.mHandler = handler;
        this.mLock = obj;
        this.mUiCallback = inlineUiEventCallback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void onCreateInlineSuggestionsRequestLocked(AutofillId autofillId, Consumer<InlineSuggestionsRequest> consumer, Bundle bundle) {
        AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession;
        if (autofillInlineSuggestionsRequestSession != null) {
            autofillInlineSuggestionsRequestSession.destroySessionLocked();
        }
        this.mInlineFillUi = null;
        AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession2 = new AutofillInlineSuggestionsRequestSession(this.mInputMethodManagerInternal, this.mUserId, this.mComponentName, this.mHandler, this.mLock, autofillId, consumer, bundle, this.mUiCallback);
        this.mSession = autofillInlineSuggestionsRequestSession2;
        autofillInlineSuggestionsRequestSession2.onCreateInlineSuggestionsRequestLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void destroyLocked(AutofillId autofillId) {
        AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession;
        if (autofillInlineSuggestionsRequestSession != null) {
            autofillInlineSuggestionsRequestSession.onInlineSuggestionsResponseLocked(InlineFillUi.emptyUi(autofillId));
            this.mSession.destroySessionLocked();
            this.mSession = null;
        }
        this.mInlineFillUi = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public Optional<InlineSuggestionsRequest> getInlineSuggestionsRequestLocked() {
        AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession;
        if (autofillInlineSuggestionsRequestSession != null) {
            return autofillInlineSuggestionsRequestSession.getInlineSuggestionsRequestLocked();
        }
        return Optional.empty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean hideInlineSuggestionsUiLocked(AutofillId autofillId) {
        AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession;
        if (autofillInlineSuggestionsRequestSession != null) {
            return autofillInlineSuggestionsRequestSession.onInlineSuggestionsResponseLocked(InlineFillUi.emptyUi(autofillId));
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void disableFilterMatching(AutofillId autofillId) {
        InlineFillUi inlineFillUi = this.mInlineFillUi;
        if (inlineFillUi == null || !inlineFillUi.getAutofillId().equals(autofillId)) {
            return;
        }
        this.mInlineFillUi.disableFilterMatching();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void resetInlineFillUiLocked() {
        this.mInlineFillUi = null;
        AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession;
        if (autofillInlineSuggestionsRequestSession != null) {
            autofillInlineSuggestionsRequestSession.resetInlineFillUiLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean filterInlineFillUiLocked(AutofillId autofillId, String str) {
        InlineFillUi inlineFillUi = this.mInlineFillUi;
        if (inlineFillUi == null || !inlineFillUi.getAutofillId().equals(autofillId)) {
            return false;
        }
        this.mInlineFillUi.setFilterText(str);
        return requestImeToShowInlineSuggestionsLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean setInlineFillUiLocked(InlineFillUi inlineFillUi) {
        this.mInlineFillUi = inlineFillUi;
        return requestImeToShowInlineSuggestionsLocked();
    }

    @GuardedBy({"mLock"})
    private boolean requestImeToShowInlineSuggestionsLocked() {
        InlineFillUi inlineFillUi;
        AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession;
        if (autofillInlineSuggestionsRequestSession == null || (inlineFillUi = this.mInlineFillUi) == null) {
            return false;
        }
        return autofillInlineSuggestionsRequestSession.onInlineSuggestionsResponseLocked(inlineFillUi);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isImeShowing() {
        AutofillInlineSuggestionsRequestSession autofillInlineSuggestionsRequestSession = this.mSession;
        if (autofillInlineSuggestionsRequestSession != null) {
            return autofillInlineSuggestionsRequestSession.isImeShowing();
        }
        return false;
    }
}
