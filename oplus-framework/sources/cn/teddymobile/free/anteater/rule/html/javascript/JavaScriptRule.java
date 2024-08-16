package cn.teddymobile.free.anteater.rule.html.javascript;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import cn.teddymobile.free.anteater.logger.Logger;
import cn.teddymobile.free.anteater.rule.html.HtmlRule;
import cn.teddymobile.free.anteater.rule.utils.ViewHierarchyUtils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class JavaScriptRule implements HtmlRule {
    public static final String JSON_FIELD_ATTRIBUTE_EXTRA = "h5_extra";
    public static final String JSON_FIELD_ATTRIBUTE_HTML = "h5_html";
    public static final String JSON_FIELD_ATTRIBUTE_TITLE = "h5_title";
    public static final String JSON_FIELD_ATTRIBUTE_URL = "h5_url";
    private static final String JS_CODE_HTML = "document.body.innerHTML";
    private static final String JS_CODE_TITLE = "document.title";
    private static final String JS_CODE_URL = "window.location.href";
    private static final String TAG = JavaScriptRule.class.getSimpleName();
    public static final String WEBVIEW_CLASS_NAME = "webview_class_name";
    private View mWebView;

    public String toString() {
        return "[JavaScriptRule]";
    }

    @Override // cn.teddymobile.free.anteater.rule.html.HtmlRule
    public String getHtml(View view) {
        return getH5Info(view, JS_CODE_HTML);
    }

    @Override // cn.teddymobile.free.anteater.rule.html.HtmlRule
    public String getTitle(View view) {
        return getH5Info(view, JS_CODE_TITLE);
    }

    @Override // cn.teddymobile.free.anteater.rule.html.HtmlRule
    public String getUrl(View view) {
        return getH5Info(view, JS_CODE_URL);
    }

    @Override // cn.teddymobile.free.anteater.rule.html.HtmlRule
    public String getExtra(View view, String extraJsCode) {
        if (!TextUtils.isEmpty(extraJsCode)) {
            return getH5Info(view, extraJsCode);
        }
        return "";
    }

    public static ArrayList<String> getWebViewClassNameFromJSON(String json) {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONObject paramObj = new JSONObject(json);
            JSONArray jsonArray = paramObj.getJSONArray(WEBVIEW_CLASS_NAME);
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return list;
        }
    }

    public static String getH5ExtraCodeFromJSON(String json) {
        try {
            JSONObject paramObj = new JSONObject(json);
            return paramObj.getString(JSON_FIELD_ATTRIBUTE_EXTRA);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getH5Info(View view, String jsCode) {
        String result = null;
        View webView = getWebView(view);
        if (webView != null) {
            WebViewHandler handler = new WebViewHandler();
            handler.enableJavaScript(webView);
            String str = TAG;
            Logger.i(str, "WebView = " + webView.getClass().getName());
            result = handler.getJSResult(webView, jsCode);
            Logger.i(str, "Get Info Done.");
        } else {
            Logger.w(TAG, "WebView is null.");
        }
        Logger.i(TAG, getClass().getSimpleName() + " Result = " + result);
        return result;
    }

    private View getWebView(View view) {
        View view2 = this.mWebView;
        if (view2 != null) {
            return view2;
        }
        if (view == null) {
            return null;
        }
        View decorView = ViewHierarchyUtils.getDecorView(view);
        if (decorView == null) {
            Logger.w(TAG, "DecorView is null.");
            return null;
        }
        View retrieveWebView = ViewHierarchyUtils.retrieveWebView(decorView);
        this.mWebView = retrieveWebView;
        return retrieveWebView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class WebViewHandler extends Handler {
        private WebViewHandler() {
            super(Looper.getMainLooper());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void enableJavaScript(final View webView) {
            Logger.i(JavaScriptRule.TAG, "Enable JavaScript.");
            Logger.i(JavaScriptRule.TAG, "WebView = " + webView.getClass().getName());
            final CountDownLatch latch = new CountDownLatch(1);
            post(new Runnable() { // from class: cn.teddymobile.free.anteater.rule.html.javascript.JavaScriptRule.WebViewHandler.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        try {
                            Method method = webView.getClass().getMethod("getSettings", new Class[0]);
                            method.setAccessible(true);
                            Object settings = method.invoke(webView, new Object[0]);
                            if (settings != null) {
                                Method method2 = settings.getClass().getMethod("setJavaScriptEnabled", Boolean.TYPE);
                                method2.setAccessible(true);
                                method2.invoke(settings, true);
                            }
                        } catch (Exception e) {
                            Logger.w(JavaScriptRule.TAG, e.getMessage(), e);
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
            try {
                latch.await();
                Logger.i(JavaScriptRule.TAG, "Enable JavaScript Done.");
            } catch (InterruptedException e) {
                Logger.e(JavaScriptRule.TAG, "Enable JavaScript: InterruptedException -- " + e.getMessage());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getJSResult(View webView, String jsCode) {
            return hookEvaluateJavascript(webView, jsCode);
        }

        private String hookEvaluateJavascript(final View webView, final String jsCode) {
            final String[] result = {""};
            final CountDownLatch latch = new CountDownLatch(1);
            post(new Runnable() { // from class: cn.teddymobile.free.anteater.rule.html.javascript.JavaScriptRule.WebViewHandler.2
                @Override // java.lang.Runnable
                public void run() {
                    Method method = WebViewHandler.this.findFunEvaluateJavascript(webView);
                    if (method != null) {
                        try {
                            Class<?> callbackClass = method.getParameterTypes()[1];
                            Object callback = Proxy.newProxyInstance(callbackClass.getClassLoader(), new Class[]{callbackClass}, new InvocationHandler() { // from class: cn.teddymobile.free.anteater.rule.html.javascript.JavaScriptRule.WebViewHandler.2.1
                                @Override // java.lang.reflect.InvocationHandler
                                public Object invoke(Object o, Method method2, Object[] objects) throws Throwable {
                                    if (objects.length == 1 && (objects[0] instanceof String) && latch.getCount() > 0) {
                                        result[0] = (String) objects[0];
                                        latch.countDown();
                                        return null;
                                    }
                                    return null;
                                }
                            });
                            method.setAccessible(true);
                            method.invoke(webView, jsCode, callback);
                            return;
                        } catch (Exception e) {
                            Logger.w(JavaScriptRule.TAG, e.getMessage(), e);
                            latch.countDown();
                            return;
                        }
                    }
                    latch.countDown();
                }
            });
            try {
                latch.await();
                Logger.i(JavaScriptRule.TAG, "Get Done: " + jsCode);
                return result[0];
            } catch (InterruptedException e) {
                Logger.e(JavaScriptRule.TAG, "Enable JavaScript: InterruptedException -- " + e.getMessage());
                return result[0];
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Method findFunEvaluateJavascript(View webView) {
            for (Method method : webView.getClass().getMethods()) {
                if (method.getName().contains("evaluateJavascript") && method.getParameterTypes().length == 2) {
                    return method;
                }
            }
            return null;
        }
    }
}
