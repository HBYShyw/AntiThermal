package com.coui.appcompat.viewpager;

import android.animation.LayoutTransition;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

/* compiled from: COUIAnimateLayoutChangeDetector.java */
/* renamed from: com.coui.appcompat.viewpager.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIAnimateLayoutChangeDetector {

    /* renamed from: b, reason: collision with root package name */
    private static final ViewGroup.MarginLayoutParams f8105b;

    /* renamed from: a, reason: collision with root package name */
    private LinearLayoutManager f8106a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIAnimateLayoutChangeDetector.java */
    /* renamed from: com.coui.appcompat.viewpager.a$a */
    /* loaded from: classes.dex */
    public class a implements Comparator<int[]> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(int[] iArr, int[] iArr2) {
            return iArr[0] - iArr2[0];
        }
    }

    static {
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(-1, -1);
        f8105b = marginLayoutParams;
        marginLayoutParams.setMargins(0, 0, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public COUIAnimateLayoutChangeDetector(LinearLayoutManager linearLayoutManager) {
        this.f8106a = linearLayoutManager;
    }

    private boolean a() {
        ViewGroup.MarginLayoutParams marginLayoutParams;
        int top;
        int i10;
        int bottom;
        int i11;
        int J = this.f8106a.J();
        if (J == 0) {
            return true;
        }
        boolean z10 = this.f8106a.p2() == 0;
        int[][] iArr = (int[][]) Array.newInstance((Class<?>) int.class, J, 2);
        for (int i12 = 0; i12 < J; i12++) {
            View I = this.f8106a.I(i12);
            if (I != null) {
                ViewGroup.LayoutParams layoutParams = I.getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                } else {
                    marginLayoutParams = f8105b;
                }
                int[] iArr2 = iArr[i12];
                if (z10) {
                    top = I.getLeft();
                    i10 = marginLayoutParams.leftMargin;
                } else {
                    top = I.getTop();
                    i10 = marginLayoutParams.topMargin;
                }
                iArr2[0] = top - i10;
                int[] iArr3 = iArr[i12];
                if (z10) {
                    bottom = I.getRight();
                    i11 = marginLayoutParams.rightMargin;
                } else {
                    bottom = I.getBottom();
                    i11 = marginLayoutParams.bottomMargin;
                }
                iArr3[1] = bottom + i11;
            } else {
                throw new IllegalStateException("null view contained in the view hierarchy");
            }
        }
        Arrays.sort(iArr, new a());
        for (int i13 = 1; i13 < J; i13++) {
            if (iArr[i13 - 1][1] != iArr[i13][0]) {
                return false;
            }
        }
        return iArr[0][0] <= 0 && iArr[J - 1][1] >= iArr[0][1] - iArr[0][0];
    }

    private boolean b() {
        int J = this.f8106a.J();
        for (int i10 = 0; i10 < J; i10++) {
            if (c(this.f8106a.I(i10))) {
                return true;
            }
        }
        return false;
    }

    private static boolean c(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            LayoutTransition layoutTransition = viewGroup.getLayoutTransition();
            if (layoutTransition != null && layoutTransition.isChangingLayout()) {
                return true;
            }
            int childCount = viewGroup.getChildCount();
            for (int i10 = 0; i10 < childCount; i10++) {
                if (c(viewGroup.getChildAt(i10))) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        return (!a() || this.f8106a.J() <= 1) && b();
    }
}
