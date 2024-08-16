package com.android.server.tv.tunerresourcemanager;

import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.annotations.VisibleForTesting;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.server.location.contexthub.ContextHubStatsLog;
import com.android.server.policy.PhoneWindowManager;
import com.android.server.sensorprivacy.SensorPrivacyService;
import com.android.server.usb.descriptors.UsbDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UseCasePriorityHints {
    private static final int INVALID_PRIORITY_VALUE = -1;
    private static final int INVALID_USE_CASE = -1;
    private static final String PATH_TO_VENDOR_CONFIG_XML = "/vendor/etc/tunerResourceManagerUseCaseConfig.xml";
    private static final String TAG = "UseCasePriorityHints";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String NS = null;
    SparseArray<int[]> mPriorityHints = new SparseArray<>();
    Set<Integer> mVendorDefinedUseCase = new HashSet();
    private int mDefaultForeground = 150;
    private int mDefaultBackground = 50;

    private static boolean isPredefinedUseCase(int i) {
        return i == 100 || i == 200 || i == 300 || i == 400 || i == 500;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getForegroundPriority(int i) {
        if (this.mPriorityHints.get(i) != null && this.mPriorityHints.get(i).length == 2) {
            return this.mPriorityHints.get(i)[0];
        }
        return this.mDefaultForeground;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getBackgroundPriority(int i) {
        if (this.mPriorityHints.get(i) != null && this.mPriorityHints.get(i).length == 2) {
            return this.mPriorityHints.get(i)[1];
        }
        return this.mDefaultBackground;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDefinedUseCase(int i) {
        return this.mVendorDefinedUseCase.contains(Integer.valueOf(i)) || isPredefinedUseCase(i);
    }

    public void parse() {
        File file = new File(PATH_TO_VENDOR_CONFIG_XML);
        if (file.exists()) {
            try {
                parseInternal(new FileInputStream(file));
                return;
            } catch (IOException e) {
                Slog.e(TAG, "Error reading vendor file: " + file, e);
                return;
            } catch (XmlPullParserException e2) {
                Slog.e(TAG, "Unable to parse vendor file: " + file, e2);
                return;
            }
        }
        if (DEBUG) {
            Slog.i(TAG, "no vendor priority configuration available. Using default priority");
        }
        addNewUseCasePriority(100, 180, 100);
        addNewUseCasePriority(UsbDescriptor.USB_CONTROL_TRANSFER_TIMEOUT_MS, 450, UsbDescriptor.USB_CONTROL_TRANSFER_TIMEOUT_MS);
        addNewUseCasePriority(300, 480, 300);
        addNewUseCasePriority(ContextHubStatsLog.CONTEXT_HUB_LOADED_NANOAPP_SNAPSHOT_REPORTED, 490, ContextHubStatsLog.CONTEXT_HUB_LOADED_NANOAPP_SNAPSHOT_REPORTED);
        addNewUseCasePriority(SensorPrivacyService.REMINDER_DIALOG_DELAY_MILLIS, PhoneWindowManager.TOAST_WINDOW_ANIM_BUFFER, SensorPrivacyService.REMINDER_DIALOG_DELAY_MILLIS);
    }

    @VisibleForTesting
    protected void parseInternal(InputStream inputStream) throws IOException, XmlPullParserException {
        TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(inputStream);
        resolvePullParser.nextTag();
        readUseCase(resolvePullParser);
        inputStream.close();
        for (int i = 0; i < this.mPriorityHints.size(); i++) {
            int keyAt = this.mPriorityHints.keyAt(i);
            int[] iArr = this.mPriorityHints.get(keyAt);
            if (DEBUG) {
                Slog.d(TAG, "{defaultFg=" + this.mDefaultForeground + ", defaultBg=" + this.mDefaultBackground + "}");
                Slog.d(TAG, "{useCase=" + keyAt + ", fg=" + iArr[0] + ", bg=" + iArr[1] + "}");
            }
        }
    }

    private void readUseCase(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        typedXmlPullParser.require(2, NS, "config");
        while (typedXmlPullParser.next() != 3) {
            if (typedXmlPullParser.getEventType() == 2) {
                String name = typedXmlPullParser.getName();
                if (name.equals("useCaseDefault")) {
                    this.mDefaultForeground = readAttributeToInt("fgPriority", typedXmlPullParser);
                    this.mDefaultBackground = readAttributeToInt("bgPriority", typedXmlPullParser);
                    typedXmlPullParser.nextTag();
                    typedXmlPullParser.require(3, NS, name);
                } else if (name.equals("useCasePreDefined")) {
                    int formatTypeToNum = formatTypeToNum("type", typedXmlPullParser);
                    if (formatTypeToNum == -1) {
                        Slog.e(TAG, "Wrong predefined use case name given in the vendor config.");
                    } else {
                        addNewUseCasePriority(formatTypeToNum, readAttributeToInt("fgPriority", typedXmlPullParser), readAttributeToInt("bgPriority", typedXmlPullParser));
                        typedXmlPullParser.nextTag();
                        typedXmlPullParser.require(3, NS, name);
                    }
                } else if (name.equals("useCaseVendor")) {
                    int readAttributeToInt = readAttributeToInt("id", typedXmlPullParser);
                    addNewUseCasePriority(readAttributeToInt, readAttributeToInt("fgPriority", typedXmlPullParser), readAttributeToInt("bgPriority", typedXmlPullParser));
                    this.mVendorDefinedUseCase.add(Integer.valueOf(readAttributeToInt));
                    typedXmlPullParser.nextTag();
                    typedXmlPullParser.require(3, NS, name);
                } else {
                    skip(typedXmlPullParser);
                }
            }
        }
    }

    private void skip(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        if (typedXmlPullParser.getEventType() != 2) {
            throw new IllegalStateException();
        }
        int i = 1;
        while (i != 0) {
            int next = typedXmlPullParser.next();
            if (next == 2) {
                i++;
            } else if (next == 3) {
                i--;
            }
        }
    }

    private int readAttributeToInt(String str, TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException {
        return typedXmlPullParser.getAttributeInt((String) null, str);
    }

    private void addNewUseCasePriority(int i, int i2, int i3) {
        this.mPriorityHints.append(i, new int[]{i2, i3});
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static int formatTypeToNum(String str, TypedXmlPullParser typedXmlPullParser) {
        char c;
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, str);
        attributeValue.hashCode();
        switch (attributeValue.hashCode()) {
            case -884787515:
                if (attributeValue.equals("USE_CASE_BACKGROUND")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 377959794:
                if (attributeValue.equals("USE_CASE_PLAYBACK")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1222007747:
                if (attributeValue.equals("USE_CASE_LIVE")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1222209876:
                if (attributeValue.equals("USE_CASE_SCAN")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1990900072:
                if (attributeValue.equals("USE_CASE_RECORD")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return 100;
            case 1:
                return 300;
            case 2:
                return ContextHubStatsLog.CONTEXT_HUB_LOADED_NANOAPP_SNAPSHOT_REPORTED;
            case 3:
                return UsbDescriptor.USB_CONTROL_TRANSFER_TIMEOUT_MS;
            case 4:
                return SensorPrivacyService.REMINDER_DIALOG_DELAY_MILLIS;
            default:
                return -1;
        }
    }
}
