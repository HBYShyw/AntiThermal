package m;

import java.util.ArrayList;

/* compiled from: WidgetContainer.java */
/* renamed from: m.m, reason: use source file name */
/* loaded from: classes.dex */
public class WidgetContainer extends ConstraintWidget {
    public ArrayList<ConstraintWidget> G0 = new ArrayList<>();

    public ArrayList<ConstraintWidget> L0() {
        return this.G0;
    }

    public void M0() {
        ArrayList<ConstraintWidget> arrayList = this.G0;
        if (arrayList == null) {
            return;
        }
        int size = arrayList.size();
        for (int i10 = 0; i10 < size; i10++) {
            ConstraintWidget constraintWidget = this.G0.get(i10);
            if (constraintWidget instanceof WidgetContainer) {
                ((WidgetContainer) constraintWidget).M0();
            }
        }
    }

    public void N0(ConstraintWidget constraintWidget) {
        this.G0.remove(constraintWidget);
        constraintWidget.x0(null);
    }

    public void O0() {
        this.G0.clear();
    }

    @Override // m.ConstraintWidget
    public void Z() {
        this.G0.clear();
        super.Z();
    }

    public void b(ConstraintWidget constraintWidget) {
        this.G0.add(constraintWidget);
        if (constraintWidget.H() != null) {
            ((WidgetContainer) constraintWidget.H()).N0(constraintWidget);
        }
        constraintWidget.x0(this);
    }

    @Override // m.ConstraintWidget
    public void b0(l.c cVar) {
        super.b0(cVar);
        int size = this.G0.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.G0.get(i10).b0(cVar);
        }
    }
}
