package com.android.server.wm;

import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.MotionEvent;
import android.view.WindowManagerPolicyConstants;
import com.android.server.UiThread;
import java.util.ArrayList;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class PointerEventDispatcher extends InputEventReceiver {
    private final ArrayList<WindowManagerPolicyConstants.PointerEventListener> mListeners;
    private WindowManagerPolicyConstants.PointerEventListener[] mListenersArray;
    private IPointerEventDispatcherExt mPointerEventDispatcherExt;

    public PointerEventDispatcher(InputChannel inputChannel) {
        super(inputChannel, ((IPointerEventDispatcherExt) ExtLoader.type(IPointerEventDispatcherExt.class).create()).getOptLooper(UiThread.getHandler().getLooper()));
        this.mListeners = new ArrayList<>();
        this.mListenersArray = new WindowManagerPolicyConstants.PointerEventListener[0];
        this.mPointerEventDispatcherExt = (IPointerEventDispatcherExt) ExtLoader.type(IPointerEventDispatcherExt.class).base(this).create();
    }

    public void onInputEvent(InputEvent inputEvent) {
        WindowManagerPolicyConstants.PointerEventListener[] pointerEventListenerArr;
        try {
            if ((inputEvent instanceof MotionEvent) && (inputEvent.getSource() & 2) != 0) {
                MotionEvent motionEvent = (MotionEvent) inputEvent;
                synchronized (this.mListeners) {
                    if (this.mListenersArray == null) {
                        WindowManagerPolicyConstants.PointerEventListener[] pointerEventListenerArr2 = new WindowManagerPolicyConstants.PointerEventListener[this.mListeners.size()];
                        this.mListenersArray = pointerEventListenerArr2;
                        this.mListeners.toArray(pointerEventListenerArr2);
                    }
                    pointerEventListenerArr = this.mListenersArray;
                }
                for (int i = 0; i < pointerEventListenerArr.length; i++) {
                    long currentTimeMillis = System.currentTimeMillis();
                    pointerEventListenerArr[i].onPointerEvent(motionEvent);
                    this.mPointerEventDispatcherExt.debugInputEventDuration(motionEvent, pointerEventListenerArr[i], currentTimeMillis);
                }
            }
        } finally {
            finishInputEvent(inputEvent, false);
        }
    }

    public void registerInputEventListener(WindowManagerPolicyConstants.PointerEventListener pointerEventListener) {
        synchronized (this.mListeners) {
            if (this.mListeners.contains(pointerEventListener)) {
                throw new IllegalStateException("registerInputEventListener: trying to register" + pointerEventListener + " twice.");
            }
            this.mListeners.add(pointerEventListener);
            this.mListenersArray = null;
        }
    }

    public void unregisterInputEventListener(WindowManagerPolicyConstants.PointerEventListener pointerEventListener) {
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(pointerEventListener)) {
                throw new IllegalStateException("registerInputEventListener: " + pointerEventListener + " not registered.");
            }
            this.mListeners.remove(pointerEventListener);
            this.mListenersArray = null;
        }
    }

    public void dispose() {
        super.dispose();
        synchronized (this.mListeners) {
            this.mListeners.clear();
            this.mListenersArray = null;
        }
    }
}
