package android.view.viewextract;

import android.app.ActivityThread;
import android.app.IAssistDataReceiver;
import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewStructure;

/* loaded from: classes.dex */
public interface IOplusViewExtractManager extends IOplusCommonFeature {
    public static final IOplusViewExtractManager DEFAULT = new IOplusViewExtractManager() { // from class: android.view.viewextract.IOplusViewExtractManager.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusViewExtractManager;
    }

    default IOplusViewExtractManager getDefault() {
        return DEFAULT;
    }

    default IOplusViewExtractManager newInstance() {
        return DEFAULT;
    }

    default void extractViewInfo(View view, Bundle bundle) {
    }

    default void extractViewBundle(Bundle bundle, ActivityThread activityThread, IBinder iBinder) {
    }

    default void initViewExtract(View view) {
    }

    default void appendViewExtractInfo(View view, ViewStructure info) {
    }

    default boolean isViewExtract(int flag) {
        return false;
    }

    default int addViewExtractFlag(int flag, int viewFlags) {
        return viewFlags;
    }

    default void reportViewExtractResult(Bundle bundle) {
    }

    default void requestViewExtractData(IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, int flags) throws RemoteException {
    }

    /* loaded from: classes.dex */
    public interface ViewExtractListener {
        default void onSaveBitmapResult(boolean result) {
        }

        default void onExtractView(String text, Bitmap bitmap) {
        }

        default void onError(String error) {
        }
    }
}
