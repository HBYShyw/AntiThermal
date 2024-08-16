package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.os.Parcelable;
import androidx.versionedparcelable.VersionedParcel;

/* loaded from: classes.dex */
public class IconCompatParcelizer {
    public static IconCompat read(VersionedParcel versionedParcel) {
        IconCompat iconCompat = new IconCompat();
        iconCompat.f2195a = versionedParcel.p(iconCompat.f2195a, 1);
        iconCompat.f2197c = versionedParcel.j(iconCompat.f2197c, 2);
        iconCompat.f2198d = versionedParcel.r(iconCompat.f2198d, 3);
        iconCompat.f2199e = versionedParcel.p(iconCompat.f2199e, 4);
        iconCompat.f2200f = versionedParcel.p(iconCompat.f2200f, 5);
        iconCompat.f2201g = (ColorStateList) versionedParcel.r(iconCompat.f2201g, 6);
        iconCompat.f2203i = versionedParcel.t(iconCompat.f2203i, 7);
        iconCompat.f2204j = versionedParcel.t(iconCompat.f2204j, 8);
        iconCompat.d();
        return iconCompat;
    }

    public static void write(IconCompat iconCompat, VersionedParcel versionedParcel) {
        versionedParcel.x(true, true);
        iconCompat.e(versionedParcel.f());
        int i10 = iconCompat.f2195a;
        if (-1 != i10) {
            versionedParcel.F(i10, 1);
        }
        byte[] bArr = iconCompat.f2197c;
        if (bArr != null) {
            versionedParcel.B(bArr, 2);
        }
        Parcelable parcelable = iconCompat.f2198d;
        if (parcelable != null) {
            versionedParcel.H(parcelable, 3);
        }
        int i11 = iconCompat.f2199e;
        if (i11 != 0) {
            versionedParcel.F(i11, 4);
        }
        int i12 = iconCompat.f2200f;
        if (i12 != 0) {
            versionedParcel.F(i12, 5);
        }
        ColorStateList colorStateList = iconCompat.f2201g;
        if (colorStateList != null) {
            versionedParcel.H(colorStateList, 6);
        }
        String str = iconCompat.f2203i;
        if (str != null) {
            versionedParcel.J(str, 7);
        }
        String str2 = iconCompat.f2204j;
        if (str2 != null) {
            versionedParcel.J(str2, 8);
        }
    }
}
