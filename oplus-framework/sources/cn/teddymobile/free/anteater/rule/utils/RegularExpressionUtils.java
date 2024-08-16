package cn.teddymobile.free.anteater.rule.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class RegularExpressionUtils {
    public static List<String> captureAll(String string, String patternString) {
        List<String> captureList = new ArrayList<>();
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            if (matcher.groupCount() == 1) {
                captureList.add(matcher.group(1));
            }
        }
        return captureList;
    }

    public static String capture(String string, String patternString) {
        if (string != null && patternString != null) {
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                if (matcher.groupCount() == 1) {
                    return matcher.group(1);
                }
            }
            return null;
        }
        return null;
    }
}
