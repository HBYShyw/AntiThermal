package com.android.server.autofill.ui;

import android.content.IntentSender;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.service.autofill.IInlineSuggestionUi;
import android.service.autofill.IInlineSuggestionUiCallback;
import android.service.autofill.ISurfacePackageResultCallback;
import android.util.Slog;
import android.view.SurfaceControlViewHost;
import com.android.internal.view.inline.IInlineContentCallback;
import com.android.server.autofill.Helper;
import com.android.server.autofill.ui.RemoteInlineSuggestionUi;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class RemoteInlineSuggestionUi {
    private static final long RELEASE_REMOTE_VIEW_HOST_DELAY_MS = 200;
    private static final String TAG = "RemoteInlineSuggestionUi";
    private int mActualHeight;
    private int mActualWidth;
    private Runnable mDelayedReleaseViewRunnable;
    private final Handler mHandler;
    private final int mHeight;
    private IInlineContentCallback mInlineContentCallback;
    private IInlineSuggestionUi mInlineSuggestionUi;
    private final RemoteInlineSuggestionViewConnector mRemoteInlineSuggestionViewConnector;
    private final int mWidth;
    private int mRefCount = 0;
    private boolean mWaitingForUiCreation = false;
    private final InlineSuggestionUiCallbackImpl mInlineSuggestionUiCallback = new InlineSuggestionUiCallbackImpl();

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteInlineSuggestionUi(RemoteInlineSuggestionViewConnector remoteInlineSuggestionViewConnector, int i, int i2, Handler handler) {
        this.mHandler = handler;
        this.mRemoteInlineSuggestionViewConnector = remoteInlineSuggestionViewConnector;
        this.mWidth = i;
        this.mHeight = i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInlineContentCallback(final IInlineContentCallback iInlineContentCallback) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                RemoteInlineSuggestionUi.this.lambda$setInlineContentCallback$0(iInlineContentCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setInlineContentCallback$0(IInlineContentCallback iInlineContentCallback) {
        this.mInlineContentCallback = iInlineContentCallback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void requestSurfacePackage() {
        this.mHandler.post(new Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                RemoteInlineSuggestionUi.this.handleRequestSurfacePackage();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$surfacePackageReleased$1() {
        handleUpdateRefCount(-1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void surfacePackageReleased() {
        this.mHandler.post(new Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                RemoteInlineSuggestionUi.this.lambda$surfacePackageReleased$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean match(int i, int i2) {
        return this.mWidth == i && this.mHeight == i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRequestSurfacePackage() {
        cancelPendingReleaseViewRequest();
        IInlineSuggestionUi iInlineSuggestionUi = this.mInlineSuggestionUi;
        if (iInlineSuggestionUi == null) {
            if (this.mWaitingForUiCreation) {
                if (Helper.sDebug) {
                    Slog.d(TAG, "Inline suggestion ui is not ready");
                    return;
                }
                return;
            } else {
                this.mRemoteInlineSuggestionViewConnector.renderSuggestion(this.mWidth, this.mHeight, this.mInlineSuggestionUiCallback);
                this.mWaitingForUiCreation = true;
                return;
            }
        }
        try {
            iInlineSuggestionUi.getSurfacePackage(new AnonymousClass1());
        } catch (RemoteException unused) {
            Slog.w(TAG, "RemoteException calling getSurfacePackage.");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.autofill.ui.RemoteInlineSuggestionUi$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass1 extends ISurfacePackageResultCallback.Stub {
        AnonymousClass1() {
        }

        public void onResult(final SurfaceControlViewHost.SurfacePackage surfacePackage) {
            RemoteInlineSuggestionUi.this.mHandler.post(new Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteInlineSuggestionUi.AnonymousClass1.this.lambda$onResult$0(surfacePackage);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onResult$0(SurfaceControlViewHost.SurfacePackage surfacePackage) {
            if (Helper.sVerbose) {
                Slog.v(RemoteInlineSuggestionUi.TAG, "Sending refreshed SurfacePackage to IME");
            }
            try {
                RemoteInlineSuggestionUi.this.mInlineContentCallback.onContent(surfacePackage, RemoteInlineSuggestionUi.this.mActualWidth, RemoteInlineSuggestionUi.this.mActualHeight);
                RemoteInlineSuggestionUi.this.handleUpdateRefCount(1);
            } catch (RemoteException unused) {
                Slog.w(RemoteInlineSuggestionUi.TAG, "RemoteException calling onContent");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdateRefCount(int i) {
        cancelPendingReleaseViewRequest();
        int i2 = this.mRefCount + i;
        this.mRefCount = i2;
        if (i2 <= 0) {
            Runnable runnable = new Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteInlineSuggestionUi.this.lambda$handleUpdateRefCount$2();
                }
            };
            this.mDelayedReleaseViewRunnable = runnable;
            this.mHandler.postDelayed(runnable, RELEASE_REMOTE_VIEW_HOST_DELAY_MS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleUpdateRefCount$2() {
        if (this.mInlineSuggestionUi != null) {
            try {
                if (Helper.sVerbose) {
                    Slog.v(TAG, "releasing the host");
                }
                this.mInlineSuggestionUi.releaseSurfaceControlViewHost();
                this.mInlineSuggestionUi = null;
            } catch (RemoteException unused) {
                Slog.w(TAG, "RemoteException calling releaseSurfaceControlViewHost");
            }
        }
        this.mDelayedReleaseViewRunnable = null;
    }

    private void cancelPendingReleaseViewRequest() {
        Runnable runnable = this.mDelayedReleaseViewRunnable;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
            this.mDelayedReleaseViewRunnable = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInlineSuggestionUiReady(IInlineSuggestionUi iInlineSuggestionUi, SurfaceControlViewHost.SurfacePackage surfacePackage, int i, int i2) {
        this.mInlineSuggestionUi = iInlineSuggestionUi;
        this.mRefCount = 0;
        this.mWaitingForUiCreation = false;
        this.mActualWidth = i;
        this.mActualHeight = i2;
        if (this.mInlineContentCallback != null) {
            try {
                if (Helper.sVerbose) {
                    Slog.v(TAG, "Sending new UI content to IME");
                }
                handleUpdateRefCount(1);
                this.mInlineContentCallback.onContent(surfacePackage, this.mActualWidth, this.mActualHeight);
            } catch (RemoteException unused) {
                Slog.w(TAG, "RemoteException calling onContent");
            }
        }
        if (surfacePackage != null) {
            surfacePackage.release();
        }
        this.mRemoteInlineSuggestionViewConnector.onRender();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnClick() {
        this.mRemoteInlineSuggestionViewConnector.onClick();
        IInlineContentCallback iInlineContentCallback = this.mInlineContentCallback;
        if (iInlineContentCallback != null) {
            try {
                iInlineContentCallback.onClick();
            } catch (RemoteException unused) {
                Slog.w(TAG, "RemoteException calling onClick");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnLongClick() {
        IInlineContentCallback iInlineContentCallback = this.mInlineContentCallback;
        if (iInlineContentCallback != null) {
            try {
                iInlineContentCallback.onLongClick();
            } catch (RemoteException unused) {
                Slog.w(TAG, "RemoteException calling onLongClick");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnError() {
        this.mRemoteInlineSuggestionViewConnector.onError();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnTransferTouchFocusToImeWindow(IBinder iBinder, int i) {
        this.mRemoteInlineSuggestionViewConnector.onTransferTouchFocusToImeWindow(iBinder, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnStartIntentSender(IntentSender intentSender) {
        this.mRemoteInlineSuggestionViewConnector.onStartIntentSender(intentSender);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class InlineSuggestionUiCallbackImpl extends IInlineSuggestionUiCallback.Stub {
        private InlineSuggestionUiCallbackImpl() {
        }

        public void onClick() {
            Handler handler = RemoteInlineSuggestionUi.this.mHandler;
            final RemoteInlineSuggestionUi remoteInlineSuggestionUi = RemoteInlineSuggestionUi.this;
            handler.post(new Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$InlineSuggestionUiCallbackImpl$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteInlineSuggestionUi.this.handleOnClick();
                }
            });
        }

        public void onLongClick() {
            Handler handler = RemoteInlineSuggestionUi.this.mHandler;
            final RemoteInlineSuggestionUi remoteInlineSuggestionUi = RemoteInlineSuggestionUi.this;
            handler.post(new Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$InlineSuggestionUiCallbackImpl$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteInlineSuggestionUi.this.handleOnLongClick();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onContent$0(IInlineSuggestionUi iInlineSuggestionUi, SurfaceControlViewHost.SurfacePackage surfacePackage, int i, int i2) {
            RemoteInlineSuggestionUi.this.handleInlineSuggestionUiReady(iInlineSuggestionUi, surfacePackage, i, i2);
        }

        public void onContent(final IInlineSuggestionUi iInlineSuggestionUi, final SurfaceControlViewHost.SurfacePackage surfacePackage, final int i, final int i2) {
            RemoteInlineSuggestionUi.this.mHandler.post(new Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$InlineSuggestionUiCallbackImpl$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteInlineSuggestionUi.InlineSuggestionUiCallbackImpl.this.lambda$onContent$0(iInlineSuggestionUi, surfacePackage, i, i2);
                }
            });
        }

        public void onError() {
            Handler handler = RemoteInlineSuggestionUi.this.mHandler;
            final RemoteInlineSuggestionUi remoteInlineSuggestionUi = RemoteInlineSuggestionUi.this;
            handler.post(new Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$InlineSuggestionUiCallbackImpl$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteInlineSuggestionUi.this.handleOnError();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTransferTouchFocusToImeWindow$1(IBinder iBinder, int i) {
            RemoteInlineSuggestionUi.this.handleOnTransferTouchFocusToImeWindow(iBinder, i);
        }

        public void onTransferTouchFocusToImeWindow(final IBinder iBinder, final int i) {
            RemoteInlineSuggestionUi.this.mHandler.post(new Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$InlineSuggestionUiCallbackImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteInlineSuggestionUi.InlineSuggestionUiCallbackImpl.this.lambda$onTransferTouchFocusToImeWindow$1(iBinder, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStartIntentSender$2(IntentSender intentSender) {
            RemoteInlineSuggestionUi.this.handleOnStartIntentSender(intentSender);
        }

        public void onStartIntentSender(final IntentSender intentSender) {
            RemoteInlineSuggestionUi.this.mHandler.post(new Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$InlineSuggestionUiCallbackImpl$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteInlineSuggestionUi.InlineSuggestionUiCallbackImpl.this.lambda$onStartIntentSender$2(intentSender);
                }
            });
        }
    }
}
