package android.view.viewextract;

import android.app.Activity;
import android.app.ActivityThread;
import android.app.IAssistDataReceiver;
import android.app.OplusActivityTaskManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.IBaseCanvasExt;
import android.graphics.Picture;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewRootImpl;
import android.view.ViewStructure;
import android.view.WindowManagerGlobal;
import android.view.viewextract.IOplusViewExtractManager;
import android.view.viewextract.ViewExtractProxy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class OplusViewExtractManager implements IOplusViewExtractManager {
    public static final String TAG = "OplusViewExtractManager";
    private static final long THREAD_LIVE_TIME = 0;
    private static final String THREAD_NAME_FORMAT = "view_extract-";
    private static final int THREAD_POOL_MAX = 20;
    private static final int THREAD_POOL_SIZE = 5;
    private final ExecutorService mExecutor = new ThreadPoolExecutor(5, 20, THREAD_LIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new ViewExtractThreadFactory(THREAD_NAME_FORMAT));

    /* loaded from: classes.dex */
    private static class ViewExtractThreadFactory implements ThreadFactory {
        private final String mNamePrefix;
        private final AtomicInteger mNextId = new AtomicInteger(1);

        ViewExtractThreadFactory(String whatFeatureOfGroup) {
            this.mNamePrefix = whatFeatureOfGroup;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable task) {
            String name = this.mNamePrefix + this.mNextId.getAndIncrement();
            return new Thread(null, task, name, OplusViewExtractManager.THREAD_LIVE_TIME);
        }
    }

    /* loaded from: classes.dex */
    public static class ExtractViewItem implements IOplusViewExtractManager.ViewExtractListener {
        private final ParcelFileDescriptor mFileDescriptor;
        private final int mId;
        private final View mView;
        private final IOplusViewExtractManager.ViewExtractListener mViewExtractListener;

        public ExtractViewItem(View view, int id, ParcelFileDescriptor parcelFileDescriptor, IOplusViewExtractManager.ViewExtractListener listener) {
            this.mView = view;
            this.mId = id;
            this.mFileDescriptor = parcelFileDescriptor;
            this.mViewExtractListener = listener;
        }

        public View getView() {
            return this.mView;
        }

        public int getViewID() {
            return this.mId;
        }

        public ParcelFileDescriptor getFd() {
            return this.mFileDescriptor;
        }

        public IOplusViewExtractManager.ViewExtractListener getViewExtractListener() {
            return this.mViewExtractListener;
        }
    }

    /* loaded from: classes.dex */
    public static class ViewExtractCanvasProvider extends ViewDebug.HardwareCanvasProvider implements IBaseCanvasExt.OnCanvasDrawCallback {
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private ExtractViewItem mItem;
        private Picture mPicture;
        private String mText;

        public ViewExtractCanvasProvider(ExtractViewItem item) {
            this.mItem = item;
        }

        public Canvas getCanvas(View view, int width, int height) {
            Picture picture = new Picture();
            this.mPicture = picture;
            Canvas beginRecording = picture.beginRecording(width, height);
            this.mCanvas = beginRecording;
            beginRecording.mBaseCanvasExt.setCanvasDrawCallback(this);
            return this.mCanvas;
        }

        public Bitmap createBitmap() {
            this.mPicture.endRecording();
            this.mCanvas.mBaseCanvasExt.setCanvasDrawCallback((IBaseCanvasExt.OnCanvasDrawCallback) null);
            finish();
            return Bitmap.createBitmap(this.mPicture);
        }

        public void onBitmapDraw(Bitmap bitmap) {
        }

        public void onTextDraw(String string) {
            this.mText = string;
        }

        public void finish() {
            this.mItem.getViewExtractListener().onExtractView(this.mText, this.mBitmap);
            Bitmap bitmap = this.mBitmap;
            if (bitmap != null) {
                bitmap.recycle();
            }
            this.mText = null;
        }
    }

    public OplusViewExtractManager() {
        ViewExtractUtils.d(TAG, "OplusViewExtractManager init" + ActivityThread.currentPackageName());
    }

    public static void saveBitmapByFileDescriptor(Bitmap bitmap, ExtractViewItem extractViewItem) {
        long time = System.currentTimeMillis();
        if (extractViewItem.getFd() == null || bitmap == null) {
            return;
        }
        boolean saveResult = true;
        ParcelFileDescriptor.AutoCloseOutputStream autoCloseInputStream = null;
        ParcelFileDescriptor newPfd = null;
        try {
            try {
                newPfd = extractViewItem.getFd().dup();
                autoCloseInputStream = new ParcelFileDescriptor.AutoCloseOutputStream(newPfd);
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, autoCloseInputStream);
                long duration = System.currentTimeMillis() - time;
                ViewExtractUtils.d(TAG, "saveBitmapByFileDescriptor success duration = " + duration);
                try {
                    autoCloseInputStream.close();
                    newPfd.close();
                } catch (IOException e) {
                    ViewExtractUtils.e(TAG, "Failed to saveBitmapByFileDescriptor " + e.getMessage());
                    saveResult = false;
                }
            } catch (IOException e2) {
                saveResult = false;
                ViewExtractUtils.e(TAG, "Failed to saveBitmapByFileDescriptor " + e2.getMessage());
                if (autoCloseInputStream != null) {
                    try {
                        autoCloseInputStream.close();
                        newPfd.close();
                    } catch (IOException e3) {
                        ViewExtractUtils.e(TAG, "Failed to saveBitmapByFileDescriptor " + e3.getMessage());
                        saveResult = false;
                    }
                }
            }
            bitmap.recycle();
            extractViewItem.getViewExtractListener().onSaveBitmapResult(saveResult);
        } catch (Throwable e4) {
            if (autoCloseInputStream != null) {
                try {
                    autoCloseInputStream.close();
                    newPfd.close();
                } catch (IOException e5) {
                    ViewExtractUtils.e(TAG, "Failed to saveBitmapByFileDescriptor " + e5.getMessage());
                    saveResult = false;
                }
            }
            bitmap.recycle();
            extractViewItem.getViewExtractListener().onSaveBitmapResult(saveResult);
            throw e4;
        }
    }

    public Bitmap extractNormalViewBySnapshot(ExtractViewItem extractViewItem) {
        long time = System.currentTimeMillis();
        ViewExtractUtils.beginTrace("extractNormalViewBySnapshot");
        Bitmap bitmap = null;
        try {
            bitmap = extractViewItem.getView().createSnapshot(extractViewItem.getView().isHardwareAccelerated() ? new ViewExtractCanvasProvider(extractViewItem) : new ViewDebug.SoftwareCanvasProvider(), false);
        } catch (Exception e) {
            extractViewItem.getViewExtractListener().onError(e.toString());
            ViewExtractUtils.d(TAG, "extractNormalViewBySnapshot" + e);
        }
        ViewExtractUtils.endTrace();
        ViewExtractUtils.d(TAG, "extractNormalViewBySnapshot: createSnapshot" + extractViewItem.getView() + " duration=" + (System.currentTimeMillis() - time));
        return bitmap;
    }

    private void extractSurfaceView(SurfaceView surfaceView, final ExtractViewItem extractViewItem) {
        try {
            final Bitmap bitmap = Bitmap.createBitmap(surfaceView.getWidth(), surfaceView.getHeight(), Bitmap.Config.RGB_565);
            PixelCopy.request(surfaceView, bitmap, new PixelCopy.OnPixelCopyFinishedListener() { // from class: android.view.viewextract.OplusViewExtractManager$$ExternalSyntheticLambda0
                @Override // android.view.PixelCopy.OnPixelCopyFinishedListener
                public final void onPixelCopyFinished(int i) {
                    OplusViewExtractManager.saveBitmapByFileDescriptor(bitmap, extractViewItem);
                }
            }, Handler.getMain());
        } catch (IllegalArgumentException illegalArgumentException) {
            ViewExtractUtils.d(TAG, "extractSurfaceView: " + illegalArgumentException);
        }
    }

    private Bitmap extractTextureView(TextureView textureView) {
        return textureView.getBitmap();
    }

    public void extractViewInfo(final View view, final ExtractViewItem viewItem) {
        long time = System.currentTimeMillis();
        if (view instanceof SurfaceView) {
            extractSurfaceView((SurfaceView) view, viewItem);
        } else if (view instanceof TextureView) {
            this.mExecutor.execute(new Runnable() { // from class: android.view.viewextract.OplusViewExtractManager$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    OplusViewExtractManager.this.lambda$extractViewInfo$1(view, viewItem);
                }
            });
        } else {
            this.mExecutor.execute(new Runnable() { // from class: android.view.viewextract.OplusViewExtractManager$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    OplusViewExtractManager.this.lambda$extractViewInfo$2(viewItem);
                }
            });
        }
        Rect rect = new Rect();
        view.getBoundsOnScreen(rect);
        ViewExtractUtils.d(TAG, "extractViewInfo: " + view.getClass().getName() + " " + rect + " duration=" + (System.currentTimeMillis() - time));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$extractViewInfo$1(View view, ExtractViewItem viewItem) {
        saveBitmapByFileDescriptor(extractTextureView((TextureView) view), viewItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$extractViewInfo$2(ExtractViewItem viewItem) {
        saveBitmapByFileDescriptor(extractNormalViewBySnapshot(viewItem), viewItem);
    }

    @Override // android.view.viewextract.IOplusViewExtractManager
    public boolean isViewExtract(int flag) {
        return (268435456 & flag) != 0;
    }

    @Override // android.view.viewextract.IOplusViewExtractManager
    public int addViewExtractFlag(int flag, int viewFlags) {
        if (isViewExtract(flag)) {
            int newFlag = viewFlags | 268435456;
            ViewExtractUtils.d(TAG, "addViewExtractFlag: " + Integer.toHexString(newFlag));
            return newFlag;
        }
        return viewFlags;
    }

    @Override // android.view.viewextract.IOplusViewExtractManager
    public void appendViewExtractInfo(View view, ViewStructure structure) {
        if (view instanceof SurfaceView) {
            structure.setClassName(SurfaceView.class.getName());
        }
        if (view instanceof TextureView) {
            structure.setClassName(TextureView.class.getName());
        }
        Rect temp = new Rect();
        view.getBoundsOnScreen(temp);
        structure.getExtras().putParcelable(ViewExtractProxy.ExtraInfo.GET_BOUNDS_ON_SCREEN, temp);
        structure.getExtras().putBoolean(ViewExtractProxy.ExtraInfo.IS_VISIBLE_TO_USER, view.isVisibleToUser());
        structure.getExtras().putString(ViewExtractProxy.ExtraInfo.EXTRA_CLASS_NAME, view.getClass().getName());
        ViewExtractUtils.i(TAG, "appendViewExtractInfo: " + view.getClass().getName() + " " + temp);
    }

    @Override // android.view.viewextract.IOplusViewExtractManager
    public void extractViewBundle(final Bundle bundle, ActivityThread activityThread, IBinder iBinder) {
        if (ViewExtractProxy.Feature.get(bundle) == 3) {
            final ArrayList<Integer> ids = bundle.getIntegerArrayList(ViewExtractProxy.Constant.VIEW_IDS);
            ArrayList<ParcelFileDescriptor> parcelFileDescriptors = bundle.getParcelableArrayList(ViewExtractProxy.Constant.FILE_DESCRIPTORS, ParcelFileDescriptor.class);
            if (ids != null && parcelFileDescriptors != null) {
                if (ids.size() == parcelFileDescriptors.size()) {
                    Activity r = activityThread.getActivity(iBinder);
                    Bundle resultBundle = new Bundle();
                    for (Iterator<Integer> it = ids.iterator(); it.hasNext(); it = it) {
                        final int id = it.next().intValue();
                        final ViewExtractProxy.ViewExtractResult viewExtractResult = new ViewExtractProxy.ViewExtractResult();
                        View view = findView(r, id);
                        if (view == null) {
                            ViewExtractUtils.e(TAG, "view is null id =" + id);
                            viewExtractResult.setError("no view");
                            viewExtractResult.setResult(false);
                            syncPut(resultBundle, id, viewExtractResult);
                            decideFinish(bundle, ids, resultBundle);
                            return;
                        }
                        ParcelFileDescriptor parcelFileDescriptor = parcelFileDescriptors.get(ids.indexOf(Integer.valueOf(id)));
                        final Bundle bundle2 = resultBundle;
                        ExtractViewItem viewItem = new ExtractViewItem(view, id, parcelFileDescriptor, new IOplusViewExtractManager.ViewExtractListener() { // from class: android.view.viewextract.OplusViewExtractManager.1
                            @Override // android.view.viewextract.IOplusViewExtractManager.ViewExtractListener
                            public void onSaveBitmapResult(boolean result) {
                                viewExtractResult.setResult(Boolean.valueOf(result));
                                OplusViewExtractManager.this.syncPut(bundle2, id, viewExtractResult);
                                OplusViewExtractManager.this.decideFinish(bundle, ids, bundle2);
                            }

                            @Override // android.view.viewextract.IOplusViewExtractManager.ViewExtractListener
                            public void onExtractView(String text, Bitmap bitmap) {
                                viewExtractResult.setText(text);
                            }

                            @Override // android.view.viewextract.IOplusViewExtractManager.ViewExtractListener
                            public void onError(String error) {
                                viewExtractResult.setError(error);
                            }
                        });
                        extractViewInfo(view, viewItem);
                        parcelFileDescriptors = parcelFileDescriptors;
                        r = r;
                        resultBundle = resultBundle;
                    }
                    return;
                }
            }
            ViewExtractUtils.e(TAG, "ids not equal pfds");
            bundle.putInt("error", 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void syncPut(Bundle resultBundle, int id, ViewExtractProxy.ViewExtractResult viewExtractResult) {
        resultBundle.putParcelable(Integer.toString(id), viewExtractResult);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void decideFinish(Bundle bundle, ArrayList<Integer> ids, Bundle resultBundle) {
        if (resultBundle.size() == ids.size()) {
            bundle.putBundle(ViewExtractProxy.Constant.RESULTS, resultBundle);
            reportViewExtractResult(bundle);
        }
    }

    public View findView(Activity activity, int viewId) {
        View view;
        if (activity == null) {
            return null;
        }
        ArrayList<ViewRootImpl> roots = WindowManagerGlobal.getInstance().getRootViews(activity.getActivityToken());
        for (int rootNum = 0; rootNum < roots.size(); rootNum++) {
            View rootView = roots.get(rootNum).getView();
            if (rootView != null && (view = rootView.findViewByAutofillIdTraversal(viewId)) != null) {
                return view;
            }
        }
        return null;
    }

    @Override // android.view.viewextract.IOplusViewExtractManager
    public void reportViewExtractResult(Bundle bundle) {
        try {
            OplusActivityTaskManager.getInstance().reportViewExtractResult(bundle);
        } catch (RemoteException e) {
            ViewExtractUtils.e(TAG, "reportViewExtractResult remoteException ");
        }
    }

    @Override // android.view.viewextract.IOplusViewExtractManager
    public void requestViewExtractData(IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, int flags) throws RemoteException {
        OplusActivityTaskManager.getInstance().requestViewExtractData(receiver, receiverExtras, activityToken, flags);
    }
}
