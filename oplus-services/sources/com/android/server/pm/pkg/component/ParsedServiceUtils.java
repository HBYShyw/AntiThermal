package com.android.server.pm.pkg.component;

import android.content.pm.parsing.result.ParseInput;
import android.content.pm.parsing.result.ParseResult;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import com.android.internal.R;
import com.android.server.pm.pkg.parsing.ParsingPackage;
import java.io.IOException;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ParsedServiceUtils {
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x013c, code lost:
    
        switch(r3) {
            case 0: goto L56;
            case 1: goto L53;
            case 2: goto L51;
            default: goto L50;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x013f, code lost:
    
        r7 = r29;
        r6 = r23;
        r1 = com.android.server.pm.pkg.parsing.ParsingUtils.unknownTag(r6, r27, r7, r33);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0155, code lost:
    
        r21 = r6;
        r17 = r8;
        r18 = r9;
        r19 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x01c6, code lost:
    
        if (r1.isError() == false) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x01cd, code lost:
    
        r8 = r17;
        r9 = r18;
        r10 = r19;
        r23 = r21;
        r13 = 0;
        r15 = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x01cc, code lost:
    
        return r33.error(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x014a, code lost:
    
        r7 = r29;
        r6 = r23;
        r1 = com.android.server.pm.pkg.component.ParsedComponentUtils.addProperty(r14, r27, r28, r7, r33);
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0160, code lost:
    
        r21 = r23;
        r17 = r8;
        r18 = r9;
        r19 = r10;
        r1 = com.android.server.pm.pkg.component.ParsedMainComponentUtils.parseIntentFilter(r14, r27, r28, r29, r16, true, false, false, false, r33);
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0193, code lost:
    
        if (r1.isSuccess() == false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0195, code lost:
    
        r2 = (com.android.server.pm.pkg.component.ParsedIntentInfoImpl) r1.getResult();
        r14.setOrder(java.lang.Math.max(r2.getIntentFilter().getOrder(), r14.getOrder()));
        r14.addIntent(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x01b2, code lost:
    
        r17 = r8;
        r18 = r9;
        r19 = r10;
        r21 = r23;
        r1 = com.android.server.pm.pkg.component.ParsedComponentUtils.addMetaData(r14, r27, r28, r29, r33);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ParseResult<ParsedService> parseService(String[] strArr, ParsingPackage parsingPackage, Resources resources, XmlResourceParser xmlResourceParser, int i, boolean z, String str, ParseInput parseInput) throws XmlPullParserException, IOException {
        TypedArray typedArray;
        ParsedServiceImpl parsedServiceImpl;
        String packageName = parsingPackage.getPackageName();
        ParsedServiceImpl parsedServiceImpl2 = new ParsedServiceImpl();
        String name = xmlResourceParser.getName();
        TypedArray obtainAttributes = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestService);
        String str2 = name;
        try {
            ParseResult parseMainComponent = ParsedMainComponentUtils.parseMainComponent(parsedServiceImpl2, name, strArr, parsingPackage, obtainAttributes, i, z, str, parseInput, 12, 7, 13, 4, 1, 0, 8, 2, 6, 15, 17, 20);
            if (parseMainComponent.isError()) {
                ParseResult<ParsedService> error = parseInput.error(parseMainComponent);
                obtainAttributes.recycle();
                return error;
            }
            typedArray = obtainAttributes;
            try {
                boolean hasValue = typedArray.hasValue(5);
                int i2 = 0;
                if (hasValue) {
                    ParsedServiceImpl parsedServiceImpl3 = parsedServiceImpl2;
                    parsedServiceImpl3.setExported(typedArray.getBoolean(5, false));
                    parsedServiceImpl = parsedServiceImpl3;
                } else {
                    parsedServiceImpl = parsedServiceImpl2;
                }
                int i3 = 3;
                String nonConfigurationString = typedArray.getNonConfigurationString(3, 0);
                if (nonConfigurationString == null) {
                    nonConfigurationString = parsingPackage.getPermission();
                }
                parsedServiceImpl.setPermission(nonConfigurationString);
                int i4 = 1;
                int i5 = 2;
                parsedServiceImpl.setForegroundServiceType(typedArray.getInt(19, 0)).setFlags(parsedServiceImpl.getFlags() | ComponentParseUtils.flag(1, 9, typedArray) | ComponentParseUtils.flag(2, 10, typedArray) | ComponentParseUtils.flag(4, 14, typedArray) | ComponentParseUtils.flag(8, 18, typedArray) | ComponentParseUtils.flag(16, 21, typedArray) | ComponentParseUtils.flag(1073741824, 11, typedArray));
                boolean z2 = typedArray.getBoolean(16, false);
                if (z2) {
                    parsedServiceImpl.setFlags(parsedServiceImpl.getFlags() | 1048576);
                    parsingPackage.setVisibleToInstantApps(true);
                }
                typedArray.recycle();
                if (parsingPackage.isSaveStateDisallowed() && Objects.equals(parsedServiceImpl.getProcessName(), packageName)) {
                    return parseInput.error("Heavy-weight applications can not have services in main process");
                }
                int depth = xmlResourceParser.getDepth();
                while (true) {
                    int next = xmlResourceParser.next();
                    if (next != i4 && (next != i3 || xmlResourceParser.getDepth() > depth)) {
                        if (next == i5) {
                            String name2 = xmlResourceParser.getName();
                            name2.hashCode();
                            int i6 = -1;
                            switch (name2.hashCode()) {
                                case -1115949454:
                                    if (name2.equals("meta-data")) {
                                        i6 = i2;
                                        break;
                                    }
                                    break;
                                case -1029793847:
                                    if (name2.equals("intent-filter")) {
                                        i6 = i4;
                                        break;
                                    }
                                    break;
                                case -993141291:
                                    if (name2.equals("property")) {
                                        i6 = i5;
                                        break;
                                    }
                                    break;
                            }
                        }
                    }
                }
                int i7 = i4;
                if (!hasValue) {
                    boolean z3 = parsedServiceImpl.getIntents().size() > 0 ? i7 : 0;
                    if (z3 != 0) {
                        ParseResult deferError = parseInput.deferError(parsedServiceImpl.getName() + ": Targeting S+ (version 31 and above) requires that an explicit value for android:exported be defined when intent filters are present", 150232615L);
                        if (deferError.isError()) {
                            return parseInput.error(deferError);
                        }
                    }
                    parsedServiceImpl.setExported(z3);
                }
                return parseInput.success(parsedServiceImpl);
            } catch (Throwable th) {
                th = th;
                typedArray.recycle();
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            typedArray = obtainAttributes;
        }
    }
}
