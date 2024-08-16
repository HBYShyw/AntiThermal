package vendor.pixelworks.hardware.display.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class PanelType {
    public static final int LCD_P3 = 1;
    public static final int LCD_SRGB = 0;
    public static final int OLED = 2;

    public static final String toString(int i) {
        if (i == 0) {
            return "LCD_SRGB";
        }
        if (i == 1) {
            return "LCD_P3";
        }
        if (i == 2) {
            return "OLED";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("LCD_SRGB");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("LCD_P3");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("OLED");
            i2 |= 2;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
