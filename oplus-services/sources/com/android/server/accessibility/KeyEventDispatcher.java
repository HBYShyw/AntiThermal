package com.android.server.accessibility;

import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.ArrayMap;
import android.util.Pools;
import android.util.Slog;
import android.view.InputEventConsistencyVerifier;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class KeyEventDispatcher implements Handler.Callback {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "KeyEventDispatcher";
    private static final int MAX_POOL_SIZE = 10;
    public static final int MSG_ON_KEY_EVENT_TIMEOUT = 1;
    private static final long ON_KEY_EVENT_TIMEOUT_MILLIS = 500;
    private final Handler mHandlerToSendKeyEventsToInputFilter;
    private Handler mKeyEventTimeoutHandler;
    private final Object mLock;
    private final int mMessageTypeForSendKeyEvent;
    private final Pools.Pool<PendingKeyEvent> mPendingEventPool;
    private final Map<KeyEventFilter, ArrayList<PendingKeyEvent>> mPendingEventsMap;
    private final PowerManager mPowerManager;
    private final InputEventConsistencyVerifier mSentEventsVerifier;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface KeyEventFilter {
        boolean onKeyEvent(KeyEvent keyEvent, int i);
    }

    public KeyEventDispatcher(Handler handler, int i, Object obj, PowerManager powerManager) {
        this.mPendingEventPool = new Pools.SimplePool(10);
        this.mPendingEventsMap = new ArrayMap();
        if (InputEventConsistencyVerifier.isInstrumentationEnabled()) {
            this.mSentEventsVerifier = new InputEventConsistencyVerifier(this, 0, KeyEventDispatcher.class.getSimpleName());
        } else {
            this.mSentEventsVerifier = null;
        }
        this.mHandlerToSendKeyEventsToInputFilter = handler;
        this.mMessageTypeForSendKeyEvent = i;
        this.mKeyEventTimeoutHandler = new Handler(handler.getLooper(), this);
        this.mLock = obj;
        this.mPowerManager = powerManager;
    }

    public KeyEventDispatcher(Handler handler, int i, Object obj, PowerManager powerManager, Handler handler2) {
        this(handler, i, obj, powerManager);
        this.mKeyEventTimeoutHandler = handler2;
    }

    public boolean notifyKeyEventLocked(KeyEvent keyEvent, int i, List<? extends KeyEventFilter> list) {
        KeyEvent obtain = KeyEvent.obtain(keyEvent);
        PendingKeyEvent pendingKeyEvent = null;
        for (int i2 = 0; i2 < list.size(); i2++) {
            KeyEventFilter keyEventFilter = list.get(i2);
            if (keyEventFilter.onKeyEvent(obtain, obtain.getSequenceNumber())) {
                if (pendingKeyEvent == null) {
                    pendingKeyEvent = obtainPendingEventLocked(obtain, i);
                }
                ArrayList<PendingKeyEvent> arrayList = this.mPendingEventsMap.get(keyEventFilter);
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                    this.mPendingEventsMap.put(keyEventFilter, arrayList);
                }
                arrayList.add(pendingKeyEvent);
                pendingKeyEvent.referenceCount++;
            }
        }
        if (pendingKeyEvent == null) {
            obtain.recycle();
            return false;
        }
        this.mKeyEventTimeoutHandler.sendMessageDelayed(this.mKeyEventTimeoutHandler.obtainMessage(1, pendingKeyEvent), ON_KEY_EVENT_TIMEOUT_MILLIS);
        return true;
    }

    public void setOnKeyEventResult(KeyEventFilter keyEventFilter, boolean z, int i) {
        synchronized (this.mLock) {
            PendingKeyEvent removeEventFromListLocked = removeEventFromListLocked(this.mPendingEventsMap.get(keyEventFilter), i);
            if (removeEventFromListLocked != null) {
                if (z && !removeEventFromListLocked.handled) {
                    removeEventFromListLocked.handled = z;
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        this.mPowerManager.userActivity(removeEventFromListLocked.event.getEventTime(), 3, 0);
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    } catch (Throwable th) {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        throw th;
                    }
                }
                removeReferenceToPendingEventLocked(removeEventFromListLocked);
            }
        }
    }

    public void flush(KeyEventFilter keyEventFilter) {
        synchronized (this.mLock) {
            ArrayList<PendingKeyEvent> arrayList = this.mPendingEventsMap.get(keyEventFilter);
            if (arrayList != null) {
                for (int i = 0; i < arrayList.size(); i++) {
                    removeReferenceToPendingEventLocked(arrayList.get(i));
                }
                this.mPendingEventsMap.remove(keyEventFilter);
            }
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message.what != 1) {
            Slog.w(LOG_TAG, "Unknown message: " + message.what);
            return false;
        }
        PendingKeyEvent pendingKeyEvent = (PendingKeyEvent) message.obj;
        synchronized (this.mLock) {
            Iterator<ArrayList<PendingKeyEvent>> it = this.mPendingEventsMap.values().iterator();
            while (it.hasNext() && (!it.next().remove(pendingKeyEvent) || !removeReferenceToPendingEventLocked(pendingKeyEvent))) {
            }
        }
        return true;
    }

    private PendingKeyEvent obtainPendingEventLocked(KeyEvent keyEvent, int i) {
        PendingKeyEvent pendingKeyEvent = (PendingKeyEvent) this.mPendingEventPool.acquire();
        if (pendingKeyEvent == null) {
            pendingKeyEvent = new PendingKeyEvent();
        }
        pendingKeyEvent.event = keyEvent;
        pendingKeyEvent.policyFlags = i;
        pendingKeyEvent.referenceCount = 0;
        pendingKeyEvent.handled = false;
        return pendingKeyEvent;
    }

    private static PendingKeyEvent removeEventFromListLocked(List<PendingKeyEvent> list, int i) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            PendingKeyEvent pendingKeyEvent = list.get(i2);
            if (pendingKeyEvent.event.getSequenceNumber() == i) {
                list.remove(pendingKeyEvent);
                return pendingKeyEvent;
            }
        }
        return null;
    }

    private boolean removeReferenceToPendingEventLocked(PendingKeyEvent pendingKeyEvent) {
        int i = pendingKeyEvent.referenceCount - 1;
        pendingKeyEvent.referenceCount = i;
        if (i > 0) {
            return false;
        }
        this.mKeyEventTimeoutHandler.removeMessages(1, pendingKeyEvent);
        if (!pendingKeyEvent.handled) {
            InputEventConsistencyVerifier inputEventConsistencyVerifier = this.mSentEventsVerifier;
            if (inputEventConsistencyVerifier != null) {
                inputEventConsistencyVerifier.onKeyEvent(pendingKeyEvent.event, 0);
            }
            this.mHandlerToSendKeyEventsToInputFilter.obtainMessage(this.mMessageTypeForSendKeyEvent, pendingKeyEvent.policyFlags | 1073741824, 0, pendingKeyEvent.event).sendToTarget();
        } else {
            pendingKeyEvent.event.recycle();
        }
        this.mPendingEventPool.release(pendingKeyEvent);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class PendingKeyEvent {
        KeyEvent event;
        boolean handled;
        int policyFlags;
        int referenceCount;

        private PendingKeyEvent() {
        }
    }
}
