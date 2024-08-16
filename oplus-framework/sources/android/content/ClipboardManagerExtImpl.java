package android.content;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class ClipboardManagerExtImpl implements IClipboardManagerExt {
    private static final boolean DEBUG = false;
    private static final String TAG = "ClipboardManagerExt";

    public ClipboardManagerExtImpl(Object obj) {
    }

    public boolean adjustBeforeGetPrimaryClip(Context context, String pkg, int userId) {
        try {
            Map<String, String> stackTraceInfo = getUserPathInfo();
            if (stackTraceInfo == null || stackTraceInfo.isEmpty()) {
                stackTraceInfo = getLocalRuleMap();
            }
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            for (StackTraceElement element : elements) {
                String keyClassName = element.getClassName().split("\\$")[0];
                if (stackTraceInfo.containsKey(keyClassName)) {
                    String valueMethod = stackTraceInfo.get(keyClassName);
                    if ("?".equals(valueMethod)) {
                        return true;
                    }
                    return Pattern.matches(valueMethod, element.getMethodName());
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "hookGetPrimaryClip has exception");
        }
        return false;
    }

    public ClipData hookGetPrimaryClip(String pkg, int userId) {
        return getUserPrimaryClip(pkg, userId);
    }

    private ClipData getUserPrimaryClip(String pkg, int userId) {
        return OplusClipboardManager.getUserPrimaryClip(pkg, userId);
    }

    private Map<String, String> getUserPathInfo() {
        return OplusClipboardManager.getUserPathInfo();
    }

    private static Map<String, String> getLocalRuleMap() {
        Map<String, String> localMap = new HashMap<>();
        localMap.put("android.widget.TextView", "canPasteAsPlainText|canPaste|paste");
        localMap.put("android.widget.Editor", "onClick");
        localMap.put("android.view.View", "performLongClick");
        localMap.put("org.chromium.ui.base.Clipboard", "?");
        return localMap;
    }
}
