package com.android.server.autofill;

import android.graphics.Rect;
import android.service.autofill.FillResponse;
import android.util.DebugUtils;
import android.util.Slog;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ViewState {
    public static final int STATE_AUTOFILLED = 4;
    public static final int STATE_AUTOFILLED_ONCE = 2048;
    public static final int STATE_AUTOFILL_FAILED = 1024;
    public static final int STATE_CHANGED = 8;
    public static final int STATE_CHAR_REMOVED = 16384;
    public static final int STATE_FILLABLE = 2;
    public static final int STATE_FILL_DIALOG_SHOWN = 131072;
    public static final int STATE_IGNORED = 128;
    public static final int STATE_INITIAL = 1;
    public static final int STATE_INLINE_DISABLED = 32768;
    public static final int STATE_INLINE_SHOWN = 8192;
    public static final int STATE_PENDING_CREATE_INLINE_REQUEST = 65536;
    public static final int STATE_RESTARTED_SESSION = 256;
    public static final int STATE_STARTED_PARTITION = 32;
    public static final int STATE_STARTED_SESSION = 16;
    public static final int STATE_TRIGGERED_AUGMENTED_AUTOFILL = 4096;
    public static final int STATE_URL_BAR = 512;
    public static final int STATE_WAITING_DATASET_AUTH = 64;
    private static final String TAG = "ViewState";
    public final AutofillId id;
    private AutofillValue mAutofilledValue;
    private AutofillValue mCurrentValue;
    private String mDatasetId;
    private final Listener mListener;
    private FillResponse mResponse;
    private AutofillValue mSanitizedValue;
    private int mState;
    private Rect mVirtualBounds;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Listener {
        void onFillReady(FillResponse fillResponse, AutofillId autofillId, AutofillValue autofillValue, int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ViewState(AutofillId autofillId, Listener listener, int i) {
        this.id = autofillId;
        this.mListener = listener;
        this.mState = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getVirtualBounds() {
        return this.mVirtualBounds;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutofillValue getCurrentValue() {
        return this.mCurrentValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCurrentValue(AutofillValue autofillValue) {
        this.mCurrentValue = autofillValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutofillValue getAutofilledValue() {
        return this.mAutofilledValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAutofilledValue(AutofillValue autofillValue) {
        this.mAutofilledValue = autofillValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutofillValue getSanitizedValue() {
        return this.mSanitizedValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSanitizedValue(AutofillValue autofillValue) {
        this.mSanitizedValue = autofillValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FillResponse getResponse() {
        return this.mResponse;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setResponse(FillResponse fillResponse) {
        this.mResponse = fillResponse;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getState() {
        return this.mState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getStateAsString() {
        return getStateAsString(this.mState);
    }

    static String getStateAsString(int i) {
        return DebugUtils.flagsToString(ViewState.class, "STATE_", i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setState(int i) {
        int i2 = this.mState;
        if (i2 == 1) {
            this.mState = i;
        } else {
            this.mState = i2 | i;
        }
        if (i == 4) {
            this.mState |= 2048;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetState(int i) {
        this.mState = (~i) & this.mState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getDatasetId() {
        return this.mDatasetId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDatasetId(String str) {
        this.mDatasetId = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void update(AutofillValue autofillValue, Rect rect, int i) {
        if (autofillValue != null) {
            this.mCurrentValue = autofillValue;
        }
        if (rect != null) {
            this.mVirtualBounds = rect;
        }
        maybeCallOnFillReady(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void maybeCallOnFillReady(int i) {
        if ((this.mState & 4) != 0 && (i & 1) == 0) {
            if (Helper.sDebug) {
                Slog.d(TAG, "Ignoring UI for " + this.id + " on " + getStateAsString());
                return;
            }
            return;
        }
        FillResponse fillResponse = this.mResponse;
        if (fillResponse != null) {
            if (fillResponse.getDatasets() == null && this.mResponse.getAuthentication() == null) {
                return;
            }
            this.mListener.onFillReady(this.mResponse, this.id, this.mCurrentValue, i);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ViewState: [id=");
        sb.append(this.id);
        if (this.mDatasetId != null) {
            sb.append(", datasetId:");
            sb.append(this.mDatasetId);
        }
        sb.append(", state:");
        sb.append(getStateAsString());
        if (this.mCurrentValue != null) {
            sb.append(", currentValue:");
            sb.append(this.mCurrentValue);
        }
        if (this.mAutofilledValue != null) {
            sb.append(", autofilledValue:");
            sb.append(this.mAutofilledValue);
        }
        if (this.mSanitizedValue != null) {
            sb.append(", sanitizedValue:");
            sb.append(this.mSanitizedValue);
        }
        if (this.mVirtualBounds != null) {
            sb.append(", virtualBounds:");
            sb.append(this.mVirtualBounds);
        }
        sb.append("]");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(String str, PrintWriter printWriter) {
        printWriter.print(str);
        printWriter.print("id:");
        printWriter.println(this.id);
        if (this.mDatasetId != null) {
            printWriter.print(str);
            printWriter.print("datasetId:");
            printWriter.println(this.mDatasetId);
        }
        printWriter.print(str);
        printWriter.print("state:");
        printWriter.println(getStateAsString());
        if (this.mResponse != null) {
            printWriter.print(str);
            printWriter.print("response id:");
            printWriter.println(this.mResponse.getRequestId());
        }
        if (this.mCurrentValue != null) {
            printWriter.print(str);
            printWriter.print("currentValue:");
            printWriter.println(this.mCurrentValue);
        }
        if (this.mAutofilledValue != null) {
            printWriter.print(str);
            printWriter.print("autofilledValue:");
            printWriter.println(this.mAutofilledValue);
        }
        if (this.mSanitizedValue != null) {
            printWriter.print(str);
            printWriter.print("sanitizedValue:");
            printWriter.println(this.mSanitizedValue);
        }
        if (this.mVirtualBounds != null) {
            printWriter.print(str);
            printWriter.print("virtualBounds:");
            printWriter.println(this.mVirtualBounds);
        }
    }
}
