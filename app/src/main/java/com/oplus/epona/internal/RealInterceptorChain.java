package com.oplus.epona.internal;

import com.oplus.epona.Call;
import com.oplus.epona.Interceptor;
import com.oplus.epona.Request;
import com.oplus.epona.Response;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
class RealInterceptorChain implements Interceptor.Chain {
    private final Call.Callback mCallback;
    private final int mIndex;
    private final List<Interceptor> mInterceptors;
    private final boolean mIsAsync;
    private final Request mRequest;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RealInterceptorChain(List<Interceptor> list, int i10, Request request, Call.Callback callback, boolean z10) {
        ArrayList arrayList = new ArrayList();
        this.mInterceptors = arrayList;
        arrayList.addAll(list);
        this.mIndex = i10;
        this.mRequest = request;
        this.mCallback = callback;
        this.mIsAsync = z10;
    }

    private RealInterceptorChain fork(int i10) {
        return new RealInterceptorChain(this.mInterceptors, i10, this.mRequest, this.mCallback, this.mIsAsync);
    }

    @Override // com.oplus.epona.Interceptor.Chain
    public Call.Callback callback() {
        return this.mCallback;
    }

    @Override // com.oplus.epona.Interceptor.Chain
    public boolean isAsync() {
        return this.mIsAsync;
    }

    @Override // com.oplus.epona.Interceptor.Chain
    public void proceed() {
        if (this.mIndex < this.mInterceptors.size()) {
            this.mInterceptors.get(this.mIndex).intercept(fork(this.mIndex + 1));
            return;
        }
        this.mCallback.onReceive(Response.errorResponse(this.mRequest.getComponentName() + "#" + this.mRequest.getActionName() + " cannot be proceeded"));
    }

    @Override // com.oplus.epona.Interceptor.Chain
    public Request request() {
        return this.mRequest;
    }
}
