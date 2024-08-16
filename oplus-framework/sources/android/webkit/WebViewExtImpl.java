package android.webkit;

import android.common.OplusFeatureCache;
import com.oplus.darkmode.IOplusDarkModeManager;

/* loaded from: classes.dex */
public class WebViewExtImpl implements IWebViewExt {
    public WebViewExtImpl(Object base) {
    }

    public void hookOnVisibilityChanged(WebView webView) {
        ((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).ensureWebSettingDarkMode(webView);
    }
}
