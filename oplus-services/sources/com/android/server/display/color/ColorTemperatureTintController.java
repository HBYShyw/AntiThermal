package com.android.server.display.color;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class ColorTemperatureTintController extends TintController {
    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract float[] computeMatrixForCct(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int getAppliedCct();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int getDisabledCct();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract CctEvaluator getEvaluator();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int getTargetCct();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void setAppliedCct(int i);

    abstract void setTargetCct(int i);
}
