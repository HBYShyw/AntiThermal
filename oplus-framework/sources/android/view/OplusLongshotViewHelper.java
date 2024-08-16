package android.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import com.oplus.screenshot.IOplusScrollCaptureResponseListener;
import com.oplus.util.OplusLog;
import com.oplus.view.analysis.OplusWindowNode;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

/* loaded from: classes.dex */
public class OplusLongshotViewHelper implements IOplusLongshotViewHelper {
    private static final boolean DBG = true;
    private static final String TAG = "LongshotDump/OplusLongshotViewHelper";
    private final OplusLongshotViewDump mDump;
    private final H mHandler;
    private final WeakReference<ViewRootImpl> mViewAncestor;

    public OplusLongshotViewHelper(WeakReference<ViewRootImpl> viewAncestor) {
        this.mViewAncestor = viewAncestor;
        OplusLongshotViewDump oplusLongshotViewDump = new OplusLongshotViewDump();
        this.mDump = oplusLongshotViewDump;
        this.mHandler = new H(viewAncestor, oplusLongshotViewDump);
    }

    @Override // android.view.IOplusLongshotWindow
    public void longshotNotifyConnected(boolean isConnected) {
        ViewRootImpl viewAncestor = this.mViewAncestor.get();
        if (viewAncestor != null && viewAncestor.mView != null) {
            IViewRootImplExt iViewRootImplExt = viewAncestor.getWrapper().getExtImpl();
            iViewRootImplExt.setConnected(isConnected);
            if (!isConnected) {
                this.mDump.reset();
            }
        }
    }

    @Override // android.view.IOplusLongshotWindow
    public void longshotDump(FileDescriptor fd, List<OplusWindowNode> systemWindows, List<OplusWindowNode> floatWindows, String[] args) {
        try {
            DumpInfoData data = new DumpInfoData(ParcelFileDescriptor.dup(fd), systemWindows, floatWindows, true, args);
            sendMessage(1, data, 0, 0, true);
        } catch (IOException e) {
            OplusLog.e(true, TAG, "longshotDump failed : " + e);
        }
    }

    @Override // android.view.IOplusLongshotWindow
    public OplusWindowNode longshotCollectWindow(boolean isStatusBar, boolean isNavigationBar) {
        ViewRootImpl viewAncestor = this.mViewAncestor.get();
        if (viewAncestor != null && viewAncestor.mView != null) {
            return this.mDump.collectWindow(viewAncestor.mView, isStatusBar, isNavigationBar);
        }
        return null;
    }

    @Override // android.view.IOplusLongshotWindow
    public void longshotInjectInput(InputEvent event, int mode) {
        ViewRootImpl viewAncestor = this.mViewAncestor.get();
        if (viewAncestor != null && viewAncestor.mView != null) {
            viewAncestor.dispatchInputEvent(event, viewAncestor.mInputEventReceiver);
        }
    }

    @Override // android.view.IOplusLongshotWindow
    public void longshotInjectInputBegin() {
        ViewRootImpl viewAncestor = this.mViewAncestor.get();
        if (viewAncestor != null && viewAncestor.mView != null) {
            this.mDump.injectInputBegin();
        }
    }

    @Override // android.view.IOplusLongshotWindow
    public void longshotInjectInputEnd() {
        ViewRootImpl viewAncestor = this.mViewAncestor.get();
        if (viewAncestor != null && viewAncestor.mView != null) {
            this.mDump.injectInputEnd();
        }
    }

    @Override // android.view.IOplusLongshotWindow
    public void screenshotDump(FileDescriptor fd) {
        try {
            DumpInfoData data = new DumpInfoData(ParcelFileDescriptor.dup(fd), null, null, false);
            sendMessage(1, data, 0, 0, true);
        } catch (IOException e) {
            OplusLog.e(true, TAG, "screenshotDump failed : " + e);
        }
    }

    @Override // android.view.IOplusLongshotWindow
    public void requestScrollCapture(IOplusScrollCaptureResponseListener listener, Bundle extras) {
        try {
            sendMessage(2, listener, 0, 0, extras, false);
        } catch (Exception e) {
            OplusLog.e(true, TAG, "requestScrollCapture failed : " + e);
        }
    }

    @Override // android.view.IOplusLongshotViewHelper
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
        List<OplusWindowNode> systemWindows;
        List<OplusWindowNode> floatWindows;
        InputEvent event;
        switch (code) {
            case 10002:
                data.enforceInterface(IOplusWindow.DESCRIPTOR);
                boolean isConnected = 1 == data.readInt();
                longshotNotifyConnected(isConnected);
                return true;
            case 10003:
                data.enforceInterface(IOplusWindow.DESCRIPTOR);
                ParcelFileDescriptor fd = data.readFileDescriptor();
                if (fd != null) {
                    if (1 == data.readInt()) {
                        systemWindows = data.createTypedArrayList(OplusWindowNode.CREATOR);
                    } else {
                        systemWindows = null;
                    }
                    if (1 == data.readInt()) {
                        floatWindows = data.createTypedArrayList(OplusWindowNode.CREATOR);
                    } else {
                        floatWindows = null;
                    }
                    String[] args = data.createStringArray();
                    longshotDump(fd.getFileDescriptor(), systemWindows, floatWindows, args);
                    try {
                        fd.close();
                    } catch (IOException e) {
                    }
                }
                return true;
            case 10004:
                data.enforceInterface(IOplusWindow.DESCRIPTOR);
                boolean isStatusBar = 1 == data.readInt();
                boolean isNavigationBar = 1 == data.readInt();
                OplusWindowNode result = longshotCollectWindow(isStatusBar, isNavigationBar);
                reply.writeNoException();
                if (result != null) {
                    reply.writeInt(1);
                    result.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            case 10005:
                data.enforceInterface(IOplusWindow.DESCRIPTOR);
                if (data.readInt() != 0) {
                    event = (InputEvent) InputEvent.CREATOR.createFromParcel(data);
                } else {
                    event = null;
                }
                int mode = data.readInt();
                longshotInjectInput(event, mode);
                return true;
            case 10006:
                data.enforceInterface(IOplusWindow.DESCRIPTOR);
                longshotInjectInputBegin();
                return true;
            case 10007:
                data.enforceInterface(IOplusWindow.DESCRIPTOR);
                longshotInjectInputEnd();
                return true;
            case 10008:
            default:
                return false;
            case 10009:
                data.enforceInterface(IOplusWindow.DESCRIPTOR);
                ParcelFileDescriptor fd2 = data.readFileDescriptor();
                if (fd2 != null) {
                    screenshotDump(fd2.getFileDescriptor());
                    try {
                        fd2.close();
                    } catch (IOException e2) {
                    }
                }
                return true;
            case 10010:
                data.enforceInterface(IOplusWindow.DESCRIPTOR);
                IOplusScrollCaptureResponseListener listener = IOplusScrollCaptureResponseListener.Stub.asInterface(data.readStrongBinder());
                Bundle extras = null;
                if (data.readInt() != 0) {
                    extras = (Bundle) Bundle.CREATOR.createFromParcel(data);
                }
                requestScrollCapture(listener, extras);
                return true;
        }
    }

    private void sendMessage(int what, Object obj, int arg1, int arg2, boolean async) {
        sendMessage(what, obj, arg1, arg2, null, async);
    }

    private void sendMessage(int what, Object obj, int arg1, int arg2, Bundle data, boolean async) {
        Message msg = this.mHandler.obtainMessage(what);
        msg.what = what;
        msg.obj = obj;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        if (data != null) {
            msg.setData(data);
        }
        if (async) {
            msg.setAsynchronous(true);
        }
        this.mHandler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class DumpInfoData {
        private final String[] mArgs;
        private final List<OplusWindowNode> mFloatWindows;
        private final boolean mIsLongshot;
        private final ParcelFileDescriptor mParcelFileDescriptor;
        private final List<OplusWindowNode> mSystemWindows;

        public DumpInfoData(ParcelFileDescriptor fd, List<OplusWindowNode> systemWindows, List<OplusWindowNode> floatWindows, boolean isLongshot) {
            this(fd, systemWindows, floatWindows, isLongshot, null);
        }

        public DumpInfoData(ParcelFileDescriptor fd, List<OplusWindowNode> systemWindows, List<OplusWindowNode> floatWindows, boolean isLongshot, String[] args) {
            this.mParcelFileDescriptor = fd;
            this.mSystemWindows = systemWindows;
            this.mFloatWindows = floatWindows;
            this.mIsLongshot = isLongshot;
            this.mArgs = args;
        }

        public ParcelFileDescriptor getParcelFileDescriptor() {
            return this.mParcelFileDescriptor;
        }

        public List<OplusWindowNode> getSystemWindows() {
            return this.mSystemWindows;
        }

        public List<OplusWindowNode> getFloatWindows() {
            return this.mFloatWindows;
        }

        public boolean isLongshot() {
            return this.mIsLongshot;
        }

        public String[] getArgs() {
            return this.mArgs;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class H extends Handler {
        public static final int MSG_DUMP_VIEW_HIERARCHY = 1;
        public static final int MSG_REQUEST_SCROLL_CAPTURE = 2;
        private final WeakReference<OplusLongshotViewDump> mDump;
        private final WeakReference<ViewRootImpl> mViewAncestor;

        public H(WeakReference<ViewRootImpl> viewAncestor, OplusLongshotViewDump dump) {
            super(Looper.getMainLooper());
            this.mViewAncestor = viewAncestor;
            this.mDump = new WeakReference<>(dump);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ViewRootImpl viewAncestor = this.mViewAncestor.get();
                    OplusLongshotViewDump dump = this.mDump.get();
                    if (viewAncestor != null && dump != null) {
                        if (viewAncestor.mView != null) {
                            DumpInfoData data = (DumpInfoData) msg.obj;
                            ParcelFileDescriptor fd = data.getParcelFileDescriptor();
                            List<OplusWindowNode> systemWindows = data.getSystemWindows();
                            List<OplusWindowNode> floatWindows = data.getFloatWindows();
                            dump.dumpViewRoot(viewAncestor, fd, systemWindows, floatWindows, data.isLongshot(), data.getArgs());
                            return;
                        }
                        OplusLog.e(true, OplusLongshotViewHelper.TAG, "longshotDump : viewAncestor.mView is null");
                        return;
                    }
                    OplusLog.e(true, OplusLongshotViewHelper.TAG, "longshotDump : viewAncestor is null");
                    return;
                case 2:
                    ViewRootImpl viewAncestor2 = this.mViewAncestor.get();
                    OplusLongshotViewDump dump2 = this.mDump.get();
                    if (dump2 != null) {
                        if (viewAncestor2 != null) {
                            if (viewAncestor2.mView != null) {
                                IOplusScrollCaptureResponseListener listener = (IOplusScrollCaptureResponseListener) msg.obj;
                                Bundle extras = msg.peekData();
                                dump2.requestScrollCapture(viewAncestor2, listener, extras);
                                return;
                            }
                            OplusLog.e(true, OplusLongshotViewHelper.TAG, "requestScrollCapture : viewAncestor.mView is null");
                            return;
                        }
                        OplusLog.e(true, OplusLongshotViewHelper.TAG, "requestScrollCapture : viewAncestor is null");
                        return;
                    }
                    OplusLog.e(true, OplusLongshotViewHelper.TAG, "requestScrollCapture : dump is null");
                    return;
                default:
                    return;
            }
        }
    }
}
