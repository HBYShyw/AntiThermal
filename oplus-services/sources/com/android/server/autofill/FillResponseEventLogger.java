package com.android.server.autofill;

import android.os.SystemClock;
import android.service.autofill.Dataset;
import android.util.Slog;
import android.view.autofill.AutofillId;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.autofill.FillResponseEventLogger;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class FillResponseEventLogger {
    public static final int AUTHENTICATION_RESULT_FAILURE = 2;
    public static final int AUTHENTICATION_RESULT_SUCCESS = 1;
    public static final int AUTHENTICATION_RESULT_UNKNOWN = 0;
    public static final int AUTHENTICATION_TYPE_DATASET_AHTHENTICATION = 1;
    public static final int AUTHENTICATION_TYPE_FULL_AHTHENTICATION = 2;
    public static final int AUTHENTICATION_TYPE_UNKNOWN = 0;
    public static final int AVAILABLE_COUNT_WHEN_FILL_REQUEST_FAILED_OR_TIMEOUT = -1;
    public static final int DETECTION_PREFER_AUTOFILL_PROVIDER = 1;
    public static final int DETECTION_PREFER_PCC = 2;
    public static final int DETECTION_PREFER_UNKNOWN = 0;
    public static final int DISPLAY_PRESENTATION_TYPE_DIALOG = 3;
    public static final int DISPLAY_PRESENTATION_TYPE_INLINE = 2;
    public static final int DISPLAY_PRESENTATION_TYPE_MENU = 1;
    public static final int DISPLAY_PRESENTATION_TYPE_UNKNOWN = 0;
    public static final int HAVE_SAVE_TRIGGER_ID = 1;
    public static final int RESPONSE_STATUS_CANCELLED = 3;
    public static final int RESPONSE_STATUS_FAILURE = 1;
    public static final int RESPONSE_STATUS_SESSION_DESTROYED = 5;
    public static final int RESPONSE_STATUS_SUCCESS = 2;
    public static final int RESPONSE_STATUS_TIMEOUT = 4;
    public static final int RESPONSE_STATUS_UNKNOWN = 0;
    private static final String TAG = "FillResponseEventLogger";
    private static final long UNINITIALIZED_TIMESTAMP = -1;
    private final int mSessionId;
    private long startResponseProcessingTimestamp = -1;
    private Optional<FillResponseEventInternal> mEventInternal = Optional.empty();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface AuthenticationResult {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface AuthenticationType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface DetectionPreference {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface DisplayPresentationType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface ResponseStatus {
    }

    private FillResponseEventLogger(int i) {
        this.mSessionId = i;
    }

    public static FillResponseEventLogger forSessionId(int i) {
        return new FillResponseEventLogger(i);
    }

    public void startLogForNewResponse() {
        if (!this.mEventInternal.isEmpty()) {
            Slog.w(TAG, "FillResponseEventLogger is not empty before starting for a new request");
        }
        this.mEventInternal = Optional.of(new FillResponseEventInternal());
    }

    public void maybeSetRequestId(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mRequestId = i;
            }
        });
    }

    public void maybeSetAppPackageUid(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mAppPackageUid = i;
            }
        });
    }

    public void maybeSetDisplayPresentationType(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mDisplayPresentationType = i;
            }
        });
    }

    public void maybeSetAvailableCount(final List<Dataset> list, final AutofillId autofillId) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                FillResponseEventLogger.lambda$maybeSetAvailableCount$3(list, autofillId, (FillResponseEventLogger.FillResponseEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$maybeSetAvailableCount$3(List list, AutofillId autofillId, FillResponseEventInternal fillResponseEventInternal) {
        fillResponseEventInternal.mAvailableCount = getDatasetCountForAutofillId(list, autofillId);
    }

    public void maybeSetAvailableCount(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mAvailableCount = i;
            }
        });
    }

    public void maybeSetTotalDatasetsProvided(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                FillResponseEventLogger.lambda$maybeSetTotalDatasetsProvided$5(i, (FillResponseEventLogger.FillResponseEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$maybeSetTotalDatasetsProvided$5(int i, FillResponseEventInternal fillResponseEventInternal) {
        if (fillResponseEventInternal.mTotalDatasetsProvided == -1) {
            fillResponseEventInternal.mTotalDatasetsProvided = i;
        }
    }

    private static int getDatasetCountForAutofillId(List<Dataset> list, AutofillId autofillId) {
        if (list == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            Dataset dataset = list.get(i2);
            if (dataset != null && dataset.getFieldIds() != null && dataset.getFieldIds().contains(autofillId)) {
                i++;
            }
        }
        return i;
    }

    public void maybeSetSaveUiTriggerIds(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mSaveUiTriggerIds = i;
            }
        });
    }

    public void maybeSetLatencyFillResponseReceivedMillis(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mLatencyFillResponseReceivedMillis = i;
            }
        });
    }

    public void maybeSetAuthenticationType(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mAuthenticationType = i;
            }
        });
    }

    public void maybeSetAuthenticationResult(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mAuthenticationResult = i;
            }
        });
    }

    public void maybeSetAuthenticationFailureReason(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mAuthenticationFailureReason = i;
            }
        });
    }

    public void maybeSetLatencyAuthenticationUiDisplayMillis(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mLatencyAuthenticationUiDisplayMillis = i;
            }
        });
    }

    public void maybeSetLatencyDatasetDisplayMillis(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mLatencyDatasetDisplayMillis = i;
            }
        });
    }

    public void maybeSetResponseStatus(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mResponseStatus = i;
            }
        });
    }

    public void startResponseProcessingTime() {
        this.startResponseProcessingTimestamp = SystemClock.elapsedRealtime();
    }

    public void maybeSetLatencyResponseProcessingMillis() {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                FillResponseEventLogger.this.lambda$maybeSetLatencyResponseProcessingMillis$14((FillResponseEventLogger.FillResponseEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeSetLatencyResponseProcessingMillis$14(FillResponseEventInternal fillResponseEventInternal) {
        if (this.startResponseProcessingTimestamp == -1 && Helper.sVerbose) {
            Slog.v(TAG, "uninitialized startResponseProcessingTimestamp");
        }
        fillResponseEventInternal.mLatencyResponseProcessingMillis = SystemClock.elapsedRealtime() - this.startResponseProcessingTimestamp;
    }

    public void maybeSetAvailablePccCount(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mAvailablePccCount = i;
            }
        });
    }

    public void maybeSetAvailablePccOnlyCount(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mAvailablePccOnlyCount = i;
            }
        });
    }

    public void maybeSetDatasetsCountAfterPotentialPccFiltering(final List<Dataset> list) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda17
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                FillResponseEventLogger.lambda$maybeSetDatasetsCountAfterPotentialPccFiltering$17(list, (FillResponseEventLogger.FillResponseEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$maybeSetDatasetsCountAfterPotentialPccFiltering$17(List list, FillResponseEventInternal fillResponseEventInternal) {
        int i;
        int i2;
        int i3 = 0;
        if (list != null) {
            i = list.size();
            int i4 = 0;
            i2 = 0;
            while (i3 < list.size()) {
                Dataset dataset = (Dataset) list.get(i3);
                if (dataset != null) {
                    if (dataset.getEligibleReason() == 4) {
                        i4++;
                    } else if (dataset.getEligibleReason() != 5) {
                    }
                    i2++;
                }
                i3++;
            }
            i3 = i4;
        } else {
            i = 0;
            i2 = 0;
        }
        fillResponseEventInternal.mAvailablePccOnlyCount = i3;
        fillResponseEventInternal.mAvailablePccCount = i2;
        fillResponseEventInternal.mAvailableCount = i;
    }

    public void maybeSetDetectionPreference(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FillResponseEventLogger.FillResponseEventInternal) obj).mDetectionPref = i;
            }
        });
    }

    public void logAndEndEvent() {
        if (!this.mEventInternal.isPresent()) {
            Slog.w(TAG, "Shouldn't be logging AutofillFillRequestReported again for same event");
            return;
        }
        FillResponseEventInternal fillResponseEventInternal = this.mEventInternal.get();
        if (Helper.sVerbose) {
            Slog.v(TAG, "Log AutofillFillResponseReported: requestId=" + fillResponseEventInternal.mRequestId + " sessionId=" + this.mSessionId + " mAppPackageUid=" + fillResponseEventInternal.mAppPackageUid + " mDisplayPresentationType=" + fillResponseEventInternal.mDisplayPresentationType + " mAvailableCount=" + fillResponseEventInternal.mAvailableCount + " mSaveUiTriggerIds=" + fillResponseEventInternal.mSaveUiTriggerIds + " mLatencyFillResponseReceivedMillis=" + fillResponseEventInternal.mLatencyFillResponseReceivedMillis + " mAuthenticationType=" + fillResponseEventInternal.mAuthenticationType + " mAuthenticationResult=" + fillResponseEventInternal.mAuthenticationResult + " mAuthenticationFailureReason=" + fillResponseEventInternal.mAuthenticationFailureReason + " mLatencyAuthenticationUiDisplayMillis=" + fillResponseEventInternal.mLatencyAuthenticationUiDisplayMillis + " mLatencyDatasetDisplayMillis=" + fillResponseEventInternal.mLatencyDatasetDisplayMillis + " mResponseStatus=" + fillResponseEventInternal.mResponseStatus + " mLatencyResponseProcessingMillis=" + fillResponseEventInternal.mLatencyResponseProcessingMillis + " mAvailablePccCount=" + fillResponseEventInternal.mAvailablePccCount + " mAvailablePccOnlyCount=" + fillResponseEventInternal.mAvailablePccOnlyCount + " mTotalDatasetsProvided=" + fillResponseEventInternal.mTotalDatasetsProvided + " mDetectionPref=" + fillResponseEventInternal.mDetectionPref);
        }
        FrameworkStatsLog.write(FrameworkStatsLog.AUTOFILL_FILL_RESPONSE_REPORTED, fillResponseEventInternal.mRequestId, this.mSessionId, fillResponseEventInternal.mAppPackageUid, fillResponseEventInternal.mDisplayPresentationType, fillResponseEventInternal.mAvailableCount, fillResponseEventInternal.mSaveUiTriggerIds, fillResponseEventInternal.mLatencyFillResponseReceivedMillis, fillResponseEventInternal.mAuthenticationType, fillResponseEventInternal.mAuthenticationResult, fillResponseEventInternal.mAuthenticationFailureReason, fillResponseEventInternal.mLatencyAuthenticationUiDisplayMillis, fillResponseEventInternal.mLatencyDatasetDisplayMillis, fillResponseEventInternal.mResponseStatus, fillResponseEventInternal.mLatencyResponseProcessingMillis, fillResponseEventInternal.mAvailablePccCount, fillResponseEventInternal.mAvailablePccOnlyCount, fillResponseEventInternal.mTotalDatasetsProvided, fillResponseEventInternal.mDetectionPref);
        this.mEventInternal = Optional.empty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class FillResponseEventInternal {
        int mRequestId = -1;
        int mAppPackageUid = -1;
        int mDisplayPresentationType = 0;
        int mAvailableCount = 0;
        int mSaveUiTriggerIds = -1;
        int mLatencyFillResponseReceivedMillis = -1;
        int mAuthenticationType = 0;
        int mAuthenticationResult = 0;
        int mAuthenticationFailureReason = -1;
        int mLatencyAuthenticationUiDisplayMillis = -1;
        int mLatencyDatasetDisplayMillis = -1;
        int mResponseStatus = 0;
        long mLatencyResponseProcessingMillis = -1;
        int mAvailablePccCount = -1;
        int mAvailablePccOnlyCount = -1;
        int mTotalDatasetsProvided = -1;
        int mDetectionPref = 0;

        FillResponseEventInternal() {
        }
    }
}
