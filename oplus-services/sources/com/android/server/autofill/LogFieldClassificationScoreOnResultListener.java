package com.android.server.autofill;

import android.os.Bundle;
import android.os.RemoteCallback;
import android.service.autofill.FieldClassification;
import android.util.Slog;
import android.view.autofill.AutofillId;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class LogFieldClassificationScoreOnResultListener implements RemoteCallback.OnResultListener {
    private static final String TAG = "LogFieldClassificationScoreOnResultListener";
    private final AutofillId[] mAutofillIds;
    private final String[] mCategoryIds;
    private final int mCommitReason;
    private final ArrayList<FieldClassification> mDetectedFieldClassifications;
    private final ArrayList<AutofillId> mDetectedFieldIds;
    private final int mSaveDialogNotShowReason;
    private Session mSession;
    private final String[] mUserValues;
    private final int mViewsSize;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LogFieldClassificationScoreOnResultListener(Session session, int i, int i2, int i3, AutofillId[] autofillIdArr, String[] strArr, String[] strArr2, ArrayList<AutofillId> arrayList, ArrayList<FieldClassification> arrayList2) {
        this.mSession = session;
        this.mSaveDialogNotShowReason = i;
        this.mCommitReason = i2;
        this.mViewsSize = i3;
        this.mAutofillIds = autofillIdArr;
        this.mUserValues = strArr;
        this.mCategoryIds = strArr2;
        this.mDetectedFieldIds = arrayList;
        this.mDetectedFieldClassifications = arrayList2;
    }

    public void onResult(Bundle bundle) {
        Session session = this.mSession;
        if (session == null) {
            Slog.wtf(TAG, "session is null when calling onResult()");
        } else {
            session.handleLogFieldClassificationScore(bundle, this.mSaveDialogNotShowReason, this.mCommitReason, this.mViewsSize, this.mAutofillIds, this.mUserValues, this.mCategoryIds, this.mDetectedFieldIds, this.mDetectedFieldClassifications);
            this.mSession = null;
        }
    }
}
