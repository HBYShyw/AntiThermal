package com.android.internal.telephony;

import android.text.TextUtils;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.lang.Character;

/* loaded from: classes.dex */
public class OplusGsmAlphabet {
    private static final String TAG = "GSM";
    private static boolean is0X80coding = false;
    private static boolean is0X81coding = false;
    private static boolean is0X82coding = false;
    private static int max;
    private static int min;

    public static byte[] stringToGsm8BitOrUCSPackedForADN(String s) {
        if (s == null) {
            return null;
        }
        try {
            int septets = countGsmSeptets(s, true, 1);
            byte[] ret = new byte[septets];
            GsmAlphabet.stringToGsm8BitUnpackedField(s, ret, 0, ret.length);
            return ret;
        } catch (EncodeException e) {
            try {
                byte[] temp = s.getBytes("utf-16be");
                byte[] ret2 = new byte[temp.length / 2];
                judge(temp, 0, temp.length);
                return ucs2ToAlphaField(temp, 0, temp.length, 0, ret2);
            } catch (UnsupportedEncodingException ex) {
                Log.e(TAG, "unsurport encoding.", ex);
                return null;
            }
        }
    }

    public static void judge(byte[] src, int srcOff, int srcLen) {
        min = 32767;
        max = 0;
        if (srcLen >= 2) {
            int i = 0;
            while (true) {
                if (i >= srcLen) {
                    break;
                }
                if (src[srcOff + i] != 0) {
                    int temp = ((src[srcOff + i] << 8) & 65280) | (src[srcOff + i + 1] & 255);
                    if (min > temp) {
                        min = temp;
                    }
                    if (max < temp) {
                        max = temp;
                    }
                } else if ((src[srcOff + i + 1] & 128) != 0) {
                    max = min + 130;
                    break;
                }
                i += 2;
            }
        }
        int i2 = max;
        int i3 = min;
        if (i2 - i3 < 129) {
            if (((byte) (i3 & 128)) == ((byte) (i2 & 128))) {
                is0X81coding = true;
                is0X82coding = false;
                is0X80coding = false;
                return;
            } else {
                is0X82coding = true;
                is0X81coding = false;
                is0X80coding = false;
                return;
            }
        }
        is0X80coding = true;
        is0X82coding = false;
        is0X81coding = false;
    }

    public static byte[] ucs2ToAlphaField(byte[] src, int srcOff, int srcLen, int destOff, byte[] dest) {
        int outOff = 0;
        if (!is0X80coding) {
            if (is0X81coding) {
                dest = new byte[(srcLen / 2) + 3];
                dest[destOff + 1] = (byte) (srcLen / 2);
                dest[destOff] = -127;
                int i = min & 32640;
                min = i;
                dest[destOff + 2] = (byte) ((i >> 7) & 255);
                outOff = destOff + 3;
            } else if (is0X82coding) {
                dest = new byte[(srcLen / 2) + 4];
                dest[destOff + 1] = (byte) (srcLen / 2);
                dest[destOff] = -126;
                int i2 = min;
                dest[destOff + 2] = (byte) ((i2 >> 8) & 255);
                dest[destOff + 3] = (byte) (i2 & 255);
                outOff = destOff + 4;
            }
            for (int i3 = 0; i3 < srcLen; i3 += 2) {
                if (src[srcOff + i3] == 0) {
                    dest[outOff] = (byte) (src[srcOff + i3 + 1] & Byte.MAX_VALUE);
                } else {
                    int temp = (((src[srcOff + i3] << 8) & 65280) | (src[(srcOff + i3) + 1] & 255)) - min;
                    dest[outOff] = (byte) (temp | 128);
                }
                outOff++;
            }
            return dest;
        }
        int i4 = srcLen + 1;
        byte[] dest2 = new byte[i4];
        dest2[destOff] = Byte.MIN_VALUE;
        System.arraycopy(src, 0, dest2, 1, srcLen);
        return dest2;
    }

    public static boolean enableToEncode0X80() {
        return is0X80coding;
    }

    public static boolean enableToEncode0X81() {
        return is0X81coding;
    }

    public static boolean enableToEncode0X82() {
        return is0X82coding;
    }

    public static int countGsmSeptets(CharSequence s, boolean throwsException, int rfu) throws EncodeException {
        int sz = s.length();
        int count = 0;
        for (int charIndex = 0; charIndex < sz; charIndex++) {
            count += GsmAlphabet.countGsmSeptets(s.charAt(charIndex), throwsException);
        }
        return count;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS && ub != Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS && ub != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A && ub != Character.UnicodeBlock.GENERAL_PUNCTUATION && ub != Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION && ub != Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return false;
        }
        return true;
    }

    public static boolean containChinese(String strName) {
        if (TextUtils.isEmpty(strName)) {
            return false;
        }
        char[] ch = strName.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEnglish(String s) {
        int sz = s.length();
        for (int i = 0; i < sz; i++) {
            char c = s.charAt(i);
            if (c < '!' || c > '~') {
                return false;
            }
        }
        return true;
    }

    public static boolean isThai(String s) {
        int sz = s.length();
        boolean ret = true;
        boolean hasThai = false;
        for (int i = 0; i < sz; i++) {
            char c = s.charAt(i);
            if (c < '!' || c > '~') {
                if (c >= 3585 && c <= 3673) {
                    hasThai = true;
                } else {
                    ret = false;
                    break;
                }
            }
        }
        return ret && hasThai;
    }

    public static boolean isRussian(String s) {
        int sz = s.length();
        boolean ret = true;
        boolean hasRussian = false;
        for (int i = 0; i < sz; i++) {
            char c = s.charAt(i);
            if (c < '!' || c > '~') {
                if (c >= 1024 && c <= 1279) {
                    hasRussian = true;
                } else {
                    ret = false;
                    break;
                }
            }
        }
        return ret && hasRussian;
    }

    public static boolean enableEncodeTo0x81(String s) {
        if (containChinese(s)) {
            return false;
        }
        if (!isThai(s) && !isRussian(s)) {
            return false;
        }
        return true;
    }

    public static byte[] encodeTo0x81(String src) {
        byte data;
        byte base = 0;
        int len = src.length();
        byte[] b0x81 = new byte[len + 3];
        b0x81[0] = -127;
        b0x81[1] = (byte) len;
        for (int i = 0; i < len; i++) {
            String temp = src.substring(i, i + 1);
            try {
                byte[] bytes = temp.getBytes("utf-16be");
                if (!isEnglish(temp)) {
                    if (base == 0) {
                        base = (byte) (bytes[0] << 1);
                        b0x81[2] = base;
                    }
                    data = bytes[1];
                    if ((data & 128) == 0) {
                        data = (byte) (data | 128);
                    }
                } else {
                    data = bytes[1];
                }
                b0x81[3 + i] = data;
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "encodeTo0x81() : unsurport encoding of " + temp, e);
                return null;
            }
        }
        return b0x81;
    }
}
