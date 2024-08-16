package com.oplus.epona.interceptor;

import com.oplus.epona.Call;
import com.oplus.epona.Constants;
import com.oplus.epona.Epona;
import com.oplus.epona.Interceptor;
import com.oplus.epona.Request;
import com.oplus.epona.Response;
import com.oplus.epona.provider.ProviderInfo;
import com.oplus.epona.utils.Logger;

/* loaded from: classes.dex */
public class CallProviderInterceptor implements Interceptor {
    private static final String TAG = "CallProviderInterceptor";

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$intercept$0(String str, Request request, Call.Callback callback, Response response) {
        Object[] objArr = new Object[4];
        if (str == null) {
            str = "";
        }
        objArr[0] = str;
        objArr[1] = request.getComponentName();
        objArr[2] = request.getActionName();
        objArr[3] = response;
        Logger.d(TAG, "Caller(%s).Component(%s).Action(%s) response : %s", objArr);
        callback.onReceive(response);
    }

    @Override // com.oplus.epona.Interceptor
    public void intercept(Interceptor.Chain chain) {
        final Request request = chain.request();
        String componentName = request.getComponentName();
        ProviderInfo findProviderComponent = Epona.findProviderComponent(componentName);
        if (findProviderComponent != null) {
            final Call.Callback callback = chain.callback();
            final String string = request.getBundle().getString(Constants.REQUEST_CALLING_PACKAGE_NAME_KEY);
            try {
                String actionName = request.getActionName();
                if (chain.isAsync()) {
                    findProviderComponent.getMethod(actionName).invoke(null, request, new Call.Callback() { // from class: com.oplus.epona.interceptor.b
                        @Override // com.oplus.epona.Call.Callback
                        public final void onReceive(Response response) {
                            CallProviderInterceptor.lambda$intercept$0(string, request, callback, response);
                        }
                    });
                } else {
                    Response response = (Response) findProviderComponent.getMethod(actionName).invoke(null, request);
                    Object[] objArr = new Object[4];
                    if (string == null) {
                        string = "";
                    }
                    objArr[0] = string;
                    objArr[1] = request.getComponentName();
                    objArr[2] = request.getActionName();
                    objArr[3] = response;
                    Logger.d(TAG, "Caller(%s).Component(%s).Action(%s) response : %s", objArr);
                    callback.onReceive(response);
                }
                return;
            } catch (Exception e10) {
                Logger.e(TAG, "fail to run static provider with componentName = %s and exception is %s", componentName, e10.toString());
                callback.onReceive(Response.defaultErrorResponse());
                return;
            }
        }
        chain.proceed();
    }
}
