package q4;

import android.graphics.Color;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.util.ArrayList;
import java.util.List;
import n4.GradientColor;
import r4.c;
import s4.MiscUtils;

/* compiled from: GradientColorParser.java */
/* renamed from: q4.p, reason: use source file name */
/* loaded from: classes.dex */
public class GradientColorParser implements ValueParser<GradientColor> {

    /* renamed from: a, reason: collision with root package name */
    private int f16879a;

    public GradientColorParser(int i10) {
        this.f16879a = i10;
    }

    private void b(GradientColor gradientColor, List<Float> list) {
        int i10 = this.f16879a * 4;
        if (list.size() <= i10) {
            return;
        }
        int size = (list.size() - i10) / 2;
        double[] dArr = new double[size];
        double[] dArr2 = new double[size];
        int i11 = 0;
        while (i10 < list.size()) {
            if (i10 % 2 == 0) {
                dArr[i11] = list.get(i10).floatValue();
            } else {
                dArr2[i11] = list.get(i10).floatValue();
                i11++;
            }
            i10++;
        }
        for (int i12 = 0; i12 < gradientColor.c(); i12++) {
            int i13 = gradientColor.a()[i12];
            gradientColor.a()[i12] = Color.argb(c(gradientColor.b()[i12], dArr, dArr2), Color.red(i13), Color.green(i13), Color.blue(i13));
        }
    }

    private int c(double d10, double[] dArr, double[] dArr2) {
        double d11;
        int i10 = 1;
        while (true) {
            if (i10 < dArr.length) {
                int i11 = i10 - 1;
                double d12 = dArr[i11];
                double d13 = dArr[i10];
                if (dArr[i10] >= d10) {
                    d11 = MiscUtils.j(dArr2[i11], dArr2[i10], MiscUtils.b((d10 - d12) / (d13 - d12), UserProfileInfo.Constant.NA_LAT_LON, 1.0d));
                    break;
                }
                i10++;
            } else {
                d11 = dArr2[dArr2.length - 1];
                break;
            }
        }
        return (int) (d11 * 255.0d);
    }

    @Override // q4.ValueParser
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public GradientColor a(r4.c cVar, float f10) {
        ArrayList arrayList = new ArrayList();
        boolean z10 = cVar.e0() == c.b.BEGIN_ARRAY;
        if (z10) {
            cVar.c();
        }
        while (cVar.w()) {
            arrayList.add(Float.valueOf((float) cVar.N()));
        }
        if (z10) {
            cVar.u();
        }
        if (this.f16879a == -1) {
            this.f16879a = arrayList.size() / 4;
        }
        int i10 = this.f16879a;
        float[] fArr = new float[i10];
        int[] iArr = new int[i10];
        int i11 = 0;
        int i12 = 0;
        for (int i13 = 0; i13 < this.f16879a * 4; i13++) {
            int i14 = i13 / 4;
            double floatValue = arrayList.get(i13).floatValue();
            int i15 = i13 % 4;
            if (i15 == 0) {
                if (i14 > 0) {
                    float f11 = (float) floatValue;
                    if (fArr[i14 - 1] >= f11) {
                        fArr[i14] = f11 + 0.01f;
                    }
                }
                fArr[i14] = (float) floatValue;
            } else if (i15 == 1) {
                i11 = (int) (floatValue * 255.0d);
            } else if (i15 == 2) {
                i12 = (int) (floatValue * 255.0d);
            } else if (i15 == 3) {
                iArr[i14] = Color.argb(255, i11, i12, (int) (floatValue * 255.0d));
            }
        }
        GradientColor gradientColor = new GradientColor(fArr, iArr);
        b(gradientColor, arrayList);
        return gradientColor;
    }
}
