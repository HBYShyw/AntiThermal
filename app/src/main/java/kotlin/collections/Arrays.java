package kotlin.collections;

import ma.UByteArray;
import ma.UIntArray;
import ma.ULongArray;
import ma.UShortArray;
import oa._UArrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Arrays.kt */
/* renamed from: kotlin.collections.l, reason: use source file name */
/* loaded from: classes2.dex */
public class Arrays extends ArraysJVM {
    /* JADX WARN: Multi-variable type inference failed */
    public static <T> boolean c(T[] tArr, T[] tArr2) {
        boolean d10;
        boolean b10;
        boolean a10;
        boolean c10;
        boolean c11;
        if (tArr == tArr2) {
            return true;
        }
        if (tArr == 0 || tArr2 == 0 || tArr.length != tArr2.length) {
            return false;
        }
        int length = tArr.length;
        for (int i10 = 0; i10 < length; i10++) {
            Object[] objArr = tArr[i10];
            Object[] objArr2 = tArr2[i10];
            if (objArr != objArr2) {
                if (objArr == 0 || objArr2 == 0) {
                    return false;
                }
                if ((objArr instanceof Object[]) && (objArr2 instanceof Object[])) {
                    c11 = c(objArr, objArr2);
                    if (!c11) {
                        return false;
                    }
                } else if ((objArr instanceof byte[]) && (objArr2 instanceof byte[])) {
                    if (!java.util.Arrays.equals((byte[]) objArr, (byte[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof short[]) && (objArr2 instanceof short[])) {
                    if (!java.util.Arrays.equals((short[]) objArr, (short[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof int[]) && (objArr2 instanceof int[])) {
                    if (!java.util.Arrays.equals((int[]) objArr, (int[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof long[]) && (objArr2 instanceof long[])) {
                    if (!java.util.Arrays.equals((long[]) objArr, (long[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof float[]) && (objArr2 instanceof float[])) {
                    if (!java.util.Arrays.equals((float[]) objArr, (float[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof double[]) && (objArr2 instanceof double[])) {
                    if (!java.util.Arrays.equals((double[]) objArr, (double[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof char[]) && (objArr2 instanceof char[])) {
                    if (!java.util.Arrays.equals((char[]) objArr, (char[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof boolean[]) && (objArr2 instanceof boolean[])) {
                    if (!java.util.Arrays.equals((boolean[]) objArr, (boolean[]) objArr2)) {
                        return false;
                    }
                } else if ((objArr instanceof UByteArray) && (objArr2 instanceof UByteArray)) {
                    c10 = _UArrays.c(((UByteArray) objArr).o(), ((UByteArray) objArr2).o());
                    if (!c10) {
                        return false;
                    }
                } else if ((objArr instanceof UShortArray) && (objArr2 instanceof UShortArray)) {
                    a10 = _UArrays.a(((UShortArray) objArr).o(), ((UShortArray) objArr2).o());
                    if (!a10) {
                        return false;
                    }
                } else if ((objArr instanceof UIntArray) && (objArr2 instanceof UIntArray)) {
                    b10 = _UArrays.b(((UIntArray) objArr).o(), ((UIntArray) objArr2).o());
                    if (!b10) {
                        return false;
                    }
                } else if ((objArr instanceof ULongArray) && (objArr2 instanceof ULongArray)) {
                    d10 = _UArrays.d(((ULongArray) objArr).o(), ((ULongArray) objArr2).o());
                    if (!d10) {
                        return false;
                    }
                } else if (!za.k.a(objArr, objArr2)) {
                    return false;
                }
            }
        }
        return true;
    }
}
