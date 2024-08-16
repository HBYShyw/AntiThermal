package n1;

import i3.COUIDisplayUtil;

/* compiled from: COUIBannerUtil.java */
/* renamed from: n1.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUIBannerUtil {

    /* renamed from: a, reason: collision with root package name */
    public static final int f15630a = COUIDisplayUtil.a(24);

    public static int a(boolean z10, int i10, int i11) {
        if (!z10) {
            return i10;
        }
        if (i10 == 0) {
            return i11 - 1;
        }
        if (i10 == i11 + 1) {
            return 0;
        }
        return i10 - 1;
    }
}
