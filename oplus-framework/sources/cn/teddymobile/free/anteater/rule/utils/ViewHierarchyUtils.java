package cn.teddymobile.free.anteater.rule.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ViewHierarchyUtils {
    private static ArrayList<String> sThirdPartyWebViewClassNames;

    static {
        ArrayList<String> arrayList = new ArrayList<>();
        sThirdPartyWebViewClassNames = arrayList;
        arrayList.add("com.uc.webview.export.WebView");
        sThirdPartyWebViewClassNames.add("com.tencent.smtt.webkit.WebView");
        sThirdPartyWebViewClassNames.add("com.tencent.smtt.sdk.WebView");
        sThirdPartyWebViewClassNames.add("sogou.webkit.WebView");
        sThirdPartyWebViewClassNames.add("com.baidu.webkit.sdk.WebView");
        sThirdPartyWebViewClassNames.add("com.oplus.webview.KKWebview");
        sThirdPartyWebViewClassNames.add("com.tencent.mm.ui.widget.MMWebView");
    }

    public static Context getActivityContext(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return context;
            }
            ContextWrapper wrapper = (ContextWrapper) context;
            Context base = wrapper.getBaseContext();
            if (base == context) {
                break;
            }
            context = base;
        }
        return context;
    }

    public static Intent getIntent(View view) {
        Context context = getActivityContext(view.getContext());
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
        View webView = null;
        if (view == null) {
            return null;
        }
        if (view instanceof WebView) {
            return view;
        }
        Iterator<String> it = sThirdPartyWebViewClassNames.iterator();
        while (it.hasNext()) {
            String className = it.next();
            if (checkClass(view, className)) {
                webView = view;
            }
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View webViewTmp = retrieveWebView(viewGroup.getChildAt(i));
                if (webViewTmp != null) {
                    webView = webViewTmp;
                }
            }
            return webView;
        }
        return webView;
    }

    private static boolean checkClass(Object object, String className) {
        for (Class clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            if (clazz.getName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    public static void addThirdPartyWebViewClassNames(List<String> include) {
        for (String s : include) {
            if (!sThirdPartyWebViewClassNames.contains(s)) {
                sThirdPartyWebViewClassNames.add(s);
            }
        }
    }
}
