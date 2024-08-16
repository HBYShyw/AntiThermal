package com.oplus.globaldrag;

import android.app.OplusActivityTaskManager;
import android.content.ClipData;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import android.view.SurfaceControl;
import com.oplus.globaldrag.IDragAndDropListener;
import java.util.Map;

/* loaded from: classes.dex */
public class DragAndDropDispatcherManager {
    private static final String TAG = "DragAndDropDispatcherManager";
    private static volatile DragAndDropDispatcherManager sInstance;
    private final Map<IDragAndDropListenerCallback, DragAndDropListenerCallbackDelegate> mCallbackMap = new ArrayMap();
    private OplusActivityTaskManager mOAtms = OplusActivityTaskManager.getInstance();

    /* loaded from: classes.dex */
    public interface IDragAndDropListenerCallback {
        void postCancelDragAndDrop();

        void postEndDrag();

        void postPerformDrag();

        void postReportDropResult();

        void prePerformDrag(String str, SurfaceControl surfaceControl, Bundle bundle, ClipData clipData);

        void preReportDropResult(String str, boolean z);
    }

    private DragAndDropDispatcherManager() {
    }

    public static DragAndDropDispatcherManager getInstance() {
        if (sInstance == null) {
            synchronized (DragAndDropDispatcherManager.class) {
                if (sInstance == null) {
                    sInstance = new DragAndDropDispatcherManager();
                }
            }
        }
        return sInstance;
    }

    public void registerDragAndDropListener(String pkgName, IDragAndDropListenerCallback callback) {
        if (callback == null) {
            return;
        }
        synchronized (this.mCallbackMap) {
            if (this.mCallbackMap.get(callback) != null) {
                Log.e(TAG, "already register before");
                return;
            }
            DragAndDropListenerCallbackDelegate delegate = new DragAndDropListenerCallbackDelegate(callback);
            try {
                this.mOAtms.registerDragAndDropListener(pkgName, delegate);
                this.mCallbackMap.put(callback, delegate);
            } catch (RemoteException e) {
                Log.e(TAG, "registerDragAndDropListener remoteException " + e);
            }
        }
    }

    public void unregisterDragAndDropListener(String pkgName, IDragAndDropListenerCallback callback) {
        if (callback == null) {
            return;
        }
        synchronized (this.mCallbackMap) {
            DragAndDropListenerCallbackDelegate delegate = this.mCallbackMap.get(callback);
            if (delegate != null) {
                try {
                    this.mCallbackMap.remove(callback);
                    this.mOAtms.unregisterDragAndDropListener(pkgName, delegate);
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterDragAndDropListener remoteException " + e);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private class DragAndDropListenerCallbackDelegate extends IDragAndDropListener.Stub {
        private final IDragAndDropListenerCallback mCallBack;

        public DragAndDropListenerCallbackDelegate(IDragAndDropListenerCallback callBack) {
            this.mCallBack = callBack;
        }

        @Override // com.oplus.globaldrag.IDragAndDropListener
        public void prePerformDrag(String name, SurfaceControl surface, Bundle bundle, ClipData data) {
            IDragAndDropListenerCallback iDragAndDropListenerCallback = this.mCallBack;
            if (iDragAndDropListenerCallback != null) {
                iDragAndDropListenerCallback.prePerformDrag(name, surface, bundle, data);
            }
        }

        @Override // com.oplus.globaldrag.IDragAndDropListener
        public void postPerformDrag() {
            IDragAndDropListenerCallback iDragAndDropListenerCallback = this.mCallBack;
            if (iDragAndDropListenerCallback != null) {
                iDragAndDropListenerCallback.postPerformDrag();
            }
        }

        @Override // com.oplus.globaldrag.IDragAndDropListener
        public void preReportDropResult(String name, boolean consumed) {
            IDragAndDropListenerCallback iDragAndDropListenerCallback = this.mCallBack;
            if (iDragAndDropListenerCallback != null) {
                iDragAndDropListenerCallback.preReportDropResult(name, consumed);
            }
        }

        @Override // com.oplus.globaldrag.IDragAndDropListener
        public void postReportDropResult() {
            IDragAndDropListenerCallback iDragAndDropListenerCallback = this.mCallBack;
            if (iDragAndDropListenerCallback != null) {
                iDragAndDropListenerCallback.postReportDropResult();
            }
        }

        @Override // com.oplus.globaldrag.IDragAndDropListener
        public void postCancelDragAndDrop() {
            IDragAndDropListenerCallback iDragAndDropListenerCallback = this.mCallBack;
            if (iDragAndDropListenerCallback != null) {
                iDragAndDropListenerCallback.postCancelDragAndDrop();
            }
        }

        @Override // com.oplus.globaldrag.IDragAndDropListener
        public void postEndDrag() {
            IDragAndDropListenerCallback iDragAndDropListenerCallback = this.mCallBack;
            if (iDragAndDropListenerCallback != null) {
                iDragAndDropListenerCallback.postEndDrag();
            }
        }
    }
}
