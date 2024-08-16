package android.view;

/* loaded from: classes.dex */
public class SurfaceExtImpl implements ISurfaceExt {
    private static volatile SurfaceExtImpl sInstance;
    private Object mLock;
    private long mNativeObject;
    private Surface mSurface;

    public native void nativeSetMaxDequeuedBufferCount(long j, int i);

    public SurfaceExtImpl(Object base) {
        Surface surface = (Surface) base;
        this.mSurface = surface;
        this.mLock = surface.getWrapper().getLock();
    }

    static {
        System.loadLibrary("oplusgui_jni");
    }

    public void setMaxDequeuedBufferCount(int bufferCount) {
        synchronized (this.mLock) {
            long nativeObject = this.mSurface.getWrapper().getNativeObject();
            this.mNativeObject = nativeObject;
            if (nativeObject != 0) {
                nativeSetMaxDequeuedBufferCount(nativeObject, bufferCount);
            }
        }
    }
}
