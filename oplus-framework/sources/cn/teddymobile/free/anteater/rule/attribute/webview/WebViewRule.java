package cn.teddymobile.free.anteater.rule.attribute.webview;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import cn.teddymobile.free.anteater.logger.Logger;
import cn.teddymobile.free.anteater.rule.attribute.AttributeRule;
import cn.teddymobile.free.anteater.rule.utils.ViewHierarchyUtils;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class WebViewRule implements AttributeRule {
    private static final String JSON_FIELD_CONTENT = "content";
    private static final String JSON_FIELD_EXTRACT_CONTENT = "extract_content";
    private static final String JSON_FIELD_EXTRACT_CONTENT_CANONICAL_NAME = "extract_content_canonical";
    private static final String JSON_FIELD_EXTRACT_TITLE = "extract_title";
    private static final String JSON_FIELD_EXTRACT_URL = "extract_url";
    private static final String JSON_FIELD_TITLE = "title";
    private static final String JSON_FIELD_URL = "url";
    private static final String TAG = WebViewRule.class.getSimpleName();
    private boolean mExtractTitle = false;
    private boolean mExtractUrl = false;
    private boolean mExtractContent = false;
    private String mExtractContentCanonicalName = "";

    public String toString() {
        return "[WebViewRule] ExtractTitle = " + this.mExtractTitle + "\nExtractUrl = " + this.mExtractUrl + "\nExtractContent = " + this.mExtractContent;
    }

    @Override // cn.teddymobile.free.anteater.rule.attribute.AttributeRule
    public void loadFromJSON(JSONObject ruleObject) throws JSONException {
        this.mExtractTitle = ruleObject.getBoolean(JSON_FIELD_EXTRACT_TITLE);
        this.mExtractUrl = ruleObject.getBoolean(JSON_FIELD_EXTRACT_URL);
        this.mExtractContent = ruleObject.getBoolean(JSON_FIELD_EXTRACT_CONTENT);
        this.mExtractContentCanonicalName = ruleObject.getString(JSON_FIELD_EXTRACT_CONTENT_CANONICAL_NAME);
    }

    @Override // cn.teddymobile.free.anteater.rule.attribute.AttributeRule
    public JSONObject extractAttribute(View view) {
        String url;
        String title;
        JSONObject result = null;
        View decorView = ViewHierarchyUtils.getDecorView(view);
        if (decorView != null) {
            result = new JSONObject();
            WebViewHandler handler = new WebViewHandler();
            View webView = ViewHierarchyUtils.retrieveWebView(decorView);
            if (webView != null) {
                try {
                    if (this.mExtractTitle && (title = handler.getTitle(webView)) != null) {
                        result.put(JSON_FIELD_TITLE, title);
                    }
                    if (this.mExtractUrl && (url = handler.getUrl(webView)) != null) {
                        result.put("url", url);
                    }
                } catch (JSONException e) {
                }
            } else {
                Logger.w(TAG, "WebView is null.");
            }
            if (this.mExtractContent) {
                try {
                    String content = handler.getContentFromAccessibilityNodeInfo(decorView, this.mExtractContentCanonicalName);
                    if (content != null) {
                        result.put("content", content);
                    }
                } catch (JSONException e2) {
                    Logger.w(TAG, "WebView rule extract content JSONException.");
                }
            }
        } else {
            Logger.w(TAG, "DecorView is null.");
        }
        Logger.i(TAG, getClass().getSimpleName() + " Result = " + result);
        return result;
    }

    /* loaded from: classes.dex */
    private static class WebViewHandler extends Handler {
        private String mContent;
        private String mTitle;
        private String mUrl;

        private WebViewHandler() {
            super(Looper.getMainLooper());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getTitle(final View webView) {
            final CountDownLatch latch = new CountDownLatch(1);
            post(new Runnable() { // from class: cn.teddymobile.free.anteater.rule.attribute.webview.WebViewRule.WebViewHandler.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        Method method = webView.getClass().getMethod("getTitle", new Class[0]);
                        method.setAccessible(true);
                        Object result = method.invoke(webView, new Object[0]);
                        if (result instanceof String) {
                            WebViewHandler.this.mTitle = (String) result;
                            latch.countDown();
                            return;
                        }
                    } catch (Exception e) {
                    }
                    WebViewHandler.this.mTitle = null;
                    latch.countDown();
                }
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
            }
            return this.mTitle;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getUrl(final View webView) {
            final CountDownLatch latch = new CountDownLatch(1);
            post(new Runnable() { // from class: cn.teddymobile.free.anteater.rule.attribute.webview.WebViewRule.WebViewHandler.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        Method method = webView.getClass().getMethod("getUrl", new Class[0]);
                        method.setAccessible(true);
                        Object result = method.invoke(webView, new Object[0]);
                        if (result instanceof String) {
                            WebViewHandler.this.mUrl = (String) result;
                            latch.countDown();
                            return;
                        }
                    } catch (Exception e) {
                    }
                    WebViewHandler.this.mUrl = null;
                    latch.countDown();
                }
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
            }
            return this.mUrl;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getContentFromAccessibilityNodeInfo(final View view, final String canonicalName) {
            final CountDownLatch latch = new CountDownLatch(1);
            post(new Runnable() { // from class: cn.teddymobile.free.anteater.rule.attribute.webview.WebViewRule.WebViewHandler.3
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        View webView = WebViewHandler.this.retrieveWebViewByCanonicalName(view, canonicalName);
                        if (webView == null) {
                            WebViewHandler.this.mContent = null;
                        } else {
                            AccessibilityNodeProvider provider = webView.getAccessibilityNodeProvider();
                            if (provider == null) {
                                WebViewHandler.this.mContent = null;
                            } else {
                                StringBuilder builder = new StringBuilder();
                                WebViewHandler.this.retrieveWebViewAccessibilityNodeInfo(webView.createAccessibilityNodeInfo(), provider, builder);
                                WebViewHandler.this.mContent = builder.toString();
                            }
                        }
                        latch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                        latch.countDown();
                        WebViewHandler.this.mContent = null;
                        latch.countDown();
                    }
                }
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
            }
            return this.mContent;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public View retrieveWebViewByCanonicalName(View view, String canonicalName) {
            if (view != null) {
                String canonicalCurrent = view.getClass().getCanonicalName();
                if (canonicalCurrent.equals(canonicalName)) {
                    return view;
                }
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    for (int i = 0; i < viewGroup.getChildCount(); i++) {
                        View v = retrieveWebViewByCanonicalName(viewGroup.getChildAt(i), canonicalName);
                        if (v != null) {
                            return v;
                        }
                    }
                    return null;
                }
                return null;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void retrieveWebViewAccessibilityNodeInfo(AccessibilityNodeInfo root, AccessibilityNodeProvider provider, StringBuilder builder) {
            if (root == null) {
                return;
            }
            if (!TextUtils.isEmpty(root.getText())) {
                builder.append(root.getText());
            }
            int childCount = root.getChildCount();
            for (int i = 0; i < childCount; i++) {
                long childNodeId = root.getChildId(i);
                AccessibilityNodeInfo child = provider.createAccessibilityNodeInfo(AccessibilityNodeInfo.getVirtualDescendantId(childNodeId));
                retrieveWebViewAccessibilityNodeInfo(child, provider, builder);
            }
        }
    }
}
