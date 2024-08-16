package com.android.server.autofill;

import android.util.Slog;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.autofill.SessionCommittedEventLogger;
import java.util.Optional;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SessionCommittedEventLogger {
    private static final String TAG = "SessionCommittedEventLogger";
    private Optional<SessionCommittedEventInternal> mEventInternal = Optional.of(new SessionCommittedEventInternal());
    private final int mSessionId;

    private SessionCommittedEventLogger(int i) {
        this.mSessionId = i;
    }

    public static SessionCommittedEventLogger forSessionId(int i) {
        return new SessionCommittedEventLogger(i);
    }

    public void maybeSetComponentPackageUid(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SessionCommittedEventLogger$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SessionCommittedEventLogger.SessionCommittedEventInternal) obj).mComponentPackageUid = i;
            }
        });
    }

    public void maybeSetRequestCount(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SessionCommittedEventLogger$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SessionCommittedEventLogger.SessionCommittedEventInternal) obj).mRequestCount = i;
            }
        });
    }

    public void maybeSetCommitReason(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SessionCommittedEventLogger$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SessionCommittedEventLogger.SessionCommittedEventInternal) obj).mCommitReason = i;
            }
        });
    }

    public void maybeSetSessionDurationMillis(final long j) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SessionCommittedEventLogger$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SessionCommittedEventLogger.SessionCommittedEventInternal) obj).mSessionDurationMillis = j;
            }
        });
    }

    public void logAndEndEvent() {
        if (!this.mEventInternal.isPresent()) {
            Slog.w(TAG, "Shouldn't be logging AutofillSessionCommitted again for same session.");
            return;
        }
        SessionCommittedEventInternal sessionCommittedEventInternal = this.mEventInternal.get();
        if (Helper.sVerbose) {
            Slog.v(TAG, "Log AutofillSessionCommitted: sessionId=" + this.mSessionId + " mComponentPackageUid=" + sessionCommittedEventInternal.mComponentPackageUid + " mRequestCount=" + sessionCommittedEventInternal.mRequestCount + " mCommitReason=" + sessionCommittedEventInternal.mCommitReason + " mSessionDurationMillis=" + sessionCommittedEventInternal.mSessionDurationMillis);
        }
        FrameworkStatsLog.write(FrameworkStatsLog.AUTOFILL_SESSION_COMMITTED, this.mSessionId, sessionCommittedEventInternal.mComponentPackageUid, sessionCommittedEventInternal.mRequestCount, sessionCommittedEventInternal.mCommitReason, sessionCommittedEventInternal.mSessionDurationMillis);
        this.mEventInternal = Optional.empty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class SessionCommittedEventInternal {
        int mComponentPackageUid = -1;
        int mRequestCount = 0;
        int mCommitReason = 0;
        long mSessionDurationMillis = 0;

        SessionCommittedEventInternal() {
        }
    }
}
