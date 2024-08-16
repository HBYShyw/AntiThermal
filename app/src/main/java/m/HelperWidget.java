package m;

import java.util.Arrays;
import java.util.HashMap;

/* compiled from: HelperWidget.java */
/* renamed from: m.j, reason: use source file name */
/* loaded from: classes.dex */
public class HelperWidget extends ConstraintWidget implements Helper {
    public ConstraintWidget[] G0 = new ConstraintWidget[4];
    public int H0 = 0;

    @Override // m.Helper
    public void a(ConstraintWidgetContainer constraintWidgetContainer) {
    }

    @Override // m.Helper
    public void b(ConstraintWidget constraintWidget) {
        if (constraintWidget == this || constraintWidget == null) {
            return;
        }
        int i10 = this.H0 + 1;
        ConstraintWidget[] constraintWidgetArr = this.G0;
        if (i10 > constraintWidgetArr.length) {
            this.G0 = (ConstraintWidget[]) Arrays.copyOf(constraintWidgetArr, constraintWidgetArr.length * 2);
        }
        ConstraintWidget[] constraintWidgetArr2 = this.G0;
        int i11 = this.H0;
        constraintWidgetArr2[i11] = constraintWidget;
        this.H0 = i11 + 1;
    }

    @Override // m.Helper
    public void c() {
        this.H0 = 0;
        Arrays.fill(this.G0, (Object) null);
    }

    @Override // m.ConstraintWidget
    public void l(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.l(constraintWidget, hashMap);
        HelperWidget helperWidget = (HelperWidget) constraintWidget;
        this.H0 = 0;
        int i10 = helperWidget.H0;
        for (int i11 = 0; i11 < i10; i11++) {
            b(hashMap.get(helperWidget.G0[i11]));
        }
    }
}
