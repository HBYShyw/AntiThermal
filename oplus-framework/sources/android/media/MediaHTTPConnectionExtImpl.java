package android.media;

import android.app.ActivityThread;
import android.os.StrictMode;
import android.util.Log;
import com.oplus.atlas.OplusAtlasManager;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.UnknownServiceException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class MediaHTTPConnectionExtImpl implements IMediaHTTPConnectionExt {
    private static final int MEDIA_ERROR_HTTP_PROTOCOL_ERROR = -214741;
    private static final String TAG = "MediaHTTPConnectionExtImpl";
    private static final String THREAD_NAME = "MediaHTTPConn";
    private static final boolean VERBOSE = false;
    private static final int WAIT_TIMEOUT_MS_OPLUS = 8000;
    private MediaHTTPConnection mMediaHTTPConnection;
    private boolean mNeedSetTimeout;

    public MediaHTTPConnectionExtImpl(Object mediaHTTPConnection) {
        this.mNeedSetTimeout = false;
        this.mMediaHTTPConnection = (MediaHTTPConnection) mediaHTTPConnection;
        String packageName = ActivityThread.currentPackageName();
        if (packageName != null && packageName.length() > 0) {
            Log.d(TAG, "app " + packageName);
            String value = OplusAtlasManager.getInstance().getAttributeByAppName("media-http", packageName);
            if (value != null) {
                this.mNeedSetTimeout = true;
            }
        }
    }

    public boolean isNeedSetTimeout() {
        Log.d(TAG, "mNeedSetTimeout=" + this.mNeedSetTimeout);
        return this.mNeedSetTimeout;
    }

    public synchronized int readAt(long offset, byte[] data, int size) {
        int ret;
        ret = readAtInternal(offset, data, size, false);
        if (ret == MEDIA_ERROR_HTTP_PROTOCOL_ERROR) {
            Log.w(TAG, "readAt " + offset + " / " + size + " => protocol error, retry");
            ret = readAtInternal(offset, data, size, true);
        }
        if (ret == MEDIA_ERROR_HTTP_PROTOCOL_ERROR) {
            Log.w(TAG, "readAt " + offset + " / " + size + " => error, convert error");
            ret = -1010;
        }
        return ret;
    }

    private synchronized int readAtInternal(long offset, byte[] data, int size, boolean forceSeek) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            try {
                try {
                } catch (UnknownServiceException e) {
                    e = e;
                } catch (IOException e2) {
                }
                try {
                    long lmCurrentOffset = this.mMediaHTTPConnection.getWrapper().getCurrentOffset();
                    if (offset != lmCurrentOffset || forceSeek) {
                        this.mMediaHTTPConnection.getWrapper().seekTo(offset);
                    }
                    int n = -999;
                    if (this.mMediaHTTPConnection.getWrapper().getInputStream() != null) {
                        n = this.mMediaHTTPConnection.getWrapper().getInputStream().read(data, 0, size);
                    }
                    if (n == -1) {
                        n = 0;
                    }
                    this.mMediaHTTPConnection.getWrapper().setCurrentOffset(lmCurrentOffset + n);
                    return n;
                } catch (UnknownServiceException e3) {
                    e = e3;
                    Log.w(TAG, "readAt " + offset + " / " + size + " => " + e);
                    return -1010;
                } catch (IOException e4) {
                    return -1;
                }
            } catch (ProtocolException e5) {
                Log.w(TAG, "readAt " + offset + " / " + size + " => " + e5);
                return MEDIA_ERROR_HTTP_PROTOCOL_ERROR;
            } catch (Exception e6) {
                return -1;
            }
        } catch (NoRouteToHostException e7) {
            Log.w(TAG, "readAt " + offset + " / " + size + " => " + e7);
            return -1010;
        }
    }

    public boolean asyncSeekTo(final long offset) {
        Log.d(TAG, "asyncSeekTo++");
        NamedThreadFactory namedThreadFactory = new NamedThreadFactory(THREAD_NAME);
        ExecutorService exec = Executors.newFixedThreadPool(1, namedThreadFactory);
        boolean ret = false;
        Callable<Boolean> call = new Callable<Boolean>() { // from class: android.media.MediaHTTPConnectionExtImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                MediaHTTPConnectionExtImpl.this.mMediaHTTPConnection.getWrapper().seekTo(offset);
                Log.d(MediaHTTPConnectionExtImpl.TAG, "asyncSeekTo-- return true");
                return true;
            }
        };
        try {
            Future<Boolean> future = exec.submit(call);
            ret = future.get(8000L, TimeUnit.MILLISECONDS).booleanValue();
            Log.i(TAG, "asyncSeekTo " + ret);
        } catch (TimeoutException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        exec.shutdown();
        Log.d(TAG, "asyncSeekTo-- return ret=" + ret);
        return ret;
    }

    /* loaded from: classes.dex */
    public class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger mCount = new AtomicInteger(0);
        private final ThreadFactory mDefaultThreadFactory = Executors.defaultThreadFactory();
        private final String mPrefix;

        public NamedThreadFactory(String prefix) {
            this.mPrefix = prefix + "-";
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = this.mDefaultThreadFactory.newThread(runnable);
            thread.setName(this.mPrefix + this.mCount.getAndIncrement());
            return thread;
        }

        public boolean namedWithPrefix(Thread thread) {
            return thread.getName().startsWith(this.mPrefix);
        }
    }
}
