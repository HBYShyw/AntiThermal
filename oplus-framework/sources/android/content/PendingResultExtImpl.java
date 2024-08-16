package android.content;

import android.app.ActivityThread;
import android.app.OplusActivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;

/* loaded from: classes.dex */
public class PendingResultExtImpl implements IPendingResultExt {
    int mHasCode;
    boolean mOrderedHint;

    public PendingResultExtImpl(Object base) {
    }

    public void setHascode(int hasCode) {
        this.mHasCode = hasCode;
    }

    public void setOrder(boolean OrderedHint) {
        this.mOrderedHint = OrderedHint;
    }

    public boolean getOrder() {
        return this.mOrderedHint;
    }

    public void setBroadcastState(int flag, int state) {
        if ((524288 & flag) != 0) {
            if ((flag & 268435456) != 0) {
                if (ActivityThread.DEBUG_BROADCAST) {
                    Slog.v("ActivityThread", "mOplusFgBrState " + state);
                }
                ActivityThread.mOplusFgBrState = state;
                return;
            } else {
                if (ActivityThread.DEBUG_BROADCAST) {
                    Slog.v("ActivityThread", "mOplusBgBrState " + state);
                }
                ActivityThread.mOplusBgBrState = state;
                return;
            }
        }
        if ((flag & 268435456) != 0) {
            if (ActivityThread.DEBUG_BROADCAST) {
                Slog.v("ActivityThread", "mFgBrState " + state);
            }
            ActivityThread.mFgBrState = state;
        } else {
            if (ActivityThread.DEBUG_BROADCAST) {
                Slog.v("ActivityThread", "mBgBrState " + state);
            }
            ActivityThread.mBgBrState = state;
        }
    }

    public void finishNotOrderReceiver(IBinder who, int resultCode, String resultData, Bundle resultExtras, boolean resultAbort) {
        try {
            new OplusActivityManager().finishNotOrderReceiver(who, this.mHasCode, resultCode, resultData, resultExtras, resultAbort);
        } catch (RemoteException ex) {
            Log.e("ActivityThread", "sendFinished RemoteException:", ex);
        }
    }
}
