package c2;

import android.util.Log;
import com.coui.appcompat.calendar.COUIDateMonthView;
import com.coui.appcompat.picker.COUILunarDatePicker;
import com.oplus.statistics.util.TimeInfoUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/* compiled from: COUILunarUtil.java */
/* renamed from: c2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUILunarUtil {

    /* renamed from: a, reason: collision with root package name */
    private static final String[] f4753a = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

    /* renamed from: b, reason: collision with root package name */
    private static final String[] f4754b = {"小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"};

    /* renamed from: c, reason: collision with root package name */
    private static final String[] f4755c = {"小寒", "大寒", "立春", "雨水", "驚蟄", "春分", "清明", "穀雨", "立夏", "小滿", "芒種", "夏至", "小暑", "大暑", "立秋", "處暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"};

    /* renamed from: d, reason: collision with root package name */
    private static final long[] f4756d = {19416, 19168, 42352, 21717, 53856, 55632, 91476, 22176, 39632, 21970, 19168, 42422, 42192, 53840, 119381, 46400, 54944, 44450, 38320, 84343, 18800, 42160, 46261, 27216, 27968, 109396, 11104, 38256, 21234, 18800, 25958, 54432, 59984, 92821, 23248, 11104, 100067, 37600, 116951, 51536, 54432, 120998, 46416, 22176, 107956, 9680, 37584, 53938, 43344, 46423, 27808, 46416, 86869, 19872, 42416, 83315, 21168, 43432, 59728, 27296, 44710, 43856, 19296, 43748, 42352, 21088, 62051, 55632, 23383, 22176, 38608, 19925, 19152, 42192, 54484, 53840, 54616, 46400, 46752, 103846, 38320, 18864, 43380, 42160, 45690, 27216, 27968, 44870, 43872, 38256, 19189, 18800, 25776, 29859, 59984, 27480, 23232, 43872, 38613, 37600, 51552, 55636, 54432, 55888, 30034, 22176, 43959, 9680, 37584, 51893, 43344, 46240, 47780, 44368, 21977, 19360, 42416, 86390, 21168, 43312, 31060, 27296, 44368, 23378, 19296, 42726, 42208, 53856, 60005, 54576, 23200, 30371, 38608, 19195, 19152, 42192, 118966, 53840, 54560, 56645, 46496, 22224, 21938, 18864, 42359, 42160, 43600, 111189, 27936, 44448};

    /* renamed from: e, reason: collision with root package name */
    private static final int[][] f4757e = {new int[]{6, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 9, 24, 8, 23, 7, 22}, new int[]{6, 21, 4, 19, 6, 21, 5, 21, 6, 22, 6, 22, 8, 23, 8, 24, 8, 24, 9, 24, 8, 23, 8, 22}, new int[]{6, 21, 5, 19, 5, 20, 5, 20, 5, 21, 5, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 21, 7, 23, 8, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{6, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 9, 24, 8, 22, 7, 22}, new int[]{6, 20, 4, 19, 6, 21, 5, 21, 6, 22, 6, 22, 8, 23, 8, 24, 8, 24, 9, 24, 8, 23, 8, 22}, new int[]{6, 21, 4, 19, 5, 20, 4, 20, 5, 21, 5, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 5, 21, 5, 20, 5, 21, 6, 21, 7, 23, 7, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 8, 24, 8, 23, 7, 22}, new int[]{6, 20, 4, 19, 6, 21, 5, 21, 6, 22, 6, 22, 8, 23, 8, 24, 8, 23, 9, 24, 8, 23, 8, 22}, new int[]{6, 21, 4, 19, 5, 20, 4, 20, 5, 21, 5, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 4, 19, 5, 21, 5, 20, 5, 21, 6, 21, 7, 23, 7, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 8, 24, 8, 22, 7, 22}, new int[]{6, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 8, 23, 8, 24, 8, 23, 9, 24, 8, 23, 8, 22}, new int[]{6, 21, 4, 19, 5, 20, 4, 20, 5, 21, 5, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 4, 19, 5, 20, 5, 20, 5, 21, 6, 21, 7, 23, 7, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 8, 24, 8, 22, 7, 22}, new int[]{6, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 8, 23, 8, 24, 8, 23, 9, 24, 8, 23, 7, 22}, new int[]{6, 21, 4, 19, 5, 20, 4, 20, 5, 21, 5, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 4, 18, 5, 20, 5, 20, 5, 21, 6, 21, 7, 23, 7, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 21, 7, 23, 8, 23, 8, 23, 8, 24, 8, 22, 7, 22}, new int[]{6, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 9, 24, 8, 23, 7, 22}, new int[]{6, 21, 4, 19, 5, 20, 4, 20, 5, 21, 5, 21, 7, 22, 7, 23, 7, 23, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 4, 18, 5, 20, 5, 20, 5, 21, 6, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{6, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 9, 24, 8, 23, 7, 22}, new int[]{6, 21, 4, 19, 5, 20, 4, 20, 5, 21, 5, 21, 7, 22, 7, 23, 7, 23, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 4, 18, 5, 20, 5, 20, 5, 21, 6, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 21, 7, 23, 8, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{6, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 9, 24, 8, 23, 7, 22}, new int[]{6, 21, 4, 19, 5, 20, 4, 20, 5, 21, 5, 21, 7, 22, 7, 23, 7, 23, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 4, 18, 5, 20, 5, 20, 5, 21, 5, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 21, 7, 23, 8, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{6, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 9, 24, 8, 23, 7, 22}, new int[]{6, 21, 4, 19, 5, 20, 4, 20, 5, 21, 5, 21, 7, 22, 7, 23, 7, 23, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 4, 18, 5, 20, 5, 20, 5, 21, 5, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 21, 7, 23, 8, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{6, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 9, 24, 8, 22, 7, 22}, new int[]{6, 20, 4, 19, 5, 20, 4, 20, 5, 21, 5, 21, 7, 22, 7, 23, 7, 23, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 3, 18, 5, 20, 4, 20, 5, 21, 5, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 5, 21, 6, 21, 7, 23, 7, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{6, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 8, 24, 8, 23, 7, 22}, new int[]{6, 21, 4, 19, 5, 20, 4, 20, 5, 20, 5, 21, 7, 22, 7, 23, 7, 22, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 4, 18, 5, 20, 4, 20, 5, 21, 5, 21, 7, 22, 7, 23, 7, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 5, 21, 5, 20, 5, 21, 6, 21, 7, 23, 7, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 8, 24, 8, 22, 7, 22}, new int[]{6, 20, 4, 19, 5, 20, 4, 19, 5, 20, 5, 21, 7, 22, 7, 23, 7, 22, 8, 23, 7, 22, 6, 21}, new int[]{5, 20, 3, 18, 5, 20, 4, 20, 5, 21, 5, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 4, 18, 5, 20, 5, 20, 5, 21, 6, 21, 7, 23, 7, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 21, 7, 23, 8, 23, 8, 23, 8, 24, 8, 22, 7, 22}, new int[]{6, 20, 4, 19, 5, 20, 4, 19, 5, 20, 5, 21, 7, 22, 7, 23, 7, 22, 8, 23, 7, 22, 6, 21}, new int[]{5, 20, 3, 18, 5, 20, 4, 20, 5, 21, 5, 21, 7, 22, 7, 23, 7, 23, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 4, 18, 5, 20, 5, 20, 5, 21, 6, 21, 7, 23, 7, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 22, 7, 23, 8, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{6, 20, 4, 19, 5, 20, 4, 19, 5, 20, 5, 21, 6, 22, 7, 23, 7, 22, 8, 23, 7, 22, 6, 21}, new int[]{5, 20, 3, 18, 5, 20, 4, 20, 5, 21, 5, 21, 7, 22, 7, 23, 7, 23, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 4, 18, 5, 20, 5, 20, 5, 21, 6, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 21, 7, 23, 8, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{6, 20, 4, 19, 5, 20, 4, 19, 5, 20, 5, 21, 6, 22, 7, 22, 7, 22, 8, 23, 7, 22, 6, 21}, new int[]{5, 20, 3, 18, 5, 20, 4, 20, 5, 21, 5, 21, 7, 22, 7, 23, 7, 23, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 4, 18, 5, 20, 5, 20, 5, 21, 6, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 6, 21, 6, 21, 7, 23, 8, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{6, 20, 4, 19, 5, 20, 4, 19, 5, 20, 5, 21, 6, 22, 7, 22, 7, 22, 8, 23, 7, 22, 6, 21}, new int[]{5, 20, 3, 18, 5, 20, 4, 20, 5, 21, 5, 21, 7, 22, 7, 23, 7, 23, 8, 23, 7, 22, 7, 21}, new int[]{5, 20, 4, 18, 5, 20, 5, 20, 5, 21, 5, 21, 7, 23, 7, 23, 7, 23, 8, 23, 7, 22, 7, 22}, new int[]{5, 20, 4, 19, 6, 21, 5, 20, 5, 21, 6, 21, 7, 23, 7, 23, 8, 23, 8, 23, 7, 22, 7, 22}, new int[]{6, 20, 4, 19, 5, 20, 4, 19, 5, 20, 5, 21, 6, 22, 7, 22, 7, 22, 8, 23, 7, 22, 6, 21}};

    /* renamed from: f, reason: collision with root package name */
    private static SimpleDateFormat f4758f = new SimpleDateFormat("yyyy年MM月dd日");

    public static int[] a(int i10, int i11, int i12) {
        Date date;
        int[] iArr = {2000, 1, 1, 1};
        if (i10 == Integer.MIN_VALUE) {
            iArr[0] = i10;
            int i13 = i11 - 1;
            iArr[1] = (i13 % 12) + 1;
            iArr[2] = i12;
            iArr[3] = i13 / 12 <= 0 ? 1 : 0;
            return iArr;
        }
        Date date2 = null;
        try {
            date = f4758f.parse("1900年1月31日");
        } catch (ParseException e10) {
            Log.e("COUILunar", "calculateLunarByGregorian(),parse baseDate error.");
            e10.printStackTrace();
            date = null;
        }
        if (date == null) {
            Log.e("COUILunar", "baseDate is null,return lunar date:2000.1.1");
            return iArr;
        }
        try {
            date2 = f4758f.parse(i10 + "年" + i11 + "月" + i12 + "日");
        } catch (ParseException e11) {
            Log.e("COUILunar", "calculateLunarByGregorian(),parse currentDate error.");
            e11.printStackTrace();
        }
        if (date2 == null) {
            Log.e("COUILunar", "currentDate is null,return lunar date:2000.1.1");
            return iArr;
        }
        int round = Math.round(((float) (date2.getTime() - date.getTime())) / 8.64E7f);
        int i14 = COUIDateMonthView.MIN_YEAR;
        int i15 = 0;
        while (i14 < 10000 && round > 0) {
            i15 = h(i14);
            round -= i15;
            i14++;
        }
        if (round < 0) {
            round += i15;
            i14--;
        }
        int k10 = k(i14);
        int i16 = 0;
        int i17 = 0;
        int i18 = 1;
        while (i18 < 13 && round > 0) {
            if (k10 > 0 && i18 == k10 + 1 && i16 == 0) {
                i18--;
                i17 = g(i14);
                i16 = 1;
            } else {
                i17 = f(i14, i18);
            }
            round -= i17;
            if (i16 != 0 && i18 == k10 + 1) {
                i16 = 0;
            }
            i18++;
        }
        if (round == 0 && k10 > 0 && i18 == k10 + 1) {
            if (i16 != 0) {
                i16 = 0;
            } else {
                i18--;
                i16 = 1;
            }
        }
        if (round < 0) {
            round += i17;
            i18--;
        }
        iArr[0] = i14;
        iArr[1] = i18;
        iArr[2] = round + 1;
        iArr[3] = i16 ^ 1;
        return iArr;
    }

    public static COUILunarDatePicker.c b(int i10, int i11, int i12, int i13) {
        int[] e10 = e(i10, i11, i13);
        Date l10 = l(i10, e10[0], d(i10, e10[0], i12, e10[1] == 0), e10[1] == 0);
        COUILunarDatePicker.c cVar = new COUILunarDatePicker.c();
        if (l10 != null) {
            cVar.n(l10.getTime());
        }
        return cVar;
    }

    public static String c(int i10) {
        String[] strArr = {"初", "十", "廿", "卅"};
        int i11 = i10 % 10;
        int i12 = i11 == 0 ? 9 : i11 - 1;
        if (i10 > 30) {
            return "";
        }
        if (i10 == 10) {
            return "初十";
        }
        if (i10 == 20) {
            return "二十";
        }
        if (i10 == 30) {
            return "三十";
        }
        return strArr[i10 / 10] + f4753a[i12];
    }

    public static int d(int i10, int i11, int i12, boolean z10) {
        int g6;
        if (!z10) {
            g6 = f(i10, i11);
        } else {
            g6 = g(i10);
        }
        return i12 > g6 ? g6 : i12;
    }

    private static int[] e(int i10, int i11, int i12) {
        return new int[]{i11, ((i12 == 0 && k(i10) == i11) ? 1 : 0) ^ 1};
    }

    public static int f(int i10, int i11) {
        if (i10 != Integer.MIN_VALUE && i10 >= 1900) {
            if (((65536 >> i11) & f4756d[i10 - COUIDateMonthView.MIN_YEAR]) == 0) {
                return 29;
            }
        }
        return 30;
    }

    public static int g(int i10) {
        if (k(i10) != 0) {
            return (f4756d[i10 + (-1900)] & 65536) != 0 ? 30 : 29;
        }
        return 0;
    }

    public static int h(int i10) {
        if (i10 == Integer.MIN_VALUE) {
            return 0;
        }
        int i11 = 348;
        for (int i12 = 32768; i12 > 8; i12 >>= 1) {
            int i13 = i10 - 1900;
            if (i13 >= 0) {
                long[] jArr = f4756d;
                if (i13 < jArr.length && (jArr[i13] & i12) != 0) {
                    i11++;
                }
            }
        }
        return i11 + g(i10);
    }

    private static int i(int i10) {
        int i11 = 348;
        for (int i12 = 32768; i12 >= 8; i12 >>= 1) {
            if ((f4756d[i10 - 1900] & 65520 & i12) != 0) {
                i11++;
            }
        }
        return i11 + g(i10);
    }

    private static boolean j(int i10, int i11, int i12, boolean z10) {
        if (i10 < 1900 || i10 > 2049 || i11 < 1 || i11 > 12 || i12 < 1 || i12 > 30) {
            return false;
        }
        return !z10 || i11 == k(i10);
    }

    public static int k(int i10) {
        if (i10 >= 1900 && i10 <= 2100) {
            return (int) (f4756d[i10 - COUIDateMonthView.MIN_YEAR] & 15);
        }
        Log.e("COUILunar", "get leapMonth:" + i10 + "is out of range.return 0.");
        return 0;
    }

    public static Date l(int i10, int i11, int i12, boolean z10) {
        if (!j(i10, i11, i12, z10)) {
            return null;
        }
        int i13 = 0;
        for (int i14 = COUIDateMonthView.MIN_YEAR; i14 < i10; i14++) {
            i13 += i(i14);
        }
        int k10 = k(i10);
        if (z10 && k10 != i11) {
            return null;
        }
        int i15 = 1;
        if (k10 == 0 || i11 < k10 || (i11 == k10 && !z10)) {
            while (i15 < i11) {
                i13 += f(i10, i15);
                i15++;
            }
            if (i12 > f(i10, i11)) {
                return null;
            }
        } else {
            while (i15 < i11) {
                i13 += f(i10, i15);
                i15++;
            }
            if (i11 > k10) {
                i13 += g(i10);
                if (i12 > f(i10, i11)) {
                    return null;
                }
            } else {
                i13 += f(i10, i11);
                if (i12 > g(i10)) {
                    return null;
                }
            }
        }
        int i16 = i13 + i12;
        try {
            Date parse = new SimpleDateFormat(TimeInfoUtil.TIME_PATTERN_03).parse("19000130");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            calendar.add(5, i16);
            return calendar.getTime();
        } catch (ParseException e10) {
            e10.printStackTrace();
            return null;
        }
    }
}
