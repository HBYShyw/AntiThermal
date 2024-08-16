package com.android.server.autofill;

import android.util.Slog;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.autofill.FieldClassificationEventLogger;
import java.util.Optional;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class FieldClassificationEventLogger {
    private static final String TAG = "FieldClassificationEventLogger";
    private Optional<FieldClassificationEventInternal> mEventInternal = Optional.empty();

    private FieldClassificationEventLogger() {
    }

    public static FieldClassificationEventLogger createLogger() {
        return new FieldClassificationEventLogger();
    }

    public void startNewLogForRequest() {
        if (!this.mEventInternal.isEmpty()) {
            Slog.w(TAG, "FieldClassificationEventLogger is not empty before starting for a new request");
        }
        this.mEventInternal = Optional.of(new FieldClassificationEventInternal());
    }

    public void maybeSetLatencyMillis(final long j) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FieldClassificationEventLogger$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FieldClassificationEventLogger.FieldClassificationEventInternal) obj).mLatencyClassificationRequestMillis = j;
            }
        });
    }

    public void logAndEndEvent() {
        if (!this.mEventInternal.isPresent()) {
            Slog.w(TAG, "Shouldn't be logging AutofillFieldClassificationEventInternal again for same event");
            return;
        }
        FieldClassificationEventInternal fieldClassificationEventInternal = this.mEventInternal.get();
        if (Helper.sVerbose) {
            Slog.v(TAG, "Log AutofillFieldClassificationEventReported: mLatencyClassificationRequestMillis=" + fieldClassificationEventInternal.mLatencyClassificationRequestMillis);
        }
        FrameworkStatsLog.write(FrameworkStatsLog.AUTOFILL_FIELD_CLASSIFICATION_EVENT_REPORTED, fieldClassificationEventInternal.mLatencyClassificationRequestMillis);
        this.mEventInternal = Optional.empty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class FieldClassificationEventInternal {
        long mLatencyClassificationRequestMillis = -1;

        FieldClassificationEventInternal() {
        }
    }
}
