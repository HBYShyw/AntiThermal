package com.oplus.epona.interceptor;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.oplus.epona.Call;
import com.oplus.epona.Constants;
import com.oplus.epona.Epona;
import com.oplus.epona.Interceptor;
import com.oplus.epona.Request;
import com.oplus.epona.Response;
import com.oplus.epona.route.RouteData;
import com.oplus.epona.route.RouteInfo;
import com.oplus.epona.utils.Logger;

/* loaded from: classes.dex */
public class RouteInterceptor implements Interceptor {
    private static final String FRAGMENT_V4 = "android.support.v4.app.Fragment";
    private static final String FRAGMENT_X = "androidx.fragment.app.Fragment";
    private static final String TAG = "RouteInterceptor";

    private void navigateActivity(Request request, RouteInfo routeInfo) {
        RouteData routeData = request.getRouteData();
        if (routeData != null && routeData.getContext() != null) {
            Context context = routeData.getContext();
            Intent intent = new Intent(context, routeInfo.getClazz());
            intent.putExtras(request.getBundle());
            int requestCode = routeData.getRequestCode();
            if (requestCode >= 0 && (context instanceof Activity)) {
                ((Activity) context).startActivityForResult(intent, requestCode);
                return;
            } else {
                context.startActivity(intent);
                return;
            }
        }
        Context context2 = Epona.getContext();
        Intent intent2 = new Intent(context2, routeInfo.getClazz());
        intent2.putExtras(request.getBundle());
        intent2.setFlags(268435456);
        context2.startActivity(intent2);
    }

    private Object navigateFragment(Request request, RouteInfo routeInfo) {
        try {
            Object newInstance = routeInfo.getClazz().getConstructor(new Class[0]).newInstance(new Object[0]);
            if (newInstance instanceof Fragment) {
                ((Fragment) newInstance).setArguments(request.getBundle());
                return newInstance;
            }
            if (Class.forName(FRAGMENT_X).isInstance(newInstance)) {
                Class.forName(FRAGMENT_X).getMethod("setArguments", Bundle.class).invoke(newInstance, request.getBundle());
                return newInstance;
            }
            if (!Class.forName(FRAGMENT_V4).isInstance(newInstance)) {
                return null;
            }
            Class.forName(FRAGMENT_V4).getMethod("setArguments", Bundle.class).invoke(newInstance, request.getBundle());
            return newInstance;
        } catch (Exception e10) {
            Logger.e(TAG, "Fetch fragment instance error with Component(%s), message:%s", request.getComponentName(), e10.toString());
            return null;
        }
    }

    @Override // com.oplus.epona.Interceptor
    public void intercept(Interceptor.Chain chain) {
        Request request = chain.request();
        String componentName = request.getComponentName();
        RouteInfo findRoute = Epona.findRoute(componentName);
        if (findRoute == null) {
            chain.proceed();
            return;
        }
        String string = request.getBundle().getString(Constants.REQUEST_CALLING_PACKAGE_NAME_KEY);
        Object[] objArr = new Object[2];
        if (string == null) {
            string = "";
        }
        objArr[0] = string;
        objArr[1] = componentName;
        Logger.d(TAG, "Caller(%s).Component(%s): found.", objArr);
        Call.Callback callback = chain.callback();
        int routeType = findRoute.getRouteType();
        if (routeType == 0) {
            navigateActivity(request, findRoute);
            callback.onReceive(Response.newResponse(null));
        } else {
            if (routeType != 1) {
                callback.onReceive(Response.errorResponse("Route type not found."));
                return;
            }
            Object navigateFragment = navigateFragment(request, findRoute);
            if (navigateFragment == null) {
                callback.onReceive(Response.errorResponse("Fetch fragment instance not found."));
                return;
            }
            Response newResponse = Response.newResponse(new Bundle());
            newResponse.setData(navigateFragment);
            callback.onReceive(newResponse);
        }
    }
}
