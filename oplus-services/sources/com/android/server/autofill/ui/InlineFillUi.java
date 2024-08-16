package com.android.server.autofill.ui;

import android.content.IntentSender;
import android.service.autofill.Dataset;
import android.service.autofill.FillResponse;
import android.service.autofill.InlinePresentation;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.view.inputmethod.InlineSuggestion;
import android.view.inputmethod.InlineSuggestionsRequest;
import android.view.inputmethod.InlineSuggestionsResponse;
import com.android.server.autofill.Helper;
import com.android.server.autofill.RemoteInlineSuggestionRenderService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class InlineFillUi {
    private static final String TAG = "InlineFillUi";
    final AutofillId mAutofillId;
    private final ArrayList<Dataset> mDatasets;
    private boolean mFilterMatchingDisabled;
    private String mFilterText;
    private final ArrayList<InlineSuggestion> mInlineSuggestions;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface InlineSuggestionUiCallback {
        void authenticate(int i, int i2);

        void autofill(Dataset dataset, int i);

        void onError();

        void onInflate();

        void startIntentSender(IntentSender intentSender);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface InlineUiEventCallback {
        void notifyInlineUiHidden(AutofillId autofillId);

        void notifyInlineUiShown(AutofillId autofillId);
    }

    public static InlineFillUi emptyUi(AutofillId autofillId) {
        return new InlineFillUi(autofillId);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class InlineFillUiInfo {
        public String mFilterText;
        public AutofillId mFocusedId;
        public InlineSuggestionsRequest mInlineRequest;
        public RemoteInlineSuggestionRenderService mRemoteRenderService;
        public int mSessionId;
        public int mUserId;

        public InlineFillUiInfo(InlineSuggestionsRequest inlineSuggestionsRequest, AutofillId autofillId, String str, RemoteInlineSuggestionRenderService remoteInlineSuggestionRenderService, int i, int i2) {
            this.mUserId = i;
            this.mSessionId = i2;
            this.mInlineRequest = inlineSuggestionsRequest;
            this.mFocusedId = autofillId;
            this.mFilterText = str;
            this.mRemoteRenderService = remoteInlineSuggestionRenderService;
        }
    }

    public static InlineFillUi forAutofill(InlineFillUiInfo inlineFillUiInfo, FillResponse fillResponse, InlineSuggestionUiCallback inlineSuggestionUiCallback) {
        if (fillResponse.getAuthentication() != null && fillResponse.getInlinePresentation() != null) {
            return new InlineFillUi(inlineFillUiInfo, InlineSuggestionFactory.createInlineAuthentication(inlineFillUiInfo, fillResponse, inlineSuggestionUiCallback));
        }
        if (fillResponse.getDatasets() != null) {
            return new InlineFillUi(inlineFillUiInfo, InlineSuggestionFactory.createInlineSuggestions(inlineFillUiInfo, "android:autofill", fillResponse.getDatasets(), inlineSuggestionUiCallback));
        }
        return new InlineFillUi(inlineFillUiInfo, (SparseArray<Pair<Dataset, InlineSuggestion>>) new SparseArray());
    }

    public static InlineFillUi forAugmentedAutofill(InlineFillUiInfo inlineFillUiInfo, List<Dataset> list, InlineSuggestionUiCallback inlineSuggestionUiCallback) {
        return new InlineFillUi(inlineFillUiInfo, InlineSuggestionFactory.createInlineSuggestions(inlineFillUiInfo, "android:platform", list, inlineSuggestionUiCallback));
    }

    private InlineFillUi(InlineFillUiInfo inlineFillUiInfo, SparseArray<Pair<Dataset, InlineSuggestion>> sparseArray) {
        this.mAutofillId = inlineFillUiInfo.mFocusedId;
        int size = sparseArray.size();
        this.mDatasets = new ArrayList<>(size);
        this.mInlineSuggestions = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Pair<Dataset, InlineSuggestion> valueAt = sparseArray.valueAt(i);
            this.mDatasets.add((Dataset) valueAt.first);
            this.mInlineSuggestions.add((InlineSuggestion) valueAt.second);
        }
        this.mFilterText = inlineFillUiInfo.mFilterText;
    }

    private InlineFillUi(InlineFillUiInfo inlineFillUiInfo, InlineSuggestion inlineSuggestion) {
        this.mAutofillId = inlineFillUiInfo.mFocusedId;
        this.mDatasets = null;
        ArrayList<InlineSuggestion> arrayList = new ArrayList<>();
        this.mInlineSuggestions = arrayList;
        arrayList.add(inlineSuggestion);
        this.mFilterText = inlineFillUiInfo.mFilterText;
    }

    private InlineFillUi(AutofillId autofillId) {
        this.mAutofillId = autofillId;
        this.mDatasets = new ArrayList<>(0);
        this.mInlineSuggestions = new ArrayList<>(0);
        this.mFilterText = null;
    }

    public AutofillId getAutofillId() {
        return this.mAutofillId;
    }

    public void setFilterText(String str) {
        this.mFilterText = str;
    }

    public InlineSuggestionsResponse getInlineSuggestionsResponse() {
        int size = this.mInlineSuggestions.size();
        if (size == 0) {
            return new InlineSuggestionsResponse(Collections.emptyList());
        }
        ArrayList arrayList = new ArrayList();
        ArrayList<Dataset> arrayList2 = this.mDatasets;
        int i = 0;
        if (arrayList2 == null || arrayList2.size() != size) {
            while (i < size) {
                arrayList.add(copy(i, this.mInlineSuggestions.get(i)));
                i++;
            }
            return new InlineSuggestionsResponse(arrayList);
        }
        while (i < size) {
            Dataset dataset = this.mDatasets.get(i);
            int indexOf = dataset.getFieldIds().indexOf(this.mAutofillId);
            if (indexOf < 0) {
                Slog.w(TAG, "AutofillId=" + this.mAutofillId + " not found in dataset");
            } else {
                InlinePresentation fieldInlinePresentation = dataset.getFieldInlinePresentation(indexOf);
                if (fieldInlinePresentation == null) {
                    Slog.w(TAG, "InlinePresentation not found in dataset");
                } else if (fieldInlinePresentation.isPinned() || includeDataset(dataset, indexOf)) {
                    arrayList.add(copy(i, this.mInlineSuggestions.get(i)));
                }
            }
            i++;
        }
        return new InlineSuggestionsResponse(arrayList);
    }

    private InlineSuggestion copy(int i, InlineSuggestion inlineSuggestion) {
        InlineContentProviderImpl contentProvider = inlineSuggestion.getContentProvider();
        if (!(contentProvider instanceof InlineContentProviderImpl)) {
            return inlineSuggestion;
        }
        InlineSuggestion inlineSuggestion2 = new InlineSuggestion(inlineSuggestion.getInfo(), contentProvider.copy());
        this.mInlineSuggestions.set(i, inlineSuggestion2);
        return inlineSuggestion2;
    }

    private boolean includeDataset(Dataset dataset, int i) {
        if (TextUtils.isEmpty(this.mFilterText)) {
            return true;
        }
        String lowerCase = this.mFilterText.toString().toLowerCase();
        Dataset.DatasetFieldFilter filter = dataset.getFilter(i);
        if (filter != null) {
            Pattern pattern = filter.pattern;
            if (pattern == null) {
                if (Helper.sVerbose) {
                    Slog.v(TAG, "Explicitly disabling filter for dataset id" + dataset.getId());
                }
                return false;
            }
            if (this.mFilterMatchingDisabled) {
                return false;
            }
            return pattern.matcher(lowerCase).matches();
        }
        AutofillValue autofillValue = (AutofillValue) dataset.getFieldValues().get(i);
        if (autofillValue == null || !autofillValue.isText()) {
            return dataset.getAuthentication() == null;
        }
        if (this.mFilterMatchingDisabled) {
            return false;
        }
        return autofillValue.getTextValue().toString().toLowerCase().toLowerCase().startsWith(lowerCase);
    }

    public void disableFilterMatching() {
        this.mFilterMatchingDisabled = true;
    }
}
