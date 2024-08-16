package com.oplus.epona.internal;

import com.oplus.epona.Call;
import com.oplus.epona.Epona;
import com.oplus.epona.Request;
import com.oplus.epona.Response;
import com.oplus.epona.Route;
import com.oplus.epona.interceptor.CallComponentInterceptor;
import com.oplus.epona.interceptor.CallProviderInterceptor;
import com.oplus.epona.interceptor.LaunchComponentInterceptor;
import com.oplus.epona.utils.Logger;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class RealCall implements Call {
    private static final String TAG = "RealCall";
    private final Request mRequest;
    private final Route mRoute;
    private AtomicBoolean sExecuted = new AtomicBoolean(false);

    /* loaded from: classes.dex */
    public class AsyncCall implements Runnable {
        private final Call.Callback mCallback;

        AsyncCall(Call.Callback callback) {
            this.mCallback = callback;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.Runnable
        public void run() {
            boolean z10 = false;
            z10 = false;
            z10 = false;
            try {
                try {
                    RealCall.this.proceedChain(this.mCallback, true);
                    Route route = RealCall.this.mRoute;
                    route.finished(this, true);
                    z10 = route;
                } catch (Exception e10) {
                    Logger.e(RealCall.TAG, "AsyncCall run failed and exception is %s", e10.toString());
                    this.mCallback.onReceive(Response.defaultErrorResponse());
                    RealCall.this.mRoute.finished(this, false);
                }
            } catch (Throwable th) {
                RealCall.this.mRoute.finished(this, z10);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SyncCallback implements Call.Callback {
        private Response mResponse;

        private SyncCallback() {
            this.mResponse = null;
        }

        public Response getResponse() {
            return this.mResponse;
        }

        @Override // com.oplus.epona.Call.Callback
        public void onReceive(Response response) {
            this.mResponse = response;
        }
    }

    private RealCall(Route route, Request request) {
        this.mRoute = route;
        this.mRequest = request;
    }

    public static RealCall newCall(Route route, Request request) {
        return new RealCall(route, request);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void proceedChain(Call.Callback callback, boolean z10) {
        ArrayList arrayList = new ArrayList(Epona.getInterceptors());
        arrayList.add(new CallComponentInterceptor());
        arrayList.add(new CallProviderInterceptor());
        arrayList.add(new LaunchComponentInterceptor());
        arrayList.add(Epona.getIPCInterceptor());
        new RealInterceptorChain(arrayList, 0, this.mRequest, callback, z10).proceed();
    }

    @Override // com.oplus.epona.Call
    public void asyncExecute(Call.Callback callback) {
        AsyncCall asyncCall = new AsyncCall(callback);
        if (this.sExecuted.getAndSet(true)) {
            Logger.w(TAG, "asyncExecute has been executed", new Object[0]);
            callback.onReceive(Response.defaultErrorResponse());
        }
        this.mRoute.asyncExecute(asyncCall);
    }

    @Override // com.oplus.epona.Call
    public Response execute() {
        Response errorResponse;
        try {
            if (this.sExecuted.getAndSet(true)) {
                Logger.w(TAG, "execute has been executed", new Object[0]);
                return Response.defaultErrorResponse();
            }
            try {
                this.mRoute.executed(this);
                SyncCallback syncCallback = new SyncCallback();
                proceedChain(syncCallback, false);
                errorResponse = syncCallback.getResponse();
            } catch (Exception e10) {
                Logger.e(TAG, "call has exception:" + e10.toString() + ", message:" + e10.getMessage(), new Object[0]);
                errorResponse = Response.errorResponse(e10.getMessage());
            }
            return errorResponse;
        } finally {
            this.mRoute.finished(this);
        }
    }

    @Override // com.oplus.epona.Call
    public Request request() {
        return null;
    }
}
