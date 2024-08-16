package com.android.server.autofill;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.service.autofill.Dataset;
import android.text.TextUtils;
import android.util.Slog;
import android.view.autofill.AutofillId;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.autofill.PresentationStatsEventLogger;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PresentationStatsEventLogger {
    public static final int AUTHENTICATION_RESULT_FAILURE = 2;
    public static final int AUTHENTICATION_RESULT_SUCCESS = 1;
    public static final int AUTHENTICATION_RESULT_UNKNOWN = 0;
    public static final int AUTHENTICATION_TYPE_DATASET_AUTHENTICATION = 1;
    public static final int AUTHENTICATION_TYPE_FULL_AUTHENTICATION = 2;
    public static final int AUTHENTICATION_TYPE_UNKNOWN = 0;
    public static final int DETECTION_PREFER_AUTOFILL_PROVIDER = 1;
    public static final int DETECTION_PREFER_PCC = 2;
    public static final int DETECTION_PREFER_UNKNOWN = 0;
    public static final int NOT_SHOWN_REASON_ACTIVITY_FINISHED = 4;
    public static final int NOT_SHOWN_REASON_ANY_SHOWN = 1;
    public static final int NOT_SHOWN_REASON_NO_FOCUS = 8;
    public static final int NOT_SHOWN_REASON_REQUEST_FAILED = 7;
    public static final int NOT_SHOWN_REASON_REQUEST_TIMEOUT = 5;
    public static final int NOT_SHOWN_REASON_SESSION_COMMITTED_PREMATURELY = 6;
    public static final int NOT_SHOWN_REASON_UNKNOWN = 0;
    public static final int NOT_SHOWN_REASON_VIEW_CHANGED = 3;
    public static final int NOT_SHOWN_REASON_VIEW_FOCUSED_BEFORE_FILL_DIALOG_RESPONSE = 9;
    public static final int NOT_SHOWN_REASON_VIEW_FOCUS_CHANGED = 2;
    public static final int PICK_REASON_NO_PCC = 1;
    public static final int PICK_REASON_PCC_DETECTION_ONLY = 4;
    public static final int PICK_REASON_PCC_DETECTION_PREFERRED_WITH_PROVIDER = 5;
    public static final int PICK_REASON_PROVIDER_DETECTION_ONLY = 2;
    public static final int PICK_REASON_PROVIDER_DETECTION_PREFERRED_WITH_PCC = 3;
    public static final int PICK_REASON_UNKNOWN = 0;
    private static final String TAG = "PresentationStatsEventLogger";
    private Optional<PresentationStatsEventInternal> mEventInternal = Optional.empty();
    private final int mSessionId;

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
    public @interface DatasetPickedReason {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface DetectionPreference {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface NotShownReason {
    }

    private int convertDatasetPickReason(int i) {
        if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 5) {
            return i;
        }
        return 0;
    }

    private static int getDisplayPresentationType(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    return 0;
                }
            }
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getNoPresentationEventReason(int i) {
        if (i == 1) {
            return 4;
        }
        if (i != 2) {
            return i != 4 ? 0 : 3;
        }
        return 6;
    }

    private PresentationStatsEventLogger(int i) {
        this.mSessionId = i;
    }

    public static PresentationStatsEventLogger forSessionId(int i) {
        return new PresentationStatsEventLogger(i);
    }

    public void startNewEvent() {
        if (this.mEventInternal.isPresent()) {
            Slog.e(TAG, "Failed to start new event because already have active event.");
        } else {
            this.mEventInternal = Optional.of(new PresentationStatsEventInternal());
        }
    }

    public void maybeSetRequestId(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda26
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mRequestId = i;
            }
        });
    }

    public void maybeSetNoPresentationEventReason(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PresentationStatsEventLogger.lambda$maybeSetNoPresentationEventReason$1(i, (PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$maybeSetNoPresentationEventReason$1(int i, PresentationStatsEventInternal presentationStatsEventInternal) {
        if (presentationStatsEventInternal.mCountShown == 0) {
            presentationStatsEventInternal.mNoPresentationReason = i;
        }
    }

    public void maybeSetNoPresentationEventReasonIfNoReasonExists(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda21
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PresentationStatsEventLogger.lambda$maybeSetNoPresentationEventReasonIfNoReasonExists$2(i, (PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$maybeSetNoPresentationEventReasonIfNoReasonExists$2(int i, PresentationStatsEventInternal presentationStatsEventInternal) {
        if (presentationStatsEventInternal.mCountShown == 0 && presentationStatsEventInternal.mNoPresentationReason == 0) {
            presentationStatsEventInternal.mNoPresentationReason = i;
        }
    }

    public void maybeSetAvailableCount(final List<Dataset> list, final AutofillId autofillId) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PresentationStatsEventLogger.lambda$maybeSetAvailableCount$3(list, autofillId, (PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$maybeSetAvailableCount$3(List list, AutofillId autofillId, PresentationStatsEventInternal presentationStatsEventInternal) {
        CountContainer datasetCountForAutofillId = getDatasetCountForAutofillId(list, autofillId);
        int i = datasetCountForAutofillId.mAvailableCount;
        presentationStatsEventInternal.mAvailableCount = i;
        presentationStatsEventInternal.mAvailablePccCount = datasetCountForAutofillId.mAvailablePccCount;
        presentationStatsEventInternal.mAvailablePccOnlyCount = datasetCountForAutofillId.mAvailablePccOnlyCount;
        presentationStatsEventInternal.mIsDatasetAvailable = i > 0;
    }

    public void maybeSetCountShown(final List<Dataset> list, final AutofillId autofillId) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda24
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PresentationStatsEventLogger.lambda$maybeSetCountShown$4(list, autofillId, (PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$maybeSetCountShown$4(List list, AutofillId autofillId, PresentationStatsEventInternal presentationStatsEventInternal) {
        int i = getDatasetCountForAutofillId(list, autofillId).mAvailableCount;
        presentationStatsEventInternal.mCountShown = i;
        if (i > 0) {
            presentationStatsEventInternal.mNoPresentationReason = 1;
        }
    }

    private static CountContainer getDatasetCountForAutofillId(List<Dataset> list, AutofillId autofillId) {
        CountContainer countContainer = new CountContainer();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Dataset dataset = list.get(i);
                if (dataset != null && dataset.getFieldIds() != null && dataset.getFieldIds().contains(autofillId)) {
                    countContainer.mAvailableCount++;
                    if (dataset.getEligibleReason() == 4) {
                        countContainer.mAvailablePccOnlyCount++;
                        countContainer.mAvailablePccCount++;
                    } else if (dataset.getEligibleReason() == 5) {
                        countContainer.mAvailablePccCount++;
                    }
                }
            }
        }
        return countContainer;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class CountContainer {
        int mAvailableCount;
        int mAvailablePccCount;
        int mAvailablePccOnlyCount;

        CountContainer() {
            this.mAvailableCount = 0;
            this.mAvailablePccCount = 0;
            this.mAvailablePccOnlyCount = 0;
        }

        CountContainer(int i, int i2, int i3) {
            this.mAvailableCount = i;
            this.mAvailablePccCount = i2;
            this.mAvailablePccOnlyCount = i3;
        }
    }

    public void maybeSetCountFilteredUserTyping(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mCountFilteredUserTyping = i;
            }
        });
    }

    public void maybeSetCountNotShownImePresentationNotDrawn(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mCountNotShownImePresentationNotDrawn = i;
            }
        });
    }

    public void maybeSetCountNotShownImeUserNotSeen(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mCountNotShownImeUserNotSeen = i;
            }
        });
    }

    public void maybeSetDisplayPresentationType(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda20
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PresentationStatsEventLogger.lambda$maybeSetDisplayPresentationType$8(i, (PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$maybeSetDisplayPresentationType$8(int i, PresentationStatsEventInternal presentationStatsEventInternal) {
        presentationStatsEventInternal.mDisplayPresentationType = getDisplayPresentationType(i);
    }

    public void maybeSetFillRequestSentTimestampMs(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda17
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mFillRequestSentTimestampMs = i;
            }
        });
    }

    public void maybeSetFillResponseReceivedTimestampMs(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda27
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mFillResponseReceivedTimestampMs = i;
            }
        });
    }

    public void maybeSetSuggestionSentTimestampMs(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda23
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mSuggestionSentTimestampMs = i;
            }
        });
    }

    public void maybeSetSuggestionPresentedTimestampMs(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mSuggestionPresentedTimestampMs = i;
            }
        });
    }

    public void maybeSetSelectedDatasetId(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mSelectedDatasetId = i;
            }
        });
    }

    public void maybeSetDialogDismissed(final boolean z) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mDialogDismissed = z;
            }
        });
    }

    public void maybeSetNegativeCtaButtonClicked(final boolean z) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mNegativeCtaButtonClicked = z;
            }
        });
    }

    public void maybeSetPositiveCtaButtonClicked(final boolean z) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mPositiveCtaButtonClicked = z;
            }
        });
    }

    public void maybeSetInlinePresentationAndSuggestionHostUid(final Context context, final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PresentationStatsEventLogger.lambda$maybeSetInlinePresentationAndSuggestionHostUid$17(context, i, (PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$maybeSetInlinePresentationAndSuggestionHostUid$17(Context context, int i, PresentationStatsEventInternal presentationStatsEventInternal) {
        presentationStatsEventInternal.mDisplayPresentationType = 2;
        String stringForUser = Settings.Secure.getStringForUser(context.getContentResolver(), "default_input_method", i);
        if (TextUtils.isEmpty(stringForUser)) {
            Slog.w(TAG, "No default IME found");
            return;
        }
        ComponentName unflattenFromString = ComponentName.unflattenFromString(stringForUser);
        if (unflattenFromString == null) {
            Slog.w(TAG, "No default IME found");
            return;
        }
        String packageName = unflattenFromString.getPackageName();
        try {
            presentationStatsEventInternal.mInlineSuggestionHostUid = context.getPackageManager().getApplicationInfoAsUser(packageName, PackageManager.ApplicationInfoFlags.of(0L), i).uid;
        } catch (PackageManager.NameNotFoundException unused) {
            Slog.w(TAG, "Couldn't find packageName: " + packageName);
        }
    }

    public void maybeSetAutofillServiceUid(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mAutofillServiceUid = i;
            }
        });
    }

    public void maybeSetIsNewRequest(final boolean z) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mIsRequestTriggered = z;
            }
        });
    }

    public void maybeSetAuthenticationType(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda22
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mAuthenticationType = i;
            }
        });
    }

    public void maybeSetAuthenticationResult(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda25
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mAuthenticationResult = i;
            }
        });
    }

    public void maybeSetLatencyAuthenticationUiDisplayMillis(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mLatencyAuthenticationUiDisplayMillis = i;
            }
        });
    }

    public void maybeSetLatencyDatasetDisplayMillis(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mLatencyDatasetDisplayMillis = i;
            }
        });
    }

    public void maybeSetAvailablePccCount(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mAvailablePccCount = i;
            }
        });
    }

    public void maybeSetAvailablePccOnlyCount(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mAvailablePccOnlyCount = i;
            }
        });
    }

    public void maybeSetSelectedDatasetPickReason(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PresentationStatsEventLogger.this.lambda$maybeSetSelectedDatasetPickReason$26(i, (PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeSetSelectedDatasetPickReason$26(int i, PresentationStatsEventInternal presentationStatsEventInternal) {
        presentationStatsEventInternal.mSelectedDatasetPickedReason = convertDatasetPickReason(i);
    }

    public void maybeSetDetectionPreference(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mDetectionPreference = i;
            }
        });
    }

    public void logAndEndEvent() {
        if (!this.mEventInternal.isPresent()) {
            Slog.w(TAG, "Shouldn't be logging AutofillPresentationEventReported again for same event");
            return;
        }
        PresentationStatsEventInternal presentationStatsEventInternal = this.mEventInternal.get();
        if (Helper.sVerbose) {
            Slog.v(TAG, "Log AutofillPresentationEventReported: requestId=" + presentationStatsEventInternal.mRequestId + " sessionId=" + this.mSessionId + " mNoPresentationEventReason=" + presentationStatsEventInternal.mNoPresentationReason + " mAvailableCount=" + presentationStatsEventInternal.mAvailableCount + " mCountShown=" + presentationStatsEventInternal.mCountShown + " mCountFilteredUserTyping=" + presentationStatsEventInternal.mCountFilteredUserTyping + " mCountNotShownImePresentationNotDrawn=" + presentationStatsEventInternal.mCountNotShownImePresentationNotDrawn + " mCountNotShownImeUserNotSeen=" + presentationStatsEventInternal.mCountNotShownImeUserNotSeen + " mDisplayPresentationType=" + presentationStatsEventInternal.mDisplayPresentationType + " mAutofillServiceUid=" + presentationStatsEventInternal.mAutofillServiceUid + " mInlineSuggestionHostUid=" + presentationStatsEventInternal.mInlineSuggestionHostUid + " mIsRequestTriggered=" + presentationStatsEventInternal.mIsRequestTriggered + " mFillRequestSentTimestampMs=" + presentationStatsEventInternal.mFillRequestSentTimestampMs + " mFillResponseReceivedTimestampMs=" + presentationStatsEventInternal.mFillResponseReceivedTimestampMs + " mSuggestionSentTimestampMs=" + presentationStatsEventInternal.mSuggestionSentTimestampMs + " mSuggestionPresentedTimestampMs=" + presentationStatsEventInternal.mSuggestionPresentedTimestampMs + " mSelectedDatasetId=" + presentationStatsEventInternal.mSelectedDatasetId + " mDialogDismissed=" + presentationStatsEventInternal.mDialogDismissed + " mNegativeCtaButtonClicked=" + presentationStatsEventInternal.mNegativeCtaButtonClicked + " mPositiveCtaButtonClicked=" + presentationStatsEventInternal.mPositiveCtaButtonClicked + " mAuthenticationType=" + presentationStatsEventInternal.mAuthenticationType + " mAuthenticationResult=" + presentationStatsEventInternal.mAuthenticationResult + " mLatencyAuthenticationUiDisplayMillis=" + presentationStatsEventInternal.mLatencyAuthenticationUiDisplayMillis + " mLatencyDatasetDisplayMillis=" + presentationStatsEventInternal.mLatencyDatasetDisplayMillis + " mAvailablePccCount=" + presentationStatsEventInternal.mAvailablePccCount + " mAvailablePccOnlyCount=" + presentationStatsEventInternal.mAvailablePccOnlyCount + " mSelectedDatasetPickedReason=" + presentationStatsEventInternal.mSelectedDatasetPickedReason + " mDetectionPreference=" + presentationStatsEventInternal.mDetectionPreference);
        }
        if (!presentationStatsEventInternal.mIsDatasetAvailable) {
            this.mEventInternal = Optional.empty();
        } else {
            FrameworkStatsLog.write(FrameworkStatsLog.AUTOFILL_PRESENTATION_EVENT_REPORTED, presentationStatsEventInternal.mRequestId, this.mSessionId, presentationStatsEventInternal.mNoPresentationReason, presentationStatsEventInternal.mAvailableCount, presentationStatsEventInternal.mCountShown, presentationStatsEventInternal.mCountFilteredUserTyping, presentationStatsEventInternal.mCountNotShownImePresentationNotDrawn, presentationStatsEventInternal.mCountNotShownImeUserNotSeen, presentationStatsEventInternal.mDisplayPresentationType, presentationStatsEventInternal.mAutofillServiceUid, presentationStatsEventInternal.mInlineSuggestionHostUid, presentationStatsEventInternal.mIsRequestTriggered, presentationStatsEventInternal.mFillRequestSentTimestampMs, presentationStatsEventInternal.mFillResponseReceivedTimestampMs, presentationStatsEventInternal.mSuggestionSentTimestampMs, presentationStatsEventInternal.mSuggestionPresentedTimestampMs, presentationStatsEventInternal.mSelectedDatasetId, presentationStatsEventInternal.mDialogDismissed, presentationStatsEventInternal.mNegativeCtaButtonClicked, presentationStatsEventInternal.mPositiveCtaButtonClicked, presentationStatsEventInternal.mAuthenticationType, presentationStatsEventInternal.mAuthenticationResult, presentationStatsEventInternal.mLatencyAuthenticationUiDisplayMillis, presentationStatsEventInternal.mLatencyDatasetDisplayMillis, presentationStatsEventInternal.mAvailablePccCount, presentationStatsEventInternal.mAvailablePccOnlyCount, presentationStatsEventInternal.mSelectedDatasetPickedReason, presentationStatsEventInternal.mDetectionPreference);
            this.mEventInternal = Optional.empty();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class PresentationStatsEventInternal {
        int mAvailableCount;
        int mCountFilteredUserTyping;
        int mCountNotShownImePresentationNotDrawn;
        int mCountNotShownImeUserNotSeen;
        int mCountShown;
        int mFillRequestSentTimestampMs;
        int mFillResponseReceivedTimestampMs;
        boolean mIsDatasetAvailable;
        boolean mIsRequestTriggered;
        int mRequestId;
        int mSuggestionPresentedTimestampMs;
        int mSuggestionSentTimestampMs;
        int mNoPresentationReason = 0;
        int mDisplayPresentationType = 0;
        int mAutofillServiceUid = -1;
        int mInlineSuggestionHostUid = -1;
        int mSelectedDatasetId = -1;
        boolean mDialogDismissed = false;
        boolean mNegativeCtaButtonClicked = false;
        boolean mPositiveCtaButtonClicked = false;
        int mAuthenticationType = 0;
        int mAuthenticationResult = 0;
        int mLatencyAuthenticationUiDisplayMillis = -1;
        int mLatencyDatasetDisplayMillis = -1;
        int mAvailablePccCount = -1;
        int mAvailablePccOnlyCount = -1;
        int mSelectedDatasetPickedReason = 0;
        int mDetectionPreference = 0;

        PresentationStatsEventInternal() {
        }
    }
}
