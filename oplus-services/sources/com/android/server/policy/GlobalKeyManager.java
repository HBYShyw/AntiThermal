package com.android.server.policy;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.UserHandle;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.io.PrintWriter;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class GlobalKeyManager {
    private static final String ATTR_COMPONENT = "component";
    private static final String ATTR_DISPATCH_WHEN_NON_INTERACTIVE = "dispatchWhenNonInteractive";
    private static final String ATTR_KEY_CODE = "keyCode";
    private static final String ATTR_VERSION = "version";
    private static final int GLOBAL_KEY_FILE_VERSION = 1;
    private static final String TAG = "GlobalKeyManager";
    private static final String TAG_GLOBAL_KEYS = "global_keys";
    private static final String TAG_KEY = "key";
    private final SparseArray<GlobalKeyAction> mKeyMapping = new SparseArray<>();
    private boolean mBeganFromNonInteractive = false;

    public GlobalKeyManager(Context context) {
        loadGlobalKeys(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean handleGlobalKey(Context context, int i, KeyEvent keyEvent) {
        GlobalKeyAction globalKeyAction;
        if (this.mKeyMapping.size() <= 0 || (globalKeyAction = this.mKeyMapping.get(i)) == null) {
            return false;
        }
        context.sendBroadcastAsUser(new GlobalKeyIntent(globalKeyAction.mComponentName, keyEvent, this.mBeganFromNonInteractive).getIntent(), UserHandle.CURRENT, null);
        if (keyEvent.getAction() == 1) {
            this.mBeganFromNonInteractive = false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldHandleGlobalKey(int i) {
        return this.mKeyMapping.get(i) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldDispatchFromNonInteractive(int i) {
        GlobalKeyAction globalKeyAction = this.mKeyMapping.get(i);
        if (globalKeyAction == null) {
            return false;
        }
        return globalKeyAction.mDispatchWhenNonInteractive;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBeganFromNonInteractive() {
        this.mBeganFromNonInteractive = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class GlobalKeyAction {
        private final ComponentName mComponentName;
        private final boolean mDispatchWhenNonInteractive;

        GlobalKeyAction(String str, String str2) {
            this.mComponentName = ComponentName.unflattenFromString(str);
            this.mDispatchWhenNonInteractive = Boolean.parseBoolean(str2);
        }
    }

    private void loadGlobalKeys(Context context) {
        try {
            XmlResourceParser xml = context.getResources().getXml(R.xml.global_keys);
            try {
                XmlUtils.beginDocument(xml, TAG_GLOBAL_KEYS);
                if (1 == xml.getAttributeIntValue(null, ATTR_VERSION, 0)) {
                    while (true) {
                        XmlUtils.nextElement(xml);
                        String name = xml.getName();
                        if (name == null) {
                            break;
                        }
                        if (TAG_KEY.equals(name)) {
                            String attributeValue = xml.getAttributeValue(null, ATTR_KEY_CODE);
                            String attributeValue2 = xml.getAttributeValue(null, ATTR_COMPONENT);
                            String attributeValue3 = xml.getAttributeValue(null, ATTR_DISPATCH_WHEN_NON_INTERACTIVE);
                            if (attributeValue != null && attributeValue2 != null) {
                                int keyCodeFromString = KeyEvent.keyCodeFromString(attributeValue);
                                if (keyCodeFromString != 0) {
                                    this.mKeyMapping.put(keyCodeFromString, new GlobalKeyAction(attributeValue2, attributeValue3));
                                } else {
                                    Log.wtf(TAG, "Global keys entry does not map to a valid key code: " + attributeValue);
                                }
                            }
                            Log.wtf(TAG, "Failed to parse global keys entry: " + xml.getText());
                        }
                    }
                }
                xml.close();
            } catch (Throwable th) {
                if (xml != null) {
                    try {
                        xml.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (Resources.NotFoundException e) {
            Log.wtf(TAG, "global keys file not found", e);
        } catch (IOException e2) {
            Log.e(TAG, "I/O exception reading global keys file", e2);
        } catch (XmlPullParserException e3) {
            Log.wtf(TAG, "XML parser exception reading global keys file", e3);
        }
    }

    public void dump(String str, PrintWriter printWriter) {
        int size = this.mKeyMapping.size();
        if (size == 0) {
            printWriter.print(str);
            printWriter.println("mKeyMapping.size=0");
            return;
        }
        printWriter.print(str);
        printWriter.println("mKeyMapping={");
        for (int i = 0; i < size; i++) {
            printWriter.print("  ");
            printWriter.print(str);
            printWriter.print(KeyEvent.keyCodeToString(this.mKeyMapping.keyAt(i)));
            printWriter.print("=");
            printWriter.print(this.mKeyMapping.valueAt(i).mComponentName.flattenToString());
            printWriter.print(",dispatchWhenNonInteractive=");
            printWriter.println(this.mKeyMapping.valueAt(i).mDispatchWhenNonInteractive);
        }
        printWriter.print(str);
        printWriter.println("}");
    }
}
