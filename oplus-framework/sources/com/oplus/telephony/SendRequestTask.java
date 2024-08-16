package com.oplus.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.WorkSource;
import android.telephony.Rlog;

/* loaded from: classes.dex */
public class SendRequestTask {
    private static final String LOG_TAG = "SendRequestTask";
    private Context mContext;
    private Handler mMainThreadHandler;

    public SendRequestTask(Context context, Handler handler) {
        this.mContext = context;
        this.mMainThreadHandler = handler;
    }

    public Object sendRequest(int command, Object argument) {
        return sendRequest(command, argument, -1, null, -1L);
    }

    public Object sendRequest(int command, Object argument, WorkSource workSource) {
        return sendRequest(command, argument, -1, workSource, -1L);
    }

    public Object sendRequest(int command, Object argument, Integer slotId) {
        return sendRequest(command, argument, slotId, null, -1L);
    }

    public Object sendRequest(int command, Object argument, Integer slotId, long timeoutInMs) {
        return sendRequest(command, argument, slotId, null, timeoutInMs);
    }

    public Object sendRequest(int command, Object argument, int slotId, WorkSource workSource) {
        return sendRequest(command, argument, Integer.valueOf(slotId), workSource, -1L);
    }

    public Object sendRequest(int command, Object argument, Integer slotId, WorkSource workSource, long timeoutInMs) {
        if (Looper.myLooper() == this.mMainThreadHandler.getLooper()) {
            throw new RuntimeException("This method will deadlock if called from the main thread.");
        }
        MainThreadRequest request = new MainThreadRequest(argument, slotId, workSource);
        Message msg = this.mMainThreadHandler.obtainMessage(command, request);
        msg.sendToTarget();
        synchronized (request) {
            if (timeoutInMs >= 0) {
                long now = SystemClock.elapsedRealtime();
                long deadline = now + timeoutInMs;
                while (request.mResult == null && now < deadline) {
                    try {
                        request.wait(deadline - now);
                    } catch (InterruptedException e) {
                    } catch (Throwable th) {
                        SystemClock.elapsedRealtime();
                        throw th;
                    }
                    now = SystemClock.elapsedRealtime();
                }
            } else {
                while (request.mResult == null) {
                    try {
                        request.wait();
                    } catch (InterruptedException e2) {
                    }
                }
            }
        }
        if (request.mResult == null) {
            Rlog.e(LOG_TAG, "sendRequest: result is null, time out.");
        }
        return request.mResult;
    }

    public void sendRequestAsync(int command) {
        this.mMainThreadHandler.sendEmptyMessage(command);
    }

    public void sendRequestAsync(int command, Object argument) {
        sendRequestAsync(command, argument, -1, null);
    }

    public void sendRequestAsync(int command, Object argument, Integer slotId, WorkSource workSource) {
        MainThreadRequest request = new MainThreadRequest(argument, slotId, workSource);
        Message msg = this.mMainThreadHandler.obtainMessage(command, request);
        msg.sendToTarget();
    }

    public void notifyRequester(MainThreadRequest request) {
        synchronized (request) {
            request.notifyAll();
        }
    }

    public void handleNullReturnEvent(Message msg, String command) {
        AsyncResult ar = (AsyncResult) msg.obj;
        MainThreadRequest request = (MainThreadRequest) ar.userObj;
        if (ar.exception == null) {
            request.mResult = true;
        } else {
            request.mResult = false;
            Rlog.e(LOG_TAG, command + ": exception ->" + ar.exception);
        }
        notifyRequester(request);
    }

    public int getSlotIdFromRequest(Object request) {
        if (request instanceof MainThreadRequest) {
            return ((MainThreadRequest) request).mSlotId.intValue();
        }
        return -1;
    }

    public WorkSource getWorkSource() {
        int uid = Binder.getCallingUid();
        String packageName = this.mContext.getPackageManager().getNameForUid(uid);
        return new WorkSource(uid, packageName);
    }

    public Object getArgumentFromRequest(Object request) {
        if (request instanceof MainThreadRequest) {
            return ((MainThreadRequest) request).mArgument;
        }
        return null;
    }

    public WorkSource getWorkSourceFromRequest(Object request) {
        if (request instanceof MainThreadRequest) {
            return ((MainThreadRequest) request).mWorkSource;
        }
        return null;
    }

    /* loaded from: classes.dex */
    public static final class MainThreadRequest {
        public Object mArgument;
        public Object mResult;
        public Integer mSlotId;
        public WorkSource mWorkSource;

        public MainThreadRequest(Object argument) {
            this.mSlotId = -1;
            this.mArgument = argument;
        }

        MainThreadRequest(Object argument, Integer slotId, WorkSource workSource) {
            this.mSlotId = -1;
            this.mArgument = argument;
            if (slotId != null) {
                this.mSlotId = slotId;
            }
            this.mWorkSource = workSource;
        }
    }
}
