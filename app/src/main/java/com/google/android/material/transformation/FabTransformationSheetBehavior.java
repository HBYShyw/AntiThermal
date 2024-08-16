package com.google.android.material.transformation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.R$animator;
import com.google.android.material.transformation.FabTransformationBehavior;
import java.util.HashMap;
import java.util.Map;
import p3.MotionSpec;
import p3.Positioning;

@Deprecated
/* loaded from: classes.dex */
public class FabTransformationSheetBehavior extends FabTransformationBehavior {

    /* renamed from: i, reason: collision with root package name */
    private Map<View, Integer> f9562i;

    public FabTransformationSheetBehavior() {
    }

    private void F(View view, boolean z10) {
        ViewParent parent = view.getParent();
        if (parent instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) parent;
            int childCount = coordinatorLayout.getChildCount();
            if (z10) {
                this.f9562i = new HashMap(childCount);
            }
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = coordinatorLayout.getChildAt(i10);
                boolean z11 = (childAt.getLayoutParams() instanceof CoordinatorLayout.e) && (((CoordinatorLayout.e) childAt.getLayoutParams()).f() instanceof FabTransformationScrimBehavior);
                if (childAt != view && !z11) {
                    if (!z10) {
                        Map<View, Integer> map = this.f9562i;
                        if (map != null && map.containsKey(childAt)) {
                            ViewCompat.w0(childAt, this.f9562i.get(childAt).intValue());
                        }
                    } else {
                        this.f9562i.put(childAt, Integer.valueOf(childAt.getImportantForAccessibility()));
                        ViewCompat.w0(childAt, 4);
                    }
                }
            }
            if (z10) {
                return;
            }
            this.f9562i = null;
        }
    }

    @Override // com.google.android.material.transformation.FabTransformationBehavior
    protected FabTransformationBehavior.e D(Context context, boolean z10) {
        int i10;
        if (z10) {
            i10 = R$animator.mtrl_fab_transformation_sheet_expand_spec;
        } else {
            i10 = R$animator.mtrl_fab_transformation_sheet_collapse_spec;
        }
        FabTransformationBehavior.e eVar = new FabTransformationBehavior.e();
        eVar.f9555a = MotionSpec.d(context, i10);
        eVar.f9556b = new Positioning(17, 0.0f, 0.0f);
        return eVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.transformation.ExpandableTransformationBehavior, com.google.android.material.transformation.ExpandableBehavior
    public boolean g(View view, View view2, boolean z10, boolean z11) {
        F(view2, z10);
        return super.g(view, view2, z10, z11);
    }

    public FabTransformationSheetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
