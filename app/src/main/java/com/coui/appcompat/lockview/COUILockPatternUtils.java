package com.coui.appcompat.lockview;

import android.util.Log;
import com.coui.appcompat.lockview.COUILockPatternView;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/* compiled from: COUILockPatternUtils.java */
/* renamed from: com.coui.appcompat.lockview.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUILockPatternUtils {
    public static String a(List<COUILockPatternView.Cell> list) {
        if (list == null) {
            return "";
        }
        int size = list.size();
        byte[] bArr = new byte[size];
        for (int i10 = 0; i10 < size; i10++) {
            COUILockPatternView.Cell cell = list.get(i10);
            bArr[i10] = (byte) ((cell.getRow() * 3) + cell.getColumn() + 49);
        }
        try {
            return new String(bArr, "UTF-8");
        } catch (UnsupportedEncodingException e10) {
            Log.e("COUILockPatternUtils", "patternToString e:" + e10.getMessage());
            e10.printStackTrace();
            return null;
        }
    }

    public static List<COUILockPatternView.Cell> b(String str) {
        byte[] bArr = null;
        if (str == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        try {
            bArr = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e10) {
            Log.e("COUILockPatternUtils", "stringToPattern e:" + e10.getMessage());
            e10.printStackTrace();
        }
        for (byte b10 : bArr) {
            byte b11 = (byte) (b10 - 49);
            arrayList.add(COUILockPatternView.Cell.e(b11 / 3, b11 % 3));
        }
        return arrayList;
    }
}
