package android.view.viewextract;

import android.app.IAssistDataReceiver;
import android.app.assist.AssistStructure;
import android.common.OplusFeatureCache;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import com.oplus.viewextract.ViewExtractSDK;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ViewExtractProxy {
    public static final int FLAG_VIEW_EXTRACT = 268435456;
    public static final String TAG = "ViewExtractProxy";

    /* loaded from: classes.dex */
    public static final class Constant {
        public static final String ACTIVITY_NAME = "activityName";
        public static final String FILE_DESCRIPTOR = "fd";
        public static final String FILE_DESCRIPTORS = "fds";
        public static final String RESULT = "result";
        public static final String RESULTS = "results";
        public static final String SUPPORT_MULTI_WINDOW = "SUPPORT_MULTI_WINDOW";
        public static final String TOKENS = "token";
        public static final String VIEW_ID = "viewId";
        public static final String VIEW_IDS = "viewIds";
    }

    /* loaded from: classes.dex */
    public static final class ExtraInfo {
        public static final String EXTRA_CLASS_NAME = "extraClassName";
        public static final String GET_BOUNDS_ON_SCREEN = "getBoundsOnScreen";
        public static final String IS_VISIBLE_TO_USER = "isVisibleToUser";
    }

    /* loaded from: classes.dex */
    public static final class Error {
        public static final int BUNDLE_NULL = 2;
        public static final int NO_ACTIVITY = 3;
        public static final int REMOTE_EXCEPTION = 1;
        public static final int SIZE_ERROR = 4;
        public static final String TAG = "error";
        public static final int UN_KNOW = 0;

        public static int get(Bundle bundle) {
            if (bundle == null) {
                return 0;
            }
            return bundle.getInt("error");
        }

        public static String toString(Bundle bundle) {
            return toString(get(bundle));
        }

        public static String toString(int error) {
            switch (error) {
                case 1:
                    return "REMOTE_EXCEPTION";
                case 2:
                    return "BUNDLE_NULL";
                case 3:
                    return "NO_ACTIVITY";
                case 4:
                    return "SIZE_ERROR";
                default:
                    return "UN_KNOW";
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class Feature {
        public static final int DEFAULT = 0;
        public static final int GET_FEATURE = 4;
        public static final int GET_VIEW_ASSIST_STRUCTURE = 1;
        public static final int GET_VIEW_BITMAP = 2;
        public static final int GET_VIEW_BITMAP_MAP = 3;
        public static final String TAG = "feature";

        public static int get(Bundle bundle) {
            if (bundle == null) {
                return 0;
            }
            return bundle.getInt(TAG);
        }

        public static String toString(Bundle bundle) {
            int feature = get(bundle);
            switch (feature) {
                case 1:
                    return "GET_VIEW_ASSIST_STRUCTURE";
                case 2:
                    return "GET_VIEW_BITMAP";
                case 3:
                    return "GET_VIEW_BITMAP_MAP";
                default:
                    return "DEFAULT";
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ViewExtractReceiver extends IAssistDataReceiver.Stub {
        ViewExtractSDK.OnViewExtractCallback mViewExtractCallback;

        public ViewExtractReceiver(ViewExtractSDK.OnViewExtractCallback onViewExtractCallback) {
            this.mViewExtractCallback = onViewExtractCallback;
        }

        public void onHandleAssistData(Bundle bundle) {
            this.mViewExtractCallback.onHandleAssistData(bundle);
            if (bundle == null) {
                this.mViewExtractCallback.onError(2);
                return;
            }
            try {
                if (Error.get(bundle) != 0) {
                    int error = bundle.getInt("error");
                    this.mViewExtractCallback.onError(error);
                    return;
                }
                int error2 = Feature.get(bundle);
                if (error2 == 3) {
                    Bundle resultBundle = bundle.getBundle(Constant.RESULTS);
                    if (resultBundle != null) {
                        ArrayMap<Integer, Boolean> booleanArrayMap = new ArrayMap<>();
                        for (String key : resultBundle.keySet()) {
                            ViewExtractResult viewExtractResult = (ViewExtractResult) resultBundle.getParcelable(key, ViewExtractResult.class);
                            if (viewExtractResult != null) {
                                booleanArrayMap.put(Integer.valueOf(key), Boolean.valueOf(viewExtractResult.getResult()));
                                Bundle rBundle = new Bundle();
                                rBundle.putString("text", viewExtractResult.getText());
                                rBundle.putString("error", viewExtractResult.getError());
                                this.mViewExtractCallback.onExtractBitmap(Integer.parseInt(key), viewExtractResult.getResult(), rBundle);
                            }
                        }
                        this.mViewExtractCallback.onExtractBitmaps(booleanArrayMap, bundle);
                        return;
                    }
                    return;
                }
                Bundle inputBundle = bundle.getBundle("receiverExtras");
                bundle.getBundle("data");
                if (Feature.get(inputBundle) == 1) {
                    AssistStructure structure = (AssistStructure) bundle.getParcelable("structure", AssistStructure.class);
                    this.mViewExtractCallback.onViewAssistStructureCallBack(structure);
                }
            } catch (Exception e) {
                ViewExtractUtils.e(ViewExtractProxy.TAG, "onHandleAssistData" + e);
            }
        }

        public void onHandleAssistScreenshot(Bitmap bitmap) throws RemoteException {
        }

        public ViewExtractSDK.OnViewExtractCallback getViewExtractCallBack() {
            return this.mViewExtractCallback;
        }
    }

    /* loaded from: classes.dex */
    public static class ViewExtractResult implements Parcelable {
        public static final Parcelable.Creator<ViewExtractResult> CREATOR = new Parcelable.Creator<ViewExtractResult>() { // from class: android.view.viewextract.ViewExtractProxy.ViewExtractResult.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ViewExtractResult createFromParcel(Parcel in) {
                return new ViewExtractResult(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ViewExtractResult[] newArray(int size) {
                return new ViewExtractResult[size];
            }
        };
        private String mError;
        private boolean mResult;
        private String mText;

        public ViewExtractResult() {
        }

        protected ViewExtractResult(Parcel in) {
            this.mResult = in.readByte() != 0;
            this.mText = in.readString();
            this.mError = in.readString();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeByte(this.mResult ? (byte) 1 : (byte) 0);
            parcel.writeString(this.mText);
            parcel.writeString(this.mError);
        }

        public void setResult(Boolean b) {
            this.mResult = b.booleanValue();
        }

        public boolean getResult() {
            return this.mResult;
        }

        public void setError(String error) {
            this.mError = error;
        }

        public String getError() {
            return this.mError;
        }

        public void setText(String text) {
            this.mText = text;
        }

        public String getText() {
            return this.mText;
        }
    }

    private static void requestViewExtractData(ViewExtractSDK.OnViewExtractCallback callback, Bundle bundle, IBinder iBinder, int flag) {
        try {
            ((IOplusViewExtractManager) OplusFeatureCache.getOrCreate(IOplusViewExtractManager.DEFAULT, new Object[0])).requestViewExtractData(new ViewExtractReceiver(callback), bundle, iBinder, flag);
        } catch (RemoteException e) {
            callback.onError(1);
        }
    }

    public static void requestViewExtractTree(ViewExtractSDK.OnExtractViewTreeCallback callback, Bundle bundle, String activityName) {
        if (Feature.get(bundle) == 4) {
            Bundle featureBundle = new Bundle();
            featureBundle.putBoolean(Constant.SUPPORT_MULTI_WINDOW, true);
            callback.onHandleAssistData(featureBundle);
            Log.d(TAG, "requestViewExtractTree: GET_FEATURE SUPPORT_MULTI_WINDOW");
            return;
        }
        if (callback == null) {
            throw new IllegalArgumentException("callback can not be null.");
        }
        if (bundle == null) {
            callback.onError(2);
            return;
        }
        bundle.putInt(Feature.TAG, 1);
        bundle.putString(Constant.ACTIVITY_NAME, activityName);
        requestViewExtractData(callback, bundle, null, 1);
    }

    public static void getBitmapByViewUid(ViewExtractSDK.OnExtractBitmapCallback callback, String activityName, Bundle bundle, int id, ParcelFileDescriptor parcelFileDescriptor) {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(Integer.valueOf(id));
        ArrayList<ParcelFileDescriptor> parcelFileDescriptors = new ArrayList<>();
        parcelFileDescriptors.add(parcelFileDescriptor);
        getBitmap(callback, activityName, bundle, ids, parcelFileDescriptors);
    }

    public static void getBitmapByViewUids(ViewExtractSDK.OnExtractBitmapsCallback callback, String activityName, Bundle bundle, ArrayList<Integer> ids, ArrayList<ParcelFileDescriptor> parcelFileDescriptors) {
        getBitmap(callback, activityName, bundle, ids, parcelFileDescriptors);
    }

    private static void getBitmap(ViewExtractSDK.OnViewExtractCallback callback, String activityName, Bundle bundle, ArrayList<Integer> ids, ArrayList<ParcelFileDescriptor> parcelFileDescriptors) {
        if (callback == null) {
            throw new IllegalArgumentException("callback can not be null.");
        }
        if (bundle == null) {
            callback.onError(2);
            return;
        }
        if (ids == null || parcelFileDescriptors == null || ids.size() != parcelFileDescriptors.size()) {
            callback.onError(4);
            return;
        }
        bundle.putInt(Feature.TAG, 3);
        bundle.putInt(Constant.TOKENS, bundle.hashCode());
        bundle.putString(Constant.ACTIVITY_NAME, activityName);
        bundle.putParcelableArrayList(Constant.FILE_DESCRIPTORS, parcelFileDescriptors);
        bundle.putIntegerArrayList(Constant.VIEW_IDS, ids);
        requestViewExtractData(callback, bundle, null, 0);
    }
}
