package com.oplus.os;

import android.content.Context;
import android.os.Environment;
import android.util.Slog;
import android.util.Xml;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
public class OplusApkInstallHelper {
    private static final String TAG = "OplusApkInstallHelper";
    private static boolean internal = false;
    private static boolean external = false;

    static {
        parsexml();
    }

    public static boolean InstallUIDisplay(Context context) {
        if (internal) {
            return true;
        }
        if (!external || OplusUsbEnvironment.isExternalSDRemoved(context)) {
            return false;
        }
        return true;
    }

    public static boolean IsInstallSdMounted(Context context) {
        if (internal) {
            boolean result = "mounted".equals(OplusUsbEnvironment.getInternalSdState(context));
            return result;
        }
        if (!external) {
            return false;
        }
        boolean result2 = "mounted".equals(OplusUsbEnvironment.getExternalSdState(context));
        return result2;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:9:0x0025. Please report as an issue. */
    private static void parsexml() {
        XmlPullParser parser;
        int eventType;
        File permFile = new File(Environment.getRootDirectory(), "etc/apk_install.xml");
        try {
            FileReader permReader = new FileReader(permFile);
            try {
                parser = Xml.newPullParser();
                parser.setInput(permReader);
                eventType = parser.getEventType();
            } catch (IOException e) {
                Slog.w(TAG, "Got execption parsing permissions.", e);
            } catch (XmlPullParserException e2) {
                Slog.w(TAG, "Got execption parsing permissions.", e2);
            }
            while (true) {
                boolean z = true;
                if (eventType != 1) {
                    switch (eventType) {
                        case 0:
                            eventType = parser.next();
                        case 1:
                        default:
                            eventType = parser.next();
                        case 2:
                            if (parser.getName().equals("path")) {
                                parser.next();
                                internal = parser.getText().equals("internal");
                                if (!parser.getText().equals("external")) {
                                    z = false;
                                }
                                external = z;
                            }
                            eventType = parser.next();
                    }
                }
                try {
                    permReader.close();
                    return;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    return;
                }
            }
        } catch (FileNotFoundException e4) {
            Slog.w(TAG, "Couldn't find or open apk_install file " + permFile);
        }
    }
}
