package com.android.server.display;

import android.content.Context;
import android.os.Handler;
import android.view.Display;
import com.android.server.display.DisplayManagerService;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class DisplayAdapter {
    public static final int DISPLAY_DEVICE_EVENT_ADDED = 1;
    public static final int DISPLAY_DEVICE_EVENT_CHANGED = 2;
    public static final int DISPLAY_DEVICE_EVENT_REMOVED = 3;
    private static final AtomicInteger NEXT_DISPLAY_MODE_ID = new AtomicInteger(1);
    private final Context mContext;
    public IDisplayAdapterExt mDisplayAdapterExt;
    private final Handler mHandler;
    private final Listener mListener;
    private final String mName;
    private final DisplayManagerService.SyncRoot mSyncRoot;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Listener {
        void onDisplayDeviceEvent(DisplayDevice displayDevice, int i);

        void onDisplayDeviceEvent(DisplayDevice displayDevice, int i, long j);

        void onTraversalRequested();
    }

    public void dumpLocked(PrintWriter printWriter) {
    }

    public void registerLocked() {
    }

    public DisplayAdapter(DisplayManagerService.SyncRoot syncRoot, Context context, Handler handler, Listener listener, String str) {
        IDisplayAdapterExt iDisplayAdapterExt = (IDisplayAdapterExt) ExtLoader.type(IDisplayAdapterExt.class).base(this).create();
        this.mDisplayAdapterExt = iDisplayAdapterExt;
        this.mSyncRoot = syncRoot;
        this.mContext = context;
        this.mHandler = handler;
        this.mListener = listener;
        this.mName = str;
        iDisplayAdapterExt.setDisplayHandler(handler);
        this.mDisplayAdapterExt.setListener(listener);
    }

    public final DisplayManagerService.SyncRoot getSyncRoot() {
        return this.mSyncRoot;
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final Handler getHandler() {
        return this.mHandler;
    }

    public final String getName() {
        return this.mName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void sendDisplayDeviceEventLocked(DisplayDevice displayDevice, int i) {
        this.mDisplayAdapterExt.sendDisplayDeviceEventLocked(displayDevice, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendTraversalRequestLocked$0() {
        this.mListener.onTraversalRequested();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void sendTraversalRequestLocked() {
        this.mHandler.post(new Runnable() { // from class: com.android.server.display.DisplayAdapter$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DisplayAdapter.this.lambda$sendTraversalRequestLocked$0();
            }
        });
    }

    public static Display.Mode createMode(int i, int i2, float f) {
        return createMode(i, i2, f, new float[0], new int[0]);
    }

    public static Display.Mode createMode(int i, int i2, float f, float[] fArr, int[] iArr) {
        return new Display.Mode(NEXT_DISPLAY_MODE_ID.getAndIncrement(), i, i2, f, fArr, iArr);
    }
}
