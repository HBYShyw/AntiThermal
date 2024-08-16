package com.oplus.gwsd;

import android.telephony.Rlog;
import com.oplus.gwsd.IGwsdListener;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class GwsdListener {
    public IGwsdListener callback = new IGwsdListenerStub(this);

    public void onAddListenered(int status, String reason) {
    }

    public void onUserSelectionModeChanged(int status, String reason) {
    }

    public void onAutoRejectModeChanged(int status, String reason) {
    }

    public void onCallValidTimerChanged(int status, String reason) {
    }

    public void onIgnoreSameNumberIntervalChanged(int status, String reason) {
    }

    public void onSyncGwsdInfoFinished(int status, String reason) {
    }

    public void onSystemStateChanged(int state) {
    }

    /* loaded from: classes.dex */
    private static class IGwsdListenerStub extends IGwsdListener.Stub {
        private String TAG = "IGwsdListenerStub";
        private WeakReference<GwsdListener> mGwsdListenerWeakRef;

        public IGwsdListenerStub(GwsdListener gwsdListener) {
            this.mGwsdListenerWeakRef = new WeakReference<>(gwsdListener);
        }

        @Override // com.oplus.gwsd.IGwsdListener
        public void onAddListenered(int status, String reason) {
            Rlog.d(this.TAG, "onAddListenered, status: " + status + " reason: " + reason);
            GwsdListener listener = this.mGwsdListenerWeakRef.get();
            if (listener != null) {
                listener.onAddListenered(status, reason);
            }
        }

        @Override // com.oplus.gwsd.IGwsdListener
        public void onUserSelectionModeChanged(int status, String reason) {
            Rlog.d(this.TAG, "onUserSelectionModeChanged, status: " + status + " reason: " + reason);
            GwsdListener listener = this.mGwsdListenerWeakRef.get();
            if (listener != null) {
                listener.onUserSelectionModeChanged(status, reason);
            }
        }

        @Override // com.oplus.gwsd.IGwsdListener
        public void onAutoRejectModeChanged(int status, String reason) {
            Rlog.d(this.TAG, "onAutoRejectModeChanged, status: " + status + " reason: " + reason);
            GwsdListener listener = this.mGwsdListenerWeakRef.get();
            if (listener != null) {
                listener.onAutoRejectModeChanged(status, reason);
            }
        }

        @Override // com.oplus.gwsd.IGwsdListener
        public void onSyncGwsdInfoFinished(int status, String reason) {
            Rlog.d(this.TAG, "onSyncGwsdInfoFinished, status: " + status + " reason: " + reason);
            GwsdListener listener = this.mGwsdListenerWeakRef.get();
            if (listener != null) {
                listener.onSyncGwsdInfoFinished(status, reason);
            }
        }

        @Override // com.oplus.gwsd.IGwsdListener
        public void onSystemStateChanged(int state) {
            Rlog.d(this.TAG, "onSystemStateChanged, state: " + state);
            GwsdListener listener = this.mGwsdListenerWeakRef.get();
            if (listener != null) {
                listener.onSystemStateChanged(state);
            }
        }

        @Override // com.oplus.gwsd.IGwsdListener
        public void onCallValidTimerChanged(int status, String reason) {
            Rlog.d(this.TAG, "onCallValidTimerChanged, status: " + status + " reason: " + reason);
            GwsdListener listener = this.mGwsdListenerWeakRef.get();
            if (listener != null) {
                listener.onCallValidTimerChanged(status, reason);
            }
        }

        @Override // com.oplus.gwsd.IGwsdListener
        public void onIgnoreSameNumberIntervalChanged(int status, String reason) {
            Rlog.d(this.TAG, "onIgnoreSameNumberIntervalChanged, status: " + status + " reason: " + reason);
            GwsdListener listener = this.mGwsdListenerWeakRef.get();
            if (listener != null) {
                listener.onIgnoreSameNumberIntervalChanged(status, reason);
            }
        }
    }
}
