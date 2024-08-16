package com.android.server.autofill;

import android.util.Slog;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.autofill.SaveEventLogger;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SaveEventLogger {
    public static final int NO_SAVE_REASON_DATASET_MATCH = 7;
    public static final int NO_SAVE_REASON_FIELD_VALIDATION_FAILED = 6;
    public static final int NO_SAVE_REASON_HAS_EMPTY_REQUIRED = 4;
    public static final int NO_SAVE_REASON_NONE = 1;
    public static final int NO_SAVE_REASON_NO_SAVE_INFO = 2;
    public static final int NO_SAVE_REASON_NO_VALUE_CHANGED = 5;
    public static final int NO_SAVE_REASON_SESSION_DESTROYED = 9;
    public static final int NO_SAVE_REASON_UNKNOWN = 0;
    public static final int NO_SAVE_REASON_WITH_DELAY_SAVE_FLAG = 3;
    public static final int NO_SAVE_REASON_WITH_DONT_SAVE_ON_FINISH_FLAG = 8;
    public static final int SAVE_UI_SHOWN_REASON_OPTIONAL_ID_CHANGE = 2;
    public static final int SAVE_UI_SHOWN_REASON_REQUIRED_ID_CHANGE = 1;
    public static final int SAVE_UI_SHOWN_REASON_TRIGGER_ID_SET = 3;
    public static final int SAVE_UI_SHOWN_REASON_UNKNOWN = 0;
    private static final String TAG = "SaveEventLogger";
    private Optional<SaveEventInternal> mEventInternal = Optional.of(new SaveEventInternal());
    private final int mSessionId;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface SaveUiNotShownReason {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface SaveUiShownReason {
    }

    private SaveEventLogger(int i) {
        this.mSessionId = i;
    }

    public static SaveEventLogger forSessionId(int i) {
        return new SaveEventLogger(i);
    }

    public void maybeSetRequestId(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mRequestId = i;
            }
        });
    }

    public void maybeSetAppPackageUid(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mAppPackageUid = i;
            }
        });
    }

    public void maybeSetSaveUiTriggerIds(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mSaveUiTriggerIds = i;
            }
        });
    }

    public void maybeSetFlag(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SaveEventLogger.lambda$maybeSetFlag$3(i, (SaveEventLogger.SaveEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$maybeSetFlag$3(int i, SaveEventInternal saveEventInternal) {
        saveEventInternal.mFlag = i;
    }

    public void maybeSetIsNewField(final boolean z) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mIsNewField = z;
            }
        });
    }

    public void maybeSetSaveUiShownReason(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mSaveUiShownReason = i;
            }
        });
    }

    public void maybeSetSaveUiNotShownReason(final int i) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mSaveUiNotShownReason = i;
            }
        });
    }

    public void maybeSetSaveButtonClicked(final boolean z) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mSaveButtonClicked = z;
            }
        });
    }

    public void maybeSetCancelButtonClicked(final boolean z) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mCancelButtonClicked = z;
            }
        });
    }

    public void maybeSetDialogDismissed(final boolean z) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mDialogDismissed = z;
            }
        });
    }

    public void maybeSetIsSaved(final boolean z) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mIsSaved = z;
            }
        });
    }

    public void maybeSetLatencySaveUiDisplayMillis(final long j) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mLatencySaveUiDisplayMillis = j;
            }
        });
    }

    public void maybeSetLatencySaveRequestMillis(final long j) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mLatencySaveRequestMillis = j;
            }
        });
    }

    public void maybeSetLatencySaveFinishMillis(final long j) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mLatencySaveFinishMillis = j;
            }
        });
    }

    public void maybeSetIsFrameworkCreatedSaveInfo(final boolean z) {
        this.mEventInternal.ifPresent(new Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((SaveEventLogger.SaveEventInternal) obj).mIsFrameworkCreatedSaveInfo = z;
            }
        });
    }

    public void logAndEndEvent() {
        if (!this.mEventInternal.isPresent()) {
            Slog.w(TAG, "Shouldn't be logging AutofillSaveEventReported again for same event");
            return;
        }
        SaveEventInternal saveEventInternal = this.mEventInternal.get();
        if (Helper.sVerbose) {
            Slog.v(TAG, "Log AutofillSaveEventReported: requestId=" + saveEventInternal.mRequestId + " sessionId=" + this.mSessionId + " mAppPackageUid=" + saveEventInternal.mAppPackageUid + " mSaveUiTriggerIds=" + saveEventInternal.mSaveUiTriggerIds + " mFlag=" + saveEventInternal.mFlag + " mIsNewField=" + saveEventInternal.mIsNewField + " mSaveUiShownReason=" + saveEventInternal.mSaveUiShownReason + " mSaveUiNotShownReason=" + saveEventInternal.mSaveUiNotShownReason + " mSaveButtonClicked=" + saveEventInternal.mSaveButtonClicked + " mCancelButtonClicked=" + saveEventInternal.mCancelButtonClicked + " mDialogDismissed=" + saveEventInternal.mDialogDismissed + " mIsSaved=" + saveEventInternal.mIsSaved + " mLatencySaveUiDisplayMillis=" + saveEventInternal.mLatencySaveUiDisplayMillis + " mLatencySaveRequestMillis=" + saveEventInternal.mLatencySaveRequestMillis + " mLatencySaveFinishMillis=" + saveEventInternal.mLatencySaveFinishMillis + " mIsFrameworkCreatedSaveInfo=" + saveEventInternal.mIsFrameworkCreatedSaveInfo);
        }
        FrameworkStatsLog.write(FrameworkStatsLog.AUTOFILL_SAVE_EVENT_REPORTED, saveEventInternal.mRequestId, this.mSessionId, saveEventInternal.mAppPackageUid, saveEventInternal.mSaveUiTriggerIds, saveEventInternal.mFlag, saveEventInternal.mIsNewField, saveEventInternal.mSaveUiShownReason, saveEventInternal.mSaveUiNotShownReason, saveEventInternal.mSaveButtonClicked, saveEventInternal.mCancelButtonClicked, saveEventInternal.mDialogDismissed, saveEventInternal.mIsSaved, saveEventInternal.mLatencySaveUiDisplayMillis, saveEventInternal.mLatencySaveRequestMillis, saveEventInternal.mLatencySaveFinishMillis, saveEventInternal.mIsFrameworkCreatedSaveInfo);
        this.mEventInternal = Optional.empty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class SaveEventInternal {
        int mRequestId;
        int mAppPackageUid = -1;
        int mSaveUiTriggerIds = -1;
        long mFlag = -1;
        boolean mIsNewField = false;
        int mSaveUiShownReason = 0;
        int mSaveUiNotShownReason = 0;
        boolean mSaveButtonClicked = false;
        boolean mCancelButtonClicked = false;
        boolean mDialogDismissed = false;
        boolean mIsSaved = false;
        long mLatencySaveUiDisplayMillis = 0;
        long mLatencySaveRequestMillis = 0;
        long mLatencySaveFinishMillis = 0;
        boolean mIsFrameworkCreatedSaveInfo = false;

        SaveEventInternal() {
        }
    }
}
