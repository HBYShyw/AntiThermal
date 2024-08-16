package oplus.util;

/* loaded from: classes.dex */
public class OplusDisplayUtils {
    private static final int[] DENSITIES = {480, 320, 1, 0};
    public static final int DENSITY_NONE = 1;

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int[] getBestDensityOrder(int density) {
        int[] iArr;
        if (density <= 1) {
            return DENSITIES;
        }
        int i = 0;
        int k = -1;
        int j = 0;
        while (true) {
            iArr = DENSITIES;
            if (j >= iArr.length) {
                break;
            }
            int i2 = iArr[j];
            if (density > i2) {
                i = 0 + 1;
                k = j;
                break;
            }
            if (density != i2) {
                j++;
            } else {
                k = j;
                break;
            }
        }
        int j2 = iArr.length;
        int[] array = new int[j2 + i];
        switch (k) {
            case 0:
                if (i == 0) {
                    return iArr;
                }
                array[k] = density;
                while (i < array.length) {
                    array[i] = DENSITIES[i - 1];
                    i++;
                }
                return array;
            case 1:
            case 2:
                int len = array.length;
                array[0] = density;
                array[len - 1] = 0;
                array[len - 2] = 1;
                if (i == 0) {
                    array[i + 1] = iArr[i];
                } else {
                    array[i] = iArr[i];
                    array[i + 1] = iArr[i - 1];
                }
                return array;
            default:
                return array;
        }
    }

    public static String getDensityName(int density) {
        switch (density) {
            case 1:
                return "nodpi";
            case 120:
                return "ldpi";
            case 160:
                return "mdpi";
            case 240:
                return "hdpi";
            case 320:
                return "xhdpi";
            case 480:
                return "xxhdpi";
            case 640:
                return "xxxhdpi";
            default:
                return "";
        }
    }

    public static String getDensitySuffix(int i) {
        String s = getDensityName(i);
        if (!s.equals("")) {
            return "-" + s;
        }
        return s;
    }

    public static String getDrawbleDensityFolder(int i) {
        return "res/" + getDrawbleDensityName(i);
    }

    public static String getDrawbleDensityName(int i) {
        return "drawable" + getDensitySuffix(i);
    }
}
