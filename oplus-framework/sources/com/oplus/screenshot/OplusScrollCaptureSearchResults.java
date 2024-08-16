package com.oplus.screenshot;

import android.graphics.Rect;
import android.os.CancellationSignal;
import android.view.ScrollCaptureCallback;
import android.view.ScrollCaptureSearchResults;
import android.view.ScrollCaptureTarget;
import com.oplus.screenshot.OplusScrollCaptureSearchResults;
import com.oplus.util.OplusLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class OplusScrollCaptureSearchResults {
    private static final String TAG = "OplusScrollCaptureSearchResults";
    private int mCompleted;
    private final Executor mExecutor;
    private Runnable mOnCompleteListener;
    private boolean mComplete = true;
    private final List<ScrollCaptureTarget> mTargets = new ArrayList();
    private final CancellationSignal mCancel = new CancellationSignal();
    private final ScrollCaptureSearchResults mResults = new ScrollCaptureSearchResults(new Executor() { // from class: com.oplus.screenshot.OplusScrollCaptureSearchResults.1
        @Override // java.util.concurrent.Executor
        public void execute(Runnable command) {
            OplusLog.d(OplusScrollCaptureSearchResults.TAG, "ignore execute(" + command + ")");
        }
    });

    public OplusScrollCaptureSearchResults(Executor executor) {
        this.mExecutor = executor;
    }

    public void addTarget(ScrollCaptureTarget target) {
        Objects.requireNonNull(target);
        this.mTargets.add(target);
        this.mComplete = false;
        final ScrollCaptureCallback callback = target.getCallback();
        final Consumer<Rect> consumer = new SearchRequest(target);
        this.mExecutor.execute(new Runnable() { // from class: com.oplus.screenshot.OplusScrollCaptureSearchResults$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                OplusScrollCaptureSearchResults.this.lambda$addTarget$0(callback, consumer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addTarget$0(ScrollCaptureCallback callback, Consumer consumer) {
        callback.onScrollCaptureSearch(this.mCancel, consumer);
    }

    public boolean isComplete() {
        return this.mComplete;
    }

    public void setOnCompleteListener(Runnable onComplete) {
        if (this.mComplete) {
            onComplete.run();
        } else {
            this.mOnCompleteListener = onComplete;
        }
    }

    public boolean isEmpty() {
        return this.mTargets.isEmpty();
    }

    public void finish() {
        if (!this.mComplete) {
            this.mCancel.cancel();
            signalComplete();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void signalComplete() {
        this.mComplete = true;
        Runnable runnable = this.mOnCompleteListener;
        if (runnable != null) {
            runnable.run();
            this.mOnCompleteListener = null;
        }
    }

    public List<ScrollCaptureTarget> getTargets() {
        return new ArrayList(this.mTargets);
    }

    public ScrollCaptureSearchResults getResults() {
        return this.mResults;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SearchRequest implements Consumer<Rect> {
        private ScrollCaptureTarget mTarget;

        SearchRequest(ScrollCaptureTarget target) {
            this.mTarget = target;
        }

        @Override // java.util.function.Consumer
        public void accept(final Rect scrollBounds) {
            if (this.mTarget == null || OplusScrollCaptureSearchResults.this.mCancel.isCanceled()) {
                return;
            }
            OplusScrollCaptureSearchResults.this.mExecutor.execute(new Runnable() { // from class: com.oplus.screenshot.OplusScrollCaptureSearchResults$SearchRequest$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    OplusScrollCaptureSearchResults.SearchRequest.this.lambda$accept$0(scrollBounds);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: consume, reason: merged with bridge method [inline-methods] */
        public void lambda$accept$0(Rect scrollBounds) {
            if (this.mTarget == null || OplusScrollCaptureSearchResults.this.mCancel.isCanceled()) {
                return;
            }
            if (!OplusScrollCaptureSearchResults.nullOrEmpty(scrollBounds)) {
                this.mTarget.setScrollBounds(scrollBounds);
                this.mTarget.updatePositionInWindow();
            }
            OplusScrollCaptureSearchResults.this.mCompleted++;
            this.mTarget = null;
            if (OplusScrollCaptureSearchResults.this.mCompleted == OplusScrollCaptureSearchResults.this.mTargets.size()) {
                OplusScrollCaptureSearchResults.this.signalComplete();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean nullOrEmpty(Rect r) {
        return r == null || r.isEmpty();
    }
}
