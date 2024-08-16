package com.oplus.wrapper.view;

import android.graphics.Region;
import android.util.Slog;
import android.view.ViewTreeObserver;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class ViewTreeObserver {
    private final Map<OnComputeInternalInsetsListener, OnComputeInternalInsetsListenerImpl> mInternalInsetsListenerMap = new ConcurrentHashMap();
    private final android.view.ViewTreeObserver mViewTreeObserver;

    /* loaded from: classes.dex */
    public interface OnComputeInternalInsetsListener {
        void onComputeInternalInsets(InternalInsetsInfo internalInsetsInfo);
    }

    public ViewTreeObserver(android.view.ViewTreeObserver viewTreeObserver) {
        this.mViewTreeObserver = viewTreeObserver;
    }

    public void addOnComputeInternalInsetsListener(OnComputeInternalInsetsListener listener) {
        if (listener == null) {
            return;
        }
        OnComputeInternalInsetsListenerImpl onComputeInternalInsetsListener = this.mInternalInsetsListenerMap.get(listener);
        if (onComputeInternalInsetsListener == null) {
            onComputeInternalInsetsListener = new OnComputeInternalInsetsListenerImpl(listener);
            this.mInternalInsetsListenerMap.put(listener, onComputeInternalInsetsListener);
        }
        this.mViewTreeObserver.addOnComputeInternalInsetsListener(onComputeInternalInsetsListener);
    }

    public void removeOnComputeInternalInsetsListener(OnComputeInternalInsetsListener victim) {
        OnComputeInternalInsetsListenerImpl onComputeInternalInsetsListener = this.mInternalInsetsListenerMap.get(victim);
        if (onComputeInternalInsetsListener == null) {
            return;
        }
        this.mViewTreeObserver.removeOnComputeInternalInsetsListener(onComputeInternalInsetsListener);
    }

    /* loaded from: classes.dex */
    public static final class InternalInsetsInfo {
        private final ViewTreeObserver.InternalInsetsInfo mInternalInsetsInfo;
        public static final int TOUCHABLE_INSETS_FRAME = getTouchableInsetsFrame();
        public static final int TOUCHABLE_INSETS_CONTENT = getTouchableInsetsContent();
        public static final int TOUCHABLE_INSETS_VISIBLE = getTouchableInsetsVisible();
        public static final int TOUCHABLE_INSETS_REGION = getTouchableInsetsRegion();

        InternalInsetsInfo(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
            this.mInternalInsetsInfo = internalInsetsInfo;
        }

        public void setTouchableInsets(int val) {
            this.mInternalInsetsInfo.setTouchableInsets(val);
        }

        public Region getTouchableRegion() {
            return this.mInternalInsetsInfo.touchableRegion;
        }

        private static int getTouchableInsetsFrame() {
            return 0;
        }

        private static int getTouchableInsetsContent() {
            return 1;
        }

        private static int getTouchableInsetsVisible() {
            return 2;
        }

        private static int getTouchableInsetsRegion() {
            return 3;
        }
    }

    /* loaded from: classes.dex */
    private static class OnComputeInternalInsetsListenerImpl implements ViewTreeObserver.OnComputeInternalInsetsListener {
        private static final String TAG = "OnComputeInternalInsetsListenerImpl";
        private final OnComputeInternalInsetsListener mOnComputeInternalInsetsListener;

        public OnComputeInternalInsetsListenerImpl(OnComputeInternalInsetsListener onComputeInternalInsetsListener) {
            this.mOnComputeInternalInsetsListener = onComputeInternalInsetsListener;
        }

        public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
            if (internalInsetsInfo == null) {
                Slog.d(TAG, "internalInsetsInfo is null");
            } else {
                this.mOnComputeInternalInsetsListener.onComputeInternalInsets(new InternalInsetsInfo(internalInsetsInfo));
            }
        }
    }
}
