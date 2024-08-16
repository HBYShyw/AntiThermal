package cn.teddymobile.free.anteater.helper.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/* loaded from: classes.dex */
public class ViewHierarchyUtils {
    private static String[] sThirdPartyWebViewClassNames = {"com.uc.webview.export.WebView", "com.tencent.smtt.webkit.WebView", "com.tencent.smtt.sdk.WebView", "sogou.webkit.WebView", "com.baidu.webkit.sdk.WebView", "com.oplus.webview.KKWebview"};

    public static Intent getIntent(View view) {
        Context context = view.getContext();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return activity.getIntent();
        }
        return null;
    }

    public static View getDecorView(View view) {
        if (view != null) {
            Context context = view.getContext();
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (activity.getWindow() != null) {
                    return activity.getWindow().getDecorView();
                }
                return null;
            }
            while (!view.getClass().getName().endsWith("DecorView")) {
                if (view.getParent() instanceof View) {
                    view = (View) view.getParent();
                } else {
                    return null;
                }
            }
            return view;
        }
        return null;
    }

    public static View retrieveWebView(View view) {
        if (view != null) {
            if (view instanceof WebView) {
                return view;
            }
            for (String className : sThirdPartyWebViewClassNames) {
                if (checkClass(view, className)) {
                    return view;
                }
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View webView = retrieveWebView(viewGroup.getChildAt(i));
                    if (webView != null) {
                        return webView;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    private static boolean checkClass(Object object, String className) {
        for (Class clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            if (clazz.getName().equals(className)) {
                return true;
            }
        }
        return false;
    }
}
