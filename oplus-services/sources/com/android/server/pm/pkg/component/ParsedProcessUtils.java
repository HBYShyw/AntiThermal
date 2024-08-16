package com.android.server.pm.pkg.component;

import android.content.pm.parsing.result.ParseInput;
import android.content.pm.parsing.result.ParseResult;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.R;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.XmlUtils;
import com.android.server.pm.pkg.parsing.ParsingPackage;
import com.android.server.pm.pkg.parsing.ParsingUtils;
import java.io.IOException;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ParsedProcessUtils {
    private static ParseResult<Set<String>> parseDenyPermission(Set<String> set, Resources resources, XmlResourceParser xmlResourceParser, ParseInput parseInput) throws IOException, XmlPullParserException {
        TypedArray obtainAttributes = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestDenyPermission);
        try {
            String nonConfigurationString = obtainAttributes.getNonConfigurationString(0, 0);
            if (nonConfigurationString != null && nonConfigurationString.equals("android.permission.INTERNET")) {
                set = CollectionUtils.add(set, nonConfigurationString);
            }
            obtainAttributes.recycle();
            XmlUtils.skipCurrentTag(xmlResourceParser);
            return parseInput.success(set);
        } catch (Throwable th) {
            obtainAttributes.recycle();
            throw th;
        }
    }

    private static ParseResult<Set<String>> parseAllowPermission(Set<String> set, Resources resources, XmlResourceParser xmlResourceParser, ParseInput parseInput) throws IOException, XmlPullParserException {
        TypedArray obtainAttributes = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestAllowPermission);
        try {
            String nonConfigurationString = obtainAttributes.getNonConfigurationString(0, 0);
            if (nonConfigurationString != null && nonConfigurationString.equals("android.permission.INTERNET")) {
                set = CollectionUtils.remove(set, nonConfigurationString);
            }
            obtainAttributes.recycle();
            XmlUtils.skipCurrentTag(xmlResourceParser);
            return parseInput.success(set);
        } catch (Throwable th) {
            obtainAttributes.recycle();
            throw th;
        }
    }

    private static ParseResult<ParsedProcess> parseProcess(Set<String> set, String[] strArr, ParsingPackage parsingPackage, Resources resources, XmlResourceParser xmlResourceParser, int i, ParseInput parseInput) throws IOException, XmlPullParserException {
        ParseResult<Set<String>> parseAllowPermission;
        ParsedProcessImpl parsedProcessImpl = new ParsedProcessImpl();
        TypedArray obtainAttributes = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestProcess);
        if (set != null) {
            try {
                parsedProcessImpl.setDeniedPermissions(new ArraySet(set));
            } finally {
                obtainAttributes.recycle();
            }
        }
        ParseResult<String> buildProcessName = ComponentParseUtils.buildProcessName(parsingPackage.getPackageName(), parsingPackage.getPackageName(), obtainAttributes.getNonConfigurationString(1, 0), i, strArr, parseInput);
        if (buildProcessName.isError()) {
            return parseInput.error(buildProcessName);
        }
        String packageName = parsingPackage.getPackageName();
        String buildClassName = ParsingUtils.buildClassName(packageName, obtainAttributes.getNonConfigurationString(0, 0));
        parsedProcessImpl.setName((String) buildProcessName.getResult());
        parsedProcessImpl.putAppClassNameForPackage(packageName, buildClassName);
        parsedProcessImpl.setGwpAsanMode(obtainAttributes.getInt(2, -1));
        parsedProcessImpl.setMemtagMode(obtainAttributes.getInt(3, -1));
        if (obtainAttributes.hasValue(4)) {
            parsedProcessImpl.setNativeHeapZeroInitialized(obtainAttributes.getBoolean(4, false) ? 1 : 0);
        }
        obtainAttributes.recycle();
        int depth = xmlResourceParser.getDepth();
        while (true) {
            int next = xmlResourceParser.next();
            if (next == 1 || (next == 3 && xmlResourceParser.getDepth() <= depth)) {
                break;
            }
            if (next != 3 && next != 4) {
                String name = xmlResourceParser.getName();
                name.hashCode();
                if (name.equals("allow-permission")) {
                    parseAllowPermission = parseAllowPermission(parsedProcessImpl.getDeniedPermissions(), resources, xmlResourceParser, parseInput);
                    if (parseAllowPermission.isSuccess()) {
                        parsedProcessImpl.setDeniedPermissions((Set) parseAllowPermission.getResult());
                    }
                } else if (name.equals("deny-permission")) {
                    parseAllowPermission = parseDenyPermission(parsedProcessImpl.getDeniedPermissions(), resources, xmlResourceParser, parseInput);
                    if (parseAllowPermission.isSuccess()) {
                        parsedProcessImpl.setDeniedPermissions((Set) parseAllowPermission.getResult());
                    }
                } else {
                    parseAllowPermission = ParsingUtils.unknownTag("<process>", parsingPackage, xmlResourceParser, parseInput);
                }
                if (parseAllowPermission.isError()) {
                    return parseInput.error(parseAllowPermission);
                }
            }
        }
        return parseInput.success(parsedProcessImpl);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x00ca, code lost:
    
        return r15.success(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0043, code lost:
    
        if (r3.equals("process") == false) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ParseResult<ArrayMap<String, ParsedProcess>> parseProcesses(String[] strArr, ParsingPackage parsingPackage, Resources resources, XmlResourceParser xmlResourceParser, int i, ParseInput parseInput) throws IOException, XmlPullParserException {
        ParseResult parseAllowPermission;
        ArrayMap arrayMap = new ArrayMap();
        int depth = xmlResourceParser.getDepth();
        Set set = null;
        while (true) {
            int next = xmlResourceParser.next();
            char c = 1;
            if (next != 1 && (next != 3 || xmlResourceParser.getDepth() > depth)) {
                if (next != 3 && next != 4) {
                    String name = xmlResourceParser.getName();
                    name.hashCode();
                    switch (name.hashCode()) {
                        case -1239165229:
                            if (name.equals("allow-permission")) {
                                c = 0;
                                break;
                            }
                            break;
                        case -309518737:
                            break;
                        case 1658008624:
                            if (name.equals("deny-permission")) {
                                c = 2;
                                break;
                            }
                            break;
                    }
                    c = 65535;
                    switch (c) {
                        case 0:
                            parseAllowPermission = parseAllowPermission(set, resources, xmlResourceParser, parseInput);
                            if (parseAllowPermission.isSuccess()) {
                                set = (Set) parseAllowPermission.getResult();
                                break;
                            }
                            break;
                        case 1:
                            parseAllowPermission = parseProcess(set, strArr, parsingPackage, resources, xmlResourceParser, i, parseInput);
                            if (parseAllowPermission.isSuccess()) {
                                ParsedProcess parsedProcess = (ParsedProcess) parseAllowPermission.getResult();
                                if (arrayMap.put(parsedProcess.getName(), parsedProcess) != null) {
                                    parseAllowPermission = parseInput.error("<process> specified existing name '" + parsedProcess.getName() + "'");
                                    break;
                                }
                            }
                            break;
                        case 2:
                            parseAllowPermission = parseDenyPermission(set, resources, xmlResourceParser, parseInput);
                            if (parseAllowPermission.isSuccess()) {
                                set = (Set) parseAllowPermission.getResult();
                                break;
                            }
                            break;
                        default:
                            parseAllowPermission = ParsingUtils.unknownTag("<processes>", parsingPackage, xmlResourceParser, parseInput);
                            break;
                    }
                    if (parseAllowPermission.isError()) {
                        return parseInput.error(parseAllowPermission);
                    }
                }
            }
        }
    }
}
