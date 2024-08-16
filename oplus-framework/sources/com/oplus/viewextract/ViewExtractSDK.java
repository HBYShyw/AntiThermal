package com.oplus.viewextract;

import android.app.assist.AssistStructure;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.ArrayMap;
import android.view.autofill.AutofillId;
import android.view.viewextract.ViewExtractProxy;
import android.view.viewextract.ViewExtractUtils;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ViewExtractSDK {

    /* loaded from: classes.dex */
    public interface OnExtractBitmapCallback extends OnViewExtractCallback {
        @Override // com.oplus.viewextract.ViewExtractSDK.OnViewExtractCallback
        void onExtractBitmap(int i, boolean z, Bundle bundle);
    }

    /* loaded from: classes.dex */
    public interface OnExtractBitmapsCallback extends OnViewExtractCallback {
        @Override // com.oplus.viewextract.ViewExtractSDK.OnViewExtractCallback
        void onExtractBitmaps(ArrayMap<Integer, Boolean> arrayMap, Bundle bundle);
    }

    /* loaded from: classes.dex */
    public interface OnExtractViewTreeCallback extends OnViewExtractCallback {
        @Override // com.oplus.viewextract.ViewExtractSDK.OnViewExtractCallback
        void onViewAssistStructureCallBack(AssistStructure assistStructure);
    }

    /* loaded from: classes.dex */
    public interface OnViewExtractCallback {
        default void onHandleAssistData(Bundle bundle) {
        }

        default void onError(int error) {
            ViewExtractUtils.e(ViewExtractProxy.Error.toString(error));
        }

        default void onViewAssistStructureCallBack(AssistStructure assistStructure) {
        }

        default void onExtractBitmap(int uniqueId, boolean result, Bundle bundle) {
        }

        default void onExtractBitmaps(ArrayMap<Integer, Boolean> booleanArrayMap, Bundle bundle) {
        }
    }

    public static int getViewUniqueId(AutofillId autofillId) {
        return autofillId.getViewId();
    }

    public static void requestViewExtractTree(OnExtractViewTreeCallback callback, Bundle bundle, String activityName) {
        ViewExtractProxy.requestViewExtractTree(callback, bundle, activityName);
    }

    public static void getBitmapBytViewUniqueId(OnExtractBitmapCallback callback, String activityName, Bundle bundle, int uniqueId, ParcelFileDescriptor parcelFileDescriptor) {
        ViewExtractProxy.getBitmapByViewUid(callback, activityName, bundle, uniqueId, parcelFileDescriptor);
    }

    public static void getBitmapByViewViewUniqueIds(OnExtractBitmapsCallback callback, String activityName, Bundle bundle, ArrayList<Integer> uniqueIds, ArrayList<ParcelFileDescriptor> parcelFileDescriptors) {
        ViewExtractProxy.getBitmapByViewUids(callback, activityName, bundle, uniqueIds, parcelFileDescriptors);
    }
}
