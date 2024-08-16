package com.google.gson.internal.bind.util;

import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes.dex */
public class ISO8601Utils {
    private static final String UTC_ID = "UTC";
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(UTC_ID);

    private static boolean checkOffset(String str, int i10, char c10) {
        return i10 < str.length() && str.charAt(i10) == c10;
    }

    public static String format(Date date) {
        return format(date, false, TIMEZONE_UTC);
    }

    private static int indexOfNonDigit(String str, int i10) {
        while (i10 < str.length()) {
            char charAt = str.charAt(i10);
            if (charAt < '0' || charAt > '9') {
                return i10;
            }
            i10++;
        }
        return str.length();
    }

    private static void padInt(StringBuilder sb2, int i10, int i11) {
        String num = Integer.toString(i10);
        for (int length = i11 - num.length(); length > 0; length--) {
            sb2.append('0');
        }
        sb2.append(num);
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x00d3 A[Catch: IndexOutOfBoundsException | NumberFormatException | IllegalArgumentException -> 0x01c0, TryCatch #0 {IndexOutOfBoundsException | NumberFormatException | IllegalArgumentException -> 0x01c0, blocks: (B:3:0x0004, B:5:0x0016, B:6:0x0018, B:8:0x0024, B:9:0x0026, B:11:0x0036, B:13:0x003c, B:18:0x0054, B:20:0x0064, B:21:0x0066, B:23:0x0072, B:24:0x0074, B:26:0x007a, B:30:0x0084, B:35:0x0094, B:37:0x009c, B:42:0x00cd, B:44:0x00d3, B:46:0x00da, B:47:0x0187, B:52:0x00e4, B:53:0x00ff, B:54:0x0100, B:57:0x011c, B:59:0x0129, B:62:0x0132, B:64:0x0151, B:67:0x0160, B:68:0x0182, B:70:0x0185, B:71:0x010b, B:72:0x01b8, B:73:0x01bf, B:74:0x00b4, B:75:0x00b7), top: B:2:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01b8 A[Catch: IndexOutOfBoundsException | NumberFormatException | IllegalArgumentException -> 0x01c0, TryCatch #0 {IndexOutOfBoundsException | NumberFormatException | IllegalArgumentException -> 0x01c0, blocks: (B:3:0x0004, B:5:0x0016, B:6:0x0018, B:8:0x0024, B:9:0x0026, B:11:0x0036, B:13:0x003c, B:18:0x0054, B:20:0x0064, B:21:0x0066, B:23:0x0072, B:24:0x0074, B:26:0x007a, B:30:0x0084, B:35:0x0094, B:37:0x009c, B:42:0x00cd, B:44:0x00d3, B:46:0x00da, B:47:0x0187, B:52:0x00e4, B:53:0x00ff, B:54:0x0100, B:57:0x011c, B:59:0x0129, B:62:0x0132, B:64:0x0151, B:67:0x0160, B:68:0x0182, B:70:0x0185, B:71:0x010b, B:72:0x01b8, B:73:0x01bf, B:74:0x00b4, B:75:0x00b7), top: B:2:0x0004 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Date parse(String str, ParsePosition parsePosition) {
        String str2;
        int i10;
        int i11;
        int i12;
        int i13;
        int length;
        TimeZone timeZone;
        char charAt;
        try {
            int index = parsePosition.getIndex();
            int i14 = index + 4;
            int parseInt = parseInt(str, index, i14);
            if (checkOffset(str, i14, '-')) {
                i14++;
            }
            int i15 = i14 + 2;
            int parseInt2 = parseInt(str, i14, i15);
            if (checkOffset(str, i15, '-')) {
                i15++;
            }
            int i16 = i15 + 2;
            int parseInt3 = parseInt(str, i15, i16);
            boolean checkOffset = checkOffset(str, i16, 'T');
            if (!checkOffset && str.length() <= i16) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(parseInt, parseInt2 - 1, parseInt3);
                gregorianCalendar.setLenient(false);
                parsePosition.setIndex(i16);
                return gregorianCalendar.getTime();
            }
            if (checkOffset) {
                int i17 = i16 + 1;
                int i18 = i17 + 2;
                int parseInt4 = parseInt(str, i17, i18);
                if (checkOffset(str, i18, COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR)) {
                    i18++;
                }
                int i19 = i18 + 2;
                int parseInt5 = parseInt(str, i18, i19);
                if (checkOffset(str, i19, COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR)) {
                    i19++;
                }
                if (str.length() > i19 && (charAt = str.charAt(i19)) != 'Z' && charAt != '+' && charAt != '-') {
                    int i20 = i19 + 2;
                    i13 = parseInt(str, i19, i20);
                    if (i13 > 59 && i13 < 63) {
                        i13 = 59;
                    }
                    if (checkOffset(str, i20, '.')) {
                        int i21 = i20 + 1;
                        int indexOfNonDigit = indexOfNonDigit(str, i21 + 1);
                        int min = Math.min(indexOfNonDigit, i21 + 3);
                        int parseInt6 = parseInt(str, i21, min);
                        int i22 = min - i21;
                        if (i22 == 1) {
                            parseInt6 *= 100;
                        } else if (i22 == 2) {
                            parseInt6 *= 10;
                        }
                        i11 = parseInt5;
                        i12 = parseInt6;
                        i10 = parseInt4;
                        i16 = indexOfNonDigit;
                    } else {
                        i11 = parseInt5;
                        i10 = parseInt4;
                        i16 = i20;
                        i12 = 0;
                    }
                    if (str.length() <= i16) {
                        char charAt2 = str.charAt(i16);
                        if (charAt2 == 'Z') {
                            timeZone = TIMEZONE_UTC;
                            length = i16 + 1;
                        } else {
                            if (charAt2 != '+' && charAt2 != '-') {
                                throw new IndexOutOfBoundsException("Invalid time zone indicator '" + charAt2 + "'");
                            }
                            String substring = str.substring(i16);
                            if (substring.length() < 5) {
                                substring = substring + "00";
                            }
                            length = i16 + substring.length();
                            if (!"+0000".equals(substring) && !"+00:00".equals(substring)) {
                                String str3 = "GMT" + substring;
                                TimeZone timeZone2 = TimeZone.getTimeZone(str3);
                                String id2 = timeZone2.getID();
                                if (!id2.equals(str3) && !id2.replace(":", "").equals(str3)) {
                                    throw new IndexOutOfBoundsException("Mismatching time zone indicator: " + str3 + " given, resolves to " + timeZone2.getID());
                                }
                                timeZone = timeZone2;
                            }
                            timeZone = TIMEZONE_UTC;
                        }
                        GregorianCalendar gregorianCalendar2 = new GregorianCalendar(timeZone);
                        gregorianCalendar2.setLenient(false);
                        gregorianCalendar2.set(1, parseInt);
                        gregorianCalendar2.set(2, parseInt2 - 1);
                        gregorianCalendar2.set(5, parseInt3);
                        gregorianCalendar2.set(11, i10);
                        gregorianCalendar2.set(12, i11);
                        gregorianCalendar2.set(13, i13);
                        gregorianCalendar2.set(14, i12);
                        parsePosition.setIndex(length);
                        return gregorianCalendar2.getTime();
                    }
                    throw new IllegalArgumentException("No time zone indicator");
                }
                i11 = parseInt5;
                i12 = 0;
                i10 = parseInt4;
                i16 = i19;
            } else {
                i10 = 0;
                i11 = 0;
                i12 = 0;
            }
            i13 = 0;
            if (str.length() <= i16) {
            }
        } catch (IndexOutOfBoundsException | NumberFormatException | IllegalArgumentException e10) {
            if (str == null) {
                str2 = null;
            } else {
                str2 = '\"' + str + '\"';
            }
            String message = e10.getMessage();
            if (message == null || message.isEmpty()) {
                message = "(" + e10.getClass().getName() + ")";
            }
            ParseException parseException = new ParseException("Failed to parse date [" + str2 + "]: " + message, parsePosition.getIndex());
            parseException.initCause(e10);
            throw parseException;
        }
    }

    private static int parseInt(String str, int i10, int i11) {
        int i12;
        int i13;
        if (i10 < 0 || i11 > str.length() || i10 > i11) {
            throw new NumberFormatException(str);
        }
        if (i10 < i11) {
            i13 = i10 + 1;
            int digit = Character.digit(str.charAt(i10), 10);
            if (digit < 0) {
                throw new NumberFormatException("Invalid number: " + str.substring(i10, i11));
            }
            i12 = -digit;
        } else {
            i12 = 0;
            i13 = i10;
        }
        while (i13 < i11) {
            int i14 = i13 + 1;
            int digit2 = Character.digit(str.charAt(i13), 10);
            if (digit2 < 0) {
                throw new NumberFormatException("Invalid number: " + str.substring(i10, i11));
            }
            i12 = (i12 * 10) - digit2;
            i13 = i14;
        }
        return -i12;
    }

    public static String format(Date date, boolean z10) {
        return format(date, z10, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean z10, TimeZone timeZone) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone, Locale.US);
        gregorianCalendar.setTime(date);
        StringBuilder sb2 = new StringBuilder(19 + (z10 ? 4 : 0) + (timeZone.getRawOffset() == 0 ? 1 : 6));
        padInt(sb2, gregorianCalendar.get(1), 4);
        sb2.append('-');
        padInt(sb2, gregorianCalendar.get(2) + 1, 2);
        sb2.append('-');
        padInt(sb2, gregorianCalendar.get(5), 2);
        sb2.append('T');
        padInt(sb2, gregorianCalendar.get(11), 2);
        sb2.append(COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        padInt(sb2, gregorianCalendar.get(12), 2);
        sb2.append(COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        padInt(sb2, gregorianCalendar.get(13), 2);
        if (z10) {
            sb2.append('.');
            padInt(sb2, gregorianCalendar.get(14), 3);
        }
        int offset = timeZone.getOffset(gregorianCalendar.getTimeInMillis());
        if (offset != 0) {
            int i10 = offset / 60000;
            int abs = Math.abs(i10 / 60);
            int abs2 = Math.abs(i10 % 60);
            sb2.append(offset >= 0 ? '+' : '-');
            padInt(sb2, abs, 2);
            sb2.append(COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            padInt(sb2, abs2, 2);
        } else {
            sb2.append('Z');
        }
        return sb2.toString();
    }
}
