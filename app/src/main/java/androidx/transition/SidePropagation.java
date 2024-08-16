package androidx.transition;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

/* compiled from: SidePropagation.java */
/* renamed from: androidx.transition.p, reason: use source file name */
/* loaded from: classes.dex */
public class SidePropagation extends VisibilityPropagation {

    /* renamed from: b, reason: collision with root package name */
    private float f4126b = 3.0f;

    /* renamed from: c, reason: collision with root package name */
    private int f4127c = 80;

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0017, code lost:
    
        r5 = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0026, code lost:
    
        if ((androidx.core.view.ViewCompat.x(r6) == 1) != false) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0013, code lost:
    
        if ((androidx.core.view.ViewCompat.x(r6) == 1) != false) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0015, code lost:
    
        r5 = 5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int h(View view, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
        int i18 = this.f4127c;
        if (i18 != 8388611) {
            if (i18 == 8388613) {
            }
        }
        if (i18 == 3) {
            return (i16 - i10) + Math.abs(i13 - i11);
        }
        if (i18 == 5) {
            return (i10 - i14) + Math.abs(i13 - i11);
        }
        if (i18 == 48) {
            return (i17 - i11) + Math.abs(i12 - i10);
        }
        if (i18 != 80) {
            return 0;
        }
        return (i11 - i15) + Math.abs(i12 - i10);
    }

    private int i(ViewGroup viewGroup) {
        int i10 = this.f4127c;
        if (i10 != 3 && i10 != 5 && i10 != 8388611 && i10 != 8388613) {
            return viewGroup.getHeight();
        }
        return viewGroup.getWidth();
    }

    @Override // androidx.transition.TransitionPropagation
    public long c(ViewGroup viewGroup, Transition transition, TransitionValues transitionValues, TransitionValues transitionValues2) {
        int i10;
        int i11;
        int i12;
        TransitionValues transitionValues3 = transitionValues;
        if (transitionValues3 == null && transitionValues2 == null) {
            return 0L;
        }
        Rect epicenter = transition.getEpicenter();
        if (transitionValues2 == null || e(transitionValues3) == 0) {
            i10 = -1;
        } else {
            transitionValues3 = transitionValues2;
            i10 = 1;
        }
        int f10 = f(transitionValues3);
        int g6 = g(transitionValues3);
        int[] iArr = new int[2];
        viewGroup.getLocationOnScreen(iArr);
        int round = iArr[0] + Math.round(viewGroup.getTranslationX());
        int round2 = iArr[1] + Math.round(viewGroup.getTranslationY());
        int width = round + viewGroup.getWidth();
        int height = round2 + viewGroup.getHeight();
        if (epicenter != null) {
            i11 = epicenter.centerX();
            i12 = epicenter.centerY();
        } else {
            i11 = (round + width) / 2;
            i12 = (round2 + height) / 2;
        }
        float h10 = h(viewGroup, f10, g6, i11, i12, round, round2, width, height) / i(viewGroup);
        long duration = transition.getDuration();
        if (duration < 0) {
            duration = 300;
        }
        return Math.round((((float) (duration * i10)) / this.f4126b) * h10);
    }

    public void j(int i10) {
        this.f4127c = i10;
    }
}
