package com.android.server.pm.pkg.component;

import android.content.IntentFilter;
import android.content.pm.parsing.result.ParseInput;
import android.content.pm.parsing.result.ParseResult;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.TypedValue;
import com.android.internal.R;
import com.android.server.oplus.osense.OsenseConstants;
import com.android.server.pm.pkg.parsing.ParsingPackage;
import com.android.server.pm.pkg.parsing.ParsingPackageUtils;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ParsedIntentInfoUtils {
    public static final boolean DEBUG = false;
    private static final String TAG = "PackageParsing";

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00b3, code lost:
    
        switch(r12) {
            case 0: goto L50;
            case 1: goto L49;
            case 2: goto L42;
            default: goto L41;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00b6, code lost:
    
        r6 = com.android.server.pm.pkg.parsing.ParsingUtils.unknownTag("<intent-filter>", r17, r19, r22);
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0112, code lost:
    
        if (r6.isError() == false) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0119, code lost:
    
        r6 = 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0118, code lost:
    
        return r22.error(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00bf, code lost:
    
        r11 = r19.getAttributeValue(com.android.server.pm.pkg.parsing.ParsingUtils.ANDROID_RES_NAMESPACE, "name");
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00c5, code lost:
    
        if (r11 != null) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00c7, code lost:
    
        r6 = r22.error("No value supplied for <android:name>");
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00d0, code lost:
    
        if (r11.isEmpty() == false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00d2, code lost:
    
        r4.addCategory(r11);
        r6 = r22.deferError("No value supplied for <android:name>", 151163173);
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00da, code lost:
    
        r4.addCategory(r11);
        r6 = r22.success((java.lang.Object) null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00e2, code lost:
    
        r6 = parseData(r3, r18, r19, r20, r22);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00ec, code lost:
    
        r11 = r19.getAttributeValue(com.android.server.pm.pkg.parsing.ParsingUtils.ANDROID_RES_NAMESPACE, "name");
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00f2, code lost:
    
        if (r11 != null) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00f4, code lost:
    
        r6 = r22.error("No value supplied for <android:name>");
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00fd, code lost:
    
        if (r11.isEmpty() == false) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00ff, code lost:
    
        r4.addAction(r11);
        r6 = r22.deferError("No value supplied for <android:name>", 151163173);
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0107, code lost:
    
        r4.addAction(r11);
        r6 = r22.success((java.lang.Object) null);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ParseResult<ParsedIntentInfoImpl> parseIntentInfo(String str, ParsingPackage parsingPackage, Resources resources, XmlResourceParser xmlResourceParser, boolean z, boolean z2, ParseInput parseInput) throws XmlPullParserException, IOException {
        ParsedIntentInfoImpl parsedIntentInfoImpl = new ParsedIntentInfoImpl();
        IntentFilter intentFilter = parsedIntentInfoImpl.getIntentFilter();
        TypedArray obtainAttributes = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestIntentFilter);
        int i = 2;
        try {
            intentFilter.setPriority(obtainAttributes.getInt(2, 0));
            intentFilter.setOrder(obtainAttributes.getInt(3, 0));
            TypedValue peekValue = obtainAttributes.peekValue(0);
            if (peekValue != null) {
                parsedIntentInfoImpl.setLabelRes(peekValue.resourceId);
                if (peekValue.resourceId == 0) {
                    parsedIntentInfoImpl.setNonLocalizedLabel(peekValue.coerceToString());
                }
            }
            if (ParsingPackageUtils.sUseRoundIcon) {
                parsedIntentInfoImpl.setIcon(obtainAttributes.getResourceId(7, 0));
            }
            if (parsedIntentInfoImpl.getIcon() == 0) {
                parsedIntentInfoImpl.setIcon(obtainAttributes.getResourceId(1, 0));
            }
            if (z2) {
                intentFilter.setAutoVerify(obtainAttributes.getBoolean(6, false));
            }
            obtainAttributes.recycle();
            int depth = xmlResourceParser.getDepth();
            while (true) {
                int next = xmlResourceParser.next();
                if (next != 1 && (next != 3 || xmlResourceParser.getDepth() > depth)) {
                    if (next == i) {
                        String name = xmlResourceParser.getName();
                        name.hashCode();
                        int i2 = -1;
                        switch (name.hashCode()) {
                            case -1422950858:
                                if (name.equals(OsenseConstants.KEY_STRING_ACTION)) {
                                    i2 = 0;
                                    break;
                                }
                                break;
                            case 3076010:
                                if (name.equals("data")) {
                                    i2 = 1;
                                    break;
                                }
                                break;
                            case 50511102:
                                if (name.equals("category")) {
                                    i2 = i;
                                    break;
                                }
                                break;
                        }
                    }
                }
            }
            parsedIntentInfoImpl.setHasDefault(intentFilter.hasCategory("android.intent.category.DEFAULT"));
            return parseInput.success(parsedIntentInfoImpl);
        } catch (Throwable th) {
            obtainAttributes.recycle();
            throw th;
        }
    }

    private static ParseResult<ParsedIntentInfo> parseData(ParsedIntentInfo parsedIntentInfo, Resources resources, XmlResourceParser xmlResourceParser, boolean z, ParseInput parseInput) {
        IntentFilter intentFilter = parsedIntentInfo.getIntentFilter();
        TypedArray obtainAttributes = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestData);
        try {
            String nonConfigurationString = obtainAttributes.getNonConfigurationString(0, 0);
            if (nonConfigurationString != null) {
                intentFilter.addDataType(nonConfigurationString);
            }
            String nonConfigurationString2 = obtainAttributes.getNonConfigurationString(10, 0);
            if (nonConfigurationString2 != null) {
                intentFilter.addMimeGroup(nonConfigurationString2);
            }
            String nonConfigurationString3 = obtainAttributes.getNonConfigurationString(1, 0);
            if (nonConfigurationString3 != null) {
                intentFilter.addDataScheme(nonConfigurationString3);
            }
            String nonConfigurationString4 = obtainAttributes.getNonConfigurationString(7, 0);
            if (nonConfigurationString4 != null) {
                intentFilter.addDataSchemeSpecificPart(nonConfigurationString4, 0);
            }
            String nonConfigurationString5 = obtainAttributes.getNonConfigurationString(8, 0);
            if (nonConfigurationString5 != null) {
                intentFilter.addDataSchemeSpecificPart(nonConfigurationString5, 1);
            }
            String nonConfigurationString6 = obtainAttributes.getNonConfigurationString(9, 0);
            if (nonConfigurationString6 != null) {
                if (!z) {
                    return parseInput.error("sspPattern not allowed here; ssp must be literal");
                }
                intentFilter.addDataSchemeSpecificPart(nonConfigurationString6, 2);
            }
            String nonConfigurationString7 = obtainAttributes.getNonConfigurationString(14, 0);
            if (nonConfigurationString7 != null) {
                if (!z) {
                    return parseInput.error("sspAdvancedPattern not allowed here; ssp must be literal");
                }
                intentFilter.addDataSchemeSpecificPart(nonConfigurationString7, 3);
            }
            String nonConfigurationString8 = obtainAttributes.getNonConfigurationString(12, 0);
            if (nonConfigurationString8 != null) {
                intentFilter.addDataSchemeSpecificPart(nonConfigurationString8, 4);
            }
            String nonConfigurationString9 = obtainAttributes.getNonConfigurationString(2, 0);
            String nonConfigurationString10 = obtainAttributes.getNonConfigurationString(3, 0);
            if (nonConfigurationString9 != null) {
                intentFilter.addDataAuthority(nonConfigurationString9, nonConfigurationString10);
            }
            String nonConfigurationString11 = obtainAttributes.getNonConfigurationString(4, 0);
            if (nonConfigurationString11 != null) {
                intentFilter.addDataPath(nonConfigurationString11, 0);
            }
            String nonConfigurationString12 = obtainAttributes.getNonConfigurationString(5, 0);
            if (nonConfigurationString12 != null) {
                intentFilter.addDataPath(nonConfigurationString12, 1);
            }
            String nonConfigurationString13 = obtainAttributes.getNonConfigurationString(6, 0);
            if (nonConfigurationString13 != null) {
                if (!z) {
                    return parseInput.error("pathPattern not allowed here; path must be literal");
                }
                intentFilter.addDataPath(nonConfigurationString13, 2);
            }
            String nonConfigurationString14 = obtainAttributes.getNonConfigurationString(13, 0);
            if (nonConfigurationString14 != null) {
                if (!z) {
                    return parseInput.error("pathAdvancedPattern not allowed here; path must be literal");
                }
                intentFilter.addDataPath(nonConfigurationString14, 3);
            }
            String nonConfigurationString15 = obtainAttributes.getNonConfigurationString(11, 0);
            if (nonConfigurationString15 != null) {
                intentFilter.addDataPath(nonConfigurationString15, 4);
            }
            return parseInput.success((Object) null);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            return parseInput.error(e.toString());
        } finally {
            obtainAttributes.recycle();
        }
    }
}
