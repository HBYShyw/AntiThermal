package android.view.autolayout;

/* loaded from: classes.dex */
public class AutoLayoutPolicy {
    public static final int DEFAULT_TYPE = -1;
    public static final int NORMAL_TYPE = 1;
    public static final int WIDGET_TYPE = 2;

    public static int getUnFoldDisplayWidth() {
        return AutoLayoutPolicyFactory.getUnFoldDisplayWidth();
    }
}
